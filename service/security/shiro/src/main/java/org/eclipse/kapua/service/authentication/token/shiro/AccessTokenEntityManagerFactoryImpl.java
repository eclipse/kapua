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
package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.locator.ComponentProvider;

/**
 * Entity manager factory for the {@link AccessTokenServiceImpl} module.
 * 
 * @since 1.0.0
 */
@ComponentProvider(provides=AccessTokenEntityManagerFactory.class)
public class AccessTokenEntityManagerFactoryImpl extends AbstractEntityManagerFactory implements AccessTokenEntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-authentication";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> UNIQUE_CONSTRAINTS = new HashMap<>();

//    private static AccessTokenEntityManagerFactory instance = new AccessTokenEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the user persistence unit.
     */
    public AccessTokenEntityManagerFactoryImpl() {
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
