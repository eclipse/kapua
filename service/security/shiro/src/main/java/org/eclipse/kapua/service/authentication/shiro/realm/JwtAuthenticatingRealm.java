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

import java.time.Duration;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
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
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialImpl;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.sso.jwt.JwtProcessor;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        final KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        final List<String> audiences = setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED);
        final List<String> expectedIssuers = setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_ISSUER_ALLOWED);

        this.jwtProcessor = new JwtProcessor(audiences, expectedIssuers, Duration.ofHours(1));
        setCredentialsMatcher(new JwtCredentialsMatcher(jwtProcessor));
    }

    @Override
    public void destroy() throws Exception {
        if (this.jwtProcessor != null) {
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

        final String name = extractUserName(jwt);
        logger.debug("JWT contains user name: {}", name);

        // Get the associated user by name

        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.findByName(name));
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

        final Credential credential = new CredentialImpl(user.getScopeId(), user.getId(), CredentialType.JWT, jwt);

        // Build AuthenticationInfo

        return new LoginAuthenticationInfo(getName(),
                account,
                user,
                credential);
    }

    /**
     * Extract the user name from the JWT
     * 
     * @param jwt
     *            the token to use
     * @return the username, never returns {@code null}
     * @throws ShiroException
     *             in case the user name could not be extracted
     */
    private String extractUserName(String jwt) {
        final String name;
        try {
            final JwtContext ctx = jwtProcessor.process(jwt);
            name = ctx.getJwtClaims().getClaimValue("preferred_username", String.class);
        } catch (final Exception e) {
            throw new ShiroException("Failed to parse JWT", e);
        }

        if (name == null || name.isEmpty()) {
            throw new ShiroException("'preferred_username' missing on JWT");
        }

        return name;
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
