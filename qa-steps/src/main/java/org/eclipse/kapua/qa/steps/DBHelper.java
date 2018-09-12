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
package org.eclipse.kapua.qa.steps;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtilsWithResources;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
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

    /**
     * Path to root of full DB scripts.
     */
    public static final String FULL_SCHEMA_PATH = "database";

    /**
     * Filter for deleting all new DB data except base data.
     */
    public static final String DELETE_SCRIPT = "all_delete.sql";

    private boolean setup;

    private Connection connection;

    public void setup() {
        if (this.setup) {
            return;
        }

        this.setup = true;

        logger.info("Setting up mock database");

        System.setProperty(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
        String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));

        String jdbcUrl = JdbcConnectionUrlResolvers.resolveJdbcUrl();

        // Start external server to provide access to in mem H2 database
        if (Boolean.parseBoolean(System.getProperty("test.h2.server", "false")) || Boolean.parseBoolean(System.getenv("test.h2.server"))) {
            try {
                Server webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();
                Server server = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9092").start();
            } catch (SQLException e) {
                logger.warn("Error setting up H2 web server.");
            }
        }
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
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @After(order = HookPriorities.DATABASE)
    public void deleteAll() throws SQLException {

        try {
            if (setup) {
                KapuaConfigurableServiceSchemaUtilsWithResources.scriptSession(FULL_SCHEMA_PATH, DELETE_SCRIPT);
            }
        } finally {

            // close the connection

            if (connection != null) {
                connection.close();
                connection = null;
            }

        }

    }
}
