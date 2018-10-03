/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.exceptions.TemporaryLockedAccountException;
import org.eclipse.kapua.service.user.User;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

public abstract class LockoutPolicyAuthenticatingRealm extends KapuaAuthenticatingRealm {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CredentialService CREDENTIAL_SERVICE = LOCATOR.getService(CredentialService.class);

    /**
     * Realm name
     */
    private static final String REALM_NAME = "lockoutPolicyAuthenticatingRealm";

    LockoutPolicyAuthenticatingRealm() {
        setName(REALM_NAME);
    }

    @Override
    protected LoginAuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        LoginAuthenticationInfo authenticationInfo = (LoginAuthenticationInfo)super.doGetAuthenticationInfo(token);

        Account account = authenticationInfo.getAccount();
        User user = authenticationInfo.getUser();
        Credential credential = authenticationInfo.getCredential();

        // Check if lockout policy is blocking credential
        Map<String, Object> credentialServiceConfig;
        try {
            credentialServiceConfig = KapuaSecurityUtils.doPrivileged(() -> CREDENTIAL_SERVICE.getConfigValues(authenticationInfo.getAccount().getId()));
            boolean lockoutPolicyEnabled = (boolean) credentialServiceConfig.get("lockoutPolicy.enabled");
            if (lockoutPolicyEnabled) {
                Date now = new Date();
                if (credential.getLockoutReset() != null && now.before(credential.getLockoutReset())) {
                    throw new TemporaryLockedAccountException(credential.getLockoutReset());
                }
            }
        } catch (KapuaException kex) {
            throw new ShiroException("Error while checking lockout policy", kex);
        }

        return new LoginAuthenticationInfo(getName(), account, user, credential, credentialServiceConfig);
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
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
                        manageLockoutPolicy(credentialServiceConfig, failedCredential);
                    }

                    credentialService.update(failedCredential);
                });
            } catch (KapuaException kex) {
                throw new ShiroException("Error while updating lockout policy", kex);
            }
            throw authenticationEx;
        }
        Credential credential = (Credential) kapuaInfo.getCredentials();
        credential.setFirstLoginFailure(null);
        credential.setLoginFailuresReset(null);
        credential.setLockoutReset(null);
        credential.setLoginFailures(0);
        try {
            KapuaSecurityUtils.doPrivileged(() -> credentialService.update(credential));
        } catch (KapuaException kex) {
            throw new ShiroException("Error while updating lockout policy", kex);
        }
    }

    private void manageLockoutPolicy(Map<String, Object> credentialServiceConfig, Credential failedCredential) {
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
        Date loginFailureWindowExpiration = new Date(firstLoginFailure.getTime() + (resetAfterSeconds * 1000));
        failedCredential.setFirstLoginFailure(firstLoginFailure);
        failedCredential.setLoginFailuresReset(loginFailureWindowExpiration);
        int maxLoginFailures = (int) credentialServiceConfig.get("lockoutPolicy.maxFailures");
        if (failedCredential.getLoginFailures() >= maxLoginFailures) {
            long lockoutDuration = (int) credentialServiceConfig.get("lockoutPolicy.lockDuration");
            Date resetDate = new Date(now.getTime() + (lockoutDuration * 1000));
            failedCredential.setLockoutReset(resetDate);
        }
    }
}
