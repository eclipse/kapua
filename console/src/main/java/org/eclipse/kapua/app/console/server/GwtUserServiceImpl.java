/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserCreator;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserQuery;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.*;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.*;

import java.util.*;

import com.extjs.gxt.ui.client.data.*;

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
            UserCreator userCreator = userFactory.newCreator(scopeId,
                    gwtUserCreator.getUsername());
            userCreator.setDisplayName(gwtUserCreator.getDisplayName());
            userCreator.setEmail(gwtUserCreator.getEmail());
            userCreator.setPhoneNumber(gwtUserCreator.getPhoneNumber());

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

            AccessInfoService userPermissionService = locator.getService(AccessInfoService.class);
            AccessInfoFactory userPermissionFactory = locator.getFactory(AccessInfoFactory.class);
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

            for (String p : permissions) {
                // UserPermissionCreator userPermissionCreator = userPermissionFactory.newCreator(user.getScopeId());
                // userPermissionCreator.setAccessId(scopeId);
                //
                // String[] tokens = p.split(":");
                // String domain = null;
                // Actions action = null;
                // KapuaId targetScopeId = null;
                // if (tokens.length > 0) {
                // domain = tokens[0];
                // }
                // if (tokens.length > 1) {
                // action = Actions.valueOf(tokens[1]);
                // }
                // if (tokens.length > 2) {
                // targetScopeId = KapuaEid.parseCompactId(tokens[2]);
                // }
                //
                // Permission permission = permissionFactory.newPermission(domain, action, targetScopeId);
                // userPermissionCreator.setPermission(permission);
                //
                // userPermissionService.create(userPermissionCreator);
            }

            //
            // Create credentials
            CredentialService credentialService = locator.getService(CredentialService.class);
            CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

            CredentialCreator credentialCreator = credentialFactory.newCreator(scopeId,
                    user.getId(),
                    CredentialType.PASSWORD,
                    gwtUserCreator.getPassword());
            credentialService.create(credentialCreator);

            // convert to GwtAccount and return
            // reload the user as we want to load all its permissions
            gwtUser = KapuaGwtModelConverter.convert(userService.find(user.getScopeId(), user.getId()));
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

                // status
                user.setStatus(UserStatus.valueOf(gwtUser.getStatus()));

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
                // credentialService.delete(oldCredential.getScopeId(), oldCredential.getId());
                //
                // //
                // // Create new PASSWORD credential
                // CredentialCreator credentialCreator = credentialFactory.newCreator(scopeId,
                // user.getId(),
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
                // convert to GwtAccount and return
                // reload the user as we want to load all its permissions
                gwtUserUpdated = KapuaGwtModelConverter.convert(userService.find(user.getScopeId(), user.getId()));
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

        KapuaId scopeId = GwtKapuaModelConverter.convert(gwtScopeId);
        KapuaId userId = GwtKapuaModelConverter.convert(gwtUserId);
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            CredentialService credentialService = locator.getService(CredentialService.class);
            User user = userService.find(scopeId, KapuaEid.parseCompactId(gwtUserId));
            if (user != null) {
                userService.delete(user);
                CredentialListResult credentialListResult = credentialService.findByUserId(scopeId, userId);
                for (Credential credential : credentialListResult.getItems()) {
                    credentialService.delete(scopeId, credential.getId());
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
                gwtUser = KapuaGwtModelConverter.convert(user);
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
                gwtUserList.add(KapuaGwtModelConverter.convert(user));
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
            UserQuery userQuery = GwtKapuaModelConverter.convertUserQuery(loadConfig, gwtUserQuery);

            // query
            UserListResult users = userService.query(userQuery);

            // If there are results
            if (!users.isEmpty()) {
                // count
                if (users.getSize() >= loadConfig.getLimit()) {
                    totalLength = new Long(userService.count(userQuery)).intValue();
                } else {
                    totalLength = users.getSize();
                }

                // Converto to GWT entity
                for (User u : users.getItems()) {
                    gwtUsers.add(KapuaGwtModelConverter.convert(u));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtUser>(gwtUsers, loadConfig.getOffset(), totalLength);
    }
}
