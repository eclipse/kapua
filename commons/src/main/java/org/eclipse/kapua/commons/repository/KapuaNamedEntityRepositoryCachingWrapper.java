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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaNamedEntityRepository;

public class KapuaNamedEntityRepositoryCachingWrapper<E extends KapuaNamedEntity, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepositoryCachingWrapper<E, L>
        implements KapuaNamedEntityRepository<E, L> {

    public KapuaNamedEntityRepositoryCachingWrapper(KapuaNamedEntityRepository<E, L> wrapped, NamedEntityCache entityCache) {
        super(wrapped, entityCache);
    }

    @Override
    public E findByName(String value) throws KapuaException {
        return doFindByName(KapuaId.ANY, value);
    }

    @Override
    public E findByName(KapuaId scopeId, String value) throws KapuaException {
        return doFindByName(scopeId, value);
    }

    private E doFindByName(KapuaId scopeId, String value) throws KapuaException {
        final NamedEntityCache namedEntityCache = (NamedEntityCache) entityCache;
        final KapuaEntity cached = namedEntityCache.get(scopeId, value);
        if (cached != null) {
            return (E) cached;
        }
        final E found = ((KapuaNamedEntityRepository<E, L>) wrapped).findByName(scopeId, value);
        return found;
    }
}
