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
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.ApiKeyCredentialsImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * {@link ApiKeyCredentials} based {@link AuthenticatingRealm} implementation.
 *
 * @since 1.0.0
 */
public class ApiKeyAuthenticatingRealm extends KapuaAuthenticatingRealm {

    private static final Logger LOG = LoggerFactory.getLogger(ApiKeyAuthenticatingRealm.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    /**
     * Realm name.
     */
    public static final String REALM_NAME = "apiKeyAuthenticatingRealm";

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public ApiKeyAuthenticatingRealm() {
        setName(REALM_NAME);

        CredentialsMatcher credentialsMather = new ApiKeyCredentialsMatcher();
        setCredentialsMatcher(credentialsMather);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //
        // Extract credentials
        ApiKeyCredentialsImpl token = (ApiKeyCredentialsImpl) authenticationToken;
        String tokenApiKey = token.getApiKey();

        //
        // Get Services
        UserService userService;
        CredentialService credentialService;

        try {
            userService = LOCATOR.getService(UserService.class);
            credentialService = LOCATOR.getService(CredentialService.class);
        } catch (KapuaRuntimeException kre) {
            throw new ShiroException("Unexpected error while loading KapuaServices!", kre);
        }

        //
        // Find credential
        Credential credential = null;
        try {
            credential = KapuaSecurityUtils.doPrivileged(() -> credentialService.findByApiKey(tokenApiKey));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (KapuaIllegalArgumentException ae) {
            LOG.warn("The given Api Key is not valid. Subsequent UnknownAccountException expected! Given ApiKey: {}", tokenApiKey);
        } catch (Exception e) {
            throw new ShiroException("Unexpected error while looking for the credentials!", e);
        }

        //
        // Check credential
        checkCredential(credential);

        //
        // Get CredentialService config
        Map<String, Object> credentialServiceConfig = getCredentialServiceConfig(credential.getScopeId());

        //
        // Check credential lockout
        checkCredentialLockout(credential, credentialServiceConfig);

        //
        // Get the associated user by name
        User user;
        try {
            Credential finalCredential = credential;
            user = KapuaSecurityUtils.doPrivileged(() -> userService.find(finalCredential.getScopeId(), finalCredential.getUserId()));
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
        return new LoginAuthenticationInfo(getName(),
                account,
                user,
                credential,
                credentialServiceConfig);
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info)
            throws AuthenticationException {
        LoginAuthenticationInfo kapuaInfo = (LoginAuthenticationInfo) info;
        CredentialService credentialService = LOCATOR.getService(CredentialService.class);
        try {
            super.assertCredentialsMatch(authcToken, info);
        } catch (AuthenticationException authenticationEx) {
            try {
                Credential failedCredential = (Credential) kapuaInfo.getCredentials();
                KapuaSecurityUtils.doPrivileged(() -> {
                    Map<String, Object> credentialServiceConfig = kapuaInfo.getCredentialServiceConfig();
                    boolean lockoutPolicyEnabled = (boolean) credentialServiceConfig.get("lockoutPolicy.enabled");
                    if (lockoutPolicyEnabled) {
                        Date now = new Date();
                        int resetAfterSeconds = (int) credentialServiceConfig.get("lockoutPolicy.resetAfter");

                        Date firstLoginFailure;
                        boolean resetAttempts = failedCredential.getFirstLoginFailure() == null || now.after(failedCredential.getLoginFailuresReset());
                        if (resetAttempts) {
                            firstLoginFailure = now;
                            failedCredential.setLoginFailures(1);
                        } else {
                            firstLoginFailure = failedCredential.getFirstLoginFailure();
                            failedCredential.setLoginFailures(failedCredential.getLoginFailures() + 1);
                        }

                        Date loginFailureWindowExpiration = new Date(firstLoginFailure.getTime() + (resetAfterSeconds * 1000L));
                        failedCredential.setFirstLoginFailure(firstLoginFailure);
                        failedCredential.setLoginFailuresReset(loginFailureWindowExpiration);
                        int maxLoginFailures = (int) credentialServiceConfig.get("lockoutPolicy.maxFailures");
                        if (failedCredential.getLoginFailures() >= maxLoginFailures) {
                            long lockoutDuration = (int) credentialServiceConfig.get("lockoutPolicy.lockDuration");
                            Date resetDate = new Date(now.getTime() + (lockoutDuration * 1000));
                            failedCredential.setLockoutReset(resetDate);
                        }
                    }

                    credentialService.update(failedCredential);
                });
            } catch (KapuaException kex) {
                throw new ShiroException("Error while updating lockout policy", kex);
            }
            throw authenticationEx;
        }

        //
        // Reset Credential lockout policy after successful login
        resetCredentialLockout((Credential) kapuaInfo.getCredentials());

        //
        // Populate Session with info
        populateSession(SecurityUtils.getSubject(), kapuaInfo);
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof ApiKeyCredentialsImpl;
    }
}
