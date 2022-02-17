/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import org.apache.commons.lang.StringUtils;

/**
 * MariaDB Jdbc url connection resolver implementation
 *
 * @since 1.0
 */
public class MariaDBJdbcConnectionUrlResolver implements JdbcConnectionUrlResolver {

    @Override
    public String connectionUrl() {
        SystemSetting config = SystemSetting.getInstance();

        // Mandatory connection parameters
        String dbName = config.getString(SystemSettingKey.DB_NAME);
        String dbConnectionScheme = config.getString(SystemSettingKey.DB_CONNECTION_SCHEME);
        String dbConnectionHost = config.getString(SystemSettingKey.DB_CONNECTION_HOST);
        String dbConnectionPort = config.getString(SystemSettingKey.DB_CONNECTION_PORT);
        boolean useSsl = config.getBoolean(SystemSettingKey.DB_CONNECTION_USE_SSL, Boolean.FALSE);
        String trustStore = config.getString(SystemSettingKey.DB_CONNECTION_TRUSTSTORE_URL);
        String trustStorePwd = config.getString(SystemSettingKey.DB_CONNECTION_TRUSTSTORE_PWD);
        String additionalOptions = config.getString(SystemSettingKey.DB_CONNECTION_ADDITIONAL_OPTIONS);

        StringBuilder dbConnectionString = new StringBuilder().append(dbConnectionScheme)
                .append("://")
                .append(dbConnectionHost)
                .append(":")
                .append(dbConnectionPort)
                .append("/")
                .append(dbName)
                .append("?");

        // Optional connection parameters
        String useTimezone = config.getString(SystemSettingKey.DB_USE_TIMEZONE);
        if (useTimezone != null) {
            dbConnectionString.append("useTimezone=")
                    .append(useTimezone)
                    .append("&");
        }

        String useLegacyDatetimeCode = config.getString(SystemSettingKey.DB_USE_LEGACY_DATETIME_CODE);
        if (useLegacyDatetimeCode != null) {
            dbConnectionString.append("useLegacyDatetimeCode=")
                    .append(useLegacyDatetimeCode)
                    .append("&");
        }

        String serverTimezone = config.getString(SystemSettingKey.DB_SERVER_TIMEZONE);
        if (serverTimezone != null) {
            dbConnectionString.append("serverTimezone=")
                    .append(serverTimezone)
                    .append("&");
        }

        String characterEncoding = config.getString(SystemSettingKey.DB_CHAR_ENCODING);
        if (characterEncoding != null) {
            dbConnectionString.append("characterEncoding=")
                    .append(characterEncoding)
                    .append("&");
        }

        dbConnectionString.append("useSSL=")
                .append(useSsl)
                .append("&");

        String enabledSslProtocolSuites = config.getString(SystemSettingKey.DB_CONNECTION_ENABLED_SSL_PROTOCOL_SUITES);
        if (enabledSslProtocolSuites != null) {
            dbConnectionString.append("enabledSslProtocolSuites=")
                    .append(enabledSslProtocolSuites)
                    .append("&");
        }

        if (StringUtils.isNotBlank(trustStore)) {
            dbConnectionString.append("trustStore=")
                    .append(trustStore)
                    .append("&");
        }

        if (StringUtils.isNotBlank(trustStorePwd)) {
            dbConnectionString.append("trustStorePassword=")
                    .append(trustStorePwd)
                    .append("&");
        }

        if (StringUtils.isNotBlank(additionalOptions)) {
            dbConnectionString.append(additionalOptions)
                    .append("&");
        }

        // Unmodifiable parameters
        dbConnectionString.append("allowMultiQueries=true");

        return dbConnectionString.toString();
    }

}
