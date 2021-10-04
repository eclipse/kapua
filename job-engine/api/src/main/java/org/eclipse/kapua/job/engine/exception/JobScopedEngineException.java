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

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;

import javax.validation.constraints.NotNull;

/**
 * Base {@link JobEngineException} for {@link Exception} that have a {@link Job} as a subject.
 *
 * @since 1.0.0
 */
public abstract class JobScopedEngineException extends JobEngineException {

    private static final long serialVersionUID = -8595904104442937089L;

    private final KapuaId scopeId;
    private final KapuaId jobId;

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param scopeId   The {@link Job#getScopeId()}.
     * @param jobId     The {@link Job#getId()}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    protected JobScopedEngineException(@NotNull JobEngineErrorCodes code, @NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull Object... arguments) {
        super(code, scopeId, jobId, arguments);

        this.scopeId = scopeId;
        this.jobId = jobId;
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param cause     The original {@link Throwable}.
     * @param scopeId   The {@link Job#getScopeId()}.
     * @param jobId     The {@link Job#getId()}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    protected JobScopedEngineException(@NotNull JobEngineErrorCodes code, @NotNull Throwable cause, @NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull Object... arguments) {
        super(code, cause, scopeId, jobId, arguments);

        this.scopeId = scopeId;
        this.jobId = jobId;
    }

    /**
     * Gets the {@link Job#getScopeId()}.
     *
     * @return The {@link Job#getScopeId()}.
     * @since 1.0.0
     */
    public KapuaId getScopeId() {
        return scopeId;
    }

    /**
     * Gets the {@link Job#getId()}.
     *
     * @return The {@link Job#getId()}.
     * @since 1.0.0
     */
    public KapuaId getJobId() {
        return jobId;
    }
}
