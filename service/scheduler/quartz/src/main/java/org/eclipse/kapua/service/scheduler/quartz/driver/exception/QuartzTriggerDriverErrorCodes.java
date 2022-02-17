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
     * @since 1.5.0
     */
    CANNOT_UNSCHEDULE_JOB,

    /**
     * @since 1.1.0
     */
    TRIGGER_NEVER_FIRES,
}
