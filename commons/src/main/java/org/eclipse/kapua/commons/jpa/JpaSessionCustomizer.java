/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;

public class JpaSessionCustomizer implements SessionCustomizer {

    private static JNDIConnector jndiConnector;

    @Override
    public void customize(Session session) throws Exception {
        DatabaseLogin databaseLogin = session.getLogin();
        if (jndiConnector == null) {
            SystemSetting config = SystemSetting.getInstance();

            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass(config.getString(SystemSettingKey.DB_JDBC_DRIVER)); //loads the jdbc driver
            comboPooledDataSource.setJdbcUrl(JdbcConnectionUrlResolvers.resolveJdbcUrl());
            comboPooledDataSource.setUser(config.getString(SystemSettingKey.DB_USERNAME));
            comboPooledDataSource.setPassword(config.getString(SystemSettingKey.DB_PASSWORD));

            // the settings below are optional -- c3p0 can work with defaults
            comboPooledDataSource.setMinPoolSize(config.getInt(SystemSettingKey.DB_POOL_SIZE_MIN, 2));
            comboPooledDataSource.setMaxPoolSize(config.getInt(SystemSettingKey.DB_POOL_SIZE_MAX, 30));
            comboPooledDataSource.setInitialPoolSize(config.getInt(SystemSettingKey.DB_POOL_SIZE_INITIAL, 5));

            comboPooledDataSource.setIdleConnectionTestPeriod(30);
            comboPooledDataSource.setPreferredTestQuery("SELECT 1");
            comboPooledDataSource.setTestConnectionOnCheckin(true);
            comboPooledDataSource.setTestConnectionOnCheckout(true);

            // The DataSource comboPooledDataSource is now a fully configured and usable pooled DataSource
            jndiConnector = new JNDIConnector(comboPooledDataSource);
        }
        databaseLogin.setConnector(jndiConnector);
    }

}
