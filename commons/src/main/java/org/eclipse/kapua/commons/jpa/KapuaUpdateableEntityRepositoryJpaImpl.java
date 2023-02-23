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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;

public class KapuaUpdateableEntityRepositoryJpaImpl<E extends KapuaUpdatableEntity> implements KapuaUpdatableEntityRepository<E> {
    private final Class<? extends E> concreteClass;
    private final EntityManagerSession entityManagerSession;

    public KapuaUpdateableEntityRepositoryJpaImpl(Class<? extends E> concreteClass, EntityManagerSession entityManagerSession) {
        this.concreteClass = concreteClass;
        this.entityManagerSession = entityManagerSession;
    }

    @Override
    public E update(E entity) {
        try {
            return entityManagerSession.doTransactedAction(em -> {

                //
                // Checking existence
                E entityToUpdate = em.find(concreteClass, entity.getId());

                //
                // Updating if not null
                if (entityToUpdate != null) {
                    AbstractKapuaUpdatableEntity updatableEntity = (AbstractKapuaUpdatableEntity) entity;
                    updatableEntity.setCreatedOn(entityToUpdate.getCreatedOn());
                    updatableEntity.setCreatedBy(entityToUpdate.getCreatedBy());

                    em.merge(entity);
                    em.flush();
                    em.refresh(entityToUpdate);
                } else {
                    throw new KapuaEntityNotFoundException(concreteClass.getSimpleName(), entity.getId());
                }

                return entityToUpdate;
            });
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
