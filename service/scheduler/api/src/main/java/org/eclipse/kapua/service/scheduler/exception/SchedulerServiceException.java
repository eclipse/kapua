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

import org.eclipse.kapua.KapuaException;

/**
 * {@link KapuaException} base for Scheduler Service {@link Exception}s
 *
 * @since 1.5.0
 */
public abstract class SchedulerServiceException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "scheduler-service-error-messages";

    /**
     * Constructor.
     *
     * @param code      The {@link SchedulerServiceErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link SchedulerServiceException}.
     * @param arguments Additional argument associated with the {@link SchedulerServiceException}.
     * @since 1.5.0
     */
    public SchedulerServiceException(SchedulerServiceErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
