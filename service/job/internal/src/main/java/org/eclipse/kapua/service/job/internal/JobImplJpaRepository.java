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

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobRepository;

import java.util.function.Supplier;

public class JobImplJpaRepository
        extends KapuaNamedEntityRepositoryJpaImpl<Job, JobImpl>
        implements JobRepository {

    public JobImplJpaRepository(Supplier<? extends KapuaListResult<Job>> listProvider, EntityManagerSession entityManagerSession) {
        super(JobImpl.class, listProvider, entityManagerSession);
    }
}
