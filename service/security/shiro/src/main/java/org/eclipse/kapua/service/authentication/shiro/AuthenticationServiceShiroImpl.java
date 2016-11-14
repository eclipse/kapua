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
package org.eclipse.kapua.service.authentication.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Authentication service implementation.
 * 
 * since 1.0
 * 
 */
public class AuthenticationServiceShiroImpl implements AuthenticationService, KapuaService {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationServiceShiroImpl.class);

    static {
        // org.apache.shiro.config.Ini
        // org.apache.shiro.config.IniSecurityManagerFactory
        // org.apache.shiro.util.Factory;
        // Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        // SecurityUtils.setSecurityManager(securityManager);

        // Make the SecurityManager instance available to the entire application:
        Collection<Realm> realms = new ArrayList<Realm>();
        try {
            realms.add(new org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticatingRealm());
            realms.add(new org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizingRealm());
        } catch (KapuaException e) {
            // TODO add default realm???
        }

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setAuthenticator(new KapuaAuthenticator());
        logger.info("Set '{}' as shiro authenticator", KapuaAuthenticator.class);
        // if (defaultSecurityManager.getAuthenticator() instanceof ModularRealmAuthenticator) {
        // KapuaAuthenticationStrategy authenticationStrategy = new KapuaAuthenticationStrategy();
        // ((ModularRealmAuthenticator) defaultSecurityManager.getAuthenticator()).setAuthenticationStrategy(authenticationStrategy);
        // logger.info("Set '{}' as shiro authentication strategy ", KapuaAuthenticationStrategy.class);
        // }
        // else {
        // logger.warn("Cannot set '{}' as shiro authentication strategy! Authenticator class is '{}' and this option is only available for ModularRealmAuthenticator!",
        // new Object[]{KapuaAuthenticationStrategy.class, defaultSecurityManager.getAuthenticator() != null ? defaultSecurityManager.getAuthenticator().getClass() : "null"});
        // }

        defaultSecurityManager.setRealms(realms);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        // TODO in the old application this code was executed only inside the broker context
        // but now it's no more needed since we are using an external service (that returns a token) to authenticate the broker component
        if (defaultSecurityManager.getSessionManager() instanceof AbstractSessionManager) {
            ((AbstractSessionManager) defaultSecurityManager.getSessionManager()).setGlobalSessionTimeout(-1);
            logger.info("Shiro global session timeout set to indefinite.");
        } else {
            logger.warn("Cannot set Shiro global session timeout to indefinite.");
        }
        if (defaultSecurityManager.getSessionManager() instanceof AbstractValidatingSessionManager) {
            ((AbstractValidatingSessionManager) defaultSecurityManager.getSessionManager()).setSessionValidationSchedulerEnabled(false);
            logger.info("Shiro global session validator scheduler disabled.");
        } else {
            logger.warn("Cannot disable Shiro session validator scheduler.");
        }
    }

    @Override
    public AccessToken login(AuthenticationCredentials authenticationToken)
            throws KapuaException {

        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            logger.info("Thread already authenticated for thread '{}' - '{}' - '{}'", new Object[] { Thread.currentThread().getId(), Thread.currentThread().getName(), currentUser.toString() });
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.SUBJECT_ALREADY_LOGGED);
        }

        AccessToken accessToken;
        if (authenticationToken instanceof UsernamePasswordCredentialsImpl) {

            UsernamePasswordCredentialsImpl usernamePasswordToken = (UsernamePasswordCredentialsImpl) authenticationToken;

            MDC.put(KapuaSecurityUtils.MDC_USERNAME, usernamePasswordToken.getUsername());

            UsernamePasswordToken shiroToken = new UsernamePasswordToken(usernamePasswordToken.getUsername(), usernamePasswordToken.getPassword());
            try {
                //
                // Shiro login
                currentUser.login(shiroToken);

                Subject shiroSubject = SecurityUtils.getSubject();
                Session shiroSession = shiroSubject.getSession();

                KapuaEid scopeId = (KapuaEid) shiroSession.getAttribute("scopeId");
                KapuaEid userId = (KapuaEid) shiroSession.getAttribute("userId");

                //
                // Create the access token
                KapuaLocator locator = KapuaLocator.getInstance();
                AccessTokenService accessTokenService = locator.getService(AccessTokenService.class);
                AccessTokenFactory accessTokenFactory = locator.getFactory(AccessTokenFactory.class);

                // Retrieve TTL access token
                KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();
                long expireTime = settings.getLong(KapuaAuthenticationSettingKeys.AUTHENTICATION_TOKEN_EXPIRE_AFTER);
                Date expireDate = new Date(new Date().getTime() + expireTime);
                String generatedTokenKey = generateToken();
                AccessTokenCreator accessTokenCreator = accessTokenFactory.newCreator(scopeId, userId, generatedTokenKey, expireDate);

                try {
                    accessToken = KapuaSecurityUtils.doPriviledge(new Callable<AccessToken>() {

                        @Override
                        public AccessToken call()
                                throws Exception {
                            return accessTokenService.create(accessTokenCreator);
                        }
                    });
                } catch (Exception e) {
                    throw KapuaAuthenticationException.internalError(e);
                }

                //
                // Add token to session
                KapuaSession kapuaSession = new KapuaSession(accessToken, scopeId, userId, usernamePasswordToken.getUsername());

                KapuaSecurityUtils.setSession(kapuaSession);

                shiroSubject.getSession().setAttribute(KapuaSession.KAPUA_SESSION_KEY, kapuaSession);
                logger.info("Login for thread '{}' - '{}' - '{}'", new Object[] { Thread.currentThread().getId(), Thread.currentThread().getName(), shiroSubject.toString() });

            } catch (ShiroException se) {

                KapuaAuthenticationException kae;
                if (se instanceof UnknownAccountException) {
                    kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_USERNAME, se, usernamePasswordToken.getUsername());
                } else if (se instanceof DisabledAccountException) {
                    kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.DISABLED_USERNAME, se, usernamePasswordToken.getUsername());
                } else if (se instanceof LockedAccountException) {
                    kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.LOCKED_USERNAME, se, usernamePasswordToken.getUsername());
                } else if (se instanceof IncorrectCredentialsException) {
                    kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS, se, usernamePasswordToken.getUsername());
                } else if (se instanceof ExpiredCredentialsException) {
                    kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.EXPIRED_CREDENTIALS, se, usernamePasswordToken.getUsername());
                } else {
                    throw KapuaAuthenticationException.internalError(se);
                }

                currentUser.logout();

                throw kae;
            }
        } else {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TOKEN_PROVIDED);
        }

        return accessToken;
    }

    @Override
    public void logout()
            throws KapuaException {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
        } finally {
            KapuaSecurityUtils.clearSession();
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AccessToken getToken(String tokenId) throws KapuaException {
        // KapuaSession kapuaSession = sessionMap.get(tokenId);
        // if (kapuaSession!=null) {
        // return kapuaSession.getAccessToken();
        // }
        // TODO choose the appropriate exception
        throw new KapuaException(KapuaErrorCodes.ENTITY_NOT_FOUND);
    }

}
