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
package org.eclipse.kapua.service.device.management.exception;

import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

public enum DeviceManagementConnectionErrorCodes implements DeviceManagementErrorCodes {

    /**
     * The device was never connected
     * The {@link org.eclipse.kapua.service.device.registry.Device} has {@code null} {@link org.eclipse.kapua.service.device.registry.connection.DeviceConnection}
     */
    DEVICE_NEVER_CONNECTED,

    /**
     * The {@link Device} is not {@link DeviceConnectionStatus#CONNECTED}
     */
    DEVICE_NOT_CONNECTED
}
