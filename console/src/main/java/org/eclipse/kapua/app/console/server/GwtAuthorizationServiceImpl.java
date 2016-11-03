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

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtUser;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.service.GwtAuthorizationService;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.UsernamePasswordTokenFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifecycleDomain;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GwtAuthorizationServiceImpl extends KapuaRemoteServiceServlet implements GwtAuthorizationService {

    private static final long   serialVersionUID     = -3919578632016541047L;

    private static final Logger s_logger             = LoggerFactory.getLogger(GwtAuthorizationServiceImpl.class);

    public static final String  SESSION_CURRENT      = "console.current.session";
    public static final String  SESSION_CURRENT_USER = "console.current.user";

    /**
     * Login call in response to the login dialog.
     */
    public GwtSession login(GwtUser tmpUser)
            throws GwtKapuaException {
        // VIP
        // keep this here to make sure we initialize the logger.
        // Without the following, console logger may not log anything when deployed into tomcat.
        s_logger.info(">>> THIS IS INFO <<<");
        s_logger.warn(">>> THIS IS WARN <<<");
        s_logger.debug(">>> THIS IS DEBUG <<<");

        GwtSession gwtSession = null;
        try {
            // Get the user
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            UsernamePasswordTokenFactory credentialsFactory = locator.getFactory(UsernamePasswordTokenFactory.class);
            AuthenticationCredentials credentials = credentialsFactory.newInstance(tmpUser.getUsername(),
                                                                                   tmpUser.getPassword().toCharArray());

            // Login
            authenticationService.login(credentials);

            // Get the session infos
            gwtSession = establishSession();
        } catch (Throwable t) {
            logout();
            KapuaExceptionHandler.handle(t);
        }
        return gwtSession;
    }

    /**
     * Return the currently authenticated user or null if no session has been established.
     */
    public GwtSession getCurrentSession()
            throws GwtKapuaException {
        GwtSession gwtSession = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            if (currentUser != null && currentUser.isAuthenticated()) {

                Session session = currentUser.getSession();
                gwtSession = (GwtSession) session.getAttribute(SESSION_CURRENT);

                // Store the user information in the sessions
                String username = (String) currentUser.getPrincipal();

                KapuaLocator locator = KapuaLocator.getInstance();
                UserService userService = locator.getService(UserService.class);
                User user = userService.findByName(username);

                // get the session
                if (gwtSession == null) {
                    gwtSession = establishSession();
                } else {
                    gwtSession.setGwtUser(KapuaGwtModelConverter.convert(user));
                }
            }
        } catch (Throwable t) {
            s_logger.warn("Error in getCurrentSession.", t);
            KapuaExceptionHandler.handle(t);
        }

        return gwtSession;
    }

    private GwtSession establishSession()
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();

        //
        // Get info from session
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();

        //
        // Get user info
        UserService userService = locator.getService(UserService.class);
        User user = userService.find(kapuaSession.getScopeId(),
                                     kapuaSession.getUserId());

        //
        // Get permission info
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        boolean hasAccountCreate = authorizationService.isPermitted(permissionFactory.newPermission(AccountDomain.ACCOUNT, Actions.write, kapuaSession.getScopeId()));
        boolean hasAccountRead = authorizationService.isPermitted(permissionFactory.newPermission(AccountDomain.ACCOUNT, Actions.read, kapuaSession.getScopeId()));
        boolean hasAccountUpdate = authorizationService.isPermitted(permissionFactory.newPermission(AccountDomain.ACCOUNT, Actions.write, kapuaSession.getScopeId()));
        boolean hasAccountDelete = authorizationService.isPermitted(permissionFactory.newPermission(AccountDomain.ACCOUNT, Actions.delete, kapuaSession.getScopeId()));
        boolean hasAccountAll = authorizationService.isPermitted(permissionFactory.newPermission(AccountDomain.ACCOUNT, null, null));

        boolean hasDeviceCreate = authorizationService.isPermitted(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.write, kapuaSession.getScopeId()));
        boolean hasDeviceRead = authorizationService.isPermitted(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.read, kapuaSession.getScopeId()));
        boolean hasDeviceUpdate = authorizationService.isPermitted(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.write, kapuaSession.getScopeId()));
        boolean hasDeviceDelete = authorizationService.isPermitted(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.delete, kapuaSession.getScopeId()));
        boolean hasDeviceManage = authorizationService.isPermitted(permissionFactory.newPermission(DeviceLifecycleDomain.DEVICE_LIFECYCLE, Actions.write, kapuaSession.getScopeId()));

        boolean hasDataRead = authorizationService.isPermitted(permissionFactory.newPermission("data", Actions.read, kapuaSession.getScopeId()));

        boolean hasUserCreate = authorizationService.isPermitted(permissionFactory.newPermission("user", Actions.write, kapuaSession.getScopeId()));
        boolean hasUserRead = authorizationService.isPermitted(permissionFactory.newPermission("user", Actions.read, kapuaSession.getScopeId()));
        boolean hasUserUpdate = authorizationService.isPermitted(permissionFactory.newPermission("user", Actions.write, kapuaSession.getScopeId()));
        boolean hasUserDelete = authorizationService.isPermitted(permissionFactory.newPermission("user", Actions.delete, kapuaSession.getScopeId()));

        //
        // Get account info
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.find(kapuaSession.getScopeId());

        //
        // Convert entities
        GwtUser gwtUser = KapuaGwtModelConverter.convert(user);
        GwtAccount gwtAccount = KapuaGwtModelConverter.convert(account);

        //
        // Build the session
        GwtSession gwtSession = new GwtSession();

        // Console info
        SystemSetting commonsConfig = SystemSetting.getInstance();
        gwtSession.setVersion(commonsConfig.getString(SystemSettingKey.VERSION));
        gwtSession.setBuildVersion(commonsConfig.getString(SystemSettingKey.BUILD_VERSION));
        gwtSession.setBuildNumber(commonsConfig.getString(SystemSettingKey.BUILD_NUMBER));

        // User info
        gwtSession.setGwtUser(gwtUser);
        gwtSession.setGwtAccount(gwtAccount);
        gwtSession.setRootAccount(gwtAccount);
        gwtSession.setSelectedAccount(gwtAccount);

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

        gwtSession.setUserCreatePermission(hasUserCreate);
        gwtSession.setUserReadPermission(hasUserRead);
        gwtSession.setUserUpdatePermission(hasUserUpdate);
        gwtSession.setUserDeletePermission(hasUserDelete);

        //
        // Saving session data in session
        // Session session = currentUser.getSession();
        // session.setAttribute(SESSION_CURRENT, gwtSession);
        // session.setAttribute(SESSION_CURRENT_USER, user);

        return gwtSession;
    }

    /**
     * Returns true if the currently connected user has the specified permission granted.
     */
    public Boolean hasAccess(String gwtPermission)
            throws GwtKapuaException {
        Boolean hasAccess = false;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthorizationService authorizationService = locator.getService(AuthorizationService.class);

            // Parse from string
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission permission = permissionFactory.parseString(gwtPermission);

            // Check
            hasAccess = authorizationService.isPermitted(permission);
        }
        catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return hasAccess;
    }

    /**
     * Logout call in response to the logout link/button.
     */
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
