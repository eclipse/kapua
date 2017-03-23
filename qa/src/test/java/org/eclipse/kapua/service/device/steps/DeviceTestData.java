/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import javax.inject.Singleton;

import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;

@Singleton
public class DeviceTestData {

    // Device registry related objects
    DeviceCreator deviceCreator;
    Device device;
    KapuaBirthMessage birthMessage;
    KapuaDisconnectMessage deathMessage;
    KapuaAppsMessage appMessage;
    KapuaMissingMessage missingMessage;

    // The registry ID of a device
    KapuaId deviceId;

    // Check if exception was fired in step.
    boolean exceptionCaught;

    // A list result for device query operations
    DeviceListResult deviceList;
    DeviceEventListResult eventList;

    DeviceEvent devEvent;

    // Item count
    long count;

    // String scratchpad
    String stringValue;

    // Data cleanup
    public void clearData() {
        deviceCreator = null;
        device = null;
        birthMessage = null;
        deathMessage = null;
        appMessage = null;
        missingMessage = null;
        deviceId = null;
        eventList = null;
        deviceList = null;
        devEvent = null;
        stringValue = "";
        count = 0;
    }
}
