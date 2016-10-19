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
package org.eclipse.kapua.service.user.internal;


import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.config.metatype.*;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountXmlRegistry;
import org.eclipse.kapua.service.account.Organization;
import org.eclipse.kapua.test.KapuaTest;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserServiceTest extends KapuaTest
{
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(AbstractUserServiceTest.class);
    
    public static String DEFAULT_PATH = "./src/main/sql/H2/";

    public static String DEFAULT_COMMONS_PATH = "../commons";
    public static String DEFAULT_FILTER = "usr_*.sql";
    public static String DROP_FILTER = "usr_*_drop.sql";

    public static void scriptSession(String path, String fileFilter)
    {
        EntityManager em = null;
        try {
            
            logger.info("Running database scripts...");
            
            em = UserEntityManagerFactory.getInstance().createEntityManager();
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
    	KapuaConfigurableServiceSchemaUtils.createSchemaObjects(DEFAULT_COMMONS_PATH);
        scriptSession(DEFAULT_PATH, DEFAULT_FILTER);
        XmlUtil.setContextProvider(new UsersJAXBContextProvider());
    }
    
    @AfterClass
    public static void tearDown()
    {
        scriptSession(DEFAULT_PATH, DROP_FILTER);
    	KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
    }
}
