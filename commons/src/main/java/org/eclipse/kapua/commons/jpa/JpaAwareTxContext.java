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

public interface JpaAwareTxContext extends TxContext {
    static EntityManager extractEntityManager(TxContext txContext) {
        if (txContext instanceof JpaAwareTxContext) {
            return ((JpaAwareTxContext) txContext).getEntityManager();
        }
        throw new RuntimeException("This repo needs to run within the context of a JPA transaction");
    }

    EntityManager getEntityManager();

}
