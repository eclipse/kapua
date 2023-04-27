/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.storage;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

import java.util.Optional;

/**
 * This utility class is provided as syntactic sugar for classes that need to wrap around a {@link KapuaEntityRepository}, decorating it with additional functionalities.
 * This way your wrapper only needs to override significant methods, avoiding boilerplate clutter.
 *
 * @param <E> The specific subclass of {@link KapuaEntity} handled by this repository
 * @param <L> The specific subclass of {@link KapuaListResult}&lt;E&gt; meant to hold list results for the kapua entity handled by this repo
 * @since 2.0.0
 */
public abstract class KapuaEntityRepositoryNoopWrapper<E extends KapuaEntity, L extends KapuaListResult<E>> implements KapuaEntityRepository<E, L> {
    protected final KapuaEntityRepository<E, L> wrapped;

    public KapuaEntityRepositoryNoopWrapper(KapuaEntityRepository<E, L> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public E create(TxContext txContext, E entity) throws KapuaException {
        return wrapped.create(txContext, entity);
    }

    @Override
    public Optional<E> find(TxContext txContext, KapuaId scopeId, KapuaId entityId) {
        return wrapped.find(txContext, scopeId, entityId);
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
        return wrapped.delete(txContext, scopeId, entityId);
    }

    @Override
    public E delete(TxContext txContext, E entityToDelete) {
        return wrapped.delete(txContext, entityToDelete);
    }
}
