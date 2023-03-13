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

public interface TxManager {

    <R> R executeWithResult(TxConsumer<R> transactionConsumer) throws KapuaException;

    void executeNoResult(TxResultlessConsumer transactionConsumer) throws KapuaException;

    @FunctionalInterface
    public interface TxConsumer<R> {
        R executeWithResult(TxContext txHolder) throws KapuaException;
    }

    @FunctionalInterface
    public interface TxResultlessConsumer {
        void executeWithoutResult(TxContext txHolder) throws KapuaException;
    }
}
