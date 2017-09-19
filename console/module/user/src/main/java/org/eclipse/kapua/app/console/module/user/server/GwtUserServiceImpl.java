/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.server;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.util.GwtKapuaAuthorizationModelConverter;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUserCreator;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUserQuery;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.util.GwtKapuaUserModelConverter;
import org.eclipse.kapua.app.console.module.user.shared.util.KapuaGwtUserModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
public class GwtUserServiceImpl extends KapuaRemoteServiceServlet implements GwtUserService {

    private static final long serialVersionUID = 7430961652373364113L;

    @Override
    public GwtUser create(GwtXSRFToken xsrfToken, GwtUserCreator gwtUserCreator)
            throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtUser gwtUser = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserFactory userFactory = locator.getFactory(UserFactory.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtUserCreator.getScopeId());
            UserCreator userCreator = userFactory.newCreator(scopeId, gwtUserCreator.getUsername());
            userCreator.setDisplayName(gwtUserCreator.getDisplayName());
            userCreator.setEmail(gwtUserCreator.getEmail());
            userCreator.setPhoneNumber(gwtUserCreator.getPhoneNumber());
            userCreator.setExpirationDate(gwtUserCreator.getExpirationDate());
            userCreator.setUserStatus(GwtKapuaUserModelConverter.convertUserStatus(gwtUserCreator.getUserStatus()));

            //
            // Create the User
            UserService userService = locator.getService(UserService.class);
            User user = userService.create(userCreator);

            //
            // Create permissions
            Set<String> permissions = new HashSet<String>();
            if (gwtUserCreator.getPermissions() != null) {
                // build the set of permissions
                permissions.addAll(Arrays.asList(gwtUserCreator.getPermissions().split(",")));
            }

            //
            // Create credentials
            CredentialService credentialService = locator.getService(CredentialService.class);
            CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

            CredentialCreator credentialCreator = credentialFactory.newCreator(scopeId,
                    user.getId(),
                    CredentialType.PASSWORD,
                    gwtUserCreator.getPassword(),
                    CredentialStatus.ENABLED,
                    null);
            credentialService.create(credentialCreator);

            // convertKapuaId to GwtAccount and return
            // reload the user as we want to load all its permissions
            gwtUser = KapuaGwtUserModelConverter.convertUser(userService.find(user.getScopeId(), user.getId()));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtUser;
    }

    @Override
    public GwtUser update(GwtXSRFToken xsrfToken, GwtUser gwtUser)
            throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtUser gwtUserUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtUser.getScopeId());
            KapuaId userId = KapuaEid.parseCompactId(gwtUser.getId());

            User user = userService.find(scopeId, userId);

            if (user != null) {

                //
                // Update user
                user.setName(gwtUser.getUnescapedUsername());
                user.setDisplayName(gwtUser.getUnescapedDisplayName());
                user.setEmail(gwtUser.getUnescapedEmail());
                user.setPhoneNumber(gwtUser.getUnescapedPhoneNumber());
                user.setExpirationDate(gwtUser.getExpirationDate());
                // status
                user.setStatus(GwtKapuaUserModelConverter.convertUserStatus(gwtUser.getStatusEnum()));

                // //
                // // Update credentials
                // if (gwtUser.getPassword() != null) {
                // CredentialService credentialService = locator.getService(CredentialService.class);
                // CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
                //
                // CredentialListResult credentials = credentialService.findByUserId(scopeId, userId);
                // if (!credentials.isEmpty()) {
                // //
                // // Delete old PASSWORD credential
                // Credential oldCredential = null;
                // for (Credential c : credentials.getItems()) {
                // if (CredentialType.PASSWORD.equals(c.getCredentialType())) {
                // oldCredential = c;
                // break;
                // }
                // }
                // credentialService.delete(oldCredential.getScopeId(), oldCredential.getViewId());
                //
                // //
                // // Create new PASSWORD credential
                // CredentialCreator credentialCreator = credentialFactory.newCreator(scopeId,
                // user.getViewId(),
                // CredentialType.PASSWORD,
                // gwtUser.getPassword());
                //
                // credentialService.create(credentialCreator);
                // }
                // }

                // optlock
                user.setOptlock(gwtUser.getOptlock());

                // update the user
                userService.update(user);

                //
                // convertKapuaId to GwtAccount and return
                // reload the user as we want to load all its permissions
                gwtUserUpdated = KapuaGwtUserModelConverter.convertUser(userService.find(user.getScopeId(), user.getId()));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtUserUpdated;
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtUserId)
            throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtUserId);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            CredentialService credentialService = locator.getService(CredentialService.class);
            User user = userService.find(scopeId, userId);

            if (user != null) {
                userService.delete(user);
                CredentialListResult credentialListResult = credentialService.findByUserId(scopeId, userId);
                for (Credential credential : credentialListResult.getItems()) {
                    credentialService.delete(scopeId, credential.getId());
                }

                AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
                AccessInfo accessInfo = accessInfoService.findByUserId(scopeId, userId);
                if (accessInfo != null) {
                    KapuaId accessInfoId = accessInfo.getId();
                    accessInfoService.delete(scopeId, accessInfoId);

                    AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
                    AccessRoleListResult accessRoles = accessRoleService.findByAccessInfoId(scopeId, accessInfoId);
                    for (AccessRole accessRole : accessRoles.getItems()) {
                        accessRoleService.delete(scopeId, accessRole.getId());
                    }

                    AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
                    AccessPermissionListResult accessPermissions = accessPermissionService.findByAccessInfoId(scopeId, accessInfoId);
                    for (AccessPermission accessPermission : accessPermissions.getItems()) {
                        accessPermissionService.delete(scopeId, accessPermission.getId());
                    }
                }
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtUser find(String accountId, String userIdString)
            throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(accountId);
        KapuaId userId = KapuaEid.parseCompactId(userIdString);

        GwtUser gwtUser = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            User user = userService.find(scopeId, userId);
            if (user != null) {
                gwtUser = KapuaGwtUserModelConverter.convertUser(user);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtUser;
    }

    @Override
    public ListLoadResult<GwtUser> findAll(String scopeIdString)
            throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
        List<GwtUser> gwtUserList = new ArrayList<GwtUser>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            UserFactory userFactory = locator.getFactory(UserFactory.class);
            UserQuery query = userFactory.newQuery(scopeId);
            UserListResult list = userService.query(query);

            for (User user : list.getItems()) {
                gwtUserList.add(KapuaGwtUserModelConverter.convertUser(user));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtUser>(gwtUserList);
    }

    @Override
    public PagingLoadResult<GwtUser> query(PagingLoadConfig loadConfig, GwtUserQuery gwtUserQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtUser> gwtUsers = new ArrayList<GwtUser>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);

            // Convert from GWT entity
            UserQuery userQuery = GwtKapuaUserModelConverter.convertUserQuery(loadConfig, gwtUserQuery);

            // query
            UserListResult users = userService.query(userQuery);

            // If there are results
            if (!users.isEmpty()) {
                // count
                if (users.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(userService.count(userQuery)).intValue();
                } else {
                    totalLength = users.getSize();
                }

                // Converto to GWT entity
                for (User u : users.getItems()) {
                    gwtUsers.add(KapuaGwtUserModelConverter.convertUser(u));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtUser>(gwtUsers, loadConfig.getOffset(), totalLength);
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getUserDescription(String shortScopeId,
            String shortUserId) throws GwtKapuaException {
        List<GwtGroupedNVPair> gwtUserDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            KapuaId scopeId = KapuaEid.parseCompactId(shortScopeId);
            KapuaId userId = KapuaEid.parseCompactId(shortUserId);
            User user = userService.find(scopeId, userId);

            if (user != null) {
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userStatus", user.getStatus().toString()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userName", user.getName()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userDisplayName", user.getDisplayName()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userEmail", user.getEmail()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userPhoneNumber", user.getPhoneNumber()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userCreatedOn", user.getCreatedOn().toString()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userCreatedBy", user.getDisplayName()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userModifiedOn", user.getModifiedOn().toString()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userModifiedBy", user.getDisplayName()));
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(gwtUserDescription);
    }

        @Override
        public PagingLoadResult<GwtUser> getUsersForRole(PagingLoadConfig pagingLoadConfig,
                GwtAccessRoleQuery query) throws GwtKapuaException {
            int totalLength = 0;
            List<GwtUser> list = new ArrayList<GwtUser>();
            try {
                KapuaLocator locator = KapuaLocator.getInstance();
                UserService userService = locator.getService(UserService.class);
                AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
                AccessRoleQuery accessRoleQuery = GwtKapuaAuthorizationModelConverter
                        .convertAccessRoleQuery(pagingLoadConfig, query);
                AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
                AccessRoleListResult accessRoleList = accessRoleService.query(accessRoleQuery);
                if (!accessRoleList.isEmpty()) {
                    if (accessRoleList.getSize() >= pagingLoadConfig.getLimit()) {
                        totalLength = Long.valueOf(accessRoleService.count(accessRoleQuery)).intValue();

                    } else {
                        totalLength = accessRoleList.getSize();
                    }

                    for (AccessRole a : accessRoleList.getItems()) {
                        AccessInfo accessInfo = accessInfoService.find(KapuaEid.parseCompactId(query.getScopeId()), a.getAccessInfoId());
                        User user = userService.find(KapuaEid.parseCompactId(query.getScopeId()), accessInfo.getUserId());
                        GwtUser gwtUser = KapuaGwtUserModelConverter.convertUser(user);
                        gwtUser.set("type", "USER");
                        list.add(gwtUser);
                    }
                }
            } catch (Exception e) {
                KapuaExceptionHandler.handle(e);
            }
            return new BasePagingLoadResult<GwtUser>(list, pagingLoadConfig.getOffset(), totalLength);
        }
}
