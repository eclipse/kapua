/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import javax.validation.constraints.NotNull;

/**
 * The {@link Exception} to throw when the {@link Device} is not connected.
 *
 * @since 1.1.0
 */
public class DeviceNotConnectedException extends DeviceManagementException {

    private final KapuaId deviceId;
    private final DeviceConnectionStatus currentConnectionStatus;

    /**
     * Constructor.
     *
     * @param deviceId The {@link Device#getId()} which is not {@link DeviceConnectionStatus#CONNECTED}
     * @since 1.1.0
     */
    public DeviceNotConnectedException(@NotNull KapuaId deviceId) {
        this(deviceId, null);
    }

    /**
     * Constructor.
     *
     * @param deviceId                The {@link Device#getId()} which is not {@link DeviceConnectionStatus#CONNECTED}
     * @param currentConnectionStatus The current {@link DeviceConnectionStatus} of the {@link Device}
     * @since 1.1.0
     */
    public DeviceNotConnectedException(@NotNull KapuaId deviceId, @NotNull DeviceConnectionStatus currentConnectionStatus) {
        super(DeviceManagementErrorCodes.DEVICE_NOT_CONNECTED, deviceId, currentConnectionStatus);

        this.deviceId = deviceId;
        this.currentConnectionStatus = currentConnectionStatus;
    }

    /**
     * Gets the {@link Device#getId()} which is not {@link DeviceConnectionStatus#CONNECTED}
     *
     * @return The {@link Device#getId()} which is not {@link DeviceConnectionStatus#CONNECTED}
     * @since 1.1.0
     */
    public KapuaId getDeviceId() {
        return deviceId;
    }

    /**
     * Gets the current {@link DeviceConnectionStatus} of the {@link Device}
     *
     * @return The current {@link DeviceConnectionStatus} of the {@link Device}
     * @since 1.1.0
     */
    public DeviceConnectionStatus getCurrentConnectionStatus() {
        return currentConnectionStatus;
    }
}
