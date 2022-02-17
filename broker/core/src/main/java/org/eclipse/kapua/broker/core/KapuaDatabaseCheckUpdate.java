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
package org.eclipse.kapua.broker.core;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Call Liquibase database schema check and update (if enabled)
 */
public class KapuaDatabaseCheckUpdate {

    private static final Logger logger = LoggerFactory.getLogger(KapuaDatabaseCheckUpdate.class);

    public KapuaDatabaseCheckUpdate() {
        logger.info("Kapua database schema check and update...");
        try {
            SystemSetting config = SystemSetting.getInstance();
            if (config.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
                logger.debug("Starting Liquibase embedded client.");
                String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
                String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
                String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));

                new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, schema).update();
                logger.info("Kapua database schema check and update... DONE");
            } else {
                logger.info("Kapua database schema check and update... skipping (not enabled by configuration) DONE");
            }
        } catch (Exception e) {
            logger.error("Error in plugin installation.", e);
            throw new SecurityException(e);
        }
    }

}
