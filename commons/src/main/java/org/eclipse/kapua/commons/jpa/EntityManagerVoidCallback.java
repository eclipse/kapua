/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;

/**
 * Entity manager callback insert result service definition.
 *
 * @param <T> Insert execution result return type
 * @since 1.0
 */
public interface EntityManagerVoidCallback {

    /**
     * Return the insert execution result invoked using the provided entity manager.<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to this method.<br>
     * The caller method performs only a rollback (if the transaction is active and an error occurred)!<br>
     * @see EntityManagerSession#onInsert(EntityManagerInsertCallback)
     *
     * @param entityManager
     * @return
     * @throws KapuaException
     */
    void onAction(EntityManager entityManager) throws KapuaException;

}
