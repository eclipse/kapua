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
import org.eclipse.kapua.commons.storage.KapuaUpdatableEntityRepositoryCachingWrapper;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

public class CachingDeviceRepository
        extends KapuaUpdatableEntityRepositoryCachingWrapper<Device, DeviceListResult>
        implements DeviceRepository {
    private final DeviceRepository wrapped;
    private final DeviceRegistryCache entityCache;

    public CachingDeviceRepository(DeviceRepository wrapped, DeviceRegistryCache entityCache) {
        super(wrapped, entityCache);
        this.wrapped = wrapped;
        this.entityCache = entityCache;
    }

    @Override
    public Optional<Device> findByClientId(TxContext tx, KapuaId scopeId, String clientId) throws KapuaException {
        final Optional<Device> cached = Optional.ofNullable((Device) entityCache.getByClientId(scopeId, clientId));
        if (cached.isPresent()) {
            return cached;
        }
        final Optional<Device> found = wrapped.findByClientId(tx, scopeId, clientId);
        found.ifPresent(entityCache::put);
        return found;
    }
}
