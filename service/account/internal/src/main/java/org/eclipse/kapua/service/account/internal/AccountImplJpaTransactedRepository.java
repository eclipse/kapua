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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaTransactedRepository;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountTransactedRepository;

import javax.persistence.TypedQuery;
import java.util.function.Supplier;

public class AccountImplJpaTransactedRepository
        extends KapuaNamedEntityJpaTransactedRepository<Account, AccountImpl, AccountListResult>
        implements AccountTransactedRepository {

    public AccountImplJpaTransactedRepository(
            Supplier<AccountListResult> listProvider,
            EntityManagerSession entityManagerSession) {
        super(AccountImpl.class, listProvider, entityManagerSession);
    }

    @Override
    public AccountListResult findChildAccountsRecursive(String parentAccountPath) {
        try {
            return entityManagerSession.doAction(
                    EntityManagerContainer.<AccountListResult>create()
                            .onResultHandler(em -> {
                                TypedQuery<Account> q = em.createNamedQuery("Account.findChildAccountsRecursive", Account.class);
                                q.setParameter("parentAccountPath", "\\" + parentAccountPath + "/%");
                                final AccountListResult result = listSupplier.get();
                                result.addItems(q.getResultList());
                                return result;
                            }));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
