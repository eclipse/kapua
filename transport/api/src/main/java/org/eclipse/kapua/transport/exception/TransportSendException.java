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

import org.eclipse.kapua.transport.message.TransportMessage;

import javax.validation.constraints.NotNull;

/**
 * The {@link Exception} to throw when sending the {@link TransportMessage} causes an error.
 *
 * @since 1.1.0
 */
public class TransportSendException extends TransportException {

    private final TransportMessage requestMessage;

    /**
     * Constructor.
     *
     * @param requestMessage The {@link TransportMessage} that we tried to send.
     * @since 1.1.0
     */
    public TransportSendException(@NotNull TransportMessage requestMessage) {
        this(null, requestMessage);
    }

    /**
     * Constructor.
     *
     * @param cause          the root cause of the {@link Exception}.
     * @param requestMessage The {@link TransportMessage} that we tried to send.
     * @since 1.1.0
     */
    public TransportSendException(@NotNull Throwable cause, @NotNull TransportMessage requestMessage) {
        super(TransportErrorCodes.SEND_ERROR, cause, requestMessage);

        this.requestMessage = requestMessage;
    }

    /**
     * Gets the {@link TransportMessage} that we tried to send.
     *
     * @return The {@link TransportMessage} that we tried to send.
     * @since 1.1.0
     */
    public TransportMessage getRequestMessage() {
        return requestMessage;
    }
}
