/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.CommonsEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.ResourceSqlScriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurable service database schema utilities with resources based
 * SQL scripts.
 *
 * @since 1.0
 */
public class KapuaConfigurableServiceSchemaUtilsWithResources {

    private static final Logger logger = LoggerFactory.getLogger(KapuaConfigurableServiceSchemaUtilsWithResources.class);

    private KapuaConfigurableServiceSchemaUtilsWithResources() {
    }

    /**
     * Executes the database script in the specified path matching the filename
     *
     * @param path path to resource in project resources
     * @param filename file name
     */
    public static void scriptSession(String path, String filename) {
        EntityManager em = null;
        try {

            logger.info("Running database script from resources...");

            em = CommonsEntityManagerFactory.getEntityManager();
            em.beginTransaction();

            ResourceSqlScriptExecutor sqlScriptExecutor = new ResourceSqlScriptExecutor();
            sqlScriptExecutor.addQuery(path + "/" + filename);
            sqlScriptExecutor.executeUpdate(em);

            em.commit();

            logger.info("...database scripts from resources done!");
        } catch (KapuaException e) {
            logger.error("Database scripts from resources failed: {}", e.getMessage());
            if (em != null) {
                em.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

}
