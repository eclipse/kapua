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

import java.util.Arrays;

/**
 * {@link Exception} to {@code throw} when there is an error while translating a {@link Payload} body with a given encoding.
 *
 * @since 1.5.0
 */
public class InvalidBodyEncodingException extends InvalidBodyException {

    private static final long serialVersionUID = 5520533060394828471L;

    private final String encoding;
    private final byte[] body;

    /**
     * Constructor.
     *
     * @param cause The {@link Throwable} cause of the error.
     * @param body  The body {@link Payload} which translation has caused the error.
     * @since 1.5.0
     */
    public InvalidBodyEncodingException(Throwable cause, String encoding, byte[] body) {
        super(TranslatorErrorCodes.INVALID_BODY_ENCODING, cause, encoding, Arrays.toString(Arrays.copyOf(body, 64)));

        this.encoding = encoding;
        this.body = body;
    }

    /**
     * Gets the target encoding.
     *
     * @return The target encoding.
     * @since 1.5.0
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Gets the {@link Payload}'s body which cannot be translated
     *
     * @return The {@link Payload}'s body which cannot be translated
     */
    public byte[] getBody() {
        return body;
    }
}
