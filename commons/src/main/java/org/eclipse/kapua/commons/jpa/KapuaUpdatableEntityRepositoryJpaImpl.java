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
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;

import java.util.function.Supplier;

public class KapuaUpdatableEntityRepositoryJpaImpl<E extends KapuaUpdatableEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaEntityRepositoryJpaImpl<E, C, L>
        implements KapuaUpdatableEntityRepository<E, L> {
    public KapuaUpdatableEntityRepositoryJpaImpl(
            Class<C> concreteClass,
            Supplier<L> listSupplier,
            EntityManagerSession entityManagerSession) {
        super(concreteClass, listSupplier, entityManagerSession);
    }

    @Override
    public E update(E entity) throws KapuaException {
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
    }
}
