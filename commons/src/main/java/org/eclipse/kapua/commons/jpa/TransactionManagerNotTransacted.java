/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
