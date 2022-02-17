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
package org.eclipse.kapua.service.job.execution.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;

/**
 * {@link JobExecution} {@link ServiceDAO}.
 *
 * @since 1.0.0
 */
public class JobExecutionDAO {

    private JobExecutionDAO() {
    }

    /**
     * Creates and return new JobExecution
     *
     * @param em
     * @param jobExecutionCreator
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static JobExecution create(EntityManager em, JobExecutionCreator jobExecutionCreator)
            throws KapuaException {

        JobExecutionImpl jobExecutionImpl = new JobExecutionImpl(jobExecutionCreator.getScopeId());
        jobExecutionImpl.setJobId(jobExecutionCreator.getJobId());
        jobExecutionImpl.setStartedOn(jobExecutionCreator.getStartedOn());
        jobExecutionImpl.setEntityAttributes(jobExecutionCreator.getEntityAttributes());
        jobExecutionImpl.setTargetIds(jobExecutionCreator.getTargetIds());

        return ServiceDAO.create(em, jobExecutionImpl);
    }

    /**
     * Updates the provided jobExecution
     *
     * @param em
     * @param jobExecution
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static JobExecution update(EntityManager em, JobExecution jobExecution)
            throws KapuaException {
        //
        // Update jobExecution
        JobExecutionImpl jobExecutionImpl = (JobExecutionImpl) jobExecution;

        return ServiceDAO.update(em, JobExecutionImpl.class, jobExecutionImpl);
    }

    /**
     * Finds the jobExecution by jobExecution identifier
     *
     * @param em
     * @param scopeId
     * @param jobExecutionId
     * @return
     * @since 1.0.0
     */
    public static JobExecution find(EntityManager em, KapuaId scopeId, KapuaId jobExecutionId) {
        return ServiceDAO.find(em, JobExecutionImpl.class, scopeId, jobExecutionId);
    }

    /**
     * Finds the jobExecution by name
     *
     * @param em
     * @param name
     * @return
     * @since 1.0.0
     */
    public static JobExecution findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, JobExecutionImpl.class, KapuaNamedEntityAttributes.NAME, name);
    }

    /**
     * Returns the jobExecution list matching the provided query
     *
     * @param em
     * @param jobExecutionQuery
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static JobExecutionListResult query(EntityManager em, KapuaQuery jobExecutionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, JobExecution.class, JobExecutionImpl.class, new JobExecutionListResultImpl(), jobExecutionQuery);
    }

    /**
     * Returns the jobExecution count matching the provided query
     *
     * @param em
     * @param jobExecutionQuery
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static long count(EntityManager em, KapuaQuery jobExecutionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, JobExecution.class, JobExecutionImpl.class, jobExecutionQuery);
    }

    /**
     * Deletes the jobExecution by jobExecution identifier
     *
     * @param em
     * @param scopeId
     * @param jobExecutionId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link JobExecution} is not found
     * @since 1.0.0
     */
    public static JobExecution delete(EntityManager em, KapuaId scopeId, KapuaId jobExecutionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, JobExecutionImpl.class, scopeId, jobExecutionId);
    }

}
