/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model.misc;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.commons.jpa.JpaTxContext;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.storage.TxContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;


public class CollisionEntityJpaRepository {

    private final int maxInsertAllowedRetry;

    public CollisionEntityJpaRepository(int maxInsertAllowedRetry) {
        this.maxInsertAllowedRetry = maxInsertAllowedRetry;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CollisionEntity create(TxContext txContext, CollisionEntity collisionEntity) {
        final EntityManager em = JpaTxContext.extractEntityManager(txContext);
        int retry = 0;
        boolean succeeded = false;
        CollisionEntity res = null;
        do {
            try {
                res = doCreate(em, collisionEntity);
                succeeded = true;
            } catch (KapuaEntityExistsException e) {
                em.detach(collisionEntity);
                if (++retry >= maxInsertAllowedRetry) {
                    throw e;
                }
                logger.warn("Entity already exists. Cannot insert the collisionEntity, try again!");
            }
        } while (!succeeded);
        return res;
    }

    protected CollisionEntity doCreate(javax.persistence.EntityManager em, CollisionEntity collisionEntity) {
        try {
            em.persist(collisionEntity);
            em.flush();
            em.refresh(collisionEntity);
            return collisionEntity;
        } catch (EntityExistsException e) {
            throw new KapuaEntityExistsException(e, collisionEntity.getId());
        } catch (PersistenceException e) {
            if (KapuaEntityJpaRepository.isInsertConstraintViolation(e)) {
                final KapuaEntity entityFound = em.find(collisionEntity.getClass(), collisionEntity.getId());
                if (entityFound == null) {
                    throw e;
                }
                throw new KapuaEntityExistsException(e, collisionEntity.getId());
            } else {
                throw e;
            }
        }
    }
}
