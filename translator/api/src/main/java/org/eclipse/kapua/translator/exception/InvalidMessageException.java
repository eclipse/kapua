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
