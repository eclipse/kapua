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
package org.eclipse.kapua.transport.exception;

import org.eclipse.kapua.KapuaException;

import org.checkerframework.checker.nullness.qual.Nullable;
import javax.validation.constraints.NotNull;

/**
 * {@link TransportException} definition.
 * <p>
 * Base class for {@code kapua-transport-api} {@link Exception}s.
 *
 * @since 1.1.0
 */
public abstract class TransportException extends KapuaException {

    private static final long serialVersionUID = -6207605695086240243L;

    private static final String KAPUA_ERROR_MESSAGES = "transport-client-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link TransportErrorCodes} associated this the {@link TransportException}
     * @since 1.1.0
     */
    protected TransportException(@NotNull TransportErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TransportErrorCodes} associated this the {@link TransportException}
     * @param arguments The arguments associated with the {@link TransportException}.
     * @since 1.1.0
     */
    protected TransportException(@NotNull TransportErrorCodes code, @Nullable Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link TransportErrorCodes} associated this the {@link TransportException}
     * @param cause     The root cause of the {@link TransportException}.
     * @param arguments The arguments associated with the {@link TransportException}.
     * @since 1.1.0
     */
    protected TransportException(@NotNull TransportErrorCodes code, @NotNull Throwable cause, @Nullable Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
