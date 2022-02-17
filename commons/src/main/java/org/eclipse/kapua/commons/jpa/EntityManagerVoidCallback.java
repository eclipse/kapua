/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
