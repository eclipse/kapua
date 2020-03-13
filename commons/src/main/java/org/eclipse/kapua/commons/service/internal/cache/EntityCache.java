/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

public class EntityCache {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EntityCache.class);

    private static final String MODULE = "commons";
    private static final String COMPONENT = "cache";
    private static final String ENTITY = "entity";
    private static final String COUNT = "count";

    protected Cache<Serializable, Serializable> idCache;
    protected Cache<Serializable, Serializable> listsCache;  // listsCache does not use the same keys as idCache
    protected Counter cacheMiss;
    protected Counter cacheHit;
    protected Counter cacheRemoval;

    public EntityCache(String idCacheName) {
        idCache = KapuaCacheManager.getCache(idCacheName);
        listsCache = KapuaCacheManager.getCache(idCacheName + "_list");
        cacheMiss = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "miss", COUNT);
        cacheHit = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "hit", COUNT);
        cacheRemoval = MetricServiceFactory.getInstance().getCounter(MODULE, COMPONENT, ENTITY, "removal", COUNT);
    }

    public KapuaEntity get(KapuaId scopeId, KapuaId kapuaId) {
        if (kapuaId != null) {
            KapuaEntity entity = (KapuaEntity) idCache.get(kapuaId);
            entity = checkResult(scopeId, entity);
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
        if (id != null) {
            return checkResult(scopeId, (KapuaListResult) listsCache.get(new ComposedKey(scopeId, id)));
        }
        return null;
    }

    public void put(KapuaEntity entity) {
        if (entity != null) {
            idCache.put(entity.getId(), entity);
        }
    }

    public void putList(KapuaId scopeId, Serializable id, KapuaListResult list) {
        listsCache.put(new ComposedKey(scopeId, id), list);
    }

    public KapuaEntity remove(KapuaId scopeId, KapuaEntity entity) {
        return remove(scopeId, entity.getId());
    }

    public KapuaEntity remove(KapuaId scopeId, KapuaId kapuaId) {
        // First get the entity in order to perform a check of the scope id
        if (kapuaId != null) {
            KapuaEntity entity = get(scopeId, kapuaId);
            if (entity != null) {
                idCache.remove(kapuaId);
                cacheRemoval.inc();
                return entity;
            }
        }
        return null;
    }

    public KapuaListResult removeList(KapuaId scopeId, Serializable id) {
        // First get the entity in order to perform a check of the scope id
        if (id != null) {
            KapuaListResult entity = getList(scopeId, id);
            if (entity != null) {
                listsCache.remove(new ComposedKey(scopeId, id));
                return entity;
            }
        }
        return null;
    }

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
}
