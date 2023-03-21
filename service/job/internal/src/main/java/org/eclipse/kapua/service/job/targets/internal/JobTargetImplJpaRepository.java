/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.targets.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetRepository;
import org.eclipse.kapua.storage.TxContext;

public class JobTargetImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<JobTarget, JobTargetImpl, JobTargetListResult>
        implements JobTargetRepository {

    public JobTargetImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(JobTargetImpl.class, () -> new JobTargetListResultImpl(), jpaRepoConfig);
    }

    @Override
    public JobTarget delete(TxContext txContext, KapuaId scopeId, KapuaId jobTargetId) throws KapuaException {
        // Check existence
        final JobTarget toBeDeleted = this.find(txContext, scopeId, jobTargetId)
                .orElseThrow(() -> new KapuaEntityNotFoundException(JobTarget.TYPE, jobTargetId));
        return this.delete(txContext, toBeDeleted);
    }
}
