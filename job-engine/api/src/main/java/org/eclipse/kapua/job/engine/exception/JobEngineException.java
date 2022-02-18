/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.KapuaException;

import javax.validation.constraints.NotNull;

/**
 * Base {@link KapuaException} for the {@code kapua-job-engine-api} module.
 *
 * @since 1.0.0
 */
public abstract class JobEngineException extends KapuaException {

    private static final long serialVersionUID = 6422745329878392484L;

    private static final String ERROR_MESSAGES_BUNDLE_NAME = "job-engine-service-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link JobEngineErrorCodes} associated with the {@link Exception}
     * @since 1.0.0
     */
    protected JobEngineException(@NotNull JobEngineErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link JobEngineErrorCodes} associated with the {@link Exception}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    protected JobEngineException(@NotNull JobEngineErrorCodes code, @NotNull Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link JobEngineErrorCodes} associated with the {@link Exception}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    protected JobEngineException(@NotNull JobEngineErrorCodes code, @NotNull Throwable cause, @NotNull Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return ERROR_MESSAGES_BUNDLE_NAME;
    }
}
