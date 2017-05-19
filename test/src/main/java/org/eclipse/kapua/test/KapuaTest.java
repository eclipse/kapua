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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.test;

import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.KapuaContainer;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.guice.KapuaLocatorImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaTest extends Assert {

    private static final Logger logger = LoggerFactory.getLogger(KapuaTest.class);

    protected static Random random = new Random();
    protected static KapuaContainer container = new KapuaContainer() {};
    protected KapuaLocator locator;

    protected static KapuaId adminUserId;
    protected static KapuaId adminScopeId;

    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        container.startup();
        locator = KapuaLocatorImpl.getInstance();

        connection = DriverManager.getConnection("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua");

        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

        logger.debug("Setting up test...");
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
    }

    @After
    public void tearDown() {
        logger.debug("Stopping Kapua test context.");

        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            logger.warn("Failed to close database", e);
        }

        try {
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);

            authenticationService.logout();
        } catch (KapuaException exc) {
            exc.printStackTrace();
        }

        container.shutdown();
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
     * @param chars
     *            length of the generated {@link String}
     * @param letters
     *            whether or not use chars
     * @param numbers
     *            whether or not use numbers
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
