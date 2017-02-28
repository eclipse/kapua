package org.eclipse.kapua.test;
/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/


import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.*;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER;

public class KapuaTest extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaTest.class);

    private static boolean isInitialized;

    protected static Random random = new Random();
    protected static KapuaLocator locator = KapuaLocator.getInstance();

    protected static KapuaId adminUserId;
    protected static KapuaId adminScopeId;

    @Before
    public void setUp() {
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

        LOG.debug("Setting up test...");
        if (!isInitialized) {
            LOG.debug("Kapua test context is not initialized. Initializing...");
            try {
                //
                // Login
                String username = "kapua-sys";
                String password = "kapua-password";

                AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
                CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);
                authenticationService.login(credentialsFactory.newUsernamePasswordCredentials(username, password.toCharArray()));

                //
                // Get current user Id
                adminUserId = KapuaSecurityUtils.getSession().getUserId();
                adminScopeId = KapuaSecurityUtils.getSession().getScopeId();
            } catch (KapuaException exc) {
                exc.printStackTrace();
            }
            isInitialized = true;
        }
    }

    @AfterClass
    public static void tearDown() {
        LOG.debug("Stopping Kapua test context.");
        isInitialized = false;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);

            authenticationService.logout();
        } catch (KapuaException exc) {
            exc.printStackTrace();
        }
    }

    //
    // Test utility methods
    //

    /**
     * Generates a new random {@link String} of 10 chars with number and letters.
     *
     * @return the generated {@link String}
     */
    protected static String generateRandomString() {
        return generateRandomString(10, true, true);
    }

    /**
     * Generates a random {@link String} from the given parameters
     *
     * @param chars length of the generated {@link String}
     * @param letters whether or not use chars
     * @param numbers whether or not use numbers
     *
     * @return the generated {@link String}
     */
    protected static String generateRandomString(int chars, boolean letters, boolean numbers) {
        return RandomStringUtils.random(chars, 0, 0, letters, numbers, null, random);
    }

    protected static void enableH2Connection() {
        System.setProperty(DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
    }

    public static void scriptSession(AbstractEntityManagerFactory entityManagerFactory, String fileFilter) throws KapuaException {
        EntityManagerSession entityManagerSession = new EntityManagerSession(entityManagerFactory);
        entityManagerSession.onTransactedAction(entityManager -> new SimpleSqlScriptExecutor().scanScripts(fileFilter).executeUpdate(entityManager));
    }


}
