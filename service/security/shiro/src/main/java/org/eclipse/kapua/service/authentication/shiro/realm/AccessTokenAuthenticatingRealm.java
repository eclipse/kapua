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
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthenticatingRealm;

/**
 * {@link AccessTokenCredentials} based {@link AuthenticatingRealm} implementation.
 * <p>
 * since 1.0
 */
public class AccessTokenAuthenticatingRealm extends KapuaAuthenticatingRealm {

    /**
     * Realm name
     */
    public static final String REALM_NAME = "accessTokenAuthenticatingRealm";

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccessTokenService ACCESS_TOKEN_SERVICE = LOCATOR.getService(AccessTokenService.class);
    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);

    /**
     * Constructor
     *
     * @throws KapuaException
     */
    public AccessTokenAuthenticatingRealm() throws KapuaException {
        setName(REALM_NAME);

        // Credential matcher for access tokens
        setCredentialsMatcher(new AccessTokenCredentialsMatcher());
    }

    @Override
    protected SessionAuthenticationInfo buildAuthenticationInfo(AuthenticationToken authenticationToken) {
        //
        // Extract credentials
        AccessTokenCredentialsImpl token = (AccessTokenCredentialsImpl) authenticationToken;
        String tokenTokenId = token.getTokenId();

        //
        // Find accessToken
        final AccessToken accessToken;
        try {
            accessToken = KapuaSecurityUtils.doPrivileged(() -> ACCESS_TOKEN_SERVICE.findByTokenId(tokenTokenId));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find access token!", e);
        }

        //
        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> USER_SERVICE.find(accessToken.getScopeId(), accessToken.getUserId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find user!", e);
        }

        //
        // Find account
        final Account account;
        try {
            account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(user.getScopeId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find account!", e);
        }

        //
        // BuildAuthenticationInfo
        return new SessionAuthenticationInfo(getName(),
                account,
                user,
                accessToken);
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof AccessTokenCredentialsImpl;
    }
}
