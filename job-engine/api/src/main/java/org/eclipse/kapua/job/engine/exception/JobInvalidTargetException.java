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

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * {@link JobEngineException} to {@code throw} if one or more {@link JobTarget#getId()}s contained in the {@link JobStartOptions#getTargetIdSublist()} is/are not already defined as a {@link JobTarget}.
 *
 * @since 1.0.0
 */
public class JobInvalidTargetException extends JobScopedEngineException {

    private static final long serialVersionUID = 2590965196292571376L;

    private final Set<KapuaId> targetSublist;

    /**
     * Constructor.
     *
     * @param scopeId       The {@link Job#getScopeId()}.
     * @param jobId         The {@link Job#getId()}.
     * @param targetSublist The {@link JobStartOptions#getTargetIdSublist()}.
     * @since 1.0.0
     */
    public JobInvalidTargetException(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull Set<KapuaId> targetSublist) {
        super(JobEngineErrorCodes.JOB_TARGET_INVALID, scopeId, jobId, targetSublist.size());

        this.targetSublist = targetSublist;
    }

    /**
     * Gets the {@link JobStartOptions#getTargetIdSublist()} which contains invalid {@link JobTarget#getId()}s
     *
     * @return The {@link JobStartOptions#getTargetIdSublist()} which contains invalid {@link JobTarget#getId()}s
     * @since 1.0.0
     */
    public Set<KapuaId> getTargetSublist() {
        return targetSublist;
    }

}
