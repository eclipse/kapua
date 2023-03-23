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
package org.eclipse.kapua.service.job.step.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.DuplicateNameCheckerImpl;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.model.query.QueryFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionRepository;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepRepository;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionRepository;

import javax.inject.Named;
import javax.inject.Singleton;

public class JobStepModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(JobStepFactory.class).to(JobStepFactoryImpl.class);
    }

    @Provides
    @Singleton
    JobStepRepository jobStepRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new JobStepImplJpaRepository(jpaRepoConfig);
    }

    @Provides
    @Singleton
    JobStepService jobStepService(AuthorizationService authorizationService,
                                  PermissionFactory permissionFactory,
                                  JobStepRepository jobStepRepository,
                                  JobStepFactory jobStepFactory,
                                  JobExecutionRepository jobExecutionRepository,
                                  JobExecutionFactory jobExecutionFactory,
                                  JobStepDefinitionRepository jobStepDefinitionRepository,
                                  QueryFactory queryFactory,
                                  @Named("maxInsertAttempts") Integer maxInsertAttempts) {
        return new JobStepServiceImpl(authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-job"), maxInsertAttempts),
                jobStepRepository,
                jobStepFactory,
                new DuplicateNameCheckerImpl<>(jobStepRepository, jobStepFactory::newQuery),
                jobExecutionRepository,
                jobExecutionFactory,
                jobStepDefinitionRepository,
                queryFactory
        );
    }
}
