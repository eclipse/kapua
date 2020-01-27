/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.util.GwtKapuaAuthorizationModelConverter;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserCreator;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserQuery;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.util.GwtKapuaUserModelConverter;
import org.eclipse.kapua.app.console.module.user.shared.util.KapuaGwtUserModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;

/**
 * The server side implementation of the RPC service.
 */
public class GwtUserServiceImpl extends KapuaRemoteServiceServlet implements GwtUserService {

    private static final long serialVersionUID = 7430961652373364113L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccessInfoService ACCESS_INFO_SERVICE = LOCATOR.getService(AccessInfoService.class);
    private static final AccessRoleService ACCESS_ROLE_SERVICE = LOCATOR.getService(AccessRoleService.class);

    private static final CredentialService CREDENTIAL_SERVICE = LOCATOR.getService(CredentialService.class);
    private static final CredentialFactory CREDENTIAL_FACTORY = LOCATOR.getFactory(CredentialFactory.class);

    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);
    private static final UserFactory USER_FACTORY = LOCATOR.getFactory(UserFactory.class);

    private static final DeviceConnectionService DEVICE_CONNECTION_SERVICE = LOCATOR.getService(DeviceConnectionService.class);
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);

    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);
    private static final DeviceRegistryService DEVICE_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
    private static final DeviceFactory DEVICE_FACTORY = LOCATOR.getFactory(DeviceFactory.class);

    @Override
    public GwtUser create(GwtXSRFToken xsrfToken, GwtUserCreator gwtUserCreator) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtUser gwtUser = null;
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtUserCreator.getScopeId());

            final UserCreator userCreator = USER_FACTORY.newCreator(scopeId, gwtUserCreator.getUsername());
            userCreator.setDisplayName(gwtUserCreator.getDisplayName());
            userCreator.setEmail(gwtUserCreator.getEmail());
            userCreator.setPhoneNumber(gwtUserCreator.getPhoneNumber());
            userCreator.setExpirationDate(gwtUserCreator.getExpirationDate());
            userCreator.setUserStatus(GwtKapuaUserModelConverter.convertUserStatus(gwtUserCreator.getUserStatus()));

            //
            // Create the User
            User user = USER_SERVICE.create(userCreator);

            //
            // Create credentials
            if (gwtUserCreator.getPassword() != null) {

                CredentialCreator credentialCreator = CREDENTIAL_FACTORY.newCreator(scopeId,
                        user.getId(),
                        CredentialType.PASSWORD,
                        gwtUserCreator.getPassword(),
                        CredentialStatus.ENABLED,
                        null);

                CREDENTIAL_SERVICE.create(credentialCreator);
            }

            // convertKapuaId to GwtAccount and return
            // reload the user as we want to load all its permissions
            gwtUser = KapuaGwtUserModelConverter.convertUser(USER_SERVICE.find(user.getScopeId(), user.getId()));
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
            KapuaId scopeId = KapuaEid.parseCompactId(gwtUser.getScopeId());
            KapuaId userId = KapuaEid.parseCompactId(gwtUser.getId());

            User user = USER_SERVICE.find(scopeId, userId);

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

                // optlock
                user.setOptlock(gwtUser.getOptlock());

                // update the user
                USER_SERVICE.update(user);

                //
                // convertKapuaId to GwtAccount and return
                // reload the user as we want to load all its permissions
                gwtUserUpdated = KapuaGwtUserModelConverter.convertUser(USER_SERVICE.find(user.getScopeId(), user.getId()));
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

        try {
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
            KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtUserId);

            User user = USER_SERVICE.find(scopeId, userId);

            if (user != null) {
                USER_SERVICE.delete(user);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtUser find(String accountId, String userIdString)
            throws GwtKapuaException {

        GwtUser gwtUser = null;
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(accountId);
            KapuaId userId = KapuaEid.parseCompactId(userIdString);

            User user = USER_SERVICE.find(scopeId, userId);

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
            UserQuery query = USER_FACTORY.newQuery(scopeId);
            UserListResult list = USER_SERVICE.query(query);

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
            // Convert from GWT entity
            UserQuery userQuery = GwtKapuaUserModelConverter.convertUserQuery(loadConfig, gwtUserQuery);

            // query
            UserListResult users = USER_SERVICE.query(userQuery);
            totalLength = users.getTotalCount().intValue();

            // If there are results
            if (!users.isEmpty()) {
                UserListResult allUsers = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return USER_SERVICE.query(USER_FACTORY.newQuery(null));
                    }
                });

                HashMap<String, String> usernameMap = new HashMap<String, String>();
                for (User user : allUsers.getItems()) {
                    usernameMap.put(user.getId().toCompactId(), user.getName());
                }

                // Convert to GWT entity
                for (User u : users.getItems()) {
                    GwtUser gwtUser = KapuaGwtUserModelConverter.convertUser(u);
                    gwtUser.setCreatedByName(usernameMap.get(u.getCreatedBy().toCompactId()));
                    gwtUser.setModifiedByName(usernameMap.get(u.getModifiedBy().toCompactId()));
                    gwtUsers.add(gwtUser);
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
            final KapuaId scopeId = KapuaEid.parseCompactId(shortScopeId);
            final KapuaId userId = KapuaEid.parseCompactId(shortUserId);

            final User user = USER_SERVICE.find(scopeId, userId);

            if (user != null) {
                UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return USER_SERVICE.query(USER_FACTORY.newQuery(null));
                    }
                });

                Map<String, String> usernameMap = new HashMap<String, String>();
                for (User userItem : userListResult.getItems()) {
                    usernameMap.put(userItem.getId().toCompactId(), userItem.getName());
                }

                DeviceConnection deviceConnection = null;
                if (deviceListQuery(scopeId) != null && AUTHORIZATION_SERVICE.isPermitted(PERMISSION_FACTORY.newPermission(new DeviceConnectionDomain(), Actions.read, scopeId))) {
                for (Device device : deviceListQuery(scopeId).getItems()) {
                    if (device.getConnectionId() != null) {
                        deviceConnection = DEVICE_CONNECTION_SERVICE.find(scopeId, device.getConnectionId());
                        break;
                    }
                }}
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userStatus", user.getStatus().toString()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userName", user.getName()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userDisplayName", user.getDisplayName()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userEmail", user.getEmail()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userPhoneNumber", user.getPhoneNumber()));
                gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "expirationDate", user.getExpirationDate()));
                gwtUserDescription.add(new GwtGroupedNVPair("entityInfo", "userCreatedOn", user.getCreatedOn()));
                gwtUserDescription.add(new GwtGroupedNVPair("entityInfo", "userCreatedBy", user.getCreatedBy() != null ? usernameMap.get(user.getCreatedBy().toCompactId()) : null));
                gwtUserDescription.add(new GwtGroupedNVPair("entityInfo", "userModifiedOn", user.getModifiedOn()));
                gwtUserDescription.add(new GwtGroupedNVPair("entityInfo", "userModifiedBy", user.getModifiedBy() != null ? usernameMap.get(user.getModifiedBy().toCompactId()) : null));
                if (deviceConnection != null && deviceConnection.getReservedUserId() != null && deviceConnection.getReservedUserId().equals(user.getId())) {
                    gwtUserDescription.add(new GwtGroupedNVPair("userInfo", "userReservedConnection", "Yes" )); }
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
            AccessRoleQuery accessRoleQuery = GwtKapuaAuthorizationModelConverter.convertAccessRoleQuery(pagingLoadConfig, query);
            AccessRoleListResult accessRoleList = ACCESS_ROLE_SERVICE.query(accessRoleQuery);
            totalLength = accessRoleList.getTotalCount().intValue();

            for (AccessRole a : accessRoleList.getItems()) {
                AccessInfo accessInfo = ACCESS_INFO_SERVICE.find(KapuaEid.parseCompactId(query.getScopeId()), a.getAccessInfoId());
                User user = USER_SERVICE.find(KapuaEid.parseCompactId(query.getScopeId()), accessInfo.getUserId());

                GwtUser gwtUser = KapuaGwtUserModelConverter.convertUser(user);
                gwtUser.set("type", "USER");
                list.add(gwtUser);
            }

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtUser>(list, pagingLoadConfig.getOffset(), totalLength);
    }

    @Override
    public PagingLoadResult<GwtUser> getUsersForAccount(PagingLoadConfig loadConfig, GwtUserQuery gwtUserQuery,
            String accountId) throws GwtKapuaException {

        int totalLength = 0;
        List<GwtUser> gwtUsers = new ArrayList<GwtUser>();
        try {
            UserQuery userQuery = GwtKapuaUserModelConverter.convertUserQuery(loadConfig, gwtUserQuery);
            UserListResult users = USER_SERVICE.query(userQuery);
            totalLength = users.getTotalCount().intValue();

            if (!users.isEmpty()) {
                final UserQuery allUsersQuery = USER_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(accountId));
                UserListResult allUsers = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                    @Override
                    public UserListResult call() throws Exception {
                        return USER_SERVICE.query(allUsersQuery);
                    }
                });

                HashMap<String, String> usernameMap = new HashMap<String, String>();
                for (User user : allUsers.getItems()) {
                    usernameMap.put(user.getId().toCompactId(), user.getName());
                }
                for (User u : users.getItems()) {
                    GwtUser gwtUser = KapuaGwtUserModelConverter.convertUser(u);
                    gwtUser.setCreatedByName(usernameMap.get(u.getCreatedBy().toCompactId()));
                    gwtUser.setModifiedByName(usernameMap.get(u.getModifiedBy().toCompactId()));
                    gwtUsers.add(gwtUser);
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtUser>(gwtUsers, loadConfig.getOffset(), totalLength);
    }

    /**
    Method which returns list of all available devices in current scope(if User have Device:read permission), otherwise null is returned.
     */
    private DeviceListResult deviceListQuery(KapuaId scopeId) throws KapuaException {
         DeviceListResult devicesList = null;
         if (AUTHORIZATION_SERVICE.isPermitted(PERMISSION_FACTORY.newPermission(new DeviceDomain(), Actions.read, scopeId))) {
         DeviceQuery deviceQuery = DEVICE_FACTORY.newQuery(scopeId);
         devicesList = DEVICE_SERVICE.query(deviceQuery);
         }
         return devicesList;
    }
}
