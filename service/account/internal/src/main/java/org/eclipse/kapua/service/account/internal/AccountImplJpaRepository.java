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
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountRepository;

import javax.persistence.TypedQuery;
import java.util.function.Supplier;

public class AccountImplJpaRepository
        extends KapuaNamedEntityRepositoryJpaImpl<Account, AccountImpl>
        implements AccountRepository {

    public AccountImplJpaRepository(
            Supplier<? extends KapuaListResult<Account>> listProvider,
            EntityManagerSession entityManagerSession) {
        super(AccountImpl.class, listProvider, entityManagerSession);
    }

    @Override
    public KapuaListResult<Account> findChildAccountsRecursive(String parentAccountPath) {
        try {
            return entityManagerSession.doAction(
                    EntityManagerContainer.<KapuaListResult<Account>>create()
                            .onResultHandler(em -> {
                                TypedQuery<Account> q = em.createNamedQuery("Account.findChildAccountsRecursive", Account.class);
                                q.setParameter("parentAccountPath", "\\" + parentAccountPath + "/%");
                                KapuaListResult<Account> result = listSupplier.get();
                                result.addItems(q.getResultList());
                                return result;
                            }));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
