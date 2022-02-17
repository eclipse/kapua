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

import org.eclipse.kapua.KapuaErrorCode;

public enum DeviceManagementErrorCodes implements KapuaErrorCode {

    //
    // Request
    //
    /**
     * See {@link DeviceManagementRequestBadMethodException}.
     *
     * @since 1.0.0
     */
    REQUEST_BAD_METHOD,

    /**
     * See {@link DeviceManagementRequestContentException}.
     *
     * @since 1.5.0
     */
    REQUEST_CONTENT,

    //
    // Response
    //

    /**
     * See {@link DeviceManagementResponseContentException}.
     *
     * @since 1.5.0
     */
    RESPONSE_CONTENT,

    /**
     * See {@link DeviceManagementResponseBadRequestException}.
     *
     * @since 1.0.0
     */
    RESPONSE_BAD_REQUEST,

    /**
     * See {@link DeviceManagementResponseNotFoundException}.
     *
     * @since 1.0.0
     */
    RESPONSE_NOT_FOUND,

    /**
     * See {@link DeviceManagementResponseInternalErrorException}.
     *
     * @since 1.0.0
     */
    RESPONSE_INTERNAL_ERROR,

    /**
     * See {@link DeviceManagementResponseUnknownCodeException}.
     *
     * @since 1.5.0
     */
    RESPONSE_UNKNOWN_CODE,

    //
    // Misc
    //

    /**
     * See {@link DeviceNotConnectedException}.
     *
     * @since 1.0.0
     */
    DEVICE_NOT_CONNECTED,

    /**
     * See {@link DeviceManagementSendException}.
     *
     * @since 1.1.0
     */
    SEND_ERROR,

    /**
     * See {@link DeviceManagementTimeoutException}.
     *
     * @since 1.1.0
     */
    TIMEOUT

}
