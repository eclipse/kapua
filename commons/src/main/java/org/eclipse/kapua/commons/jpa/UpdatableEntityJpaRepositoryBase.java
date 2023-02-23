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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaEntityRepository;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;

import java.util.function.Supplier;

public abstract class UpdatableEntityJpaRepositoryBase<E extends KapuaUpdatableEntity, C extends E>
        extends EntityRepositoryJpaRepositoryBase<E, C>
        implements KapuaEntityRepository<E>,
        KapuaUpdatableEntityRepository<E> {
    private final KapuaUpdatableEntityRepository<E> kapuaUpdatableEntityRepository;

    public UpdatableEntityJpaRepositoryBase(
            Class<C> concreteClass,
            Supplier<? extends KapuaListResult<E>> listProvider,
            EntityManagerFactory entityManagerFactory) {
        super(concreteClass, listProvider, entityManagerFactory);
        this.kapuaUpdatableEntityRepository = new KapuaUpdateableEntityRepositoryJpaImpl<>(concreteClass, this.entityManagerSession);
    }

    @Override
    public E update(E tag) {
        return kapuaUpdatableEntityRepository.update(tag);
    }
}
