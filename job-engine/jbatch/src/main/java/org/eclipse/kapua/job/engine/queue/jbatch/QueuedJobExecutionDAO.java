/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.execution.JobExecution;

/**
 * {@link QueuedJobExecution} {@link ServiceDAO}
 *
 * @since 1.1.0
 */
public class QueuedJobExecutionDAO {

    private QueuedJobExecutionDAO() {
    }

    /**
     * Creates and return new {@link QueuedJobExecution}
     *
     * @param em
     * @param queuedJobExecutionCreator
     * @return
     * @throws KapuaException
     */
    public static QueuedJobExecution create(EntityManager em, QueuedJobExecutionCreator queuedJobExecutionCreator)
            throws KapuaException {

        QueuedJobExecutionImpl queuedJobExecutionImpl = new QueuedJobExecutionImpl(queuedJobExecutionCreator.getScopeId());
        queuedJobExecutionImpl.setJobId(queuedJobExecutionCreator.getJobId());
        queuedJobExecutionImpl.setJobExecutionId(queuedJobExecutionCreator.getJobExecutionId());
        queuedJobExecutionImpl.setWaitForJobExecutionId(queuedJobExecutionCreator.getWaitForJobExecutionId());
        queuedJobExecutionImpl.setStatus(queuedJobExecutionCreator.getStatus());

        return ServiceDAO.create(em, queuedJobExecutionImpl);
    }

    /**
     * Updates the provided {@link QueuedJobExecution}
     *
     * @param em
     * @param queuedJobExecution
     * @return
     * @throws KapuaException
     */
    public static QueuedJobExecution update(EntityManager em, QueuedJobExecution queuedJobExecution)
            throws KapuaException {

        QueuedJobExecutionImpl jobExecutionImpl = (QueuedJobExecutionImpl) queuedJobExecution;
        return ServiceDAO.update(em, QueuedJobExecutionImpl.class, jobExecutionImpl);
    }

    /**
     * Finds the {@link QueuedJobExecution} by {@link QueuedJobExecution} identifier
     *
     * @param em
     * @param scopeId
     * @param queuedJobExecutionId
     * @return
     */
    public static QueuedJobExecution find(EntityManager em, KapuaId scopeId, KapuaId queuedJobExecutionId) {
        return ServiceDAO.find(em, QueuedJobExecutionImpl.class, scopeId, queuedJobExecutionId);
    }

    /**
     * Returns the {@link QueuedJobExecutionListResult} matching the provided query
     *
     * @param em
     * @param queuedJobExecutionKapuaQuery
     * @return
     * @throws KapuaException
     */
    public static QueuedJobExecutionListResult query(EntityManager em, KapuaQuery queuedJobExecutionKapuaQuery)
            throws KapuaException {
        return ServiceDAO.query(em, QueuedJobExecution.class, QueuedJobExecutionImpl.class, new QueuedJobExecutionListResultImpl(), queuedJobExecutionKapuaQuery);
    }

    /**
     * Returns the {@link QueuedJobExecution} count matching the provided query
     *
     * @param em
     * @param queuedJobExecutionKapuaQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery queuedJobExecutionKapuaQuery)
            throws KapuaException {
        return ServiceDAO.count(em, QueuedJobExecution.class, QueuedJobExecutionImpl.class, queuedJobExecutionKapuaQuery);
    }

    /**
     * Deletes the {@link QueuedJobExecution} by {@link QueuedJobExecution} identifier
     *
     * @param em
     * @param scopeId
     * @param queuedJobExecutionId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link JobExecution} is not found
     */
    public static QueuedJobExecution delete(EntityManager em, KapuaId scopeId, KapuaId queuedJobExecutionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, QueuedJobExecutionImpl.class, scopeId, queuedJobExecutionId);
    }

}
