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
package org.eclipse.kapua.service.job.exception;

import org.eclipse.kapua.KapuaException;

/**
 * {@link KapuaException} base for Scheduler Service {@link Exception}s
 *
 * @since 1.5.0
 */
public abstract class JobServiceException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "job-service-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link JobServiceErrorCodes}.
     * @since 1.5.0
     */
    public JobServiceException(JobServiceErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link JobServiceErrorCodes}.
     * @param arguments Additional argument associated with the {@link JobServiceException}.
     * @since 1.5.0
     */
    public JobServiceException(JobServiceErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link JobServiceErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link JobServiceException}.
     * @param arguments Additional argument associated with the {@link JobServiceException}.
     * @since 1.5.0
     */
    public JobServiceException(JobServiceErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
