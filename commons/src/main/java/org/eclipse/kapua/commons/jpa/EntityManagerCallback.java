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
 * Entity manager callback insert result service definition.
 *
 * @param <T> Insert execution result return type
 * @since 1.0
 */
public interface EntityManagerCallback<T> {

    /**
     * Return the insert execution result invoked using the provided entity manager.<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to this method.<br>
     * The caller method performs only a rollback (if the transaction is active and an error occurred)!<br>
     * @see EntityManagerSession#doAction(EntityManagerContainer)
     *
     * @param entityManager
     * @return
     * @throws KapuaException
     */
    T onAction(EntityManager entityManager) throws KapuaException;

}
