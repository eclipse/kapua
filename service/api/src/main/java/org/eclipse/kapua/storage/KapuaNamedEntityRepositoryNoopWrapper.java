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
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;

import java.util.Optional;

/**
 * This utility class is provided as syntactic sugar for classes that need to wrap around a {@link KapuaNamedEntityRepository}, decorating it with additional functionalities.
 * This way your wrapper only needs to override significant methods, avoiding boilerplate clutter.
 *
 * @param <E> The specific subclass of {@link KapuaNamedEntity} handled by this repository
 * @param <L> The specific subclass of {@link KapuaListResult}&lt;E&gt; meant to hold list results for the kapua entity handled by this repo
 * @since 2.0.0
 */
public abstract class KapuaNamedEntityRepositoryNoopWrapper<E extends KapuaNamedEntity, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepositoryNoopWrapper<E, L>
        implements KapuaNamedEntityRepository<E, L> {

    protected final KapuaNamedEntityRepository<E, L> wrapped;

    public KapuaNamedEntityRepositoryNoopWrapper(KapuaNamedEntityRepository<E, L> wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public Optional<E> findByName(TxContext txContext, String value) {
        return wrapped.findByName(txContext, value);
    }

    @Override
    public Optional<E> findByName(TxContext txContext, KapuaId scopeId, String value) {
        return wrapped.findByName(txContext, scopeId, value);
    }

    @Override
    public long countEntitiesWithName(TxContext tx, String name) throws KapuaException {
        return wrapped.countEntitiesWithName(tx, name);
    }

    @Override
    public long countEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, String name) throws KapuaException {
        return wrapped.countEntitiesWithNameInScope(tx, scopeId, name);
    }

    @Override
    public long countOtherEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, KapuaId excludedId, String name) throws KapuaException {
        return wrapped.countOtherEntitiesWithNameInScope(tx, scopeId, excludedId, name);
    }
}
