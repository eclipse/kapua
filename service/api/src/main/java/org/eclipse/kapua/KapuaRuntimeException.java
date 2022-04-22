/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

import java.util.Locale;

/**
 * {@link KapuaRuntimeException} definition.
 *
 * @since 1.0.0
 */
public class KapuaRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5389531827567100733L;

    private static final String KAPUA_ERROR_MESSAGES = "kapua-service-error-messages";

    private final KapuaErrorCode code;
    private final Object[] args;

    /**
     * Constructor.
     *
     * @param code The {@link KapuaErrorCode} associated with the {@link Exception}
     * @since 1.0.0
     */
    public KapuaRuntimeException(KapuaErrorCode code) {
        this(code, (Object[]) null);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    public KapuaRuntimeException(KapuaErrorCode code, Object... arguments) {
        this(code, null, arguments);
    }

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code      The {@link KapuaErrorCode} associated with the {@link Exception}.
     * @param cause     The original {@link Throwable}.
     * @param arguments The arguments associated with the {@link Exception}.
     * @since 1.0.0
     */
    public KapuaRuntimeException(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(cause);

        this.code = code;
        this.args = arguments;
    }

    /**
     * Factory method to build a {@link KapuaRuntimeException} with the {@link KapuaErrorCodes#INTERNAL_ERROR}.
     *
     * @param cause   The original {@link Throwable}.
     * @param message The message associated with the {@link Exception}.
     * @return A {@link KapuaRuntimeException} with {@link KapuaErrorCodes#INTERNAL_ERROR}.
     * @since 1.0.0
     */
    public static KapuaRuntimeException internalError(Throwable cause, String message) {
        return new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, cause, message);
    }

    /**
     * Factory method to build a {@link KapuaRuntimeException} with the {@link KapuaErrorCodes#INTERNAL_ERROR}.
     *
     * @param cause The original {@link Throwable}.
     * @return A {@link KapuaRuntimeException} with {@link KapuaErrorCodes#INTERNAL_ERROR}.
     * @since 1.0.0
     */
    public static KapuaRuntimeException internalError(Throwable cause) {
        String arg = cause.getMessage();
        if (arg == null) {
            arg = cause.getClass().getName();
        }
        return new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, cause, arg);
    }

    /**
     * Factory method to build a {@link KapuaRuntimeException} with the {@link KapuaErrorCodes#INTERNAL_ERROR}.
     *
     * @param message The message associated with the {@link Exception}.
     * @return A {@link KapuaRuntimeException} with {@link KapuaErrorCodes#INTERNAL_ERROR}.
     * @since 1.0.0
     */
    public static KapuaRuntimeException internalError(String message) {
        return new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, null, message);
    }

    //
    // Getters
    //
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }

    /**
     * Gets the {@link KapuaErrorCode}.
     *
     * @return The {@link KapuaErrorCode}.
     * @since 1.0.0
     */
    public KapuaErrorCode getCode() {
        return code;
    }

    /**
     * Gets the arguments.
     *
     * @return The arguments.
     * @since 1.0.0
     */
    private Object[] getArgs() {
        return args;
    }

    //
    // Exception messages.
    //

    /**
     * @since 1.0.0
     */
    @Override
    public String getMessage() {
        return ExceptionMessageUtils.getLocalizedMessage(getKapuaErrorMessagesBundle(), Locale.US, getCode(), getArgs());
    }

    /**
     * @since 1.0.0
     */
    @Override
    public String getLocalizedMessage() {
        return ExceptionMessageUtils.getLocalizedMessage(getKapuaErrorMessagesBundle(), Locale.getDefault(), getCode(), getArgs());
    }
}
