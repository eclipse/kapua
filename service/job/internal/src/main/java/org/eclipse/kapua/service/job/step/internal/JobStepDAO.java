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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepRepository;

/**
 * JobStep DAO
 *
 * @since 1.0
 * @deprecated since 2.0.0, use {@link JobStepRepository} instead
 */
@Deprecated
public class JobStepDAO {

    private JobStepDAO() {
    }

    /**
     * Creates and return new JobStep
     *
     * @param em
     * @param jobStepCreator
     * @return
     * @throws KapuaException
     */
    public static org.eclipse.kapua.service.job.step.JobStep create(EntityManager em, JobStepCreator jobStepCreator)
            throws KapuaException {
        //
        // Create JobStep

        JobStepImpl jobStepImpl = new JobStepImpl(jobStepCreator.getScopeId());
        jobStepImpl.setName(jobStepCreator.getName());
        jobStepImpl.setDescription(jobStepCreator.getDescription());
        jobStepImpl.setJobId(jobStepCreator.getJobId());
        jobStepImpl.setJobStepDefinitionId(jobStepCreator.getJobStepDefinitionId());
        jobStepImpl.setStepIndex(jobStepCreator.getStepIndex());
        jobStepImpl.setStepProperties(jobStepCreator.getStepProperties());

        return ServiceDAO.create(em, jobStepImpl);
    }

    /**
     * Updates the provided jobStep
     *
     * @param em
     * @param jobStep
     * @return
     * @throws KapuaException
     */
    public static org.eclipse.kapua.service.job.step.JobStep update(EntityManager em, org.eclipse.kapua.service.job.step.JobStep jobStep)
            throws KapuaException {
        //
        // Update jobStep
        JobStepImpl jobStepImpl = (JobStepImpl) jobStep;

        return ServiceDAO.update(em, JobStepImpl.class, jobStepImpl);
    }

    /**
     * Finds the jobStep by jobStep identifier
     *
     * @param em
     * @param scopeId
     * @param jobStepId
     * @return
     */
    public static org.eclipse.kapua.service.job.step.JobStep find(EntityManager em, KapuaId scopeId, KapuaId jobStepId) {
        return ServiceDAO.find(em, JobStepImpl.class, scopeId, jobStepId);
    }

    /**
     * Finds the jobStep by name
     *
     * @param em
     * @param name
     * @return
     */
    public static org.eclipse.kapua.service.job.step.JobStep findByName(EntityManager em, String name) {
        return ServiceDAO.findByName(em, JobStepImpl.class, name);
    }

    /**
     * Returns the jobStep list matching the provided query
     *
     * @param em
     * @param jobStepQuery
     * @return
     * @throws KapuaException
     */
    public static JobStepListResult query(EntityManager em, KapuaQuery jobStepQuery)
            throws KapuaException {
        return ServiceDAO.query(em, org.eclipse.kapua.service.job.step.JobStep.class, JobStepImpl.class, new JobStepListResultImpl(), jobStepQuery);
    }

    /**
     * Returns the jobStep count matching the provided query
     *
     * @param em
     * @param jobStepQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery jobStepQuery)
            throws KapuaException {
        return ServiceDAO.count(em, org.eclipse.kapua.service.job.step.JobStep.class, JobStepImpl.class, jobStepQuery);
    }


    /**
     * Deletes the jobStep by jobStep identifier
     *
     * @param em
     * @param scopeId
     * @param jobStepId
     * @return the deleted entity
     * @throws KapuaEntityNotFoundException If the {@link org.eclipse.kapua.service.job.step.JobStep} is not found
     */
    public static org.eclipse.kapua.service.job.step.JobStep delete(EntityManager em, KapuaId scopeId, KapuaId jobStepId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, JobStepImpl.class, scopeId, jobStepId);
    }
}
