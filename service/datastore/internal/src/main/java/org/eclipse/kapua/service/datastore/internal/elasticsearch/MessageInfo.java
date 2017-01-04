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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.service.account.Account;

/**
 * Message information object.<br>
 * It is used as a container for the user dependent information during the store process (typically {@link Account} informations)
 * 
 * @since 1.0
 *
 */
public class MessageInfo
{
    private Account account;

    /**
     * Construct a new message information using the given account
     * 
     * @param account
     */
    public MessageInfo(Account account)
    {
        this.account = account;
    }

    /**
     * Get the account
     * 
     * @return
     */
    public Account getAccount()
    {
        return this.account;
    }
}
