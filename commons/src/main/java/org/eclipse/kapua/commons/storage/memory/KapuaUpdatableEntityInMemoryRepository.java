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
package org.eclipse.kapua.commons.storage.memory;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaUpdatableEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class KapuaUpdatableEntityInMemoryRepository<E extends KapuaUpdatableEntity, L extends KapuaListResult<E>>
        extends KapuaEntityInMemoryRepository<E, L>
        implements KapuaUpdatableEntityRepository<E, L> {

    public KapuaUpdatableEntityInMemoryRepository(Class<E> clazz, Supplier<L> listSupplier,
                                                  KapuaQueryConverter kapuaQueryConverter,
                                                  Map<String, Function<E, Object>> fieldPluckers) {
        super(clazz, listSupplier, kapuaQueryConverter, fieldPluckers);
    }

    @Override
    public E update(TxContext txContext, E entity) throws KapuaException {
        final Optional<E> current = doFind(entity.getScopeId(), entity.getId());
        current.ifPresent(e -> this.entities.remove(e));
        this.entities.add(entity);
        return entity;
    }

    @Override
    public E update(TxContext txContext, E currentEntity, E updatedEntity) {
        this.entities.remove(currentEntity);
        this.entities.add(updatedEntity);
        return updatedEntity;
    }
}
