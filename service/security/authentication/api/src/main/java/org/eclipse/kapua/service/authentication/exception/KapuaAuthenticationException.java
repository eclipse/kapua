/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.exception;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;

import javax.validation.constraints.NotNull;

/**
 * {@link KapuaException} for `kapua-authentication-api` module.
 *
 * @since 2.0.0
 */
public class KapuaAuthenticationException extends KapuaException {

    private static final String ERROR_MESSAGE_RESOURCE_BUNDLE = "authentication-error-messages";

    /**
     * Constructor.
     *
     * @param code The {@link KapuaAuthenticationErrorCodes} associated with the {@link KapuaAuthenticationException}.
     * @since 2.0.0
     */
    public KapuaAuthenticationException(@NotNull KapuaAuthenticationErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaAuthenticationErrorCodes} associated with the {@link KapuaAuthenticationException}.
     * @param arguments The arguments associated with the {@link KapuaAuthenticationException}.
     * @since 2.0.0
     */
    public KapuaAuthenticationException(@NotNull KapuaAuthenticationErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaAuthenticationErrorCodes} associated with the {@link KapuaAuthenticationException}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link KapuaAuthenticationException}.
     * @since 2.0.0
     */
    public KapuaAuthenticationException(@NotNull KapuaAuthenticationErrorCodes code, @NotNull Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }


    /**
     * /**
     * Constructor.
     *
     * @param code The {@link KapuaErrorCode} associated with the {@link KapuaAuthenticationException}.
     * @since 2.0.0
     */
    public KapuaAuthenticationException(@NotNull AuthenticationErrorCodes code) {
        super(code);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link KapuaAuthenticationException}.
     * @param arguments The arguments associated with the {@link KapuaAuthenticationException}.
     * @since 2.0.0
     */
    public KapuaAuthenticationException(@NotNull AuthenticationErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link KapuaAuthenticationException}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link KapuaAuthenticationException}.
     * @since 2.0.0
     */
    public KapuaAuthenticationException(@NotNull AuthenticationErrorCodes code, @NotNull Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    public String getKapuaErrorMessagesBundle() {
        return ERROR_MESSAGE_RESOURCE_BUNDLE;
    }
}
