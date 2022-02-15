/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.execution.JobExecution;

import javax.validation.constraints.NotNull;

/**
 * The {@link org.eclipse.kapua.service.job.Job} resume has thrown an error.
 *
 * @since 1.0.0
 */
public class JobResumingException extends JobScopedEngineException {

    private static final long serialVersionUID = 3492844432935460889L;

    private final KapuaId jobExecutionId;

    /**
     * Constructor.
     *
     * @param scopeId        The {@link Job#getScopeId()}.
     * @param jobId          The {@link Job#getId()}.
     * @param jobExecutionId The {@link JobExecution#getId()} that cannot be stopped.
     * @since 1.0.0
     */
    public JobResumingException(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull KapuaId jobExecutionId) {
        super(JobEngineErrorCodes.JOB_RESUMING, scopeId, jobId, jobExecutionId);

        this.jobExecutionId = jobExecutionId;
    }

    /**
     * Constructor.
     *
     * @param cause          The original {@link Throwable}.
     * @param scopeId        The {@link Job#getScopeId()}.
     * @param jobId          The {@link Job#getId()}.
     * @param jobExecutionId The {@link JobExecution#getId()} that cannot be stopped.
     * @since 1.0.0
     */
    public JobResumingException(@NotNull Throwable cause, @NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull KapuaId jobExecutionId) {
        super(JobEngineErrorCodes.JOB_RESUMING, cause, scopeId, jobId, jobExecutionId);

        this.jobExecutionId = jobExecutionId;
    }

    /**
     * Gets the {@link JobExecution#getId()} which was not able to resume.
     *
     * @return The {@link JobExecution#getId()} which was not able to resume.
     * @since 2.0.0
     */
    public KapuaId getJobExecutionId() {
        return jobExecutionId;
    }
}
