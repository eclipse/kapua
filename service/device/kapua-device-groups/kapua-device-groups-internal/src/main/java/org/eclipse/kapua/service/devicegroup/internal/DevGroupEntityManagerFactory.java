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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.devicegroup.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DevGroupEntityManagerFactory extends AbstractEntityManagerFactory {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(DevGroupEntityManagerFactory.class);

    private static final String PERSISTENCE_UNIT_NAME = "kapua-devicegroup";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> uniqueConstraints = new HashMap<>();

    private static DevGroupEntityManagerFactory instance = new DevGroupEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the user
     * persistence unit.
     */
    private DevGroupEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME, DATASOURCE_NAME, uniqueConstraints);
    }

    /**
     * Return the {@link EntityManager} singleton instance
     * 
     * @return
     */
    public static EntityManagerFactory getInstance() {
        return instance;
    }
}
