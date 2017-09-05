/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.quartz.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.quartz.utils.ConnectionProvider;

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
