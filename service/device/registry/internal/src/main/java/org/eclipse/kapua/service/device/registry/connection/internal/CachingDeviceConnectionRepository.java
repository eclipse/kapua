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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionRepository;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCache;
import org.eclipse.kapua.storage.TxContext;

public class CachingDeviceConnectionRepository
        implements DeviceConnectionRepository {

    private final DeviceConnectionRepository wrapped;
    private final DeviceRegistryCache deviceRegistryCache;

    public CachingDeviceConnectionRepository(DeviceConnectionRepository wrapped, DeviceRegistryCache deviceRegistryCache) {
        this.wrapped = wrapped;
        this.deviceRegistryCache = deviceRegistryCache;
    }

    @Override
    public DeviceConnection update(TxContext tx, DeviceConnection deviceConnection) throws KapuaException {
        deviceRegistryCache.removeByDeviceConnectionId(deviceConnection.getScopeId(), deviceConnection.getId());
        return wrapped.update(tx, deviceConnection);
    }

    @Override
    public DeviceConnection create(TxContext txContext, DeviceConnection entity) throws KapuaException {
        return wrapped.create(txContext, entity);
    }

    @Override
    public DeviceConnection find(TxContext txContext, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        return wrapped.find(txContext, scopeId, entityId);
    }

    @Override
    public DeviceConnectionListResult query(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        return wrapped.query(txContext, kapuaQuery);
    }

    @Override
    public long count(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        return wrapped.count(txContext, kapuaQuery);
    }

    @Override
    public DeviceConnection delete(TxContext txContext, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        deviceRegistryCache.removeByDeviceConnectionId(scopeId, entityId);
        return wrapped.delete(txContext, scopeId, entityId);
    }

    @Override
    public DeviceConnection delete(TxContext txContext, DeviceConnection entityToDelete) {
        deviceRegistryCache.removeByDeviceConnectionId(entityToDelete.getScopeId(), entityToDelete.getId());
        return wrapped.delete(txContext, entityToDelete);
    }

    @Override
    public DeviceConnection update(TxContext txContext, DeviceConnection currentEntity, DeviceConnection updatedEntity) throws KapuaException {
        return wrapped.update(txContext, currentEntity, updatedEntity);
    }

    @Override
    public long countByClientId(TxContext tx, KapuaId scopeId, String clientId) throws KapuaException {
        return wrapped.countByClientId(tx, scopeId, clientId);
    }
}
