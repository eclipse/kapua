/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtilsWithResources;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.service.internal.cache.KapuaCacheManager;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.inject.Singleton;

/**
 * Singleton for managing database creation and deletion inside Gherkin scenarios.
 */
@Singleton
public class DBHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);

    /**
     * Path to root of full DB scripts.
     */
    private static final String FULL_SCHEMA_PATH = "sql";

    /**
     * Filter for deleting all new DB data except base data.
     */
    private static final String DELETE_SCRIPT = "all_delete.sql";

    private Connection connection;

    public void setup() {
        logger.warn("########################### Called DBHelper ###########################");
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

        new KapuaLiquibaseClient(jdbcUrl, dbUsername, dbPassword, schema).update();
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

    public void deleteAllAndClose() {
        try {
            deleteAll();
        } finally {
            // close the connection
            close();
        }
    }

    /**
     * Method that unconditionally deletes database.
     */
    public void deleteAll() {

        KapuaConfigurableServiceSchemaUtilsWithResources.scriptSession(FULL_SCHEMA_PATH, DELETE_SCRIPT);
        KapuaCacheManager.invalidateAll();
    }

    public void dropAll() throws SQLException {
        if (!connection.isClosed()) {
            String[] types = {"TABLE"};
            ResultSet sqlResults = connection.getMetaData().getTables(null, null, "%", types);

            while (sqlResults.next()) {
                String sqlStatement = String.format("DROP TABLE %s", sqlResults.getString("TABLE_NAME").toUpperCase());
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                    preparedStatement.execute();
                }
            }
            this.close();
        }
        KapuaCacheManager.invalidateAll();
    }

}
