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
 *******************************************************************************/
package org.eclipse.kapua.test.device;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;

@KapuaProvider
public class DeviceRegistryServiceMock implements DeviceRegistryService {

    private final Map<KapuaId, DeviceMock> deviceRegistry;

    public DeviceRegistryServiceMock() {
        deviceRegistry = new HashMap<>();
    }

    @Override
    public Device create(DeviceCreator creator) throws KapuaException {
        DeviceMock device = new DeviceMock(creator.getScopeId(), creator.getClientId());
        deviceRegistry.put(device.getId(), device);
        return device;
    }

    @Override
    public Device find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        if (!deviceRegistry.containsKey(entityId)) {
            throw KapuaException.internalError("Device not found");
        }

        return deviceRegistry.get(entityId);
    }

    @Override
    public DeviceListResult query(KapuaQuery<Device> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<Device> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        if (!deviceRegistry.containsKey(entityId)) {
            throw KapuaException.internalError("Device not found");
        }

        deviceRegistry.remove(entityId);
    }

    @Override
    public Device update(Device entity) throws KapuaException {
        if (!deviceRegistry.containsKey(entity.getId())) {
            throw KapuaException.internalError("Device not found");
        }

        Device device = deviceRegistry.get(entity.getId());
        device.setDisplayName(entity.getDisplayName());
        return device;
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        Iterator<DeviceMock> devices = deviceRegistry.values().iterator();
        while (devices.hasNext()) {
            DeviceMock device = devices.next();
            if (device.getScopeId().equals(scopeId) &&
                    device.getClientId() != null && device.getClientId().equals(clientId)) {
                return device;
            }
        }
        throw KapuaException.internalError("Device not found");
    }

    @Override
    public KapuaTocd getConfigMetadata() throws KapuaException {
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return null;
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {

    }

    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        // TODO Auto-generated method stub
    }
}
