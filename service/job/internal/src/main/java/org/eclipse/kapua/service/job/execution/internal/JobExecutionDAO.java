/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.execution.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;

/**
 * JobExecution DAO
 * 
 * @since 1.0
 *
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
     */
    public static JobExecution create(EntityManager em, JobExecutionCreator jobExecutionCreator)
            throws KapuaException {
        //
        // Create JobExecution

        JobExecutionImpl jobExecutionImpl = new JobExecutionImpl(jobExecutionCreator.getScopeId());
        jobExecutionImpl.setJobId(jobExecutionCreator.getJobId());
        jobExecutionImpl.setStartedOn(jobExecutionCreator.getStartedOn());
        jobExecutionImpl.setEntityAttributes(jobExecutionCreator.getEntityAttributes());

        return ServiceDAO.create(em, jobExecutionImpl);
    }

    /**
     * Updates the provided jobExecution
     * 
     * @param em
     * @param jobExecution
     * @return
     * @throws KapuaException
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
     */
    public static JobExecution findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, JobExecutionImpl.class, "name", name);
    }

    /**
     * Returns the jobExecution list matching the provided query
     *
     * @param em
     * @param jobExecutionQuery
     * @return
     * @throws KapuaException
     */
    public static JobExecutionListResult query(EntityManager em, KapuaQuery<JobExecution> jobExecutionQuery)
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
     */
    public static long count(EntityManager em, KapuaQuery<JobExecution> jobExecutionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, JobExecution.class, JobExecutionImpl.class, jobExecutionQuery);
    }

    /**
     * Deletes the jobExecution by jobExecution identifier
     *
     * @param em
     * @param scopeId
     * @param jobExecutionId
     * @throws KapuaEntityNotFoundException
     *             If the {@link JobExecution} is not found
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId jobExecutionId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, JobExecutionImpl.class, scopeId, jobExecutionId);
    }

}
