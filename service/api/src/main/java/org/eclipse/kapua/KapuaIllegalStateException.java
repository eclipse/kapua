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

/**
 * KapuaIllegalStateException is thrown when the user session is inconsistent.
 *
 * @since 1.0.0
 */
public class KapuaIllegalStateException extends KapuaRuntimeException {

    private static final long serialVersionUID = -912672615903975466L;

    private final String message;

    /**
     * Constructor.
     *
     * @param message The message describing the illegal state.
     * @since 1.0.0
     */
    public KapuaIllegalStateException(String message) {
        super(KapuaErrorCodes.ILLEGAL_STATE, message);

        this.message = message;
    }

    /**
     * Constructor
     *
     * @param cause   The original {@link Throwable} error that caused this exception.
     * @param message The message describing the illegal state.
     * @since 1.0.0
     */
    public KapuaIllegalStateException(Throwable cause, String message) {
        super(KapuaErrorCodes.ILLEGAL_STATE, cause, message);

        this.message = message;
    }

    /**
     * Gets the message describing the illegal state.
     *
     * @return The message describing the illegal state.
     * @since 2.0.0
     */
    @Override
    public String getMessage() {
        return message;
    }
}
