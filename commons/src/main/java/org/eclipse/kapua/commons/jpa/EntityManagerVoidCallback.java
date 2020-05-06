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
 * Entity manager callback service definition.
 *
 * @since 1.0
 */
public interface EntityManagerVoidCallback {

    /**
     * Execute the action using the provided entityManager<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to this method.<br>
     * The caller method performs only a rollback (if the transaction is active and an error occurred)!<br>
     * @see EntityManager
     *
     * @param entityManager
     *
     * @throws KapuaException
     */
    void onAction(EntityManager entityManager) throws KapuaException;

}
