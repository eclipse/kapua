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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.model;


import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.jpa.CommonsEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommonServiceTest
{
    private static final Logger logger = LoggerFactory.getLogger(AbstractCommonServiceTest.class);
    
    public static String DEFAULT_PATH = "./src/main/sql/H2/";
    public static String DEFAULT_TEST_PATH = "./src/test/sql/H2/";

    public static String DEFAULT_COMMONS_PATH = "../commons";
    public static String DEFAULT_TEST_FILTER  = "test_*.sql";
    public static String DROP_TEST_FILTER     = "test_*_drop.sql";

    public static void scriptSession(String path, String fileFilter)
    {
        EntityManager em = null;
        try {
            
            logger.info("Running database scripts...");
            
            em = CommonsEntityManagerFactory.getInstance().createEntityManager();
            em.beginTransaction();
                        
            SimpleSqlScriptExecutor sqlScriptExecutor = new SimpleSqlScriptExecutor();
            sqlScriptExecutor.scanScripts(path, fileFilter);
            sqlScriptExecutor.executeUpdate(em);
            
            em.commit();
            
            logger.info("...database scripts done!");
        }
        catch (KapuaException e) {
            logger.error("Database scripts failed: {}", e.getMessage());
            if (em != null)
                em.rollback();
        }
        finally {
            if (em != null)
                em.close();
        }

    }
    
    @BeforeClass
    public static void tearUp()
        throws KapuaException
    {
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();
        scriptSession(DEFAULT_TEST_PATH, DEFAULT_TEST_FILTER);
    }
    
    @AfterClass
    public static void tearDown()
    {
        scriptSession(DEFAULT_TEST_PATH, DROP_TEST_FILTER);
    	KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
    }
}
