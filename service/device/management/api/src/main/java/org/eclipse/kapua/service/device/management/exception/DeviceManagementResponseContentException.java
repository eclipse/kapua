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
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * {@link DeviceManagementResponseException} to throw when the content of the {@link KapuaResponseMessage} cannot be parsed.
 *
 * @since 1.5.0
 */
public class DeviceManagementResponseContentException extends DeviceManagementResponseException {

    private static final long serialVersionUID = -5787008473862756065L;

    private final KapuaResponsePayload responsePayload;

    /**
     * Constructor.
     *
     * @param responsePayload the invalid {@link KapuaResponsePayload}.
     */
    public DeviceManagementResponseContentException(@Null KapuaResponsePayload responsePayload) {
        super(DeviceManagementErrorCodes.RESPONSE_CONTENT, responsePayload);

        this.responsePayload = responsePayload;
    }

    /**
     * Constructor.
     *
     * @param cause           the root cause of the {@link Exception}.
     * @param responsePayload the invalid {@link KapuaResponsePayload}.
     */
    public DeviceManagementResponseContentException(@NotNull Throwable cause, @Null KapuaResponsePayload responsePayload) {
        super(DeviceManagementErrorCodes.RESPONSE_CONTENT, cause, responsePayload);

        this.responsePayload = responsePayload;
    }

    /**
     * Gets the {@link KapuaResponsePayload}
     *
     * @return The {@link KapuaResponsePayload}
     */
    public KapuaResponsePayload getResponsePayload() {
        return responsePayload;
    }
}
