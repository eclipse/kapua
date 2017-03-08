/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;

/**
 * Datastore entity manager factory implementation
 * 
 * @since 1.0
 *
 */
public class DatastoreEntityManagerFactory extends AbstractEntityManagerFactory implements EntityManagerFactory
{
    private static final String              PERSISTENCE_UNIT_NAME = "kapua-datastore";
    private static final String              DATASOURCE_NAME       = "kapua-dbpool";
    private static final Map<String, String> s_uniqueConstraints   = new HashMap<>();

    private static DatastoreEntityManagerFactory instance = new DatastoreEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the account persistence unit.
     */
    private DatastoreEntityManagerFactory()
    {
        super(PERSISTENCE_UNIT_NAME,
              DATASOURCE_NAME,
              s_uniqueConstraints);
    }

    /**
     * Return the {@link EntityManager} singleton instance
     * 
     * @return
     */
    public static DatastoreEntityManagerFactory getInstance()
    {
        return instance;
    }
}
