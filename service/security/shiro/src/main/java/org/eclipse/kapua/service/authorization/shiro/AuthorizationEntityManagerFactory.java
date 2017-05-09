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
package org.eclipse.kapua.service.authorization.shiro;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.locator.KapuaProvider;

/**
 * Entity manager factory for the authorization module.
 *
 * @since 1.0
 *
 */
@KapuaProvider
public class AuthorizationEntityManagerFactory extends AbstractEntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "kapua-authorization";
    private static final String DATASOURCE_NAME = "kapua-dbpool";
    private static final Map<String, String> s_uniqueConstraints = new HashMap<>();

    private static AuthorizationEntityManagerFactory instance = new AuthorizationEntityManagerFactory();

    /**
     * Constructs a new entity manager factory and configure it to use the authorization persistence unit.
     */
    private AuthorizationEntityManagerFactory() {
        super(PERSISTENCE_UNIT_NAME,
                DATASOURCE_NAME,
                s_uniqueConstraints);
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
    public static AuthorizationEntityManagerFactory getInstance() {
        return instance;
    }

}
