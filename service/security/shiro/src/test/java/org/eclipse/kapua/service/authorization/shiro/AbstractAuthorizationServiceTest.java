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
package org.eclipse.kapua.service.authorization.shiro;

import java.util.Random;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAuthorizationServiceTest extends Assert {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthorizationServiceTest.class);
    protected static Random random = new Random();

    public static String DEFAULT_PATH = "../../../dev-tools/src/main/database";
    public static String DROP_ALL_TABLES = "all_drop.sql";

    // Drop the whole database. All tables are deleted.
    public static void dropDatabase() {
        scriptSession(DEFAULT_PATH, DROP_ALL_TABLES);
    }

    // Create the database tables and seed them with default data.
    public static void setupDatabase() {
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();
    }

    /**
     * Execute a specific SQL script
     *
     * @param path
     *            The path to the sql script. It can be either absolute or relative.
     * @param fileFilter
     *            The filename of the SQL script to be executed. Wildcards are allowed.
     */
    public static void scriptSession(String path, String fileFilter) {
        EntityManager em = null;
        try {

            logger.info("Running database scripts...");

            em = AuthenticationEntityManagerFactory.getEntityManager();
            em.beginTransaction();

            SimpleSqlScriptExecutor sqlScriptExecutor = new SimpleSqlScriptExecutor();
            sqlScriptExecutor.scanScripts(path, fileFilter);
            sqlScriptExecutor.executeUpdate(em);

            em.commit();

            logger.info("...database scripts done!");
        } catch (KapuaException e) {
            logger.error("Database scripts failed: {}", e.getMessage());
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
