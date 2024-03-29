/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.commons.storage.KapuaNamedEntityRepositoryCachingWrapper;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.storage.TxContext;

public class CachingAccountRepository extends KapuaNamedEntityRepositoryCachingWrapper<Account, AccountListResult> implements AccountRepository {
    public CachingAccountRepository(AccountRepository wrapped, NamedEntityCache entityCache) {
        super(wrapped, entityCache);
    }

    @Override
    public AccountListResult findChildAccountsRecursive(TxContext tx, String parentAccountPath) throws KapuaException {
        return ((AccountRepository) wrapped).findChildAccountsRecursive(tx, parentAccountPath);
    }
}
