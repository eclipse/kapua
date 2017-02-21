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
package org.eclipse.kapua.service.authentication.shiro.realm;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AccessTokenCredentials} based {@link AuthenticatingRealm} implementation.
 * 
 * since 1.0
 * 
 */
public class AccessTokenAuthenticatingRealm extends AuthenticatingRealm {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(AccessTokenAuthenticatingRealm.class);

    /**
     * Realm name
     */
    public static final String REALM_NAME = "accessTokenAuthenticatingRealm";

    /**
     * Constructor
     * 
     * @throws KapuaException
     */
    public AccessTokenAuthenticatingRealm() throws KapuaException {
        setName(REALM_NAME);

        // Credential matcher for access tokens
        setCredentialsMatcher(new AccessTokenCredentialsMatcher());

        // // Access token caching
        // KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();
        // boolean cachingEnabled = settings.getBoolean(KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_CACHE_ENABLE);
        //
        // setCachingEnabled(cachingEnabled);
        //
        // if (cachingEnabled) {
        //
        //// setCacheManager(cacheManager);
        // }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //
        // Extract credentials
        AccessTokenCredentialsImpl token = (AccessTokenCredentialsImpl) authenticationToken;
        String tokenTokenId = token.getTokenId();

        //
        // Get Services
        KapuaLocator locator;
        UserService userService;
        AccountService accountService;
        AccessTokenService accessTokenService;

        try {
            locator = KapuaLocator.getInstance();
            userService = locator.getService(UserService.class);
            accountService = locator.getService(AccountService.class);
            accessTokenService = locator.getService(AccessTokenService.class);
        } catch (KapuaRuntimeException kre) {
            throw new ShiroException("Error while getting services!", kre);
        }

        //
        // Find accessToken
        final AccessToken accessToken;
        try {
            accessToken = KapuaSecurityUtils.doPrivileged(() -> accessTokenService.findByTokenId(tokenTokenId));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find access token!", e);
        }

        // Check existence
        if (accessToken == null) {
            throw new UnknownAccountException();
        }

        // Check validity
        if (accessToken.getExpiresOn() != null &&
                accessToken.getExpiresOn().before(new Date())) {
            throw new ExpiredCredentialsException();
        }

        //
        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.find(accessToken.getScopeId(), accessToken.getUserId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find user!", e);
        }

        // Check existence
        if (user == null) {
            throw new UnknownAccountException();
        }

        // Check disabled
        if (UserStatus.DISABLED.equals(user.getStatus())) {
            throw new DisabledAccountException();
        }

        //
        // Find account
        final Account account;
        try {
            account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(user.getScopeId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find account!", e);
        }

        // Check existence
        if (account == null) {
            throw new UnknownAccountException();
        }

        //
        // BuildAuthenticationInfo
        return new SessionAuthenticationInfo(getName(),
                account,
                user,
                accessToken);
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info)
            throws AuthenticationException {
        SessionAuthenticationInfo kapuaInfo = (SessionAuthenticationInfo) info;

        //
        // Credential match
        super.assertCredentialsMatch(authcToken, info);

        //
        // Set kapua session
        AccessToken accessToken = kapuaInfo.getAccessToken();
        KapuaSession kapuaSession = new KapuaSession(accessToken, accessToken.getScopeId(), accessToken.getUserId());
        KapuaSecurityUtils.setSession(kapuaSession);

        //
        // Set shiro session
        Subject currentSubject = SecurityUtils.getSubject();
        currentSubject.getSession().setAttribute(KapuaSession.KAPUA_SESSION_KEY, kapuaSession);
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof AccessTokenCredentialsImpl;
    }
}
