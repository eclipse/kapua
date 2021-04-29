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

import com.zaxxer.hikari.HikariDataSource;
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

            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setDriverClassName(config.getString(SystemSettingKey.DB_JDBC_DRIVER)); //loads the jdbc driver
            hikariDataSource.setJdbcUrl(JdbcConnectionUrlResolvers.resolveJdbcUrl());
            hikariDataSource.setUsername(config.getString(SystemSettingKey.DB_USERNAME));
            hikariDataSource.setPassword(config.getString(SystemSettingKey.DB_PASSWORD));

            hikariDataSource.setMaximumPoolSize(config.getInt(SystemSettingKey.DB_POOL_SIZE_MAX, 30));
            hikariDataSource.setMinimumIdle(config.getInt(SystemSettingKey.DB_POOL_SIZE_MIN, 2));
            hikariDataSource.setKeepaliveTime(30000);
            hikariDataSource.setConnectionTestQuery("SELECT 1");

            jndiConnector = new JNDIConnector(hikariDataSource);
        }
        databaseLogin.setConnector(jndiConnector);
    }

}
