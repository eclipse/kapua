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
package org.eclipse.kapua.service.job.execution.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionRepository;
import org.eclipse.kapua.service.job.execution.JobExecutionService;

import javax.inject.Singleton;

public class JobExecutionModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobExecutionFactory.class).to(JobExecutionFactoryImpl.class);
    }

    @Provides
    @Singleton
    JobExecutionService jobExecutionService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            JobExecutionRepository jobExecutionRepository,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new JobExecutionServiceImpl(
                authorizationService,
                permissionFactory,
                jpaTxManagerFactory.create("kapua-job"),
                jobExecutionRepository);
    }

    @Provides
    @Singleton
    JobExecutionRepository jobExecutionRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new JobExecutionImplJpaRepository(jpaRepoConfig);
    }

}
