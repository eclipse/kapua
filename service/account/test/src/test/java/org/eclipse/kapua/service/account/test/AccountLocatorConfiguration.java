/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.cucumber.java.Before;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.model.query.QueryFactoryImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.query.QueryFactory;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.eclipse.kapua.service.account.AccountDomains;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountChildrenFinderImpl;
import org.eclipse.kapua.service.account.internal.AccountEntityManagerFactory;
import org.eclipse.kapua.service.account.internal.AccountFactoryImpl;
import org.eclipse.kapua.service.account.internal.AccountImplJpaRepository;
import org.eclipse.kapua.service.account.internal.AccountServiceImpl;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.mockito.Matchers;
import org.mockito.Mockito;

@Singleton
public class AccountLocatorConfiguration {

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for sevice unit tests.
     */
    @Before(value = "@setup", order = 1)
    public void setupDI() {
        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {
                bind(RootUserTester.class).toInstance(Mockito.mock(RootUserTester.class));
                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }

                bind(QueryFactory.class).toInstance(new QueryFactoryImpl());

                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                final PermissionFactory mockPermissionFactory = Mockito.mock(PermissionFactory.class);
                bind(PermissionFactory.class).toInstance(mockPermissionFactory);
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());
                // Inject actual account related services
                final AccountEntityManagerFactory entityManagerFactory = AccountEntityManagerFactory.getInstance();
                bind(AccountEntityManagerFactory.class).toInstance(entityManagerFactory);
                final AccountFactory accountFactory = new AccountFactoryImpl();
                bind(AccountFactory.class).toInstance(accountFactory);
                bind(AccountChildrenFinder.class).to(AccountChildrenFinderImpl.class);
                final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
                bind(ServiceConfigurationManager.class)
                        .annotatedWith(Names.named("AccountServiceConfigurationManager"))
                        .toInstance(new ResourceLimitedServiceConfigurationManagerImpl(
                                AccountService.class.getName(),
                                AccountDomains.ACCOUNT_DOMAIN,
                                new ServiceConfigImplJpaRepository(new EntityManagerSession(entityManagerFactory)),
                                mockPermissionFactory,
                                mockedAuthorization,
                                Mockito.mock(RootUserTester.class),
                                Mockito.mock(AccountChildrenFinder.class),
                                new UsedEntitiesCounterImpl(
                                        accountFactory,
                                        AccountDomains.ACCOUNT_DOMAIN,
                                        accountRepository,
                                        mockedAuthorization,
                                        mockPermissionFactory)
                        ));
                bind(AccountService.class).to(AccountServiceImpl.class);
                bind(AccountRepository.class).toInstance(new AccountImplJpaRepository(() -> accountFactory.newListResult(), new EntityManagerSession(entityManagerFactory)));
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }
}
