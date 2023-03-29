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
package org.eclipse.kapua.storage;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class TxManagerImpl implements TxManager {

    private final Supplier<TxContext> txContextSupplier;
    private final int maxInsertAttempts;

    public TxManagerImpl(Supplier<TxContext> txContextSupplier, Integer maxInsertAttempts) {
        this.txContextSupplier = txContextSupplier;
        this.maxInsertAttempts = maxInsertAttempts;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public <R> R execute(TxConsumer<R> transactionConsumer, BiConsumer<TxContext, R>... additionalTxConsumers) throws KapuaException {
        int retry = 0;
        final TxContext txContext = txContextSupplier.get();
        try {
            while (true) {
                try {
                    final R res = transactionConsumer.execute(txContext);
                    Arrays.stream(additionalTxConsumers)
                            .forEach(additionalTxConsumer -> additionalTxConsumer.accept(txContext, res));
                    txContext.commit();
                    return res;
                } catch (KapuaEntityExistsException e) {
                    /*
                     * Most KapuaEntities inherit from AbstractKapuaEntity, which auto-generates ids via a method marked with @PrePersist and the use of
                     * a org.eclipse.kapua.commons.model.id.IdGenerator. Ids are pseudo-randomic. To deal with potential conflicts, a number of retries
                     * is allowed. The entity needs to be detached in order for the @PrePersist method to be invoked once more, generating a new id
                     * */
                    logger.warn("Conflict on entity creation. Cannot insert the entity, trying again!");
                    txContext.rollback();
                    if (++retry >= maxInsertAttempts) {
                        logger.error("Maximum number of attempts reached, aborting operation!");
                        throw txContext.convertPersistenceException(e);
                    }
                } catch (Exception ex) {
                    txContext.rollback();
                    throw txContext.convertPersistenceException(ex);
                }
            }
        } finally {
            try {
                txContext.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TxContext getTxContext() {
        return txContextSupplier.get();
    }
}
