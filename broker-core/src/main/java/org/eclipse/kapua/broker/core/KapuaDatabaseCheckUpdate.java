/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core;

import java.util.Optional;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

/**
 * Call Liquibase database schema check and update (if enabled)
 *
 */
public class KapuaDatabaseCheckUpdate {

    private static final Logger logger = LoggerFactory.getLogger(KapuaDatabaseCheckUpdate.class);

    public KapuaDatabaseCheckUpdate() throws Exception {
        logger.info("Kapua database schema check and update...");
        try {
            SystemSetting config = SystemSetting.getInstance();
            if(config.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
                logger.debug("Starting Liquibase embedded client.");
                String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
                String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
                String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
                new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
                logger.info("Kapua database schema check and update... DONE");
            }
            else {
                logger.info("Kapua database schema check and update... skipping (not enabled by configuration) DONE");
            }
        } catch (Throwable t) {
            logger.error("Error in plugin installation.", t);
            throw new SecurityException(t);
        }
    }

}