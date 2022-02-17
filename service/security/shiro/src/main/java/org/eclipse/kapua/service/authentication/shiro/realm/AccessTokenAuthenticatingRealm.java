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
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.AccessTokenCredentialsImpl;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenAttributes;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccessTokenService ACCESS_TOKEN_SERVICE = LOCATOR.getService(AccessTokenService.class);
    private static final AccessTokenFactory ACCESS_TOKEN_FACTORY = LOCATOR.getFactory(AccessTokenFactory.class);
    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);

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
        //
        // Extract credentials
        AccessTokenCredentialsImpl token = (AccessTokenCredentialsImpl) authenticationToken;
        String tokenTokenId = token.getTokenId();

        Date now = new Date();
        //
        // Find accessToken
        final AccessToken accessToken;
        try {
            AccessTokenQuery accessTokenQuery = ACCESS_TOKEN_FACTORY.newQuery(null);
            AndPredicate andPredicate = accessTokenQuery.andPredicate(
                    accessTokenQuery.attributePredicate(AccessTokenAttributes.EXPIRES_ON, new java.sql.Timestamp(now.getTime()), Operator.GREATER_THAN_OR_EQUAL),
                    accessTokenQuery.attributePredicate(AccessTokenAttributes.INVALIDATED_ON, null, Operator.IS_NULL),
                    accessTokenQuery.attributePredicate(AccessTokenAttributes.TOKEN_ID, tokenTokenId)
            );
            accessTokenQuery.setPredicate(andPredicate);
            accessTokenQuery.setLimit(1);
            accessToken = KapuaSecurityUtils.doPrivileged(() -> ACCESS_TOKEN_SERVICE.query(accessTokenQuery).getFirstItem());
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Unexpected error while looking for the access token!", e);
        }

        // Check existence
        if (accessToken == null) {
            throw new UnknownAccountException();
        }

        // Check validity
        if ((accessToken.getExpiresOn() != null && accessToken.getExpiresOn().before(now)) ||
                (accessToken.getInvalidatedOn() != null && accessToken.getInvalidatedOn().before(now))) {
            throw new ExpiredCredentialsException();
        }

        //
        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> USER_SERVICE.find(accessToken.getScopeId(), accessToken.getUserId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Unexpected error while looking for the user!", e);
        }

        //
        // Check user
        checkUser(user);

        //
        // Check account
        Account account = checkAccount(user.getScopeId());

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
