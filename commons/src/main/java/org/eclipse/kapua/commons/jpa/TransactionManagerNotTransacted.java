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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transactional {@link TransactionManager} implementation.
 *
 * @since 1.0
 */
public class TransactionManagerNotTransacted implements TransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(TransactionManagerNotTransacted.class);

    @Override
    public void beginTransaction(EntityManager manager) throws KapuaException {

    }

    @Override
    public void commit(EntityManager manager) throws KapuaException {
        if (manager.isTransactionActive()) {
            logger.error("A transaction is left active without commit or rollback! Please check the code!");
        }
    }

}
