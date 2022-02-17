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
package org.eclipse.kapua.service.scheduler.quartz.driver.exception;

import org.quartz.TriggerKey;

import javax.validation.constraints.NotNull;

/**
 * Exception to {@code throw} when {@link org.quartz.Scheduler#unscheduleJob(TriggerKey)} fails.
 *
 * @see org.quartz.Scheduler#unscheduleJob(TriggerKey)
 * @since 1.5.0
 */
public class CannotUnscheduleJobException extends QuartzTriggerDriverException {

    private final TriggerKey triggerKey;

    /**
     * Constructor that wraps the original exception when invoking {@link org.quartz.Scheduler#unscheduleJob(TriggerKey)}
     *
     * @param cause      The original exception
     * @param triggerKey The {@link TriggerKey} that failed to schedule.
     * @since 1.5.0
     */
    public CannotUnscheduleJobException(@NotNull Throwable cause, @NotNull TriggerKey triggerKey) {
        super(QuartzTriggerDriverErrorCodes.CANNOT_SCHEDULE_JOB, cause, triggerKey);

        this.triggerKey = triggerKey;
    }

    /**
     * Gets the {@link TriggerKey} that failed to schedule.
     *
     * @return The {@link TriggerKey} that failed to schedule.
     * @since 1.5.0
     */
    public TriggerKey getTriggerKey() {
        return triggerKey;
    }
}
