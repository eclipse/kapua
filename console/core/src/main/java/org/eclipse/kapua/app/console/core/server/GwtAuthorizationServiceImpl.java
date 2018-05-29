/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.server;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.core.shared.model.authentication.GwtJwtCredential;
import org.eclipse.kapua.app.console.core.shared.model.authentication.GwtLoginCredential;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationService;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.util.KapuaGwtAccountModelConverter;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermission;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionAction;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSessionPermissionScope;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.util.KapuaGwtUserModelConverter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

public class GwtAuthorizationServiceImpl extends KapuaRemoteServiceServlet implements GwtAuthorizationService {

    private static final long serialVersionUID = -3919578632016541047L;

    private static final Logger logger = LoggerFactory.getLogger(GwtAuthorizationServiceImpl.class);

    public static final String SESSION_CURRENT = "console.current.session";
    public static final String SESSION_CURRENT_USER = "console.current.user";

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccessInfoService ACCESS_INFO_SERVICE = LOCATOR.getService(AccessInfoService.class);
    private static final AccessPermissionService ACCESS_PERMISSION_SERVICE = LOCATOR.getService(AccessPermissionService.class);

    private static final AccessRoleService ACCESS_ROLE_SERVICE = LOCATOR.getService(AccessRoleService.class);
    private static final RoleService ROLE_SERVICE = LOCATOR.getService(RoleService.class);
    private static final RolePermissionService ROLE_PERMISSION_SERVICE = LOCATOR.getService(RolePermissionService.class);

    /**
     * Login call in response to the login dialog.
     */
    @Override
    public GwtSession login(GwtLoginCredential gwtLoginCredentials)
            throws GwtKapuaException {

        try {
            // Get the user
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
            LoginCredentials credentials = credentialsFactory.newUsernamePasswordCredentials(gwtLoginCredentials.getUsername(), gwtLoginCredentials.getPassword());

            // Login
            authenticationService.login(credentials);

            // Get the session infos
            return establishSession();
        } catch (Throwable t) {
            logout();
            KapuaExceptionHandler.handle(t);
        }
        return null;
    }

    @Override
    public GwtSession login(GwtJwtCredential gwtAccessTokenCredentials) throws GwtKapuaException {
        // VIP
        // keep this here to make sure we initialize the logger.
        // Without the following, console logger may not log anything when deployed into tomcat.
        logger.info(">>> THIS IS INFO <<<");
        logger.warn(">>> THIS IS WARN <<<");
        logger.debug(">>> THIS IS DEBUG <<<");

        try {
            // Get the user

            KapuaLocator locator = KapuaLocator.getInstance();
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
            JwtCredentials credentials = credentialsFactory.newJwtCredentials(gwtAccessTokenCredentials.getAccessToken());

            // Login

            handleLogin(authenticationService, credentials);

            // Get the session infos

            return establishSession();
        } catch (Throwable t) {
            logout();
            KapuaExceptionHandler.handle(t);
        }
        return null;
    }

    private void handleLogin(AuthenticationService authenticationService, JwtCredentials credentials) throws KapuaException {
        try {
            authenticationService.login(credentials);
        } catch (KapuaAuthenticationException e) {
            logger.debug("First level login attempt failed", e);
            handleLoginError(authenticationService, credentials, e);
        }
    }

    private void handleLoginError(AuthenticationService authenticationService, JwtCredentials credentials, KapuaAuthenticationException e) throws KapuaException {
        logger.debug("Handling error code: {}", e.getCode());

        if (!isAccountCreationEnabled()) {
            logger.debug("Account creation is not active");
            throw e;
        }

        if (e.getCode().equals(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL)) {
            try {
                logger.info("Trying auto account creation");
                if (KapuaLocator.getInstance().getService(RegistrationService.class).createAccount(credentials)) {
                    logger.info("Created new account");
                    authenticationService.login(credentials);
                } else {
                    logger.info("New account did not get created");
                    throw e; // throw the original error
                }
            } catch (Exception e1) {
                logger.warn("Failed to auto-create account", e1);
                throw e; // we throw the original error instead
            }
        } else {
            // it is an exception we can't handle by auto-account-creation
            throw e;
        }
    }

    private boolean isAccountCreationEnabled() {
        return KapuaLocator.getInstance().getService(RegistrationService.class).isAccountCreationEnabled();
    }

    /**
     * Return the currently authenticated user or null if no session has been established.
     */
    @Override
    public GwtSession getCurrentSession()
            throws GwtKapuaException {
        GwtSession gwtSession = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            if (currentUser != null && currentUser.isAuthenticated()) {

                Session session = currentUser.getSession();
                gwtSession = (GwtSession) session.getAttribute(SESSION_CURRENT);

                // Store the user information in the sessions
                String username = ((User) currentUser.getPrincipal()).getName();

                KapuaLocator locator = KapuaLocator.getInstance();
                UserService userService = locator.getService(UserService.class);

                // get the session
                if (gwtSession == null) {
                    gwtSession = establishSession();
                } else {
                    User user = userService.findByName(username);
                    gwtSession.setUserId(user.getId().toCompactId());
                }
            }
        } catch (Throwable t) {
            logger.warn("Error in getCurrentSession.", t);
            KapuaExceptionHandler.handle(t);
        }

        return gwtSession;
    }

    private GwtSession establishSession() throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();

        //
        // Get info from session
        final KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        logger.debug("Kapua session: {}", kapuaSession);

        //
        // Get user info
        final UserService userService = locator.getService(UserService.class);
        logger.debug("Looking up - scopeId: {}, userId: {}", kapuaSession.getScopeId(), kapuaSession.getUserId());
        final User user = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

            @Override
            public User call() throws Exception {
                return userService.find(kapuaSession.getScopeId(), kapuaSession.getUserId());
            }
        });

        //
        // Get account info
        final AccountService accountService = locator.getService(AccountService.class);
        Account account = KapuaSecurityUtils.doPrivileged(new Callable<Account>() {

            @Override
            public Account call() throws Exception {
                return accountService.find(kapuaSession.getScopeId());
            }
        });

        //
        // Convert entities
        GwtUser gwtUser = KapuaGwtUserModelConverter.convertUser(user);
        GwtAccount gwtAccount = KapuaGwtAccountModelConverter.convertAccount(account);

        //
        // Build the session
        final GwtSession gwtSession = new GwtSession();

        // Console info
        SystemSetting commonsConfig = SystemSetting.getInstance();
        gwtSession.setVersion(commonsConfig.getString(SystemSettingKey.VERSION));
        gwtSession.setBuildVersion(commonsConfig.getString(SystemSettingKey.BUILD_VERSION));
        gwtSession.setBuildNumber(commonsConfig.getString(SystemSettingKey.BUILD_NUMBER));

        // User info
        gwtSession.setUserId(gwtUser.getId());
        gwtSession.setAccountId(gwtAccount.getId());
        gwtSession.setRootAccountId(gwtAccount.getId());
        gwtSession.setSelectedAccountId(gwtAccount.getId());

        gwtSession.setUserName(gwtUser.getUsername());
        gwtSession.setUserDisplayName(gwtUser.getDisplayName());
        gwtSession.setRootAccountName(gwtAccount.getName());
        gwtSession.setSelectedAccountName(gwtAccount.getName());

        gwtSession.setAccountPath(gwtAccount.getParentAccountPath());
        gwtSession.setSelectedAccountPath(gwtAccount.getParentAccountPath());

        //
        // Load permissions
        KapuaSecurityUtils.doPrivileged(new ThrowingRunnable() {

            @Override
            public void run() throws Exception {
                AccessInfo userAccessInfo = ACCESS_INFO_SERVICE.findByUserId(user.getScopeId(), user.getId());

                if (userAccessInfo != null) {

                    // Permission info
                    AccessPermissionListResult accessPermissions = ACCESS_PERMISSION_SERVICE.findByAccessInfoId(userAccessInfo.getScopeId(), userAccessInfo.getId());
                    for (AccessPermission ap : accessPermissions.getItems()) {
                        gwtSession.addSessionPermission(convert(ap.getPermission()));
                    }

                    // Role info
                    AccessRoleListResult accessRoles = ACCESS_ROLE_SERVICE.findByAccessInfoId(userAccessInfo.getScopeId(), userAccessInfo.getId());

                    for (AccessRole ar : accessRoles.getItems()) {
                        Role role = ROLE_SERVICE.find(ar.getScopeId(), ar.getRoleId());

                        RolePermissionListResult rolePermissions = ROLE_PERMISSION_SERVICE.findByRoleId(role.getScopeId(), role.getId());
                        for (RolePermission rp : rolePermissions.getItems()) {
                            gwtSession.addSessionPermission(convert(rp.getPermission()));
                        }
                    }
                }
            }
        });

        return gwtSession;
    }

    /**
     * Logout call in response to the logout link/button.
     */
    @Override
    public void logout()
            throws GwtKapuaException {
        try {
            // Logout
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            authenticationService.logout();

            // Invalidate http session
            HttpServletRequest request = getThreadLocalRequest();
            request.getSession().invalidate();
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    private GwtSessionPermission convert(Permission permission) {
        // Domain parsing
        String domain = permission.getDomain();

        // Action parsing
        GwtSessionPermissionAction action = convert(permission.getAction());

        // Target scope parsing
        GwtSessionPermissionScope gwtSessionPermissionScope;
        if (permission.getTargetScopeId() == null) {
            gwtSessionPermissionScope = GwtSessionPermissionScope.ALL;
        } else if (permission.getForwardable()) {
            gwtSessionPermissionScope = GwtSessionPermissionScope.CHILDREN;
        } else {
            gwtSessionPermissionScope = GwtSessionPermissionScope.SELF;
        }

        // Create converted object
        return new GwtSessionPermission(domain, action, gwtSessionPermissionScope);
    }

    private GwtSessionPermissionAction convert(Actions action) {
        return action != null ? GwtSessionPermissionAction.valueOf(action.name()) : null;
    }

}
