/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_PASSWORD;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA_ENV;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_USERNAME;

import java.util.Optional;

import javax.inject.Singleton;

import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;

/**
 * Singleton for managing database creation and deletion inside Gherkin scenarios.
 */
@Singleton
public class DBHelper {

    /**
     * Path to root of full DB scripts.
     */
    public static final String FULL_SCHEMA_PATH = "../dev-tools/src/main/database/";

    /**
     * Filter for deleting all new DB data except base data.
     */
    public static final String DELETE_FILTER = "all_delete.sql";

    public DBHelper() {

        System.setProperty(DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(DB_USERNAME);
        String dbPassword = config.getString(DB_PASSWORD);
        String schema = firstNonNull(config.getString(DB_SCHEMA_ENV), config.getString(DB_SCHEMA));
        new KapuaLiquibaseClient(resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
    }

    public void deleteAll() {
        KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DELETE_FILTER);
    }
}
