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
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;

/**
 * JobStepDefinition DAO
 *
 * @since 1.0
 */
public class JobStepDefinitionDAO {

    private JobStepDefinitionDAO() {
    }

    /**
     * Creates and return new JobStepDefinition
     *
     * @param em
     * @param jobStepDefinitionCreator
     * @return
     * @throws KapuaException
     */
    public static JobStepDefinition create(EntityManager em, JobStepDefinitionCreator jobStepDefinitionCreator)
            throws KapuaException {
        //
        // Create JobStepDefinition

        JobStepDefinitionImpl jobStepDefinitionImpl = new JobStepDefinitionImpl(jobStepDefinitionCreator.getScopeId());
        jobStepDefinitionImpl.setName(jobStepDefinitionCreator.getName());
        jobStepDefinitionImpl.setDescription(jobStepDefinitionCreator.getDescription());
        jobStepDefinitionImpl.setStepType(jobStepDefinitionCreator.getStepType());
        jobStepDefinitionImpl.setReaderName(jobStepDefinitionCreator.getReaderName());
        jobStepDefinitionImpl.setProcessorName(jobStepDefinitionCreator.getProcessorName());
        jobStepDefinitionImpl.setWriterName(jobStepDefinitionCreator.getWriterName());
        jobStepDefinitionImpl.setStepProperties(jobStepDefinitionCreator.getStepProperties());

        return ServiceDAO.create(em, jobStepDefinitionImpl);
    }

    /**
     * Updates the provided stepDefinition
     *
     * @param em
     * @param jobStepDefinition
     * @return
     * @throws KapuaException
     */
    public static JobStepDefinition update(EntityManager em, JobStepDefinition jobStepDefinition)
            throws KapuaException {
        //
        // Update stepDefinition
        JobStepDefinitionImpl jobStepDefinitionImpl = (JobStepDefinitionImpl) jobStepDefinition;

        return ServiceDAO.update(em, JobStepDefinitionImpl.class, jobStepDefinitionImpl);
    }

    /**
     * Finds the stepDefinition by stepDefinition identifier
     */
    public static JobStepDefinition find(EntityManager em, KapuaId scopeId, KapuaId stepDefinitionId) {
        return ServiceDAO.find(em, JobStepDefinitionImpl.class, scopeId, stepDefinitionId);
    }

    /**
     * Returns the stepDefinition list matching the provided query
     *
     * @param em
     * @param stepDefinitionQuery
     * @return
     * @throws KapuaException
     */
    public static JobStepDefinitionListResult query(EntityManager em, KapuaQuery stepDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, JobStepDefinition.class, JobStepDefinitionImpl.class, new JobStepDefinitionListResultImpl(), stepDefinitionQuery);
    }

    /**
     * Returns the stepDefinition count matching the provided query
     *
     * @param em
     * @param stepDefinitionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery stepDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, JobStepDefinition.class, JobStepDefinitionImpl.class, stepDefinitionQuery);
    }

    /**
     * Deletes the stepDefinition by stepDefinition identifier
     *
     * @param em
     * @param scopeId
     * @param stepDefinitionId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link JobStepDefinition} is not found
     */
    public static JobStepDefinition delete(EntityManager em, KapuaId scopeId, KapuaId stepDefinitionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, JobStepDefinitionImpl.class, scopeId, stepDefinitionId);
    }

    public static JobStepDefinition findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, JobStepDefinitionImpl.class, KapuaNamedEntityAttributes.NAME, name);
    }

}
