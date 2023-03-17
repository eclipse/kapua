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

import org.eclipse.kapua.KapuaException;

/**
 * Entity manager factory definition
 *
 * @since 1.0
 * @deprecated since 2.0.0, use {@link org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory} instead
 */
@Deprecated
public interface EntityManagerFactory {

    /**
     * Creates an instance of {@link EntityManager}
     *
     * @return
     * @throws KapuaException
     */
    public EntityManager createEntityManager() throws KapuaException;

    /**
     * This method is provided as a compatibility step in the process of removing kapua's overlapping implementation of JPA's concepts.
     * Use it as a stepping stone to remove usages of this class entirely
     *
     * @return A {@link javax.persistence.EntityManagerFactory}
     */
    javax.persistence.EntityManagerFactory getJpaEntityManagerFactory();
}
