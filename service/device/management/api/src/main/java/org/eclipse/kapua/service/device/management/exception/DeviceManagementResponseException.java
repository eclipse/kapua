/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;

public class DeviceManagementResponseException extends DeviceManagementException {

    private static final long serialVersionUID = -5787008473862756065L;

    private final KapuaResponsePayload responsePayload;
    private final Object responsePayloadBody;

    /**
     * Constructor.
     */
    public DeviceManagementResponseException() {
        super(DeviceManagementResponseErrorCodes.RESPONSE_PARSE_EXCEPTION);
        this.responsePayload = null;
        this.responsePayloadBody = null;
    }

    /**
     * Constructor.
     *
     * @param cause the root cause of the {@link Exception}.
     */
    public DeviceManagementResponseException(@NotNull Throwable cause, @NotNull KapuaResponsePayload responsePayload) {
        super(DeviceManagementResponseErrorCodes.RESPONSE_PARSE_EXCEPTION, cause, responsePayload);
        this.responsePayload = responsePayload;
        this.responsePayloadBody = null;
    }

    /**
     * Constructor.
     *
     * @param cause the root cause of the {@link Exception}.
     */
    public DeviceManagementResponseException(@NotNull Throwable cause, @NotNull Object responsePayloadBody) {
        super(DeviceManagementResponseErrorCodes.RESPONSE_PARSE_EXCEPTION, cause, responsePayloadBody);
        this.responsePayload = null;
        this.responsePayloadBody = responsePayloadBody;
    }

    /**
     * Constructor for overriding exception classes
     *
     * @param code
     * @param responseCode
     * @param exceptionMessage
     * @param exceptionStack
     * @param arguments
     */
    public DeviceManagementResponseException(DeviceManagementErrorCodes code, KapuaResponseCode responseCode, String exceptionMessage, String exceptionStack,
                                             Object... arguments) {
        super(code, responseCode, exceptionMessage, exceptionStack, arguments);
        this.responsePayload = null;
        this.responsePayloadBody = null;
    }

    /**
     * Gets the {@link KapuaResponsePayload}
     *
     * @return The {@link KapuaResponsePayload}
     */
    public KapuaResponsePayload getResponsePayload() {
        return responsePayload;
    }

    /**
     * Gets the response payload body
     *
     * @return The response payload body
     * @since 1.1.0
     */
    public Object getResponsePayloadBody() {
        return responsePayloadBody;
    }

}
