/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaTransactedRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepTransactedRepository;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class JobStepMigratorJpaTransactedRepository
        extends KapuaNamedEntityJpaTransactedRepository<JobStep, JobStepMigrator, JobStepListResult>
        implements JobStepTransactedRepository {

    public JobStepMigratorJpaTransactedRepository(TxManager txManager, Supplier<JobStepListResult> listSupplier) {
        super(txManager, JobStepMigrator.class, listSupplier);
    }

    @Override
    public JobStep update(JobStep updatedEntity) throws KapuaException {
        JobStepMigrator jobStepImpl = (JobStepMigrator) updatedEntity;

        for (JobStepProperty jobStepProperty : jobStepImpl.getStepProperties()) {
            jobStepProperty.setPropertyValue(jobStepProperty.getPropertyValue());
        }
        return super.update(updatedEntity);
    }

    @Override
    public JobStep find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JobStep create(JobStep entity) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JobStep delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JobStep findByName(String name) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JobStep findByName(KapuaId scopeId, String name) throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
