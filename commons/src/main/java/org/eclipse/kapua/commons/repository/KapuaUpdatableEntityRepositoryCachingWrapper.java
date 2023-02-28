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
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;

public class KapuaUpdatableEntityRepositoryCachingWrapper<E extends KapuaUpdatableEntity, L extends KapuaListResult<E>>
        extends KapuaEntityRepositoryCachingWrapper<E, L>
        implements KapuaUpdatableEntityRepository<E, L> {

    public KapuaUpdatableEntityRepositoryCachingWrapper(KapuaUpdatableEntityRepository<E, L> wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
    }

    @Override
    public E update(E entity) throws KapuaException {
        entityCache.remove(KapuaId.ANY, entity);
        final E updated = ((KapuaUpdatableEntityRepository<E, L>) wrapped).update(entity);
        return updated;
    }
}
