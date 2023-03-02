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

    private final KapuaNamedEntityRepository<E, L> wrappedNamed;

    public KapuaNamedEntityRepositoryCachingWrapper(KapuaNamedEntityRepository<E, L> wrapped, NamedEntityCache entityCache) {
        super(wrapped, entityCache);
        wrappedNamed = wrapped;
    }

    @Override
    public E findByName(TxContext txContext, String value) throws KapuaException {
        return findByNameCached(KapuaId.ANY, value, (scope, name) -> wrappedNamed.findByName(txContext, scope, name));
    }

    @Override
    public E findByName(TxContext txContext, KapuaId scopeId, String value) throws KapuaException {
        return findByNameCached(scopeId, value, (scope, name) -> wrappedNamed.findByName(txContext, scope, name));
    }

    @FunctionalInterface
    public interface FetchEntity<E> {
        E findBy(KapuaId scopeId, String entityName) throws KapuaException;
    }

    private E findByNameCached(KapuaId scopeId, String value, FetchEntity<E> actualSupplier) throws KapuaException {
        final NamedEntityCache namedEntityCache = (NamedEntityCache) entityCache;
        final KapuaEntity cached = namedEntityCache.get(scopeId, value);
        if (cached != null) {
            return (E) cached;
        }
        final E found = actualSupplier.findBy(scopeId, value);
        return found;
    }
}
