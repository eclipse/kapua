/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.cucumber.java.Before;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.model.query.QueryFactoryImpl;
import org.eclipse.kapua.job.engine.client.JobEngineServiceClient;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.query.QueryFactory;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountFactoryImpl;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.internal.JobFactoryImpl;
import org.eclipse.kapua.service.job.internal.JobImplJpaRepository;
import org.eclipse.kapua.service.job.internal.JobServiceImpl;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerDefinitionFactoryImpl;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerDefinitionImplJpaRepository;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerDefinitionServiceImpl;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerFactoryImpl;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerImplJpaRepository;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerServiceImpl;
import org.mockito.Matchers;
import org.mockito.Mockito;

@Singleton
public class SchedulerLocatorConfiguration {

    @Before(value = "@setup", order = 1)
    public void setupDI() {
        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();
        final int maxInsertAttempts = 3;

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                bind(KapuaJpaRepositoryConfiguration.class).toInstance(new KapuaJpaRepositoryConfiguration());

                // Inject mocked Permission Factory
                final PermissionFactory permissionFactory = Mockito.mock(PermissionFactory.class);
                bind(PermissionFactory.class).toInstance(permissionFactory);
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // binding Account related services
                bind(AccountChildrenFinder.class).toInstance(Mockito.mock(AccountChildrenFinder.class));
                bind(AccountService.class).toInstance(Mockito.mock(AccountService.class));
                bind(AccountFactory.class).toInstance(Mockito.spy(new AccountFactoryImpl()));
                bind(RootUserTester.class).toInstance(Mockito.mock(RootUserTester.class));

                // Inject actual Tag service related services
                bind(JobFactory.class).toInstance(new JobFactoryImpl());
                final JobFactory jobFactory = new JobFactoryImpl();
                final KapuaJpaRepositoryConfiguration jpaRepoConfig = new KapuaJpaRepositoryConfiguration();
                final JobImplJpaRepository jobRepository = new JobImplJpaRepository(jpaRepoConfig);
                final TriggerImplJpaRepository triggerRepository = new TriggerImplJpaRepository(jpaRepoConfig);
                bind(JobService.class).toInstance(new JobServiceImpl(
                        Mockito.mock(ServiceConfigurationManager.class),
                        new JobEngineServiceClient(),
                        permissionFactory,
                        mockedAuthorization,
                        new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-job"),
                        jobRepository,
                        triggerRepository
                ));
                final TriggerDefinitionFactoryImpl triggerDefinitionFactory = new TriggerDefinitionFactoryImpl();
                final TriggerDefinitionImplJpaRepository triggerDefinitionRepository = new TriggerDefinitionImplJpaRepository(jpaRepoConfig);
                final TriggerFactoryImpl triggerFactory = new TriggerFactoryImpl();
                bind(TriggerService.class).toInstance(new TriggerServiceImpl(
                        mockedAuthorization,
                        permissionFactory,
                        new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-scheduler"),
                        triggerRepository,
                        triggerFactory,
                        triggerDefinitionRepository,
                        triggerDefinitionFactory
                ));
                bind(TriggerFactory.class).toInstance(triggerFactory);
                bind(TriggerDefinitionService.class).toInstance(new TriggerDefinitionServiceImpl(
                        mockedAuthorization,
                        permissionFactory,
                        new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-scheduler"),
                        triggerDefinitionRepository,
                        triggerDefinitionFactory));
                bind(TriggerDefinitionFactory.class).toInstance(triggerDefinitionFactory);

                // bind Query related services
                bind(QueryFactory.class).toInstance(new QueryFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }
}
