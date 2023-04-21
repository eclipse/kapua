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

import java.util.function.BiConsumer;

/**
 * Transaction-managing interface. Use as a transaction boundary, to coordinate multiple operations within a single transaction.
 */
public interface TxManager {

    /**
     * @param transactionConsumer                   The actual set of operations to be executed within the transaction
     * @param justBeforeCommitAdditionalTxConsumers Consumers of the result of the main transaction, still executed within the tx boundary.
     *                                              Use this for event storing, auditing, etc.
     * @param <R>                                   The type of the value returned by the main transaction
     * @return the result returned by the <code>transactionConsumer</code> execution within the transaction
     * @throws KapuaException propagating exceptions thrown during tx execution
     */
    <R> R execute(TxConsumer<R> transactionConsumer,
                  BiConsumer<TxContext, R>... justBeforeCommitAdditionalTxConsumers)
            throws KapuaException;

    /**
     * Prefer using {@link #execute(TxConsumer, BiConsumer[])} whenever possible
     *
     * @return a {@link TxContext} for manual usage - provided only to support legacy implementation,  which spread the tx management across multiple methods.
     */
    TxContext getTxContext();

    /**
     * This interface is provided only in order to support checked exceptions ({@link KapuaException})
     * Should {@link KapuaException} become unchecked in the future, this can be replaced by  a simple {@link java.util.function.Function}
     *
     * @param <R> The type of the final value returned by the sequence of operations executed in the  context of the transaction
     */
    @FunctionalInterface
    public interface TxConsumer<R> {
        R execute(TxContext txContext) throws KapuaException;
    }
}
