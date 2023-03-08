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
import org.eclipse.kapua.commons.jpa.DuplicateNameCheckerImpl;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobRepository;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.scheduler.trigger.TriggerRepository;

import javax.inject.Named;
import javax.inject.Singleton;

public class JobModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobFactory.class).to(JobFactoryImpl.class);
    }

    @Provides
    @Singleton
    JobService jobService(
            @Named("JobServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            JobEngineService jobEngineService,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            JobRepository jobRepository,
            TriggerRepository triggerRepository) {

        return new JobServiceImpl(
                serviceConfigurationManager,
                jobEngineService,
                permissionFactory,
                authorizationService,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-job")),
                jobRepository,
                triggerRepository,
                new DuplicateNameCheckerImpl<>(jobRepository, scopeId -> new JobQueryImpl(scopeId))
        );
    }

    @Provides
    @Singleton
    @Named("JobServiceConfigurationManager")
    public ServiceConfigurationManager jobServiceConfigurationManager(
            JobFactory factory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder,
            JobRepository jobRepository
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        JobService.class.getName(),
                        JobDomains.JOB_DOMAIN,
                        new JpaTxManager(new KapuaEntityManagerFactory("kapua-job")),
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                new JpaTxManager(new KapuaEntityManagerFactory("kapua-job")),
                                jobRepository
                        )));

    }

    @Provides
    @Singleton
    JobRepository jobRepository() {
        return new JobImplJpaRepository();
    }
}
