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

import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

/**
 * Utility interface mark JPA-compatible implementations of {@link TxContext},
 * providing a method to retrieve JPA's {@link EntityManager}
 */
public interface JpaAwareTxContext extends TxContext {
    /**
     * Convenience method that performs safe casting-and-execution of the provided context,
     * in order to obtain a {@link EntityManager} out of it.
     *
     * @param txContext The {@link TxContext} to extract from
     * @return the {@link EntityManager}, according to {{@link #getEntityManager()}}
     */
    static EntityManager extractEntityManager(TxContext txContext) {
        if (txContext instanceof JpaAwareTxContext) {
            return ((JpaAwareTxContext) txContext).getEntityManager();
        }
        throw new RuntimeException("This repo needs to run within the context of a JPA transaction");
    }

    /**
     * @return a ready-to-be-used {@link EntityManager}, with an open transaction.
     */
    javax.persistence.EntityManager getEntityManager();
}
