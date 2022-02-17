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

import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * {@link JobScopedEngineException} to {@code throw} when {@link JobEngineService#startJob(KapuaId, KapuaId)} is invoked on a set of {@link JobTarget}s already in processing.
 *
 * @since 1.0.0
 */
public class JobAlreadyRunningException extends JobScopedEngineException {

    private static final long serialVersionUID = -2111938324698571671L;

    private final KapuaId jobExecutionId;
    private final Set<KapuaId> jobTargetIdSubset;

    /**
     * Constuctor.
     *
     * @param scopeId           The {@link Job#getScopeId()}.
     * @param jobId             The {@link Job#getId()}.
     * @param jobExecutionId    The current {@link JobExecution#getId()}
     * @param jobTargetIdSubset The sub{@link Set} of {@link JobTarget#getId()}s.
     * @since 1.0.0
     */
    public JobAlreadyRunningException(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull KapuaId jobExecutionId, @NotNull Set<KapuaId> jobTargetIdSubset) {
        super(JobEngineErrorCodes.JOB_ALREADY_RUNNING, scopeId, jobId, jobExecutionId, jobTargetIdSubset.toArray());

        this.jobExecutionId = jobExecutionId;
        this.jobTargetIdSubset = jobTargetIdSubset;
    }

    /**
     * Gets the current {@link JobExecution#getId()} that cannot be started.
     *
     * @return The current {@link JobExecution#getId()} that cannot be started.
     * @since 1.0.0
     */
    public KapuaId getJobExecutionId() {
        return jobExecutionId;
    }

    /**
     * Gets the {@link Set} of {@link JobTarget}s to process for this {@link JobExecution}.
     *
     * @return The {@link Set} of {@link JobTarget}s to process for this {@link JobExecution}.
     * @since 1.0.0
     */
    public Set<KapuaId> getJobTargetIdSubset() {
        return jobTargetIdSubset;
    }
}
