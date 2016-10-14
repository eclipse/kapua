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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

/**
 * Jdbc connection url resolver service reference implementation
 * 
 * @since 1.0
 *
 */
public class DefaultConfigurableJdbcConnectionUrlResolver implements JdbcConnectionUrlResolver {

    @Override
    public String connectionUrl() {
        SystemSetting config = SystemSetting.getInstance();

        // Mandatory connection parameters
        String dbName = config.getString(SystemSettingKey.DB_NAME);
        String dbConnectionScheme = config.getString(SystemSettingKey.DB_CONNECTION_SCHEME);
        String dbConnectionHost = config.getString(SystemSettingKey.DB_CONNECTION_HOST);
        String dbConnectionPort = config.getString(SystemSettingKey.DB_CONNECTION_PORT);

        StringBuilder dbConnectionString = new StringBuilder().append(dbConnectionScheme)
                .append("://")
                .append(dbConnectionHost)
                .append(":")
                .append(dbConnectionPort)
                .append("/")
                .append(dbName)
                .append(";");

        // Optional connection parameters
        String schema = config.getString(SystemSettingKey.DB_SCHEMA);
        if (schema != null) {
        	dbConnectionString.append("schema=")
        		.append(schema)
        		.append(";");

        // This deletes the trailing '?' or '&'
        dbConnectionString.deleteCharAt(dbConnectionString.length() - 1);

        }
        return dbConnectionString.toString();

    }
    
}
