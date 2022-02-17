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
package org.eclipse.kapua.translator.exception;

import org.eclipse.kapua.message.Payload;

/**
 * {@link Exception} to {@code throw} when there is an error while translating a {@link Payload} body to a given type.
 *
 * @since 1.5.0
 */
public class InvalidBodyContentException extends InvalidBodyException {

    private static final long serialVersionUID = -4616416494412835320L;

    private final Class<?> type;
    private final String bodyString;

    /**
     * Constructor.
     *
     * @param cause The {@link Throwable} cause of the error.
     * @param type  The {@link Class} in which to serialize the given body.
     * @param body  The body {@link Payload} which translation has caused the error.
     * @since 1.5.0
     */
    public InvalidBodyContentException(Throwable cause, Class<?> type, byte[] body) {
        this(cause, type, new String(body));
    }

    /**
     * Constructor.
     *
     * @param cause      The {@link Throwable} cause of the error.
     * @param type       The {@link Class} in which to serialize the given body.
     * @param bodyString The {@link String} encoded body {@link Payload}which translation has caused the error.
     * @since 1.5.0
     */
    public InvalidBodyContentException(Throwable cause, Class<?> type, String bodyString) {
        super(TranslatorErrorCodes.INVALID_BODY_CONTENT, cause, type, bodyString);

        this.type = type;
        this.bodyString = bodyString;
    }

    /**
     * Gets the {@link Class} type.
     *
     * @return The {@link Class} type.
     * @since 1.5.0
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Gets the {@link Payload}'s body which cannot be translated.
     *
     * @return The {@link Payload}'s body which cannot be translated.
     * @since 1.5.0
     */
    public String getBodyString() {
        return bodyString;
    }
}
