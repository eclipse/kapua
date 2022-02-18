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
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;

/**
 * Kapua {@link AuthenticationInfo} implementation
 *
 * @since 1.0
 *
 */
public class SessionAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = -8682457531010599453L;

    private String realmName;
    private Account account;
    private User user;
    private AccessToken accessToken;

    /**
     * Constructor
     *
     * @param realmName
     * @param account
     * @param user
     * @param accessToken
     */
    public SessionAuthenticationInfo(String realmName,
            Account account,
            User user,
            AccessToken accessToken) {
        this.realmName = realmName;
        this.account = account;
        this.user = user;
        this.accessToken = accessToken;
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

    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(getUser(), getRealmName());
    }

    @Override
    public Object getCredentials() {
        return getAccessToken();
    }
}
