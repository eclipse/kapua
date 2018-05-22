/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.job.JobDomain;

public interface JobEngineService extends KapuaService, KapuaDomainService<JobDomain> {

    JobDomain JOB_DOMAIN = new JobDomain();

    @Override
    default JobDomain getServiceDomain() {
        return JOB_DOMAIN;
    }

    /**
     * Starts the {@link org.eclipse.kapua.service.job.Job}
     *
     * @param scopeId The scopeId of the {@link org.eclipse.kapua.service.job.Job}
     * @param jobId   The id of the {@link org.eclipse.kapua.service.job.Job}
     * @throws KapuaException if something goes bad when starting the job
     * @since 1.0.0
     */
    void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException;

    /**
     * Starts the {@link org.eclipse.kapua.service.job.Job} with the given {@link JobStartOptions}.
     *
     * @param scopeId         The scopeId of the {@link org.eclipse.kapua.service.job.Job}
     * @param jobId           The id of the {@link org.eclipse.kapua.service.job.Job}
     * @param jobStartOptions The {@link JobStartOptions} for this start {@link org.eclipse.kapua.service.job.Job} request.
     * @throws KapuaException if something goes bad when starting the job
     * @since 1.0.0
     */
    void startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) throws KapuaException;

    /**
     * Checks whether or not the {@link org.eclipse.kapua.service.job.Job} is running.
     *
     * @param scopeId The scopeId of the {@link org.eclipse.kapua.service.job.Job}
     * @param jobId   The id of the {@link org.eclipse.kapua.service.job.Job}
     * @return {@code true} if the {@link org.eclipse.kapua.service.job.Job} is currently running inside the {@link JobEngineService}
     * @throws KapuaException if something goes bad when checking the status of the job
     * @since 1.0.0
     */
    boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException;

    /**
     * Stops the {@link org.eclipse.kapua.service.job.Job}.
     * <p>
     * This method does not wait fot the {@link org.eclipse.kapua.service.job.Job} to completely stop.
     * It ask the {@link org.eclipse.kapua.service.job.Job} to stop but it can take some time to completely stop,
     * depending on the current status of the {@link org.eclipse.kapua.service.job.Job}.
     * <p>
     * {@link JobEngineService#isRunning(KapuaId, KapuaId)} can be used to check the actual running status of the {@link org.eclipse.kapua.service.job.Job}
     *
     * @param scopeId The scopeId of the {@link org.eclipse.kapua.service.job.Job}
     * @param jobId   The id of the {@link org.eclipse.kapua.service.job.Job}
     * @throws KapuaException if something goes bad when checking the status of the job
     * @since 1.0.0
     */
    void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException;

    /**
     * Cleans all the Job related data from the data structures supporting the {@link JobEngineService}
     *
     * @param scopeId The scopeId of the {@link org.eclipse.kapua.service.job.Job}
     * @param jobId   The id of the {@link org.eclipse.kapua.service.job.Job}
     * @throws KapuaException if something goes bad when checking the status of the job
     * @since 1.0.0
     */
    void cleanJobData(KapuaId scopeId, KapuaId jobId) throws KapuaException;
}
