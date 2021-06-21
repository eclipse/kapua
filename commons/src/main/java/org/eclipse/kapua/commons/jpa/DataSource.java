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

public final class DataSource {

    private static HikariDataSource hikariDataSource;

    private DataSource() {}

    public static HikariDataSource getDataSource() {
        if (hikariDataSource == null) {
            SystemSetting config = SystemSetting.getInstance();

            hikariDataSource = new HikariDataSource();
            hikariDataSource.setDriverClassName(config.getString(SystemSettingKey.DB_JDBC_DRIVER));
            hikariDataSource.setJdbcUrl(JdbcConnectionUrlResolvers.resolveJdbcUrl());
            hikariDataSource.setUsername(config.getString(SystemSettingKey.DB_USERNAME));
            hikariDataSource.setPassword(config.getString(SystemSettingKey.DB_PASSWORD));

            hikariDataSource.setMaximumPoolSize(config.getInt(SystemSettingKey.DB_POOL_SIZE_MAX, 20));
            hikariDataSource.setMinimumIdle(config.getInt(SystemSettingKey.DB_POOL_SIZE_MIN, 1));
            hikariDataSource.setIdleTimeout(config.getInt(SystemSettingKey.DB_POOL_IDLE_TIMEOUT, 180000));
            hikariDataSource.setKeepaliveTime(config.getInt(SystemSettingKey.DB_POOL_KEEPALIVE_TIME, 30000));
            hikariDataSource.setConnectionTestQuery(config.getString(SystemSettingKey.DB_POOL_TEST_QUERY, "SELECT 1"));
        }
        return hikariDataSource;
    }
}
