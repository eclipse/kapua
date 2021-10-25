/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.Destroyable;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialImpl;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.utils.JwtProcessors;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ApiKeyCredentials} based {@link AuthenticatingRealm} implementation.
 *
 * @since 1.0.0
 */
public class JwtAuthenticatingRealm extends KapuaAuthenticatingRealm implements Destroyable {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticatingRealm.class);

    /**
     * Realm name.
     */
    public static final String REALM_NAME = "jwtAuthenticatingRealm";

    /**
     * JWT Processor.
     */
    private JwtProcessor jwtProcessor;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public JwtAuthenticatingRealm() {
        setName(REALM_NAME);
    }

    @Override
    protected void onInit() {
        super.onInit();

        try {
            jwtProcessor = JwtProcessors.createDefault();
            setCredentialsMatcher(new JwtCredentialsMatcher(jwtProcessor));
        } catch (OpenIDException se) {
            throw new ShiroException("Unexpected error while creating Jwt Processor!", se);
        }
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
        String idToken = token.getIdToken();

        //
        // Get Services
        final KapuaLocator locator;
        final UserService userService;
        try {
            locator = KapuaLocator.getInstance();
            userService = locator.getService(UserService.class);
        } catch (KapuaRuntimeException kre) {
            throw new ShiroException("Unexpected error while loading KapuaServices!", kre);
        }

        String id = extractExternalId(idToken);
        LOG.debug("JWT contains external id: {}", id);

        //
        // Get the associated user by external id
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.findByExternalId(id));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Unexpected error while looking for the user", e);
        }

        //
        // Check user
        checkUser(user);

        //
        // Check account
        Account account = checkAccount(user.getScopeId());

        //
        // Create credential
        Credential credential = new CredentialImpl(user.getScopeId(), user.getId(), CredentialType.JWT, idToken, CredentialStatus.ENABLED, null);

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
     * @param jwt the token to use
     * @return the subject, never returns {@code null}
     * @throws ShiroException in case the subject could not be extracted
     */
    private String extractExternalId(String jwt) {
        final String id;
        try {
            final JwtContext ctx = jwtProcessor.process(jwt);
            id = ctx.getJwtClaims().getClaimValueAsString(jwtProcessor.getExternalIdClaimName());
        } catch (Exception e) {
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
        LoginAuthenticationInfo kapuaInfo = (LoginAuthenticationInfo) info;

        super.assertCredentialsMatch(authcToken, info);

        //
        // Populate Session with info
        populateSession(SecurityUtils.getSubject(), kapuaInfo);
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtCredentialsImpl;
    }
}
