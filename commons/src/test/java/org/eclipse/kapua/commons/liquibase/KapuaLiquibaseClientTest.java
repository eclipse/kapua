/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.liquibase;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Category(JUnitTests.class)
public class KapuaLiquibaseClientTest {

    private static final String JDBC_URL = "jdbc:h2:mem:kapua;MODE=MySQL;";
    private static final String USERNAME = "kapua";
    private static final String PASSWORD = "kapua";

    private static final String TABLE_NAME = "TST_LIQUIBASE";

    private static final String QUERY_SHOW_TABLES = "SHOW TABLES";

    private Connection connection;

    @Before
    public void start() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        dropAllTables();
    }

    @After
    public void stop() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void shouldCreateTable() throws Exception {
        // Given
        System.setProperty("LIQUIBASE_ENABLED", "true");

        // When
        new KapuaLiquibaseClient(JDBC_URL, USERNAME, PASSWORD).update();

        // Then
        ResultSet sqlResults = connection.prepareStatement(QUERY_SHOW_TABLES).executeQuery();
        List<String> tables = new LinkedList<>();
        while (sqlResults.next()) {
            tables.add(sqlResults.getString(1));
        }
        Assertions.assertThat(tables).contains(TABLE_NAME);
    }

    @Test
    public void shouldCreateTableOnlyOnce() throws Exception {
        // Given
        System.setProperty("LIQUIBASE_ENABLED", "true");

        // When
        new KapuaLiquibaseClient(JDBC_URL, USERNAME, PASSWORD).update();
        new KapuaLiquibaseClient(JDBC_URL, USERNAME, PASSWORD).update();

        // Then
        ResultSet sqlResults = connection.prepareStatement(QUERY_SHOW_TABLES).executeQuery();
        List<String> tables = new LinkedList<>();
        while (sqlResults.next()) {
            tables.add(sqlResults.getString(1));
        }
        Assertions.assertThat(tables).contains(TABLE_NAME);
    }

    @Test
    public void shouldSkipDatabaseUpdate() throws Exception {
        // Given
        connection.prepareStatement("DROP TABLE IF EXISTS DATABASECHANGELOG").execute();
        System.setProperty("LIQUIBASE_ENABLED", "false");

        // When
        new KapuaLiquibaseClient(JDBC_URL, USERNAME, PASSWORD).update();

        // Then
        ResultSet sqlResults = connection.prepareStatement(QUERY_SHOW_TABLES).executeQuery();
        Assertions.assertThat(sqlResults.next()).isFalse();
    }

    @Test(expected = Exception.class)
    public void shouldCreateAttempToUseCustomSchema() throws Exception {
        // Given
        System.setProperty("LIQUIBASE_ENABLED", "true");

        // When
        try {
            new KapuaLiquibaseClient(JDBC_URL, USERNAME, PASSWORD, "foo").update();
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).hasMessageContaining("Schema \"FOO\" not found");
            throw e;
        }
    }

    // *******************
    // * Private Helpers *
    // *******************

    private void dropAllTables() throws SQLException {

        String[] types = {"TABLE"};
        ResultSet sqlResults = connection.getMetaData().getTables(null, null, "%", types);

        while (sqlResults.next()) {
            String sqlStatement = String.format("DROP TABLE IF EXISTS %s", sqlResults.getString("TABLE_NAME").toUpperCase());
            connection.prepareStatement(sqlStatement).execute();
        }
    }

}
