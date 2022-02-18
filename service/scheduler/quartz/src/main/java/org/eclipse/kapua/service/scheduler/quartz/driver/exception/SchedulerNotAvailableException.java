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

import org.quartz.SchedulerFactory;

import javax.validation.constraints.NotNull;

/**
 * Exception to {@code throw} when {@link SchedulerFactory#getScheduler()} fails.
 *
 * @see SchedulerFactory#getScheduler()
 * @since 1.1.0
 */
public class SchedulerNotAvailableException extends QuartzTriggerDriverException {

    /**
     * Constructor that wraps the original exception when invoking {@link SchedulerFactory#getScheduler()}
     *
     * @param cause The original exception
     * @since 1.1.0
     */
    public SchedulerNotAvailableException(@NotNull Throwable cause) {
        super(QuartzTriggerDriverErrorCodes.SCHEDULER_NOT_AVAILABLE, cause, (Object) null);
    }
}
