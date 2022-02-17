/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.message.Message;

/**
 * {@link Exception} to {@code throw} when there is an error while translating a {@link Message}.
 *
 * @since 1.2.0
 */
public class InvalidMessageException extends TranslateException {

    private final Message<?, ?> msg;

    /**
     * Constructor.
     *
     * @param cause   The {@link Throwable} cause of the error.
     * @param message The {@link Message} which translation has caused the error.
     * @since 1.2.0
     */
    public InvalidMessageException(Throwable cause, Message<?, ?> message) {
        super(TranslatorErrorCodes.INVALID_MESSAGE, cause, message);

        this.msg = message;
    }

    /**
     * The {@link Message} which translation has caused the error.
     *
     * @return The {@link Message} which translation has caused the error.
     * @since 1.2.0
     */
    public Message<?, ?> getMsg() {
        return msg;
    }
}
