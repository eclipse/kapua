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
package org.eclipse.kapua.job.engine.queue.jbatch;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionFactory;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionRepository;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Named;
import javax.inject.Singleton;

public class JobEngineQueueJbatchModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(QueuedJobExecutionFactory.class).to(QueuedJobExecutionFactoryImpl.class);
    }

    @Provides
    @Singleton
    QueuedJobExecutionService queuedJobExecutionService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            QueuedJobExecutionRepository repository,
            @Named("maxInsertAttempts") Integer maxInsertAttempts) {
        return new QueuedJobExecutionServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-job-engine"), maxInsertAttempts),
                repository);
    }

    @Provides
    @Singleton
    QueuedJobExecutionRepository queuedJobExecutionRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new QueuedJobExecutionImplJpaRepository(jpaRepoConfig);
    }
}
