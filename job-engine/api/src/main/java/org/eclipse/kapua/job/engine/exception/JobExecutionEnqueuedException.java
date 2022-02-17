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
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link JobExecutionEnqueuedException} definition.
 * <p>
 * Occuus when a {@link org.eclipse.kapua.service.job.execution.JobExecution} is enqueued and it is thrown to stop the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
 *
 * @since 1.1.0
 */
public class JobExecutionEnqueuedException extends JobEngineException {

    private final KapuaId scopeId;
    private final KapuaId jobId;
    private final KapuaId jobExecutionId;
    private final KapuaId enqueuedJobExecutionId;

    /**
     * Constructor.
     *
     * @param scopeId        The scope {@link KapuaId}.
     * @param jobId          The {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     * @param jobExecutionId The current {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId}.
     * @param enqueuedJobId  The enqueued {@link org.eclipse.kapua.service.job.execution.JobExecution} {@link KapuaId}.
     * @since 1.1.0
     */
    public JobExecutionEnqueuedException(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId, KapuaId enqueuedJobId) {
        super(KapuaJobEngineErrorCodes.JOB_EXECUTION_ENQUEUED, scopeId, jobId, jobExecutionId, enqueuedJobId);
        this.scopeId = scopeId;
        this.jobId = jobId;
        this.jobExecutionId = jobExecutionId;
        this.enqueuedJobExecutionId = enqueuedJobId;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public KapuaId getJobId() {
        return jobId;
    }

    public KapuaId getJobExecutionId() {
        return jobExecutionId;
    }

    public KapuaId getEnqueuedJobExecutionId() {
        return enqueuedJobExecutionId;
    }
}
