/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.quartz.persistence;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.quartz.utils.ConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The {@link ConnectionProvider} for Quartz.
 * <p>
 * This bridges the {@link ConnectionProvider} over our {@link JdbcConnectionUrlResolvers}.
 *
 * @since 1.0.0
 */
public class KapuaQuartzConnectionProvider implements ConnectionProvider {

    private static final String JDBC_CONNECTION_URL = JdbcConnectionUrlResolvers.resolveJdbcUrl();

    private static final SystemSetting CONFIG = SystemSetting.getInstance();
    private static final String USERNAME = CONFIG.getString(SystemSettingKey.DB_USERNAME);
    private static final String PASSWORD = CONFIG.getString(SystemSettingKey.DB_PASSWORD);

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_CONNECTION_URL, USERNAME, PASSWORD);
    }

    @Override
    public void shutdown() throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void initialize() throws SQLException {
        // TODO Auto-generated method stub

    }

}
