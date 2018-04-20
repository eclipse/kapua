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
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;

/**
 * Device service factory implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceFactoryImpl implements DeviceFactory {

    @Override
    public DeviceCreator newCreator(KapuaId scopeId, String clientId) {
        DeviceCreator deviceCreator = newCreator(scopeId);
        deviceCreator.setClientId(clientId);
        return deviceCreator;
    }

    @Override
    public DeviceQuery newQuery(KapuaId scopeId) {
        return new DeviceQueryImpl(scopeId);
    }

    @Override
    public DeviceListResult newListResult() {
        return new DeviceListResultImpl();
    }

    @Override
    public Device newEntity(KapuaId scopeId) {
        return new DeviceImpl(scopeId);
    }

    @Override
    public DeviceCreator newCreator(KapuaId scopeId) {
        return new DeviceCreatorImpl(scopeId);
    }

}
