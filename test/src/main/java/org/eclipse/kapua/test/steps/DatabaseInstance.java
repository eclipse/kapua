/*******************************************************************************
 * Copyright (c) 2017, 2018 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.test.steps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.commons.setting.system.SystemSetting;

import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_PASSWORD;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_USERNAME;

@ScenarioScoped
public class DatabaseInstance {

    private String dbUsername;

    private String dbPassword;

    private String jdbcUrl;

    private Connection connection;

    public DatabaseInstance() throws SQLException {
        System.setProperty(DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
        SystemSetting config = SystemSetting.getInstance();
        dbUsername = config.getString(DB_USERNAME);
        dbPassword = config.getString(DB_PASSWORD);
        jdbcUrl = resolveJdbcUrl();

        // FIXME: find out why start/stop don't get called
        connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }

    @Before(order = 100)
    public void start() throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }

    @After
    public void stop() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
