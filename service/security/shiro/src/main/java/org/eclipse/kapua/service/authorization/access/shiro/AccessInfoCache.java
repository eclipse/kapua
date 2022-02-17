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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.service.internal.cache.KapuaCacheManager;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

import javax.cache.Cache;
import java.io.Serializable;

/**
 * {@link AccessInfoServiceImpl} dedicated cache.
 * Extends the {@link EntityCache} by providing a further {@link Cache} object, called {@code accessInfoByUserIdCache}.
 * The {@code accessInfoByUserIdCache} cache adopts the userId as key and the entity id as value.
 * In such a way the correspondence with {@link EntityCache#idCache} is preserved.
 */
public class AccessInfoCache extends EntityCache {

    protected Cache<Serializable, Serializable> accessInfoByUserIdCache;

    public AccessInfoCache(String idCacheName, String nameCacheName) {
        super(idCacheName);
        accessInfoByUserIdCache = KapuaCacheManager.getCache(nameCacheName);
    }

    public KapuaEntity getByUserId(KapuaId scopeId, KapuaId userId) {
        if (userId != null) {
            try {
                KapuaId entityId = (KapuaId) accessInfoByUserIdCache.get(userId);
                return get(scopeId, entityId);
            } catch (Exception e) {
                cacheErrorLogger("getByUserId", accessInfoByUserIdCache.getName(), userId, e);
            }
        }
        return null;
    }

    @Override
    public void put(KapuaEntity entity) {
        if (entity != null) {
            try {
                idCache.put(entity.getId(), entity);
            } catch (Exception e) {
                cacheErrorLogger("put", idCache.getName(), entity.getId(), e);
                return; // the 'put' on accessInfoByUserIdCache is performed only if the 'put' on idCache was successful (the latter is needed by the former)
            }
            try {
                accessInfoByUserIdCache.put(((AccessInfo) entity).getUserId(), entity.getId());
            } catch (Exception e) {
                // if the insertion on accessInfoByUserIdCache is failing, but the one on the idCache was successful, the situation is still ok:
                // a 'get' from the idCache will work anyway, while a 'get' from the accessInfoByUserIdCache will not work,
                // forcing again a 'put' in the next operation
                cacheErrorLogger("put", accessInfoByUserIdCache.getName(), ((AccessInfo) entity).getUserId(), e);
            }
        }
    }

    @Override
    public KapuaEntity remove(KapuaId scopeId, KapuaId kapuaId) {
        KapuaEntity kapuaEntity = super.remove(scopeId, kapuaId);
        if (kapuaEntity != null) {
            try {
                accessInfoByUserIdCache.remove(((AccessInfo) kapuaEntity).getUserId());
            } catch (Exception e) {
                cacheErrorLogger("remove", accessInfoByUserIdCache.getName(), ((AccessInfo) kapuaEntity).getUserId(), e);
            }
        }
        return kapuaEntity;
    }
}
