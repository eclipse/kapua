/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link QuartzTriggerDriverException}.
 *
 * @since 1.1.0
 */
public enum QuartzTriggerDriverErrorCodes implements KapuaErrorCode {
    /**
     * @since 1.1.0
     */
    SCHEDULER_NOT_AVAILABLE,

    /**
     * @since 1.1.0
     */
    CANNOT_ADD_JOB,

    /**
     * @since 1.1.0
     */
    CANNOT_SCHEDULE_JOB,

    /**
     * @since 1.1.0
     */
    TRIGGER_NEVER_FIRES,
}
