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
package org.eclipse.kapua.test.account;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountService;

@TestService
@KapuaProvider
public class AccountServiceMock implements AccountService {

    private final AccountMock kapuaRootAccount;
    private final Map<KapuaId, AccountMock> accounts;

    public AccountServiceMock() {
        accounts = new HashMap<>();
        kapuaRootAccount = new AccountMock(new KapuaEid(BigInteger.valueOf(1)), "kapua-sys");
        accounts.put(kapuaRootAccount.getId(), kapuaRootAccount);
    }

    @Override
    public Account create(AccountCreator creator)
            throws KapuaException {
        // TODO Auto-generated method stub
        AccountMock account = new AccountMock(creator.getScopeId(), creator.getName());
        return account;
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        if (!accounts.containsKey(entityId)) {
            throw KapuaException.internalError("User not found");
        }

        return accounts.get(entityId);
    }

    @Override
    public AccountListResult query(KapuaQuery<Account> query)
            throws KapuaException {
        throw KapuaException.internalError("Not implemented");
    }

    @Override
    public long count(KapuaQuery<Account> query)
            throws KapuaException {
        throw KapuaException.internalError("Not implemented");
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accountId)
            throws KapuaException {
        if (!accounts.containsKey(accountId)) {
            throw KapuaException.internalError("User not found");
        }

        accounts.remove(accountId);
    }

    @Override
    public Account update(Account account)
            throws KapuaException {
        if (!accounts.containsKey(account.getId())) {
            throw KapuaException.internalError("User not found");
        }

        AccountMock accountMock = accounts.get(account.getId());
        return accountMock;
    }

    @Override
    public Account findByName(String name)
            throws KapuaException {
        Iterator<AccountMock> accountMocks = accounts.values().iterator();
        while (accountMocks.hasNext()) {
            AccountMock accountMock = accountMocks.next();
            if (accountMock.getName() != null && accountMock.getName().equals(name)) {
                return accountMock;
            }
        }
        throw KapuaException.internalError("User not found");
    }

    @Override
    public KapuaTocd getConfigMetadata()
            throws KapuaException {
        throw KapuaException.internalError("Not implemented");
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId)
            throws KapuaException {
        throw KapuaException.internalError("Not implemented");
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values)
            throws KapuaException {
        throw KapuaException.internalError("Not implemented");
    }

    @Override
    public Account find(KapuaId id)
            throws KapuaException {
        if (!accounts.containsKey(id)) {
            throw KapuaException.internalError("User not found");
        }

        return accounts.get(id);
    }

    @Override
    public AccountListResult findChildsRecursively(KapuaId accountId)
            throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }
}
