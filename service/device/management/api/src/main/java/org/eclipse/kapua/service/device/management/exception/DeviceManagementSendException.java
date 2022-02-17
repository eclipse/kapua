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
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

import javax.validation.constraints.NotNull;

/**
 * The {@link Exception} to throw when sending the {@link KapuaRequestMessage} causes an error.
 *
 * @since 1.1.0
 */
public class DeviceManagementSendException extends DeviceManagementException {

    private static final long serialVersionUID = 8314422738805026338L;

    private final KapuaRequestMessage<?, ?> requestMessage;

    /**
     * Constructor.
     *
     * @param requestMessage The {@link KapuaRequestMessage} that we tried to send.
     * @since 1.1.0
     */
    public DeviceManagementSendException(@NotNull KapuaRequestMessage<?, ?> requestMessage) {
        super(DeviceManagementErrorCodes.SEND_ERROR, requestMessage);

        this.requestMessage = requestMessage;
    }

    /**
     * Constructor.
     *
     * @param cause          the root cause of the {@link Exception}.
     * @param requestMessage The {@link KapuaRequestMessage} that we tried to send.
     * @since 1.1.0
     */
    public DeviceManagementSendException(@NotNull Throwable cause, @NotNull KapuaRequestMessage<?, ?> requestMessage) {
        super(DeviceManagementErrorCodes.SEND_ERROR, cause, requestMessage);
        this.requestMessage = requestMessage;
    }

    /**
     * Gets the {@link KapuaRequestMessage} that we tried to send.
     *
     * @return The {@link KapuaRequestMessage} that we tried to send.
     * @since 1.1.0
     */
    public KapuaRequestMessage<?, ?> getRequestMessage() {
        return requestMessage;
    }
}
