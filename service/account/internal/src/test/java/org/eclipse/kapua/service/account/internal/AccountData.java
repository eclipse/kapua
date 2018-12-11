/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;

import javax.inject.Singleton;

@Singleton
public class AccountData {

    // Step scratchpad data
    public Account account;
    public AccountCreator accountCreator;
    public KapuaId currentAccountId;

    public AccountData() {
        cleanup();
    }

    public void cleanup() {
        accountCreator = null;
        account = null;
        currentAccountId = null;
    }
}
