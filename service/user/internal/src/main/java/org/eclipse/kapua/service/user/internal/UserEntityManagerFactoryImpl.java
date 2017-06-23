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
package org.eclipse.kapua.service.user.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.locator.inject.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity manager factory for the user module.
 * 
 * @since 1.0
 *
 */
@ComponentProvider
@Service(provides=UserEntityManagerFactory.class)
public class UserEntityManagerFactoryImpl extends AbstractEntityManagerFactory implements UserEntityManagerFactory {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(UserEntityManagerFactoryImpl.class);

    private static final String PERSISTENCE_UNIT_NAME = "kapua-user";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONSTRAINTS = new HashMap<>();

//    private static UserEntityManagerFactoryImpl instance = new UserEntityManagerFactoryImpl();

    /**
     * Constructs a new entity manager factory and configure it to use the user persistence unit.
     */
    public UserEntityManagerFactoryImpl() {
        super(PERSISTENCE_UNIT_NAME, 
              DATASOURCE_NAME, 
              UNIQUE_CONSTRAINTS);
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
