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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;
import java.util.function.Supplier;

public class KapuaNamedEntityJpaRepository<E extends KapuaNamedEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityJpaRepository<E, C, L>
        implements KapuaNamedEntityRepository<E, L> {
    public KapuaNamedEntityJpaRepository(Class<C> concreteClass, Supplier<L> listSupplier) {
        super(concreteClass, listSupplier);
    }

    @Override
    public Optional<E> findByName(TxContext txContext, String name) {
        return doFindByField(txContext, KapuaId.ANY, KapuaNamedEntityAttributes.NAME, name);
    }

    @Override
    public Optional<E> findByName(TxContext txContext, KapuaId scopeId, String name) {
        return doFindByField(txContext, scopeId, KapuaNamedEntityAttributes.NAME, name);
    }
}
