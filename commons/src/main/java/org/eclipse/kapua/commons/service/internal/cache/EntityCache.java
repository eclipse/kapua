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

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import java.io.Serializable;

/**
 * The basic cache class, it contains two {@link Cache} objects.
 * The {@code idCache} cache contains {@link KapuaEntity} objects, while the {@code listsCache} contains {@link KapuaListResult} objects.
 */
public class EntityCache {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EntityCache.class);

    // metrics naming variables
    private static final String MODULE = "commons";
    private static final String COMPONENT = "cache";
    private static final String ENTITY = "entity";
    private static final String COUNT = "count";

    protected Cache<Serializable, Serializable> idCache;
    protected Cache<Serializable, Serializable> listsCache;  // listsCache does not use the same keys as idCache
    protected Counter cacheMiss;
    protected Counter cacheHit;
    protected Counter cacheRemoval;
    protected Counter cacheError;

    /**
     * The constructor initializes the {@link #idCache} and the {@link #listsCache}.
     * It also initializes metrics counters for analysis ({@link #cacheMiss}, {@link #cacheHit} and {@link #cacheRemoval} counters).
     *
     * @param idCacheName
     */
    public EntityCache(String idCacheName) {
        idCache = KapuaCacheManager.getCache(idCacheName);
        listsCache = KapuaCacheManager.getCache(idCacheName + "_list");
        cacheMiss = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "miss", COUNT);
        cacheHit = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "hit", COUNT);
        cacheRemoval = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "removal", COUNT);
        cacheError = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "error", COUNT);
    }

    public KapuaEntity get(KapuaId scopeId, KapuaId kapuaId) {
        if (kapuaId != null) {
            KapuaEntity entity = null;
            try {
                entity = (KapuaEntity) idCache.get(kapuaId);
                entity = checkResult(scopeId, entity);
            } catch (Exception e) {
                cacheErrorLogger("get", idCache.getName(), kapuaId, e);
            }
            if (entity == null) {
                cacheMiss.inc();
            } else {
                cacheHit.inc();
            }
            return entity;
        }
        return null;
    }

    public KapuaListResult getList(KapuaId scopeId, Serializable id) {
        ComposedKey listKey = new ComposedKey(scopeId, id);
        if (id != null) {
            try {
                return checkResult(scopeId, (KapuaListResult) listsCache.get(listKey));
            } catch (Exception e) {
                cacheErrorLogger("getList", listsCache.getName(), listKey, e);
            }
        }
        return null;
    }

    public void put(KapuaEntity entity) {
        if (entity != null) {
            try {
                idCache.put(entity.getId(), entity);
            } catch (Exception e) {
                cacheErrorLogger("put", idCache.getName(), entity.getId(), e);
            }
        }
    }

    public void putList(KapuaId scopeId, Serializable id, KapuaListResult list) {
        ComposedKey listKey = new ComposedKey(scopeId, id);
        try {
            listsCache.put(listKey, list);
        } catch (Exception e) {
            cacheErrorLogger("putList", listsCache.getName(), listKey, e);
        }
    }

    public KapuaEntity remove(KapuaId scopeId, KapuaEntity entity) {
        return remove(scopeId, entity.getId());
    }

    public KapuaEntity remove(KapuaId scopeId, KapuaId kapuaId) {
        // First get the entity in order to perform a check of the scope id
        if (kapuaId != null) {
            KapuaEntity entity = get(scopeId, kapuaId);
            if (entity != null) {
                try {
                    idCache.remove(kapuaId);
                    cacheRemoval.inc();
                    return entity;
                } catch (Exception e) {
                    cacheErrorLogger("remove", idCache.getName(), kapuaId, e);
                }
            }
        }
        return null;
    }

    public KapuaListResult removeList(KapuaId scopeId, Serializable id) {
        // First get the entity in order to perform a check of the scope id
        if (id != null) {
            KapuaListResult entity = getList(scopeId, id);
            if (entity != null) {
                ComposedKey listKey = new ComposedKey(scopeId, id);
                try {
                    listsCache.remove(listKey);
                    return entity;
                } catch (Exception e) {
                    cacheErrorLogger("removeList", idCache.getName(), listKey, e);
                }
            }
        }
        return null;
    }

    /**
     * Checks that the scopeId of the entity matches the provided one.
     * This mimics the checks that are performed in the 'find' method of the {@link org.eclipse.kapua.commons.service.internal.ServiceDAO} class.
     *
     * @param scopeId a {@link KapuaId} representing the scopeId
     * @param entity the {@link KapuaEntity} to be checked
     * @return the provided entity if it has the required scopeId, null otherwise
     */
    protected KapuaEntity checkResult(KapuaId scopeId, KapuaEntity entity) {
        if (entity != null) {
            if (scopeId == null) {
                return entity;
            } else if (entity.getScopeId() == null) {
                return entity;
            } else if (entity.getScopeId().equals(scopeId)) {
                return entity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Checks that the scopeId of the entity matches the provided one.
     * This mimics the checks that are performed in the 'find' method of the {@link org.eclipse.kapua.commons.service.internal.ServiceDAO} class.
     *
     * @param scopeId a {@link KapuaId} representing the scopeId
     * @param entity the {@link KapuaListResult} entity to be checked
     * @return the provided entity if it has the required scopeId, null otherwise
     */
    protected KapuaListResult checkResult(KapuaId scopeId, KapuaListResult entity) {
        if (entity != null) {
            if (entity.getSize() == 0) {
                return entity;  // If the list is empty, I want to return the empty list
            } else if (scopeId == null) {
                return entity;
            } else if (entity.getFirstItem().getScopeId() == null) {
                return entity;
            } else if (entity.getFirstItem().getScopeId().equals(scopeId)) {
                return entity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Handles logging for cache exceptions.
     *
     * @param operation the name of the method/operation
     * @param cacheName the name of the cache in which the operation is performed
     * @param keyId     the Id of the entry's key in the cache
     * @param t         the exception
     */
    protected void cacheErrorLogger(String operation, String cacheName, Serializable keyId, Throwable t) {
        cacheError.inc();
        LOGGER.warn("Cache error while performing {} on {} for key {} : {}", operation, cacheName, keyId, t.getLocalizedMessage());
        LOGGER.debug("Cache exception", t);
    }
}
