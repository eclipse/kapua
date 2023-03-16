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
import org.eclipse.kapua.storage.TxManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.BiConsumer;

public class JpaTxManager implements TxManager {

    private final EntityManagerFactory entityManagerFactory;

    public JpaTxManager(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public <R> R executeWithResult(TxConsumer<R> transactionConsumer, BiConsumer<TxContext, R>... additionalTxConsumers) throws KapuaException {
        final EntityManager em = entityManagerFactory.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            final JpaTxContext txHolder = new JpaTxContext(em);
            final R res = transactionConsumer.<R>executeWithResult(txHolder);
            for (final BiConsumer<TxContext, R> additionalTxConsumer : additionalTxConsumers) {
                additionalTxConsumer.accept(txHolder, res);
            }
            tx.commit();
            return res;
        } catch (Exception ex) {
            tx.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(ex);
        } finally {
            em.close();
        }
    }
}
