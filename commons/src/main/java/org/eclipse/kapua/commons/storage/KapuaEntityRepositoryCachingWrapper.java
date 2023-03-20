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
package org.eclipse.kapua.commons.storage;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.storage.KapuaEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

public class KapuaEntityRepositoryCachingWrapper<E extends KapuaEntity, L extends KapuaListResult<E>>
        implements KapuaEntityRepository<E, L> {
    protected final KapuaEntityRepository<E, L> wrapped;
    protected final EntityCache entityCache;

    public KapuaEntityRepositoryCachingWrapper(KapuaEntityRepository<E, L> wrapped, EntityCache entityCache) {
        this.wrapped = wrapped;
        this.entityCache = entityCache;
    }

    @Override
    public E create(TxContext txContext, E entity) throws KapuaException {
        return wrapped.create(txContext, entity);
    }

    @Override
    public Optional<E> find(TxContext txContext, KapuaId scopeId, KapuaId entityId) {
        final Optional<E> fromCache = Optional.ofNullable(entityCache.get(scopeId, entityId)).map(e -> (E) e);
        if (fromCache.isPresent()) {
            return fromCache;
        }
        final Optional<E> found = wrapped.find(txContext, scopeId, entityId);
        found.ifPresent(entityCache::put);
        return found;
    }

    @Override
    public L query(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        return wrapped.query(txContext, kapuaQuery);
    }

    @Override
    public long count(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        return wrapped.count(txContext, kapuaQuery);
    }

    @Override
    public E delete(TxContext txContext, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        final E deleted = wrapped.delete(txContext, scopeId, entityId);
        entityCache.remove(scopeId, entityId);
        return deleted;
    }

    @Override
    public E delete(TxContext txContext, E entityToDelete) {
        final E deleted = wrapped.delete(txContext, entityToDelete);
        entityCache.remove(entityToDelete.getScopeId(), entityToDelete.getId());
        return deleted;
    }
}
