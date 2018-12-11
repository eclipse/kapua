/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.CommonsEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtilsWithResources;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.After;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Singleton for managing database creation and deletion inside Gherkin scenarios.
 */
@ScenarioScoped
public class DBHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);

    private static final boolean NO_EMBEDDED_SERVERS = Boolean.getBoolean("org.eclipse.kapua.qa.noEmbeddedServers");

    /**
     * Path to root of full DB scripts.
     */
    private static final String FULL_SCHEMA_PATH = "database";
    private static final String DROP_ALL_TABLES = "sql/all_drop.sql";

    /**
     * Filter for deleting all new DB data except base data.
     */
    private static final String DELETE_SCRIPT = "all_delete.sql";

    private static boolean isSetup;

    /**
     * Web access to DB.
     */
    private static Server webServer;

    /**
     * TCP access to DB.
     */
    private static Server server;

    private Connection connection;

    public void setup() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        boolean h2TestServer = Boolean.parseBoolean(System.getProperty("test.h2.server", "false"))
                || Boolean.parseBoolean(System.getenv("test.h2.server"));

        if (isSetup) {
            return;
        }

        isSetup = true;

        if (h2TestServer) {
            // Start external server to provide access to in mem H2 database
            if ((webServer == null) && (server == null)) {
                if (h2TestServer) {
                    try {
                        webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();
                        server = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9092").start();
                        logger.info("H2 TCP and Web server started.");
                    } catch (SQLException e) {
                        logger.warn("Error setting up H2 web server.", e);
                    }
                }
            }
        }

        logger.info("Setting up embedded database");

        System.setProperty(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
        String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
        String jdbcUrl = JdbcConnectionUrlResolvers.resolveJdbcUrl();

        try {
            /*
             * Keep a connection open during the tests, as this may be an in-memory
             * database and closing the last connection might destroy the database
             * otherwise
             */
            this.connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        new KapuaLiquibaseClient(jdbcUrl, dbUsername, dbPassword, Optional.ofNullable(schema)).update();
    }

    public void close() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if ((server != null) && (server.isRunning(true))) {
            server.shutdown();
            server = null;
        }
        if ((webServer != null) && (webServer.isRunning(true))) {
            webServer.shutdown();
            webServer = null;
        }
        isSetup = false;
    }

    @After(order = HookPriorities.DATABASE)
    public void deleteAllAndClose() {

        try {
            if (isSetup) {
                KapuaConfigurableServiceSchemaUtilsWithResources.scriptSession(FULL_SCHEMA_PATH, DELETE_SCRIPT);
            }
        } finally {
            // close the connection
            this.close();
        }
    }

    /**
     * Method that unconditionally deletes database.
     */
    public void deleteAll() {

        KapuaConfigurableServiceSchemaUtilsWithResources.scriptSession(FULL_SCHEMA_PATH, DELETE_SCRIPT);
    }

    public void dropAll() throws KapuaException {
        this.resourceSession(CommonsEntityManagerFactory.getInstance(),DROP_ALL_TABLES);
        this.close();
    }

    public void resourceSession(AbstractEntityManagerFactory entityManagerFactory, String scriptName) throws KapuaException {
        EntityManagerSession entityManagerSession = new EntityManagerSession(entityManagerFactory);
        entityManagerSession.onTransactedAction(entityManager -> new SimpleSqlScriptExecutor().scanResources(scriptName).executeUpdate(entityManager));
    }
}
