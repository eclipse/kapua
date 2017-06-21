/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Destroyable;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialImpl;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.utils.JwtProcessors;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.sso.jwt.JwtProcessor;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * {@link ApiKeyCredentials} based {@link AuthenticatingRealm} implementation.
 */
public class JwtAuthenticatingRealm extends AuthenticatingRealm implements Destroyable {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticatingRealm.class);

    /**
     * Realm name
     */
    public static final String REALM_NAME = "jwtAuthenticatingRealm";

    /**
     * JWT Processor
     */
    private JwtProcessor jwtProcessor;

    /**
     * Constructor
     *
     * @throws KapuaException
     */
    public JwtAuthenticatingRealm() throws KapuaException {
        setName(REALM_NAME);
    }

    @Override
    protected void onInit() {
        super.onInit();

        jwtProcessor = JwtProcessors.createDefault();
        setCredentialsMatcher(new JwtCredentialsMatcher(jwtProcessor));
    }

    @Override
    public void destroy() throws Exception {
        if (jwtProcessor != null) {
            jwtProcessor.close();
            jwtProcessor = null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //
        // Extract credentials
        JwtCredentialsImpl token = (JwtCredentialsImpl) authenticationToken;
        String jwt = token.getJwt();

        //
        // Get Services
        final KapuaLocator locator;
        final UserService userService;
        final AccountService accountService;
        try {
            locator = KapuaLocator.getInstance();
            userService = locator.getService(UserService.class);
            accountService = locator.getService(AccountService.class);
        } catch (KapuaRuntimeException kre) {
            throw new ShiroException("Error while getting services!", kre);
        }

        final String id = extractExternalId(jwt);
        logger.debug("JWT contains external id: {}", id);

        // Get the associated user by name

        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.findByExternalId(id));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error looking up the user", e);
        }

        // Check user existence

        if (user == null) {
            throw new UnknownAccountException();
        }

        // Check disabled

        if (UserStatus.DISABLED.equals(user.getStatus())) {
            throw new DisabledAccountException();
        }

        // Check if expired
        if (user.getExpirationDate() != null && !user.getExpirationDate().after(new Date())) {
            throw new ExpiredCredentialsException();
        }

        // Find account

        final Account account;
        try {
            account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(user.getScopeId()));
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new ShiroException("Error while find account!", e);
        }

        // Check account existence

        if (account == null) {
            throw new UnknownAccountException();
        }

        // Create credential

        final Credential credential = new CredentialImpl(user.getScopeId(), user.getId(), CredentialType.JWT, jwt, CredentialStatus.ENABLED, null);

        // Build AuthenticationInfo

        return new LoginAuthenticationInfo(getName(),
                account,
                user,
                credential,
               null);
    }

    /**
     * Extract the subject information
     *
     * @param jwt
     *            the token to use
     * @return the subject, never returns {@code null}
     * @throws ShiroException
     *             in case the subject could not be extracted
     */
    private String extractExternalId(String jwt) {
        final String id;
        try {
            final JwtContext ctx = jwtProcessor.process(jwt);
            id = ctx.getJwtClaims().getSubject();
        } catch (final Exception e) {
            throw new ShiroException("Failed to parse JWT", e);
        }

        if (id == null || id.isEmpty()) {
            throw new ShiroException("'subject' missing on JWT");
        }

        return id;
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info)
            throws AuthenticationException {
        final LoginAuthenticationInfo kapuaInfo = (LoginAuthenticationInfo) info;

        super.assertCredentialsMatch(authcToken, info);

        final Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("scopeId", kapuaInfo.getUser().getScopeId());
        session.setAttribute("userId", kapuaInfo.getUser().getId());
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtCredentialsImpl;
    }
}
