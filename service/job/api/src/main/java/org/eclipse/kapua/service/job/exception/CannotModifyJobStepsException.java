/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.exception;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.step.JobStep;

/**
 * {@link JobServiceException} to throw when trying to modify {@link JobStep}s of a {@link Job} that already started once.
 *
 * @since 1.5.0
 */
public class CannotModifyJobStepsException extends JobServiceException {

    private final KapuaId jobId;

    /**
     * Constructor.
     *
     * @param jobId The {@link Job#getId()}.
     * @since 1.5.0
     */
    public CannotModifyJobStepsException(KapuaId jobId) {
        super(JobServiceErrorCodes.CANNOT_MODIFY_JOB_STEPS, jobId);

        this.jobId = jobId;
    }

    /**
     * Gets the {@link Job#getId()}.
     *
     * @return The {@link Job#getId()}.
     * @since 1.5.0
     */
    public KapuaId getJobId() {
        return jobId;
    }
}
