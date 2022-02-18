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

import javax.validation.constraints.NotNull;

/**
 * {@link JobScopedEngineException} to {@code throw} when {@link JobEngineService#cleanJobData(KapuaId, KapuaId)} fails.
 *
 * @since 1.0.0
 */
public class CleanJobDataException extends JobScopedEngineException {

    private static final long serialVersionUID = 1707473193853657195L;

    /**
     * Constructor.
     *
     * @param scopeId The {@link Job#getScopeId()}
     * @param jobId   The {@link Job#getId()}
     * @since 1.0.0
     */
    public CleanJobDataException(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        super(JobEngineErrorCodes.CANNOT_CLEANUP_JOB_DATA, scopeId, jobId);
    }

    /**
     * Constructor.
     *
     * @param cause   The original {@link Throwable}.
     * @param scopeId The {@link Job#getScopeId()}
     * @param jobId   The {@link Job#getId()}
     * @since 1.0.0
     */
    public CleanJobDataException(@NotNull Throwable cause, @NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        super(JobEngineErrorCodes.CANNOT_CLEANUP_JOB_DATA, cause, scopeId, jobId);
    }
}
