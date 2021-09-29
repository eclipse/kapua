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

import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.step.JobStep;

import javax.validation.constraints.NotNull;

/**
 * {@link JobEngineException} to {@code throw} when {@link JobEngineService#startJob(KapuaId, KapuaId)} is invoked on a {@link Job} that has no {@link JobStep} defined.
 *
 * @since 1.0.0
 */
public class JobMissingTargetException extends JobScopedEngineException {

    private static final long serialVersionUID = 8149524601245117470L;

    /**
     * Constructor.
     *
     * @param scopeId The {@link Job#getScopeId()}.
     * @param jobId   The {@link Job#getId()}.
     * @since 1.0.0
     */
    public JobMissingTargetException(@NotNull KapuaId scopeId, @NotNull KapuaId jobId) {
        super(JobEngineErrorCodes.JOB_TARGET_MISSING, scopeId, jobId);
    }
}
