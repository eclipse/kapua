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
package org.eclipse.kapua.service.scheduler.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link SchedulerServiceException}
 *
 * @since 1.5.0
 */
public enum SchedulerServiceErrorCodes implements KapuaErrorCode {

    /**
     * See {@link TriggerInvalidDatesException}.
     *
     * @since 1.5.0
     */
    TRIGGER_INVALID_DATES,

    /**
     * See {@link TriggerInvalidSchedulingException}
     *
     * @since 1.5.0
     */
    TRIGGER_INVALID_SCHEDULE
}
