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

import org.eclipse.kapua.message.Channel;

/**
 * {@link Exception} to {@code throw} when there is an error while translating a {@link Channel}.
 *
 * @since 1.2.0
 */
public class InvalidChannelException extends TranslateException {

    private final Channel channel;

    /**
     * Constructor.
     *
     * @param cause   The {@link Throwable} cause of the error.
     * @param channel The {@link Channel} which translation has caused the error.
     * @since 1.2.0
     */
    public InvalidChannelException(Throwable cause, Channel channel) {
        super(TranslatorErrorCodes.INVALID_CHANNEL, cause, channel);

        this.channel = channel;
    }

    /**
     * The {@link Channel} which translation has caused the error.
     *
     * @return The {@link Channel} which translation has caused the error.
     * @since 1.2.0
     */
    public Channel getChannel() {
        return channel;
    }
}
