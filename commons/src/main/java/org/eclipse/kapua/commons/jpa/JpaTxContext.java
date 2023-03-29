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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.util.Optional;

public class JpaTxContext implements JpaAwareTxContext, TxContext {
    public final EntityManagerFactory entityManagerFactory;
    Optional<EntityManager> entityManager = Optional.empty();

    public JpaTxContext(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager() {
        if (!entityManager.isPresent()) {
            entityManager = Optional.of(entityManagerFactory.createEntityManager());
        }

        final EntityTransaction tx = entityManager.get().getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
        return entityManager.get();
    }

    @Override
    public void commit() {
        entityManager.ifPresent(e -> e.getTransaction().commit());
    }

    @Override
    public void rollback() {
        entityManager.ifPresent(entityManager -> {
            final EntityTransaction tx = entityManager.getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
        });
    }

    @Override
    public void close() throws IOException {
        entityManager.ifPresent(entityManager -> entityManager.close());
    }

    @Override
    public KapuaException convertPersistenceException(Exception ex) {
        return KapuaExceptionUtils.convertPersistenceException(ex);
    }
}
