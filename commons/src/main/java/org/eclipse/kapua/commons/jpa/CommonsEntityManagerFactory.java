/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;

/**
 * Commons module entity manager reference service.
 *
 * @since 1.0
 */
public class CommonsEntityManagerFactory extends AbstractEntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-commons";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> s_uniqueConstraints = new HashMap<>();

    private static CommonsEntityManagerFactory instance = new CommonsEntityManagerFactory();

    /**
     * Constructor
     */
    private CommonsEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME,
                DATASOURCE_NAME,
                s_uniqueConstraints);
    }

    /**
     * Return the entity manager
     *
     * @return
     * @throws KapuaException
     */
    public static EntityManager getEntityManager()
            throws KapuaException {
        return instance.createEntityManager();
    }

    /**
     * Return the {@link CommonsEntityManagerFactory} instance (singleton)
     *
     * @return
     */
    public static CommonsEntityManagerFactory getInstance() {
        return instance;
    }
}
