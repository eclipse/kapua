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

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.utils.JwtProcessors;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.sso.jwt.JwtProcessor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Destroyable;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ApiKeyCredentials} based {@link AuthenticatingRealm} implementation.
 */
public class JwtAuthenticatingRealm extends KapuaAuthenticatingRealm implements Destroyable {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticatingRealm.class);
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);
    private static final CredentialFactory CREDENTIAL_FACTORY = LOCATOR.getFactory(CredentialFactory.class);

    /**
     * Realm name
     */
    private static final String REALM_NAME = "jwtAuthenticatingRealm";

    /**
     * JWT Processor
     */
    private JwtProcessor jwtProcessor;

    /**
     * Constructor
     */
    public JwtAuthenticatingRealm() {
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
    protected LoginAuthenticationInfo buildAuthenticationInfo(AuthenticationToken authenticationToken) {
        //
        // Extract credentials
        JwtCredentialsImpl token = (JwtCredentialsImpl) authenticationToken;
        String jwt = token.getJwt();

        final String id = extractExternalId(jwt);
        logger.debug("JWT contains external id: {}", id);

        //
        // Get the associated user by external id
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> USER_SERVICE.findByExternalId(id));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error looking up the user", e);
        }

        //
        // Find account
        final Account account;
        try {
            account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(user.getScopeId()));
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new ShiroException("Error while find account!", e);
        }

        //
        // Create credential
        final Credential credential = CREDENTIAL_FACTORY.newCredential(user.getScopeId(), user.getId(), CredentialType.JWT, jwt, CredentialStatus.ENABLED, null);

        //
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
    protected void assertCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info) {
        final LoginAuthenticationInfo kapuaInfo = (LoginAuthenticationInfo) info;

        super.assertCredentialsMatch(authenticationToken, info);

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
