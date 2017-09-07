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
package org.eclipse.kapua.app.console.core.server;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.kapua.app.console.core.shared.model.authentication.GwtJwtCredential;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.util.KapuaGwtAccountModelConverter;
import org.eclipse.kapua.app.console.module.user.shared.util.KapuaGwtUserModelConverter;
import org.eclipse.kapua.app.console.core.shared.model.authentication.GwtLoginCredential;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDomain;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.group.shiro.GroupDomain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.shiro.RoleDomain;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.job.internal.JobDomain;
import org.eclipse.kapua.service.tag.internal.TagDomain;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GwtAuthorizationServiceImpl extends KapuaRemoteServiceServlet implements GwtAuthorizationService {

    private static final long serialVersionUID = -3919578632016541047L;

    private static final Logger logger = LoggerFactory.getLogger(GwtAuthorizationServiceImpl.class);

    private static final Domain ACCOUNT_DOMAIN = new AccountDomain();
    private static final Domain DEVICE_DOMAIN = new DeviceDomain();
    private static final Domain DATASTORE_DOMAIN = new DatastoreDomain();
    private static final Domain TAG_DOMAIN = new TagDomain();
    private static final Domain USER_DOMAIN = new UserDomain();
    private static final Domain ROLE_DOMAIN = new RoleDomain();
    private static final Domain GROUP_DOMAIN = new GroupDomain();
    private static final Domain CREDENTIAL_DOMAIN = new CredentialDomain();
    private static final Domain CONNECTION_DOMAIN = new DeviceConnectionDomain();
    private static final Domain JOB_DOMAIN = new JobDomain();

    public static final String SESSION_CURRENT = "console.current.session";
    public static final String SESSION_CURRENT_USER = "console.current.user";

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

    private void handleLogin(final AuthenticationService authenticationService, final JwtCredentials credentials) throws KapuaException {
        try {
            authenticationService.login(credentials);
        } catch (final KapuaAuthenticationException e) {
            logger.debug("First level login attempt failed", e);
            handleLoginError(authenticationService, credentials, e);
        }
    }

    private void handleLoginError(final AuthenticationService authenticationService, final JwtCredentials credentials, final KapuaAuthenticationException e) throws KapuaException {
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
                User user = userService.findByName(username);

                // get the session
                if (gwtSession == null) {
                    gwtSession = establishSession();
                } else {
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
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        logger.debug("Kapua session: {}", kapuaSession);

        //
        // Get user info
        UserService userService = locator.getService(UserService.class);
        logger.debug("Looking up - scopeId: {}, userId: {}", kapuaSession.getScopeId(), kapuaSession.getUserId());
        User user = userService.find(kapuaSession.getScopeId(), kapuaSession.getUserId());

        //
        // Get permission info
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        boolean hasAccountCreate = authorizationService.isPermitted(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasAccountRead = authorizationService.isPermitted(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasAccountUpdate = authorizationService.isPermitted(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasAccountDelete = authorizationService.isPermitted(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.delete, kapuaSession.getScopeId()));
        boolean hasAccountAll = authorizationService.isPermitted(permissionFactory.newPermission(ACCOUNT_DOMAIN, null, null));

        boolean hasDeviceCreate = authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasDeviceRead = authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasDeviceUpdate = authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasDeviceDelete = authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_DOMAIN, Actions.delete, kapuaSession.getScopeId()));
        boolean hasDeviceManage = authorizationService.isPermitted(permissionFactory.newPermission(DEVICE_DOMAIN, Actions.write, kapuaSession.getScopeId()));

        boolean hasDataRead = authorizationService.isPermitted(permissionFactory.newPermission(DATASTORE_DOMAIN, Actions.read, kapuaSession.getScopeId()));

        boolean hasTagCreate = authorizationService.isPermitted(permissionFactory.newPermission(TAG_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasTagRead = authorizationService.isPermitted(permissionFactory.newPermission(TAG_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasTagUpdate = authorizationService.isPermitted(permissionFactory.newPermission(TAG_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasTagDelete = authorizationService.isPermitted(permissionFactory.newPermission(TAG_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        boolean hasUserCreate = authorizationService.isPermitted(permissionFactory.newPermission(USER_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasUserRead = authorizationService.isPermitted(permissionFactory.newPermission(USER_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasUserUpdate = authorizationService.isPermitted(permissionFactory.newPermission(USER_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasUserDelete = authorizationService.isPermitted(permissionFactory.newPermission(USER_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        boolean hasRoleCreate = authorizationService.isPermitted(permissionFactory.newPermission(ROLE_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasRoleRead = authorizationService.isPermitted(permissionFactory.newPermission(ROLE_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasRoleUpdate = authorizationService.isPermitted(permissionFactory.newPermission(ROLE_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasRoleDelete = authorizationService.isPermitted(permissionFactory.newPermission(ROLE_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        boolean hasGroupCreate = authorizationService.isPermitted(permissionFactory.newPermission(GROUP_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasGroupRead = authorizationService.isPermitted(permissionFactory.newPermission(GROUP_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasGroupUpdate = authorizationService.isPermitted(permissionFactory.newPermission(GROUP_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasGroupDelete = authorizationService.isPermitted(permissionFactory.newPermission(GROUP_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        boolean hasCredentialCreate = authorizationService.isPermitted(permissionFactory.newPermission(CREDENTIAL_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasCredentialRead = authorizationService.isPermitted(permissionFactory.newPermission(CREDENTIAL_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasCredentialUpdate = authorizationService.isPermitted(permissionFactory.newPermission(CREDENTIAL_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasCredentialDelete = authorizationService.isPermitted(permissionFactory.newPermission(CREDENTIAL_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        boolean hasConnectionCreate = authorizationService.isPermitted(permissionFactory.newPermission(CONNECTION_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasConnectionRead = authorizationService.isPermitted(permissionFactory.newPermission(CONNECTION_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasConnectionUpdate = authorizationService.isPermitted(permissionFactory.newPermission(CONNECTION_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasConnectionDelete = authorizationService.isPermitted(permissionFactory.newPermission(CONNECTION_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        boolean hasJobCreate = authorizationService.isPermitted(permissionFactory.newPermission(JOB_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasJobRead = authorizationService.isPermitted(permissionFactory.newPermission(JOB_DOMAIN, Actions.read, kapuaSession.getScopeId()));
        boolean hasJobUpdate = authorizationService.isPermitted(permissionFactory.newPermission(JOB_DOMAIN, Actions.write, kapuaSession.getScopeId()));
        boolean hasJobDelete = authorizationService.isPermitted(permissionFactory.newPermission(JOB_DOMAIN, Actions.delete, kapuaSession.getScopeId()));

        //
        // Get account info
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.find(kapuaSession.getScopeId());

        //
        // Convert entities
        GwtUser gwtUser = KapuaGwtUserModelConverter.convertUser(user);
        GwtAccount gwtAccount = KapuaGwtAccountModelConverter.convertAccount(account);

        //
        // Build the session
        GwtSession gwtSession = new GwtSession();

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

        // Permission info
        gwtSession.setAccountCreatePermission(hasAccountCreate);
        gwtSession.setAccountReadPermission(hasAccountRead);
        gwtSession.setAccountUpdatePermission(hasAccountUpdate);
        gwtSession.setAccountDeletePermission(hasAccountDelete);
        gwtSession.setAccountAllPermission(hasAccountAll);

        gwtSession.setDeviceCreatePermission(hasDeviceCreate);
        gwtSession.setDeviceReadPermission(hasDeviceRead);
        gwtSession.setDeviceUpdatePermission(hasDeviceUpdate);
        gwtSession.setDeviceDeletePermission(hasDeviceDelete);
        gwtSession.setDeviceManagePermission(hasDeviceManage);

        gwtSession.setDataReadPermission(hasDataRead);

        gwtSession.setTagCreatePermission(hasTagCreate);
        gwtSession.setTagReadPermission(hasTagRead);
        gwtSession.setTagUpdatePermission(hasTagUpdate);
        gwtSession.setTagDeletePermission(hasTagDelete);

        gwtSession.setUserCreatePermission(hasUserCreate);
        gwtSession.setUserReadPermission(hasUserRead);
        gwtSession.setUserUpdatePermission(hasUserUpdate);
        gwtSession.setUserDeletePermission(hasUserDelete);

        gwtSession.setRoleCreatePermission(hasRoleCreate);
        gwtSession.setRoleReadPermission(hasRoleRead);
        gwtSession.setRoleUpdatePermission(hasRoleUpdate);
        gwtSession.setRoleDeletePermission(hasRoleDelete);

        gwtSession.setGroupCreatePermission(hasGroupCreate);
        gwtSession.setGroupReadPermission(hasGroupRead);
        gwtSession.setGroupUpdatePermission(hasGroupUpdate);
        gwtSession.setGroupDeletePermission(hasGroupDelete);

        gwtSession.setCredentialCreatePermission(hasCredentialCreate);
        gwtSession.setCredentialReadPermission(hasCredentialRead);
        gwtSession.setCredentialUpdatePermission(hasCredentialUpdate);
        gwtSession.setCredentialDeletePermission(hasCredentialDelete);

        gwtSession.setConnectionCreatePermission(hasConnectionCreate);
        gwtSession.setConnectionReadPermission(hasConnectionRead);
        gwtSession.setConnectionUpdatePermission(hasConnectionUpdate);
        gwtSession.setConnectionDeletePermission(hasConnectionDelete);

        gwtSession.setJobCreatePermission(hasJobCreate);
        gwtSession.setJobReadPermission(hasJobRead);
        gwtSession.setJobUpdatePermission(hasJobUpdate);
        gwtSession.setJobDeletePermission(hasJobDelete);

        //
        // Saving session data in session
        // Session session = currentUser.getSession();
        // session.setAttribute(SESSION_CURRENT, gwtSession);
        // session.setAttribute(SESSION_CURRENT_USER, user);

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

}
