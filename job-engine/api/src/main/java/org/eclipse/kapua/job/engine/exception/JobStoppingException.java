/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
 * {@link JobEngineException} to {@code throw} when a {@link Job} cannot be stopped for internal reasons.
 *
 * @since 1.0.0
 */
public class JobStoppingException extends JobScopedEngineException {

    private static final long serialVersionUID = 2961246592680304270L;

    private final KapuaId jobExecutionId;

    /**
     * Constructor.
     *
     * @param scopeId        The {@link Job#getScopeId()}.
     * @param jobId          The {@link Job#getId()}.
     * @param jobExecutionId The {@link JobExecution#getId()} that cannot be stopped.
     * @since 1.0.0
     */
    public JobStoppingException(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull KapuaId jobExecutionId) {
        super(JobEngineErrorCodes.JOB_EXECUTION_STOPPING, scopeId, jobId, jobExecutionId);

        this.jobExecutionId = jobExecutionId;
    }

    /**
     * Constructor.
     *
     * @param cause   The original {@link Throwable}.
     * @param scopeId The {@link Job#getScopeId()}.
     * @param jobId   The {@link Job#getId()}.
     * @since 1.0.0
     */
    public JobStoppingException(@NotNull Throwable cause, @NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        super(JobEngineErrorCodes.JOB_STOPPING, cause, scopeId, jobId);

        this.jobExecutionId = null;
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
    public JobStoppingException(@NotNull Throwable cause, @NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull KapuaId jobExecutionId) {
        super(JobEngineErrorCodes.JOB_EXECUTION_STOPPING, cause, scopeId, jobId, jobExecutionId);

        this.jobExecutionId = jobExecutionId;
    }

    /**
     * Gets the {@link JobExecution#getId()} that cannot be stopped.
     *
     * @return The {@link JobExecution#getId()} that cannot be stopped.
     * @since 2.0.0
     */
    public KapuaId getJobExecutionId() {
        return jobExecutionId;
    }
}
