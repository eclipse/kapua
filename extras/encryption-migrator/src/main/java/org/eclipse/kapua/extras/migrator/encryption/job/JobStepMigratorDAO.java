/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption.job;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

/**
 * JobStep DAO
 *
 * @since 2.0.0
 */
public class JobStepMigratorDAO {

    private JobStepMigratorDAO() {
    }

    public static JobStep update(EntityManager em, JobStep jobStep) throws KapuaException {
        JobStepMigrator jobStepImpl = (JobStepMigrator) jobStep;

        for (JobStepProperty jobStepProperty : jobStepImpl.getStepProperties()) {
            jobStepProperty.setPropertyValue(jobStepProperty.getPropertyValue());
        }

        return ServiceDAO.update(em, JobStepMigrator.class, jobStepImpl);
    }

    public static JobStepListResult query(EntityManager em, KapuaQuery jobStepQuery) throws KapuaException {
        return ServiceDAO.query(em, JobStep.class, JobStepMigrator.class, new JobStepMigratorListResultImpl(), jobStepQuery);
    }

    public static long count(EntityManager em, KapuaQuery jobStepQuery) throws KapuaException {
        return ServiceDAO.count(em, JobStep.class, JobStepMigrator.class, jobStepQuery);
    }

    //
    // Unsupported methods
    //

    public static JobStep create(EntityManager em, JobStepCreator jobStepCreator) {
        throw new UnsupportedOperationException();
    }

    public static JobStep find(EntityManager em, KapuaId scopeId, KapuaId jobStepId) {
        throw new UnsupportedOperationException();
    }

    public static JobStep findByName(EntityManager em, String name) {
        throw new UnsupportedOperationException();
    }

    public static JobStep delete(EntityManager em, KapuaId scopeId, KapuaId jobStepId) throws KapuaEntityNotFoundException {
        throw new UnsupportedOperationException();
    }
}
