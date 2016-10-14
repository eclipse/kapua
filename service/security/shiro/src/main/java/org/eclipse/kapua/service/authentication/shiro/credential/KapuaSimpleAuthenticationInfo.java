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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.credential;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.user.User;

/**
 * Kapua {@link SimpleAuthenticationInfo} implementation
 * 
 * @since 1.0
 *
 */
public class KapuaSimpleAuthenticationInfo extends SimpleAuthenticationInfo
{
    private static final long serialVersionUID = -8682457531010599453L;

    private User              user;
    private Credential        credential;
    private Account           account;

    /**
     * Constructor
     * 
     * @param user
     * @param credential
     * @param account
     * @param realmName
     */
    public KapuaSimpleAuthenticationInfo(User user,
                                         Credential credential,
                                         Account account,
                                         String realmName)
    {
        super(user.getName(),
              credential.getCredentialKey(),
              realmName);
        this.user = user;
        this.credential = credential;
        this.account = account;
    }

    /**
     * Return the user
     * 
     * @return
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Return the account
     * 
     * @return
     */
    public Account getAccount()
    {
        return account;
    }

}
