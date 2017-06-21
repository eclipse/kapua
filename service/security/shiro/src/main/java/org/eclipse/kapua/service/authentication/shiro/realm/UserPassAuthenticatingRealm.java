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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.exceptions.TemporaryLockedAccountException;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;

import java.util.Date;
import java.util.Map;

/**
 * {@link UsernamePasswordCredentials} based {@link AuthenticatingRealm} implementation.
 * <p>
 * since 1.0
 */
public class UserPassAuthenticatingRealm extends AuthenticatingRealm {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    /**
     * Realm name
     */
    public static final String REALM_NAME = "userPassAuthenticatingRealm";

    /**
     * Constructor
     *
     * @throws KapuaException
     */
    public UserPassAuthenticatingRealm() throws KapuaException {
        setName(REALM_NAME);

        CredentialsMatcher credentialsMather = new UserPassCredentialsMatcher();
        setCredentialsMatcher(credentialsMather);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //
        // Extract credentials
        UsernamePasswordCredentialsImpl token = (UsernamePasswordCredentialsImpl) authenticationToken;
        String tokenUsername = token.getUsername();

        //
        // Get Services
        KapuaLocator locator;
        UserService userService;
        AccountService accountService;
        CredentialService credentialService;

        try {
            locator = KapuaLocator.getInstance();
            userService = locator.getService(UserService.class);
            accountService = locator.getService(AccountService.class);
            credentialService = locator.getService(CredentialService.class);
        } catch (KapuaRuntimeException kre) {
            throw new ShiroException("Error while getting services!", kre);
        }

        //
        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> userService.findByName(tokenUsername));
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

        // Check if expired
        if (user.getExpirationDate() != null && !user.getExpirationDate().after(new Date())) {
            throw new ExpiredCredentialsException();
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
        // Find credentials
        // FIXME: manage multiple credentials and multiple credentials type
        Credential credential;
        try {
            credential = KapuaSecurityUtils.doPrivileged(() -> {
                CredentialListResult credentialList = credentialService.findByUserId(user.getScopeId(), user.getId());

                if (credentialList != null && !credentialList.isEmpty()) {
                    Credential credentialMatched = null;
                    for (Credential c : credentialList.getItems()) {
                        if (CredentialType.PASSWORD.equals(c.getCredentialType())) {
                            credentialMatched = c;
                            break;
                        }
                    }
                    return credentialMatched;
                } else {
                    return null;
                }
            });
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find credentials!", e);
        }

        // Check existence
        if (credential == null) {
            throw new UnknownAccountException();
        }

        // Check credential disabled
        if (CredentialStatus.DISABLED.equals(credential.getStatus())) {
            throw new DisabledAccountException();
        }

        // Check if credential expired
        if (credential.getExpirationDate() != null && !credential.getExpirationDate().after(new Date())) {
            throw new ExpiredCredentialsException();
        }

        // Check if lockout policy is blocking credential
        Map<String, Object> credentialServiceConfig;
        try {
            credentialServiceConfig = KapuaSecurityUtils.doPrivileged(() -> credentialService.getConfigValues(account.getScopeId()));
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
                // TODO Update Lockout Policy fields
                Credential failedCredential = (Credential) kapuaInfo.getCredentials();
                KapuaSecurityUtils.doPrivileged(() -> {
                    Map<String, Object> credentialServiceConfig = kapuaInfo.getCredentialServiceConfig();
                    boolean lockoutPolicyEnabled = (boolean) credentialServiceConfig.get("lockoutPolicy.enabled");
                    if (lockoutPolicyEnabled) {
                        Date now = new Date();
                        int resetAfterSeconds = (int)credentialServiceConfig.get("lockoutPolicy.resetAfter");
                        Date firstLoginFailure = (failedCredential.getFirstLoginFailure() == null || now.after(failedCredential.getLoginFailuresReset())) ?
                                now :
                                failedCredential.getFirstLoginFailure();
                        Date loginFailureWindowExpiration = new Date(firstLoginFailure.getTime() + (resetAfterSeconds * 1000));
                        if (now.after(loginFailureWindowExpiration)) {
                            failedCredential.setLoginFailures(1);
                        } else {
                            failedCredential.setLoginFailures(failedCredential.getLoginFailures() + 1);
                        }
                        failedCredential.setFirstLoginFailure(firstLoginFailure);
                        failedCredential.setLoginFailuresReset(loginFailureWindowExpiration);
                        int maxLoginFailures = (int)credentialServiceConfig.get("lockoutPolicy.maxFailures");
                        if (failedCredential.getLoginFailures() >= maxLoginFailures) {
                            long lockoutDuration = (int)credentialServiceConfig.get("lockoutPolicy.lockDuration");
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
        // TODO Clear Lockout Policy fields
        Credential credential = (Credential) kapuaInfo.getCredentials();
        credential.setFirstLoginFailure(null);
        credential.setLoginFailuresReset(null);
        credential.setLoginFailures(0);
        try {
            KapuaSecurityUtils.doPrivileged(() -> credentialService.update(credential));
        } catch (KapuaException kex) {
            throw new ShiroException("Error while updating lockout policy", kex);
        }
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("scopeId", kapuaInfo.getUser().getScopeId());
        session.setAttribute("userId", kapuaInfo.getUser().getId());
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordCredentialsImpl;
    }
}
