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
package org.eclipse.kapua.service.scheduler.quartz.driver.exception;

import org.quartz.JobDetail;
import org.quartz.JobKey;

import javax.validation.constraints.NotNull;

/**
 * Exception to {@code throw} when {@link org.quartz.Scheduler#addJob(JobDetail, boolean)} fails.
 *
 * @see org.quartz.Scheduler#addJob(JobDetail, boolean)
 * @since 1.1.0
 */
public class CannotAddQuartzJobException extends QuartzTriggerDriverException {

    private final Class quartzJob;
    private final JobKey jobKey;

    /**
     * Constructor that wraps the original exception when invoking {@link org.quartz.Scheduler#addJob(JobDetail, boolean)} }
     *
     * @param cause The original exception
     * @since 1.1.0
     */
    public CannotAddQuartzJobException(@NotNull Throwable cause, @NotNull Class quartzJob, @NotNull JobKey jobKey) {
        super(QuartzTriggerDriverErrorCodes.CANNOT_ADD_JOB, cause, quartzJob.getName(), jobKey.getName());

        this.quartzJob = quartzJob;
        this.jobKey = jobKey;
    }

    /**
     * Gets the {@link org.quartz.Job} that has caused the original exception.
     *
     * @return The {@link org.quartz.Job}.
     * @since 1.1.0
     */
    public Class getQuartzJob() {
        return quartzJob;
    }

    /**
     * Gets the {@link JobKey} that has caused the original exception.
     *
     * @return The {@link JobKey}.
     * @since 1.1.0
     */
    public JobKey getJobKey() {
        return jobKey;
    }
}
