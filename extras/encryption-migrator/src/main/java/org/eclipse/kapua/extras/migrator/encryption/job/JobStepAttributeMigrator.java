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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.extras.migrator.encryption.api.EntitySecretAttributeMigrator;
import org.eclipse.kapua.extras.migrator.encryption.job.JobStepMigratorQueryImpl;
import org.eclipse.kapua.extras.migrator.encryption.job.JobStepMigratorServiceImpl;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.service.job.step.JobStep;

import java.util.List;

public class JobStepAttributeMigrator implements EntitySecretAttributeMigrator<JobStep> {

    private static final JobStepMigratorServiceImpl JOB_STEP_MIGRATOR_SERVICE = new JobStepMigratorServiceImpl();

    @Override
    public String getEntityName() {
        return JobStep.TYPE;
    }

    @Override
    public void migrate(List<JobStep> entitiesToMigrate) throws KapuaException {
        for (JobStep jobStep : entitiesToMigrate) {
            JOB_STEP_MIGRATOR_SERVICE.update(jobStep);
        }
    }

    @Override
    public List<JobStep> getChunk(int offset, int limit) throws KapuaException {
        JobStepMigratorQueryImpl query = new JobStepMigratorQueryImpl(null);

        // This is the most stable sorting even if it is not always indexed
        query.setSortCriteria(query.fieldSortCriteria(KapuaEntityAttributes.CREATED_ON, SortOrder.ASCENDING));

        query.setOffset(offset);
        query.setLimit(limit);

        return JOB_STEP_MIGRATOR_SERVICE.query(query).getItems();
    }

    @Override
    public long getTotalCount() throws KapuaException {
        JobStepMigratorQueryImpl query = new JobStepMigratorQueryImpl(null);

        return JOB_STEP_MIGRATOR_SERVICE.count(query);
    }
}
