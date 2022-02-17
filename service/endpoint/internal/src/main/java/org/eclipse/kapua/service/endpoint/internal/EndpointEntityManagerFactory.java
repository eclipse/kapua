/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity manager factory for the endpointInfo module.
 *
 * @since 1.0.0
 */
public class EndpointEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-endpoint";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONSTRAINTS = new HashMap<>();

    private static EndpointEntityManagerFactory instance = new EndpointEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the endpointInfo persistence unit.
     */
    private EndpointEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME,
                DATASOURCE_NAME,
                UNIQUE_CONSTRAINTS);
    }

    /**
     * Return the {@link EntityManager} singleton instance
     *
     * @return
     * @throws KapuaException
     */
    public static EntityManager getEntityManager()
            throws KapuaException {
        return instance.createEntityManager();
    }

    /**
     * Return the {@link EntityManager} singleton instance
     *
     * @return
     */
    public static EndpointEntityManagerFactory getInstance() {
        return instance;
    }

}
