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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.repository.KapuaEntityRepository;

import java.util.function.Supplier;

public abstract class EntityRepositoryJpaRepositoryBase<E extends KapuaUpdatableEntity, C extends E>
        implements KapuaEntityRepository<E> {
    protected final EntityManagerSession entityManagerSession;
    protected final KapuaEntityRepository<E> entityRepository;

    public EntityRepositoryJpaRepositoryBase(
            Class<C> concreteClass,
            Supplier<? extends KapuaListResult<E>> listProvider,
            EntityManagerFactory entityManagerFactory) {
        this.entityManagerSession = new EntityManagerSession(entityManagerFactory);
        this.entityRepository = new KapuaEntityRepositoryJpaImpl<E, C>(concreteClass, listProvider, entityManagerSession);
    }

    @Override
    public E create(E entity) {
        return entityRepository.create(entity);
    }

    @Override
    public E find(KapuaId scopeId, KapuaId entityId) {
        return entityRepository.find(scopeId, entityId);
    }

    @Override
    public KapuaListResult<E> query(KapuaQuery kapuaQuery) {
        return entityRepository.query(kapuaQuery);
    }

    @Override
    public long count(KapuaQuery kapuaQuery) {
        return entityRepository.count(kapuaQuery);
    }

    @Override
    public E delete(KapuaId scopeId, KapuaId entityId) {
        return entityRepository.delete(scopeId, entityId);
    }
}
