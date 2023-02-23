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
import org.eclipse.kapua.commons.configuration.AccountChildrenFinder;
import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.configuration.UsedEntitiesCounterImpl;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobService;

import javax.inject.Named;

public class JobModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobFactory.class).to(JobFactoryImpl.class);
        bind(JobService.class).to(JobServiceImpl.class);
    }

    @Provides
    @Named("JobServiceConfigurationManager")
    public ServiceConfigurationManager jobServiceConfigurationManager(
            JobFactory factory,
            JobEntityManagerFactory jobEntityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester,
            AccountChildrenFinder accountChildrenFinder
    ) {
        return new ServiceConfigurationManagerCachingWrapper(
                new ResourceLimitedServiceConfigurationManagerImpl(
                        JobService.class.getName(),
                        JobDomains.JOB_DOMAIN,
                        new ServiceConfigImplJpaRepository(jobEntityManagerFactory),
                        permissionFactory,
                        authorizationService,
                        rootUserTester,
                        accountChildrenFinder,
                        new UsedEntitiesCounterImpl(
                                factory,
                                JobDomains.JOB_DOMAIN,
                                JobDAO::count,
                                authorizationService,
                                permissionFactory,
                                new EntityManagerSession(jobEntityManagerFactory)
                        )));

    }
}
