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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;

/**
 * The {@link DeviceManagementException} to throw when the {@link Device} has no {@link DeviceConnection} assigned.
 */
public class DeviceNeverConnectedException extends DeviceNotConnectedException {

    private static final long serialVersionUID = -8225341071813065890L;

    public DeviceNeverConnectedException(KapuaId deviceId) {
        super(deviceId);
    }
}
