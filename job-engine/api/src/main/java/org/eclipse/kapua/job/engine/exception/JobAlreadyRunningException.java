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

import java.util.Set;

/**
 * {@link JobAlreadyRunningException} definition.
 * <p>
 * Occurs when you start a {@link org.eclipse.kapua.service.job.execution.JobExecution} and there is another {@link org.eclipse.kapua.service.job.execution.JobExecution}
 * running with the same {@link org.eclipse.kapua.job.engine.JobStartOptions}.
 *
 * @since 1.0.0
 */
public class JobAlreadyRunningException extends JobEngineException {

    private final KapuaId scopeId;
    private final KapuaId jobId;
    private final KapuaId jobExecutionId;
    private final Set<KapuaId> jobTargetIdSubset;

    /**
     * Constuctor.
     *
     * @param scopeId           The scope {@link KapuaId}.
     * @param jobId             The {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     * @param jobExecutionId    The current {@link org.eclipse.kapua.service.job.execution.JobExecution}
     * @param jobTargetIdSubset The sub{@link Set} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s
     * @since 1.0.0
     */
    public JobAlreadyRunningException(KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId, Set<KapuaId> jobTargetIdSubset) {
        super(KapuaJobEngineErrorCodes.JOB_ALREADY_RUNNING, scopeId, jobId, jobExecutionId, jobTargetIdSubset.toArray());

        this.scopeId = scopeId;
        this.jobId = jobId;
        this.jobExecutionId = jobExecutionId;
        this.jobTargetIdSubset = jobTargetIdSubset;
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

    public Set<KapuaId> getJobTargetIdSubset() {
        return jobTargetIdSubset;
    }
}
