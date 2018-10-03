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

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.user.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Kapua {@link AuthenticationInfo} implementation
 * 
 * @since 1.0
 *
 */
public class LoginAuthenticationInfo extends KapuaAuthenticationInfo {

    private static final long serialVersionUID = -8682457531010599453L;


    private Credential credentials;
    private Map<String, Object> credentialServiceConfig;

    LoginAuthenticationInfo(String realmName, Account account, User user, Credential credentials) {
        super(realmName,account, user);
        this.credentials = credentials;
        this.credentialServiceConfig = new HashMap<>();
    }

    /**
     * Constructor
     * 
     * @param realmName
     * @param account
     * @param user
     * @param credentials
     */
    LoginAuthenticationInfo(String realmName,
            Account account,
            User user,
            Credential credentials,
            Map<String, Object> credentialServiceConfig) {
        super(realmName,account, user);
        this.credentials = credentials;
        this.credentialServiceConfig = credentialServiceConfig;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(getUser(), getRealmName());
    }

    public Credential getCredential() {
        return credentials;
    }

    @Override
    public Object getCredentials() {
        return getCredential();
    }

    Map<String, Object> getCredentialServiceConfig() {
        return credentialServiceConfig;
    }
}
