/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.storage.KapuaUpdatableEntityTransactedRepositoryCachingWrapper;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceTransactedRepository;

public class CachingDeviceTransactedRepository
        extends KapuaUpdatableEntityTransactedRepositoryCachingWrapper<Device, DeviceListResult>
        implements DeviceTransactedRepository {
    public CachingDeviceTransactedRepository(DeviceTransactedRepository wrapped, DeviceRegistryCache entityCache) {
        super(wrapped, entityCache);
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        final Device cached = (Device) ((DeviceRegistryCache) entityCache).getByClientId(scopeId, clientId);
        if (cached == null) {
            final Device found = ((DeviceTransactedRepository) wrapped).findByClientId(scopeId, clientId);
            if (found != null) {
                entityCache.put(found);
            }
            return null;
        }
        return cached;
    }
}
