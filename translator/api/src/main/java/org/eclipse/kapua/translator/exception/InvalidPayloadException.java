/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.exception;

import org.eclipse.kapua.message.Payload;

/**
 * {@link Exception} to {@code throw} when there is an error while translating a {@link Payload}.
 *
 * @since 1.2.0
 */
public class InvalidPayloadException extends TranslateException {

    private final Payload payload;

    /**
     * Constructor.
     *
     * @param cause   The {@link Throwable} cause of the error.
     * @param payload The {@link Payload} which translation has caused the error.
     * @since 1.2.0
     */
    public InvalidPayloadException(Throwable cause, Payload payload) {
        super(TranslatorErrorCodes.INVALID_PAYLOAD, cause, payload);

        this.payload = payload;
    }

    /**
     * The {@link Payload} which translation has caused the error.
     *
     * @return The {@link Payload} which translation has caused the error.
     * @since 1.2.0
     */
    public Payload getPayload() {
        return payload;
    }
}
