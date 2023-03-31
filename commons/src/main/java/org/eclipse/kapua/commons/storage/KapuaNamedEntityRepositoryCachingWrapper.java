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
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

public class KapuaNamedEntityRepositoryCachingWrapper<
        E extends KapuaNamedEntity,
        L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepositoryCachingWrapper<E, L>
        implements KapuaNamedEntityRepository<E, L> {

    protected final KapuaNamedEntityRepository<E, L> wrapped;
    protected final NamedEntityCache entityCache;

    public KapuaNamedEntityRepositoryCachingWrapper(KapuaNamedEntityRepository<E, L> wrapped, NamedEntityCache entityCache) {
        super(wrapped, entityCache);
        this.wrapped = wrapped;
        this.entityCache = entityCache;
    }

    @Override
    public Optional<E> findByName(TxContext txContext, String value) {
        return findByNameCached(txContext, KapuaId.ANY, value);
    }

    @Override
    public Optional<E> findByName(TxContext txContext, KapuaId scopeId, String value) {
        return findByNameCached(txContext, scopeId, value);
    }

    private Optional<E> findByNameCached(TxContext txContext, KapuaId scopeId, String name) {
        final Optional<E> cached = Optional.ofNullable(entityCache.get(scopeId, name)).map(ke -> (E) ke);
        if (cached.isPresent()) {
            return cached;
        }
        final Optional<E> found = wrapped.findByName(txContext, scopeId, name);
        found.ifPresent(entityCache::put);
        return found;
    }

    @Override
    public long countOtherEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, KapuaId excludedId, String name) throws KapuaException {
        return wrapped.countOtherEntitiesWithNameInScope(tx, scopeId, excludedId, name);
    }

    @Override
    public long countEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, String name) throws KapuaException {
        return wrapped.countEntitiesWithNameInScope(tx, scopeId, name);
    }

    @Override
    public long countEntitiesWithName(TxContext tx, String name) throws KapuaException {
        return wrapped.countEntitiesWithName(tx, name);
    }
}
