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
package org.eclipse.kapua.service.device.call.exception;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

import javax.validation.constraints.NotNull;

/**
 * The {@link Exception} to throw when sending the {@link DeviceMessage} causes an error.
 *
 * @since 1.1.0
 */
public class DeviceCallSendException extends DeviceCallException {

    private final DeviceMessage requestMessage;

    /**
     * Constructor.
     *
     * @param requestMessage The {@link DeviceMessage} that we tried to send.
     * @since 1.1.0
     */
    public DeviceCallSendException(@NotNull DeviceMessage requestMessage) {
        super(DeviceCallErrorCodes.SEND_ERROR, requestMessage);
        this.requestMessage = requestMessage;
    }

    /**
     * Constructor.
     *
     * @param cause          the root cause of the {@link Exception}.
     * @param requestMessage The {@link DeviceMessage} that we tried to send.
     * @since 1.1.0
     */
    public DeviceCallSendException(@NotNull Throwable cause, @NotNull DeviceMessage requestMessage) {
        super(DeviceCallErrorCodes.SEND_ERROR, cause, requestMessage);
        this.requestMessage = requestMessage;
    }

    /**
     * Gets the {@link DeviceMessage} that we tried to send.
     *
     * @return The {@link DeviceMessage} that we tried to send.
     * @since 1.1.0
     */
    public DeviceMessage getRequestMessage() {
        return requestMessage;
    }
}
