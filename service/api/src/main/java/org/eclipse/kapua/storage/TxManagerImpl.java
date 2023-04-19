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
    public <R> R execute(TxConsumer<R> transactionConsumer, BiConsumer<TxContext, R>... justBeforeCommitAdditionalTxConsumers) throws KapuaException {
        int retry = 0;
        final TxContext txContext = txContextSupplier.get();
        try {
            while (true) {
                try {
                    final R res = transactionConsumer.execute(txContext);
                    Arrays.stream(justBeforeCommitAdditionalTxConsumers)
                            .forEach(additionalTxConsumer -> additionalTxConsumer.accept(txContext, res));
                    txContext.commit();
                    return res;
                } catch (Exception ex) {
                    txContext.rollback();
                    if (!txContext.isRecoverableException(ex)) {
                        throw txContext.convertPersistenceException(ex);
                    }
                    if (++retry >= maxInsertAttempts) {
                        logger.error("Recoverable exception, but retry attempts exceeded, failing", ex);
                        throw txContext.convertPersistenceException(ex);
                    }
                    logger.warn("Recoverable exception, retrying", ex);
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
