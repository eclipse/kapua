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
 *     Red Hat
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;

/**
 * Not transactional {@link TransactionManager} implementation.
 *
 * @since 1.0
 */
public class TransactionManagerTransacted implements TransactionManager {

    @Override
    public void beginTransaction(EntityManager manager) throws KapuaException {
        manager.beginTransaction();
    }

    @Override
    public void commit(EntityManager manager) throws KapuaException {
        manager.commit();
    }

}
