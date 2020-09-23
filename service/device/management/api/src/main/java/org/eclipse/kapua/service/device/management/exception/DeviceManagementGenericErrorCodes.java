/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

/**
 * @since 1.1.0
 */
public enum DeviceManagementGenericErrorCodes implements DeviceManagementErrorCodes {

    /**
     * The device was never connected
     * The {@link org.eclipse.kapua.service.device.registry.Device} has {@code null} {@link org.eclipse.kapua.service.device.registry.connection.DeviceConnection}
     */
    DEVICE_NEVER_CONNECTED,

    /**
     * The {@link Device} is not {@link DeviceConnectionStatus#CONNECTED}
     */
    DEVICE_NOT_CONNECTED,

    /**
     * Request exception
     */
    REQUEST_EXCEPTION,

    /**
     * Bad request method
     */
    REQUEST_BAD_METHOD,

    /**
     * Response parse exception
     */
    RESPONSE_PARSE_EXCEPTION,

    /**
     * Bad request
     */
    RESPONSE_BAD_REQUEST,

    /**
     * Response not found
     */
    RESPONSE_NOT_FOUND,

    /**
     * Response internal error
     */
    RESPONSE_INTERNAL_ERROR,

    /**
     * An error occurred when sending the {@link org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage}.
     *
     * @since 1.1.0
     */
    SEND_ERROR,

    /**
     * A response as not been received within the given timeout.
     *
     * @since 1.1.0
     */
    TIMEOUT

}
