/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.repository;

import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.repository.KapuaEntityRepository;

public class KapuaEntityRepositoryCachingWrapper<E extends KapuaEntity> implements KapuaEntityRepository<E> {
    protected final KapuaEntityRepository<E> wrapped;
    protected final EntityCache entityCache;

    public KapuaEntityRepositoryCachingWrapper(KapuaEntityRepository<E> wrapped, EntityCache entityCache) {
        this.wrapped = wrapped;
        this.entityCache = entityCache;
    }

    @Override
    public E create(E entity) {
        final E created = wrapped.create(entity);
        entityCache.put(created);
        return created;
    }

    @Override
    public E find(KapuaId scopeId, KapuaId entityId) {
        final KapuaEntity cached = entityCache.get(scopeId, entityId);
        if (cached != null) {
            return (E) cached;
        }
        final E found = wrapped.find(scopeId, entityId);
        entityCache.put(found);
        return found;
    }

    @Override
    public KapuaListResult<E> query(KapuaQuery kapuaQuery) {
        return wrapped.query(kapuaQuery);
    }

    @Override
    public long count(KapuaQuery kapuaQuery) {
        return wrapped.count(kapuaQuery);
    }

    @Override
    public E delete(KapuaId scopeId, KapuaId entityId) {
        final E deleted = wrapped.delete(scopeId, entityId);
        entityCache.remove(scopeId, entityId);
        return deleted;
    }
}
