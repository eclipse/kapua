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

import org.eclipse.kapua.storage.TxGlobalPostActionConsumer;
import org.eclipse.kapua.storage.TxManager;
import org.eclipse.kapua.storage.TxManagerImpl;

import java.util.Set;

public class KapuaJpaTxManagerFactory {
    private final int maxInsertAttempts;
    private final Set<TxGlobalPostActionConsumer> txGlobalPostActionConsumers;

    public KapuaJpaTxManagerFactory(int maxInsertAttempts,
                                    Set<TxGlobalPostActionConsumer> txGlobalPostActionConsumers) {
        this.maxInsertAttempts = maxInsertAttempts;
        this.txGlobalPostActionConsumers = txGlobalPostActionConsumers;
    }

    public TxManager create(String persistenceUnitName) {
        return new TxManagerImpl(
                () -> new JpaTxContext(new KapuaEntityManagerFactory(persistenceUnitName)),
                maxInsertAttempts,
                txGlobalPostActionConsumers);
    }
}
