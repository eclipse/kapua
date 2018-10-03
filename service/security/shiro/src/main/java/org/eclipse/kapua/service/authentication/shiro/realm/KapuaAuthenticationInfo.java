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

import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.user.User;

import org.apache.shiro.authc.AuthenticationInfo;

public abstract class KapuaAuthenticationInfo implements AuthenticationInfo {
    private String realmName;
    private Account account;
    private User user;

    public KapuaAuthenticationInfo(String realmName, Account account, User user) {
        this.realmName = realmName;
        this.account = account;
        this.user = user;
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

    String getRealmName() {
        return realmName;
    }
}
