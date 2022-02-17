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
package org.eclipse.kapua.service.device.management.job.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationListResult;

/**
 * {@link JobDeviceManagementOperation} {@link ServiceDAO}
 *
 * @since 1.1.0
 */
public class JobDeviceManagementOperationDAO {

    private JobDeviceManagementOperationDAO() {
    }

    /**
     * Creates and return new {@link JobDeviceManagementOperation}
     *
     * @param em
     * @param jobDeviceManagementOperationCreator
     * @return
     * @throws KapuaException
     * @since 1.1.0
     */
    public static JobDeviceManagementOperation create(EntityManager em, JobDeviceManagementOperationCreator jobDeviceManagementOperationCreator)
            throws KapuaException {
        //
        // Create JobDeviceManagementOperation
        JobDeviceManagementOperationImpl jobDeviceManagementOperationImpl = new JobDeviceManagementOperationImpl(jobDeviceManagementOperationCreator.getScopeId());
        jobDeviceManagementOperationImpl.setJobId(jobDeviceManagementOperationCreator.getJobId());
        jobDeviceManagementOperationImpl.setDeviceManagementOperationId(jobDeviceManagementOperationCreator.getDeviceManagementOperationId());

        return ServiceDAO.create(em, jobDeviceManagementOperationImpl);
    }

    /**
     * Finds the {@link JobDeviceManagementOperation} by its identifier
     *
     * @param em
     * @param scopeId
     * @param jobDeviceManagementOperationId
     * @return
     * @since 1.1.0
     */
    public static JobDeviceManagementOperation find(EntityManager em, KapuaId scopeId, KapuaId jobDeviceManagementOperationId) {
        return ServiceDAO.find(em, JobDeviceManagementOperationImpl.class, scopeId, jobDeviceManagementOperationId);
    }

    /**
     * Returns the JobDeviceManagementOperation list matching the provided query
     *
     * @param em
     * @param jobDeviceManagementOperationQuery
     * @return
     * @throws KapuaException
     * @since 1.1.0
     */
    public static JobDeviceManagementOperationListResult query(EntityManager em, KapuaQuery jobDeviceManagementOperationQuery)
            throws KapuaException {
        return ServiceDAO.query(em, JobDeviceManagementOperation.class, JobDeviceManagementOperationImpl.class, new JobDeviceManagementOperationListResultImpl(), jobDeviceManagementOperationQuery);
    }

    /**
     * Returns the jobDeviceManagementOperation count matching the provided query
     *
     * @param em
     * @param jobDeviceManagementOperationQuery
     * @return
     * @throws KapuaException
     * @since 1.1.0
     */
    public static long count(EntityManager em, KapuaQuery jobDeviceManagementOperationQuery)
            throws KapuaException {
        return ServiceDAO.count(em, JobDeviceManagementOperation.class, JobDeviceManagementOperationImpl.class, jobDeviceManagementOperationQuery);
    }


    /**
     * Deletes the {@link JobDeviceManagementOperation} by its identifier
     *
     * @param em
     * @param scopeId
     * @param jobDeviceManagementOperationId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link JobDeviceManagementOperation} is not found
     * @since 1.1.0
     */
    public static JobDeviceManagementOperation delete(EntityManager em, KapuaId scopeId, KapuaId jobDeviceManagementOperationId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, JobDeviceManagementOperationImpl.class, scopeId, jobDeviceManagementOperationId);
    }
}
