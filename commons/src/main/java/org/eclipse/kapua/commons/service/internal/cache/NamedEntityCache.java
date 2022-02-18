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
package org.eclipse.kapua.commons.service.internal.cache;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;

import javax.cache.Cache;
import java.io.Serializable;

/**
 * Extends the {@link EntityCache} by providing a further {@link Cache} object, called {@code nameCache}.
 * The {@code nameCache} cache adopts the entity name as key and the entity id as value.
 * In such a way the correspondence with {@link EntityCache#idCache} is preserved.
 */
public class NamedEntityCache extends EntityCache {

    protected Cache<Serializable, Serializable> nameCache;

    public NamedEntityCache(String idCacheName, String nameCacheName) {
        super(idCacheName);
        nameCache = KapuaCacheManager.getCache(nameCacheName);
    }

    public KapuaEntity get(KapuaId scopeId, String name) {
        if (name != null) {
            try {
                KapuaId entityId = (KapuaId) nameCache.get(name);
                return get(scopeId, entityId);
            } catch (Exception e) {
                cacheErrorLogger("get", nameCache.getName(), name, e);
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
                return; // the 'put' on the nameCache is performed only if the 'put' on idCache has been successful (since the latter is needed by the former)
            }
            try {
                nameCache.put(((KapuaNamedEntity) entity).getName(), entity.getId());
            } catch (Exception e) {
                // if the insertion on nameCache is failing, but the one on the idCache was successful, the situation is still ok:
                // a 'get' from the idCache will work anyway, while a 'get' from the nameCache will not work, forcing again a 'put' in the next operation
                cacheErrorLogger("put", nameCache.getName(), ((KapuaNamedEntity) entity).getName(), e);
            }
        }
    }

    @Override
    public KapuaEntity remove(KapuaId scopeId, KapuaId kapuaId) {
        KapuaEntity kapuaEntity = super.remove(scopeId, kapuaId);
        if (kapuaEntity != null) {
            try {
                nameCache.remove(((KapuaNamedEntity) kapuaEntity).getName());
            } catch (Exception e) {
                cacheErrorLogger("remove", nameCache.getName(), ((KapuaNamedEntity) kapuaEntity).getName(), e);
            }
        }
        return kapuaEntity;
    }
}
