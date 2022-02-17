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

/**
 * {@link SchedulerServiceException} to throw when {@link org.eclipse.kapua.service.scheduler.trigger.Trigger} will never fire for timing reasons
 *
 * @since 1.5.0
 */
public abstract class TriggerCannotFireException extends SchedulerServiceException {

    /**
     * Constructor.
     *
     * @param code      The {@link SchedulerServiceErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link TriggerCannotFireException}.
     * @param arguments Additional argument associated with the {@link TriggerCannotFireException}.
     * @since 1.5.0
     */
    public TriggerCannotFireException(SchedulerServiceErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
