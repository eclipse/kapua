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

import java.io.Closeable;

/**
 * Represents the in-flight transaction, most likely handled by a {@link TxManager}
 */
public interface TxContext extends Closeable {
    /**
     * Commit the current transaction. In most cases, this should be invoked only by  the {@link TxManager}
     */
    void commit();

    /**
     * Rolls back the current transaction. In most cases, this should be invoked only by  the {@link TxManager}
     */
    void rollback();

    /**
     * Convert exceptions happening within the transaction in a more generic form
     */
    KapuaException convertPersistenceException(Exception e);

    boolean isRecoverableException(Exception ex);
}


