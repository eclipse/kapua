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

/**
 * Defines the transaction manager behavior used by the {@link EntityManagerSession}
 *
 * @since 1.0
 */
public interface TransactionManager {

    /**
     * Perform the commit call
     *
     * @param manager
     * @throws KapuaException
     */
    public void commit(EntityManager manager) throws KapuaException;

    /**
     * Create the transaction
     *
     * @param manager
     * @throws KapuaException
     */
    public void beginTransaction(EntityManager manager) throws KapuaException;

}
