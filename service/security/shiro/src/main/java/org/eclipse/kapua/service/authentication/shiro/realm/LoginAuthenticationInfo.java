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

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.user.User;

import java.util.Map;

/**
 * Kapua {@link AuthenticationInfo} implementation
 *
 * @since 1.0
 *
 */
public class LoginAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = -8682457531010599453L;

    private String realmName;
    private Account account;
    private User user;
    private Credential credentials;
    private Map<String, Object> credentialServiceConfig;

    /**
     * Constructor
     *
     * @param realmName
     * @param account
     * @param user
     * @param credentials
     */
    public LoginAuthenticationInfo(String realmName,
            Account account,
            User user,
            Credential credentials,
            Map<String, Object> credentialServiceConfig) {
        this.realmName = realmName;
        this.account = account;
        this.user = user;
        this.credentials = credentials;
        this.credentialServiceConfig = credentialServiceConfig;
    }

    /**
     * Return the user
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Return the account
     *
     * @return
     */
    public Account getAccount() {
        return account;
    }

    public String getRealmName() {
        return realmName;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(getUser(), getRealmName());
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public Map<String, Object> getCredentialServiceConfig() {
        return credentialServiceConfig;
    }
}
