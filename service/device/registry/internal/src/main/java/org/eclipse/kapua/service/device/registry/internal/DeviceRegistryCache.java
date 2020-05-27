/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.service.internal.cache.KapuaCacheManager;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;

import javax.cache.Cache;
import java.io.Serializable;

/**
 * {@link DeviceRegistryServiceImpl} dedicated cache.
 * Extends the {@link EntityCache} by providing two further {@link Cache} objects,
 * called {@code deviceByClientIdCache} and {@code deviceByConnectionIdCache}.
 * The {@code deviceByClientIdCache} cache adopts the clientId as key and the entity id as value,
 * while the {@code deviceByConnectionIdCache} cache adopts the connectionId as key and the entity id as value.
 * In such a way the correspondence with {@link EntityCache#idCache} is preserved.
 */
public class DeviceRegistryCache extends EntityCache {

    protected Cache<Serializable, Serializable> deviceByClientIdCache;
    protected Cache<Serializable, Serializable> deviceByConnectionIdCache;

    public DeviceRegistryCache(String idCacheName, String deviceByClientIdCacheName, String deviceByConnectionIdCacheName) {
        super(idCacheName);
        deviceByClientIdCache = KapuaCacheManager.getCache(deviceByClientIdCacheName);
        deviceByConnectionIdCache = KapuaCacheManager.getCache(deviceByConnectionIdCacheName);
    }

    public KapuaEntity getByClientId(KapuaId scopeId, String clientId) {
        if (clientId != null) {
            KapuaId entityId = (KapuaId) deviceByClientIdCache.get(clientId);
            return get(scopeId, entityId);
        }
        return null;
    }

    public KapuaEntity getByDeviceConnectionId(KapuaId scopeId, KapuaId deviceConnectionId) {
        if (deviceConnectionId != null) {
            KapuaId entityId = (KapuaId) deviceByConnectionIdCache.get(deviceConnectionId);
            return get(scopeId, entityId);
        }
        return null;
    }

    @Override
    public void put(KapuaEntity entity) {
        if (entity != null) {
            idCache.put(entity.getId(), entity);
            deviceByClientIdCache.put(((Device) entity).getClientId(), entity.getId());
            if (((Device) entity).getConnectionId() != null) {
                deviceByConnectionIdCache.put(((Device) entity).getConnectionId(), entity.getId());
            }
        }
    }

    @Override
    public KapuaEntity remove(KapuaId scopeId, KapuaId kapuaId) {
        KapuaEntity kapuaEntity = super.remove(scopeId, kapuaId);
        if (kapuaEntity != null) {
            deviceByClientIdCache.remove(((Device) kapuaEntity).getClientId());
            if (((Device) kapuaEntity).getConnectionId() != null) {
                deviceByConnectionIdCache.remove(((Device) kapuaEntity).getConnectionId());
            }
        }
        return kapuaEntity;
    }

    public KapuaEntity removeByDeviceConnectionId(KapuaId scopeId, KapuaId deviceConnectionId) {
        KapuaEntity kapuaEntity = getByDeviceConnectionId(scopeId, deviceConnectionId);
        if (kapuaEntity != null) {
            return remove(kapuaEntity.getScopeId(), kapuaEntity.getId());
        }
        return null;
    }
}
