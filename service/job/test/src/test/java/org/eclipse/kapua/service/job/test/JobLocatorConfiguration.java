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
package org.eclipse.kapua.service.job.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.cucumber.java.Before;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.DuplicateNameCheckerImpl;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.model.query.QueryFactoryImpl;
import org.eclipse.kapua.job.engine.JobEngineService;
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
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionFactoryImpl;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionImplJpaRepository;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionServiceImpl;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.internal.JobFactoryImpl;
import org.eclipse.kapua.service.job.internal.JobImplJpaRepository;
import org.eclipse.kapua.service.job.internal.JobQueryImpl;
import org.eclipse.kapua.service.job.internal.JobServiceImpl;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionFactoryImpl;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionServiceImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepFactoryImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepServiceImpl;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.internal.JobTargetFactoryImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetServiceImpl;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerDefinitionFactoryImpl;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerDefinitionServiceImpl;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerFactoryImpl;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerImplJpaRepository;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerServiceImpl;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class JobLocatorConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JobLocatorConfiguration.class);

    @Before(value = "@setup", order = 1)
    public void setupDI() {
        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Commons
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());
                bind(QueryFactory.class).toInstance(new QueryFactoryImpl());

                // Account
                bind(AccountChildrenFinder.class).toInstance(Mockito.mock(AccountChildrenFinder.class));
                bind(AccountService.class).toInstance(Mockito.mock(AccountService.class));
                bind(AccountFactory.class).toInstance(Mockito.spy(new AccountFactoryImpl()));
                bind(RootUserTester.class).toInstance(Mockito.mock(RootUserTester.class));

                // Auth
                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                    bind(AuthorizationService.class).toInstance(mockedAuthorization);
                } catch (Exception e) {
                    LOG.warn("Error while setting mock AuthorizationService. This may lead to failures...", e);
                }
                final PermissionFactory mockedPermissionFactory = Mockito.mock(PermissionFactory.class);
                bind(PermissionFactory.class).toInstance(mockedPermissionFactory);

                // Job
                JobEntityManagerFactory jobEntityManagerFactory = JobEntityManagerFactory.getInstance();
                bind(JobEntityManagerFactory.class).toInstance(jobEntityManagerFactory);

                bind(JobFactory.class).toInstance(new JobFactoryImpl());
                bind(JobService.class).toInstance(new JobServiceImpl(
                        Mockito.mock(ServiceConfigurationManager.class),
                        Mockito.mock(JobEngineService.class),
                        mockedPermissionFactory,
                        mockedAuthorization,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-job")),
                        new JobImplJpaRepository(),
                        new TriggerImplJpaRepository(),
                        new DuplicateNameCheckerImpl<>(new JobImplJpaRepository(), scopeId -> new JobQueryImpl(scopeId))
                ));
                bind(JobStepDefinitionService.class).toInstance(new JobStepDefinitionServiceImpl());
                bind(JobStepDefinitionFactory.class).toInstance(new JobStepDefinitionFactoryImpl());
                bind(JobStepService.class).toInstance(new JobStepServiceImpl());
                bind(JobStepFactory.class).toInstance(new JobStepFactoryImpl());
                bind(JobTargetService.class).toInstance(new JobTargetServiceImpl());
                bind(JobTargetFactory.class).toInstance(new JobTargetFactoryImpl());
                bind(JobExecutionService.class).toInstance(new JobExecutionServiceImpl(
                        mockedAuthorization,
                        mockedPermissionFactory,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-job")),
                        new JobExecutionImplJpaRepository()
                ));
                bind(JobExecutionFactory.class).toInstance(new JobExecutionFactoryImpl());

                // Trigger
                bind(TriggerService.class).toInstance(new TriggerServiceImpl());
                bind(TriggerFactory.class).toInstance(new TriggerFactoryImpl());
                bind(TriggerDefinitionService.class).toInstance(new TriggerDefinitionServiceImpl());
                bind(TriggerDefinitionFactory.class).toInstance(new TriggerDefinitionFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }
}
