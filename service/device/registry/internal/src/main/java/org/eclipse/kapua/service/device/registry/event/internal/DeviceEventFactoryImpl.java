/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.registry.event.*;

import java.util.Date;

/**
 * Device event factory service implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceEventFactoryImpl implements DeviceEventFactory {

    @Override
    public DeviceEventCreator newCreator(KapuaId scopeId, KapuaId deviceId, Date receivedOn, String resource) {
        DeviceEventCreatorImpl creator = new DeviceEventCreatorImpl(scopeId);
        creator.setDeviceId(deviceId);
        creator.setAction(KapuaMethod.CREATE);
        creator.setReceivedOn(new Date(receivedOn.getTime()));
        creator.setResource(resource);
        return creator;
    }

    @Override
    public DeviceEventQuery newQuery(KapuaId scopeId) {
        return new DeviceEventQueryImpl(scopeId);
    }

    @Override
    public DeviceEvent newDeviceEvent() {
        return new DeviceEventImpl();
    }

    @Override public DeviceEventListResult newDeviceListResult() {
        return new DeviceEventListResultImpl();
    }
}
