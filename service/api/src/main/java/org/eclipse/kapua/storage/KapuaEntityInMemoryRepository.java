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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class KapuaEntityInMemoryRepository<E extends KapuaEntity, L extends KapuaListResult<E>> implements KapuaEntityRepository<E, L> {
    protected final List<E> entities;
    protected final Class<E> clazz;
    protected final Supplier<L> listSupplier;

    public KapuaEntityInMemoryRepository(Class<E> clazz, Supplier<L> listSupplier) {
        this.clazz = clazz;
        this.listSupplier = listSupplier;
        this.entities = new ArrayList<>();
    }

    @Override
    public E create(TxContext txContext, E entity) throws KapuaException {
        this.entities.add(entity);
        return entity;
    }

    @Override
    public Optional<E> find(TxContext txContext, KapuaId scopeId, KapuaId entityId) {
        return doFind(scopeId, entityId);
    }

    protected Optional<E> doFind(KapuaId scopeId, KapuaId entityId) {
        return this.entities
                .stream()
                .filter(e -> scopeId == null || KapuaId.ANY.equals(scopeId) ? true : scopeId.equals(e.getScopeId()))
                .filter(e -> entityId == null ? true : entityId.equals(e.getId()))
                .findFirst();
    }

    @Override
    public L query(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        final L res = this.listSupplier.get();
        //TODO: filter according to the query above
        res.addItems(this.entities);
        return res;
    }

    @Override
    public long count(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        //TODO: filter according to the query above
        return this.entities.size();
    }

    @Override
    public E delete(TxContext txContext, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        final Optional<E> toDelete = doFind(scopeId, entityId);
        if (!toDelete.isPresent()) {
            throw new KapuaEntityNotFoundException(clazz.getSimpleName(), entityId);
        }
        toDelete.ifPresent(e -> this.entities.remove(e));
        return null;
    }

    @Override
    public E delete(TxContext txContext, E entityToDelete) {
        return null;
    }
}
