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

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import javax.validation.constraints.NotNull;

/**
 * Exception to {@code throw} when {@link org.quartz.Scheduler#scheduleJob(JobDetail, Trigger)} fails.
 *
 * @see org.quartz.Scheduler#scheduleJob(JobDetail, Trigger)
 * @since 1.1.0
 */
public class CannotScheduleJobException extends QuartzTriggerDriverException {

    private final JobDetail jobDetail;
    private final TriggerKey triggerKey;
    private final JobDataMap triggerDataMap;

    /**
     * Constructor that wraps the original exception when invoking {@link org.quartz.Scheduler#scheduleJob(JobDetail, Trigger)}
     *
     * @param cause          The original exception
     * @param jobDetail      The {@link JobDetail} that failed to schedule.
     * @param triggerKey     The {@link TriggerKey} that failed to schedule.
     * @param triggerDataMap The {@link JobDataMap} that failed to schedule.
     * @since 1.1.0
     */
    public CannotScheduleJobException(@NotNull Throwable cause, @NotNull JobDetail jobDetail, @NotNull TriggerKey triggerKey, @NotNull JobDataMap triggerDataMap) {
        super(QuartzTriggerDriverErrorCodes.CANNOT_SCHEDULE_JOB, cause, jobDetail.getKey());

        this.jobDetail = jobDetail;
        this.triggerKey = triggerKey;
        this.triggerDataMap = triggerDataMap;
    }

    /**
     * Gets the {@link JobDetail} that failed to schedule.
     *
     * @return The {@link JobDetail} that failed to schedule.
     * @since 1.1.0
     */
    public JobDetail getJobDetail() {
        return jobDetail;
    }

    /**
     * Gets the {@link TriggerKey} that failed to schedule.
     *
     * @return The {@link TriggerKey} that failed to schedule.
     * @since 1.1.0
     */
    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    /**
     * Gets the {@link JobDataMap} that failed to schedule.
     *
     * @return The {@link JobDataMap} that failed to schedule.
     * @since 1.1.0
     */
    public JobDataMap getTriggerDataMap() {
        return triggerDataMap;
    }
}
