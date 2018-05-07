/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.StringUtils;

/**
 * Configurable JDBC connection URL resolver implementation. Can be configured using Kubernetes service discovery or
 * properties.
 *
 * @since 1.0
 */
public class DefaultConfigurableJdbcConnectionUrlResolver implements JdbcConnectionUrlResolver {

    private final SystemSetting config = SystemSetting.getInstance();

    @Override
    public String connectionUrl() {
        // Mandatory connection parameters
        String dbName = config.getString(SystemSettingKey.DB_NAME);
        String dbConnectionScheme = config.getString(SystemSettingKey.DB_CONNECTION_SCHEME);
        String dbConnectionHost = MoreObjects.firstNonNull(System.getenv("SQL_SERVICE_HOST"), config.getString(SystemSettingKey.DB_CONNECTION_HOST));
        String dbConnectionPort = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_CONNECTION_PORT), "3306");

        StringBuilder dbConnectionString = new StringBuilder().append(dbConnectionScheme)
                .append("://")
                .append(dbConnectionHost)
                .append(":")
                .append(dbConnectionPort)
                .append("/")
                .append(dbName)
                .append(";");

        // Optional connection parameters
        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
        if (StringUtils.isNotBlank(schema)) {
            dbConnectionString.append("schema=")
                    .append(schema)
                    .append(";");

            // This deletes the trailing '?' or '&'
            dbConnectionString.deleteCharAt(dbConnectionString.length() - 1);

        }
        return dbConnectionString.toString();
    }

}
