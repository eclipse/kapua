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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;

/**
 * Entity manager callback result service definition.
 * 
 * @param <T> Execution result return type
 * 
 * @since 1.0
 * 
 */
public interface EntityManagerResultCallback<T> {

    /**
     * Return the execution result invoked using the provided entity manager.<br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to this method.<br>
     * The caller method performs only a rollback (if the transaction is active and an error occurred)!<br>
     * (@see {@link EntityManagerSession#onEntityManagerResult}<br>
     * 
     * @param entityManager
     * @return
     * @throws KapuaException
     */
    T onResult(EntityManager entityManager) throws KapuaException;

}
