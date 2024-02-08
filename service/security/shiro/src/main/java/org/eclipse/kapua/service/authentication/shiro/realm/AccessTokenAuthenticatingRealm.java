/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaParsingException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.exceptions.ExpiredAccessTokenException;
import org.eclipse.kapua.service.authentication.shiro.exceptions.InvalidatedAccessTokenException;
import org.eclipse.kapua.service.authentication.shiro.exceptions.MalformedAccessTokenException;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.authentication.AuthenticationService;

import java.util.Date;

/**
 * {@link AccessTokenCredentials} based {@link AuthenticatingRealm} implementation.
 *
 * @since 1.0.0
 */
public class AccessTokenAuthenticatingRealm extends KapuaAuthenticatingRealm {

    /**
     * Realm name.
     */
    public static final String REALM_NAME = "accessTokenAuthenticatingRealm";
    private final AuthenticationService authenticationService = KapuaLocator.getInstance().getService(AuthenticationService.class);
    private final UserService userService = KapuaLocator.getInstance().getService(UserService.class);

    /**
     * Constructor
     *
     * @since 1.0.0
     */
    public AccessTokenAuthenticatingRealm() {
        setName(REALM_NAME);

        CredentialsMatcher credentialsMatcher = new AccessTokenCredentialsMatcher();
        setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // Extract credentials
        AccessTokenCredentialsImpl token = (AccessTokenCredentialsImpl) authenticationToken;
        String tokenTokenId = token.getTokenId();
        // Find accessToken
        final AccessToken accessToken;
        try {
            accessToken = authenticationService.findAccessToken(tokenTokenId);
        } catch (KapuaParsingException e) {
            throw new MalformedAccessTokenException();
        } catch (KapuaException ke) {
            throw new AuthenticationException();
        }

        // Check existence
        if (accessToken == null) {
            throw new UnknownAccountException();
        }

        // Check validity
        Date now = new Date();
        if ((accessToken.getExpiresOn() != null && accessToken.getExpiresOn().before(now))) {
            throw new ExpiredAccessTokenException();
        }

        if (accessToken.getInvalidatedOn() != null && accessToken.getInvalidatedOn().before(now)) {
            throw new InvalidatedAccessTokenException();
        }

        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.find(accessToken.getScopeId(), accessToken.getUserId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Unexpected error while looking for the user!", e);
        }
        // Check user
        checkUser(user);
        // Check account
        Account account = checkAccount(user.getScopeId());
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
        // Credential match
        super.assertCredentialsMatch(authcToken, info);
        // Set kapua session
        AccessToken accessToken = kapuaInfo.getAccessToken();
        KapuaSession kapuaSession = new KapuaSession(accessToken, accessToken.getScopeId(), accessToken.getUserId());
        KapuaSecurityUtils.setSession(kapuaSession);
        // Set shiro session
        Subject currentSubject = SecurityUtils.getSubject();
        currentSubject.getSession().setAttribute(KapuaSession.KAPUA_SESSION_KEY, kapuaSession);
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof AccessTokenCredentialsImpl;
    }
}
