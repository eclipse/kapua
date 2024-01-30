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
package org.eclipse.kapua.service.job.internal;

import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.domain.DomainEntry;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobRepository;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;

import javax.inject.Named;
import javax.inject.Singleton;

public class JobModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobFactory.class).to(JobFactoryImpl.class);
    }

    @ProvidesIntoSet
    public Domain jobdomain() {
        return new DomainEntry(Domains.JOB, JobService.class.getName(), false, Actions.read, Actions.delete, Actions.write, Actions.execute);
    }

    @Provides
    @Singleton
    JobService jobService(
            @Named("JobServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            JobEngineService jobEngineService,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            JobRepository jobRepository,
            TriggerService triggerService,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {

        return new JobServiceImpl(
                serviceConfigurationManager,
                jobEngineService,
                permissionFactory,
                authorizationService,
                jpaTxManagerFactory.create("kapua-job"),
                jobRepository,
                triggerService
        );
    }

    @Provides
    @Singleton
    @Named("JobServiceConfigurationManager")
    public ServiceConfigurationManager jobServiceConfigurationManager(
            JobFactory factory,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            JobRepository jobRepository,
            KapuaJpaRepositoryConfiguration jpaRepoConfig
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        JobService.class.getName(),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                jobRepository
                        )));

    }

    @Provides
    @Singleton
    JobRepository jobRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new JobImplJpaRepository(jpaRepoConfig);
    }
}
