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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaEntityRepository;
import org.eclipse.kapua.repository.KapuaNamedEntityRepository;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;

import java.util.function.Supplier;

public abstract class NamedEntityRepositoryJpaRepositoryBase<E extends KapuaNamedEntity, C extends E>
        extends UpdatableEntityJpaRepositoryBase<E, C>
        implements KapuaEntityRepository<E>,
        KapuaNamedEntityRepository<E>,
        KapuaUpdatableEntityRepository<E> {
    private final KapuaNamedEntityRepository<E> namedEntityRepository;

    public NamedEntityRepositoryJpaRepositoryBase(
            Class<C> concreteClass,
            Supplier<? extends KapuaListResult<E>> listProvider,
            EntityManagerFactory entityManagerFactory
    ) {
        super(concreteClass, listProvider, entityManagerFactory);
        this.namedEntityRepository = new KapuaNamedEntityRepositoryJpaImpl<E, C>(concreteClass, this.entityManagerSession);
    }

    @Override
    public E findByName(String value) {
        return this.namedEntityRepository.findByName(value);
    }

    @Override
    public E findByName(KapuaId scopeId, String value) {
        return this.namedEntityRepository.findByName(scopeId, value);
    }
}
