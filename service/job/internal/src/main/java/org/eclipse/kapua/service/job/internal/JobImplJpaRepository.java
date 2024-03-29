/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobRepository;

public class JobImplJpaRepository
        extends KapuaNamedEntityJpaRepository<Job, JobImpl, JobListResult>
        implements JobRepository {

    public JobImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(JobImpl.class, Job.TYPE, () -> new JobListResultImpl(), jpaRepoConfig);
    }
}
