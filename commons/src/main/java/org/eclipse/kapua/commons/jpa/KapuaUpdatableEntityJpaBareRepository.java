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
import org.eclipse.kapua.storage.KapuaUpdatableEntityBareRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.function.Supplier;

public class KapuaUpdatableEntityJpaBareRepository<E extends KapuaUpdatableEntity, C extends E, L extends KapuaListResult<E>>
        extends KapuaEntityJpaBareRepository<E, C, L>
        implements KapuaUpdatableEntityBareRepository<E, L> {
    public KapuaUpdatableEntityJpaBareRepository(
            Class<C> concreteClass,
            Supplier<L> listSupplier) {
        super(concreteClass, listSupplier);
    }

    @Override
    public E update(TxContext txContext, E updatedEntity) throws KapuaException {
        final javax.persistence.EntityManager em = extractEntityManager(txContext);
        //
        // Checking existence
        E currentEntity = doFind(em, updatedEntity.getScopeId(), updatedEntity.getId());

        //
        // Updating if not null
        if (currentEntity == null) {
            throw new KapuaEntityNotFoundException(concreteClass.getSimpleName(), updatedEntity.getId());
        }

        return doUpdate(txContext, currentEntity, updatedEntity);
    }

    @Override
    public E doUpdate(TxContext txContext, E currentEntity, E updatedEntity) throws KapuaEntityNotFoundException {
        final javax.persistence.EntityManager em = extractEntityManager(txContext);

        AbstractKapuaUpdatableEntity updatableEntity = (AbstractKapuaUpdatableEntity) updatedEntity;
        updatableEntity.setCreatedOn(currentEntity.getCreatedOn());
        updatableEntity.setCreatedBy(currentEntity.getCreatedBy());

        em.merge(updatedEntity);
        em.flush();
        em.refresh(currentEntity);
        return currentEntity;
    }
}
