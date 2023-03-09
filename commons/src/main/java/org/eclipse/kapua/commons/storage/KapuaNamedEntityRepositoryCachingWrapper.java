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
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.TxContext;

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
    public E findByName(TxContext txContext, String value) throws KapuaException {
        return findByNameCached(txContext, KapuaId.ANY, value);
    }

    @Override
    public E findByName(TxContext txContext, KapuaId scopeId, String value) throws KapuaException {
        return findByNameCached(txContext, scopeId, value);
    }

    @FunctionalInterface
    public interface FetchEntity<E> {
        E findBy(KapuaId scopeId, String entityName) throws KapuaException;
    }

    private E findByNameCached(TxContext txContext, KapuaId scopeId, String name) throws KapuaException {
        final KapuaEntity cached = entityCache.get(scopeId, name);
        if (cached != null) {
            return (E) cached;
        }
        final E found = wrapped.findByName(txContext, scopeId, name);
        return found;
    }
}
