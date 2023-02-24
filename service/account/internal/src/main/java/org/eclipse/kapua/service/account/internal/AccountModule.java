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

import com.google.inject.Module;
import com.google.inject.Provides;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.service.account.AccountDomains;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Named;

/**
 * {@code kapua-account-internal} {@link Module} implementation.
 *
 * @since 2.0.0
 */
public class AccountModule extends AbstractKapuaModule implements Module {

    @Override
    protected void configureModule() {
        bind(AccountFactory.class).to(AccountFactoryImpl.class);
        bind(AccountChildrenFinder.class).to(AccountChildrenFinderImpl.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
    }

    @Provides
    @Named("AccountServiceConfigurationManager")
    ServiceConfigurationManager accountServiceConfigurationManager(
            AccountEntityManagerFactory entityManagerFactory,
            AccountFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            AccountRepository accountRepository
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        AccountService.class.getName(),
                        AccountDomains.ACCOUNT_DOMAIN,
                        new ServiceConfigImplJpaRepository(new EntityManagerSession(entityManagerFactory)),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                AccountDomains.ACCOUNT_DOMAIN,
                                accountRepository,
                                authorizationService,
                                permissionFactory)
                ));
    }

    @Provides
    AccountRepository accountRepository(AccountFactory accountFactory, AccountCacheFactory accountCacheFactory) {
        final AccountImplJpaRepository wrapped = new AccountImplJpaRepository(
                () -> accountFactory.newListResult(),
                new EntityManagerSession(new AbstractEntityManagerFactory("kapua-account") {
                }));
        final NamedEntityCache cache = (NamedEntityCache) accountCacheFactory.createCache();
        return new CachingAccountRepository(wrapped, cache);
    }
}
