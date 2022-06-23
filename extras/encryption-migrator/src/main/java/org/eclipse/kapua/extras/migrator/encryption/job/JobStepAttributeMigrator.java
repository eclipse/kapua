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

import org.eclipse.kapua.extras.migrator.encryption.api.AbstractEntityAttributeMigrator;
import org.eclipse.kapua.extras.migrator.encryption.api.EntitySecretAttributeMigrator;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.step.JobStep;

public class JobStepAttributeMigrator extends AbstractEntityAttributeMigrator<JobStep> implements EntitySecretAttributeMigrator<JobStep> {

    private static final JobStepMigratorServiceImpl JOB_STEP_MIGRATOR_SERVICE = new JobStepMigratorServiceImpl();

    public JobStepAttributeMigrator() {
        super(JOB_STEP_MIGRATOR_SERVICE);
    }

    @Override
    public String getEntityName() {
        return JobStep.TYPE;
    }

    @Override
    protected KapuaQuery newEntityQuery() {
        return new JobStepMigratorQueryImpl();
    }
}
