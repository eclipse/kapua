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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepRepository;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Singleton;

/**
 * {@link JobStepService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class JobStepMigratorServiceImpl implements JobStepService {
    private final TxManager txManager;
    private final JobStepRepository jobStepRepository;

    public JobStepMigratorServiceImpl(TxManager txManager, JobStepRepository jobStepRepository) {
        this.txManager = txManager;
        this.jobStepRepository = jobStepRepository;
    }

    @Override
    public JobStep update(JobStep jobStep) throws KapuaException {
        return txManager.execute(tx -> jobStepRepository.update(tx, jobStep));
    }

    @Override
    public JobStepListResult query(KapuaQuery query) throws KapuaException {
        return txManager.execute(tx -> jobStepRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        return txManager.execute(tx -> jobStepRepository.count(tx, query));
    }

    //
    // Unsupported methods
    //

    @Override
    public JobStep create(JobStepCreator jobStepCreator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JobStep find(KapuaId scopeId, KapuaId jobStepId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobStepId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getJobStepPropertyMaxLength() throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
