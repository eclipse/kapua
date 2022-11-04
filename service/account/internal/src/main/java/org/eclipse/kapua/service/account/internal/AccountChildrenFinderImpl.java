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
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.account.AccountDomains;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Inject;

public class AccountChildrenFinderImpl extends AbstractKapuaService implements AccountChildrenFinder {

    private final AccountFactory accountFactory;
    private final PermissionFactory permissionFactory;
    private final AuthorizationService authorizationService;

    @Inject
    public AccountChildrenFinderImpl(
            AccountEntityManagerFactory accountEntityManagerFactory,
            AccountCacheFactory accountCacheFactory,
            AccountFactory accountFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService) {
        super(accountEntityManagerFactory, accountCacheFactory);
        this.accountFactory = accountFactory;
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
    }

    @Override
    public AccountListResult findChildren(KapuaId scopeId, KapuaId excludeTargetScopeId) throws KapuaException {
        final AccountQuery childAccountsQuery = accountFactory.newQuery(scopeId);
        // Exclude the scope that is under config update
        if (excludeTargetScopeId != null) {
            childAccountsQuery.setPredicate(childAccountsQuery.attributePredicate(KapuaEntityAttributes.ENTITY_ID, excludeTargetScopeId, AttributePredicate.Operator.NOT_EQUAL));
        }

        //What follows is a duplication of AccountServiceImpl.query implementation
        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, childAccountsQuery.getScopeId()));

        //
        // Do query
        final AccountListResult childAccounts = entityManagerSession.doAction(
                EntityManagerContainer.<AccountListResult>create().onResultHandler(em -> AccountDAO.query(em, childAccountsQuery))
        );

        return childAccounts;
    }
}
