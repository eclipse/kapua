/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
