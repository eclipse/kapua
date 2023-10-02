/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.liquibase;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Call Liquibase database schema check and update (if enabled)
 */
public class DatabaseCheckUpdate {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCheckUpdate.class);

    public DatabaseCheckUpdate() {
        final SystemSetting systemSetting = SystemSetting.getInstance();
        logger.info("Kapua database schema check and update...");
        try {
            final boolean runLiquibase = systemSetting.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false);
            if (runLiquibase) {
                logger.info("Initialize Kapua Liquibase configuration...");
                final String dbUsername = systemSetting.getString(SystemSettingKey.DB_USERNAME);
                final String dbPassword = systemSetting.getString(SystemSettingKey.DB_PASSWORD);
                final String schema = MoreObjects.firstNonNull(
                        systemSetting.getString(SystemSettingKey.DB_SCHEMA_ENV),
                        systemSetting.getString(SystemSettingKey.DB_SCHEMA)
                );

                // Try initialize JDBC Driver
                final String jdbcDriverName = systemSetting.getString(SystemSettingKey.DB_JDBC_DRIVER);
                try {
                    Class.forName(jdbcDriverName);
                } catch (ClassNotFoundException e) {
                    logger.warn("Could not find/load JDBC driver: {}. Subsequent errors may be expected...", jdbcDriverName);
                }

                final String jdbcUrl = JdbcConnectionUrlResolvers.resolveJdbcUrl();
                logger.info("Initialize Kapua Liquibase configuration... DONE! ");

                final KapuaLiquibaseClient liquibaseClient = new KapuaLiquibaseClient(jdbcUrl, dbUsername, dbPassword, schema);
                liquibaseClient.update();
            } else {
                logger.warn("Skipping Kapua Liquibase Client as per configured property! {}=false", SystemSettingKey.DB_SCHEMA_UPDATE);
            }
        } catch (Exception e) {
            logger.error("Kapua database schema check and update... ERROR: {}", e.getMessage(), e);
            throw new SecurityException(e);
        }
    }

}
