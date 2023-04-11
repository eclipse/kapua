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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class QueuedJobExecutionImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<QueuedJobExecution, QueuedJobExecutionImpl, QueuedJobExecutionListResult>
        implements QueuedJobExecutionRepository {
    public QueuedJobExecutionImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(QueuedJobExecutionImpl.class, () -> new QueuedJobExecutionListResultImpl(), jpaRepoConfig);
    }

    //overwritten just to change exception type
    @Override
    public QueuedJobExecution delete(TxContext txContext, KapuaId scopeId, KapuaId queuedJobExecutionId) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        return this.doFind(em, scopeId, queuedJobExecutionId)
                .map(toBeDeleted -> this.doDelete(em, toBeDeleted))
                .orElseThrow(() -> new KapuaEntityNotFoundException(JobExecution.TYPE, queuedJobExecutionId));
    }
}
