/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_PASSWORD;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
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
    public static final String FULL_SCHEMA_PATH = "../dev-tools/src/main/database/";

    /**
     * Filter for deleting all new DB data except base data.
     */
    public static final String DELETE_FILTER = "all_delete.sql";

    private boolean setup;

    private Connection connection;

    public void setup() {
        if (this.setup) {
            return;
        }

        this.setup = true;

        logger.info("Setting up mock database");

        System.setProperty(DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(DB_USERNAME);
        String dbPassword = config.getString(DB_PASSWORD);
        // String schema = firstNonNull(config.getString(DB_SCHEMA_ENV), config.getString(DB_SCHEMA));

        String jdbcUrl = resolveJdbcUrl();

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

        new KapuaLiquibaseClient(jdbcUrl, dbUsername, dbPassword).update();

    }

    @After(order = HookPriorities.DATABASE)
    public void deleteAll() throws SQLException {

        try {
            if (setup) {
                KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DELETE_FILTER);
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
