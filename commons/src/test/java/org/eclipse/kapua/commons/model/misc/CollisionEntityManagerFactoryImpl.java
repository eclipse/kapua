/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.locator.ComponentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentProvider(provides=CollisionEntityManagerFactory.class)
public class CollisionEntityManagerFactoryImpl extends AbstractEntityManagerFactory implements CollisionEntityManagerFactory {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(CollisionEntityManagerFactoryImpl.class);

    private static final String PERSISTENCE_UNIT_NAME = "kapua-commons-unit-test";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONTRAINTS = new HashMap<>();

//    private static CollisionEntityManagerFactory instance = new CollisionEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the user persistence unit.
     */
    public CollisionEntityManagerFactoryImpl() {
        super(PERSISTENCE_UNIT_NAME, 
              DATASOURCE_NAME, 
              UNIQUE_CONTRAINTS);
    }

    /**
     * Return the {@link EntityManager} singleton instance
     *
     * @return
     */
//    public static EntityManagerFactory getInstance() {
//        return instance;
//    }
}