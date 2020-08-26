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

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

/**
 * @since 1.1.0
 */
public enum DeviceManagementErrorCodes implements KapuaErrorCode {

    /**
     * The {@link Device} is not {@link DeviceConnectionStatus#CONNECTED}
     */
    DEVICE_NOT_CONNECTED,

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
