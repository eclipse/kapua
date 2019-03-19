/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.Organization;

@TestService
@KapuaProvider
public class AccountFactoryMock implements AccountFactory {

    @Override
    public AccountCreator newCreator(KapuaId scopeId, String name) {
        AccountCreatorMock accountCreatorMock = new AccountCreatorMock();
        accountCreatorMock.setScopeId(scopeId);
        accountCreatorMock.setName(name);
        return accountCreatorMock;
    }

    @Override
    public AccountQuery newQuery(KapuaId scopeId) {
        return new AccountQueryMock();
    }

    @Override
    public Organization newOrganization() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AccountCreator newCreator(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Account newEntity(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AccountListResult newListResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Account clone(Account entity) {
        throw new UnsupportedOperationException();
    }
}
