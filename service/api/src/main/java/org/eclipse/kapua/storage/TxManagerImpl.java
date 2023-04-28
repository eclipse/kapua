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
import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Default, generic implementation of {@link TxManager}
 */
public class TxManagerImpl implements TxManager {

    /**
     * Default (and only) constructor
     *
     * @param txContextSupplier       factory for {@link TxContext} instances
     * @param maxRetryAttemptsAllowed number of tx execution retries allowed in case of recoverable
     *                                errors (see {@link TxContext#isRecoverableException(Exception)})
     */
    public TxManagerImpl(Supplier<TxContext> txContextSupplier, Integer maxRetryAttemptsAllowed,
                         Set<TxGlobalPostActionConsumer> globalJustBeforeCommitAdditionalTxConsumers) {
        this.txContextSupplier = txContextSupplier;
        this.maxRetryAttemptsAllowed = maxRetryAttemptsAllowed;
        this.globalJustBeforeCommitAdditionalTxConsumers = globalJustBeforeCommitAdditionalTxConsumers;
    }


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Supplier<TxContext> txContextSupplier;
    private final int maxRetryAttemptsAllowed;
    private final Collection<TxGlobalPostActionConsumer> globalJustBeforeCommitAdditionalTxConsumers;

    @Override
    public <R extends Object> R execute(TxConsumer<R> transactionConsumer,
                                        BiConsumer<TxContext, R>... justBeforeCommitAdditionalTxConsumers)
            throws KapuaException {
        int retry = 0;
        final TxContext txContext = txContextSupplier.get();
        try {
            while (true) {
                try {
                    final R res = transactionConsumer.execute(txContext);
                    globalJustBeforeCommitAdditionalTxConsumers.stream()
                            .filter(c -> c.canConsume(res))
                            .forEach(additionalTxConsumer -> additionalTxConsumer.consume(txContext, res));
                    Arrays.stream(justBeforeCommitAdditionalTxConsumers).forEach(additionalTxConsumer -> additionalTxConsumer.accept(txContext, res));
                    txContext.commit();
                    return res;
                } catch (Exception ex) {
                    //In JPA, all exceptions (even caught ones) force the transaction in rollback-only mode
                    txContext.rollback();
                    final boolean canTryAgain = txContext.isRecoverableException(ex)
                            && ++retry <= maxRetryAttemptsAllowed;
                    if (canTryAgain) {
                        logger.warn("Recoverable exception, retrying", ex);
                        continue;
                    }
                    logger.error("Non-recoverable exception or retry attempts exceeded, failing", ex);
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
