/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.liquibaseUnlocker;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class.getName());

    private static final SystemSetting SYSTEM_SETTING = SystemSetting.getInstance();

    private Application() {
    }

    public static void main(String[] args) {
        LOG.info("Liquibase change log table unlocker tool... STARTING");
        try {
            {
                String dbUsername = SYSTEM_SETTING.getString(SystemSettingKey.DB_USERNAME);
                String dbPassword = SYSTEM_SETTING.getString(SystemSettingKey.DB_PASSWORD);
                String schema = MoreObjects.firstNonNull(
                        SYSTEM_SETTING.getString(SystemSettingKey.DB_SCHEMA_ENV),
                        SYSTEM_SETTING.getString(SystemSettingKey.DB_SCHEMA)
                );

                new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, schema).forceReleaseChangelogLock();
            }
        } catch (Exception e) {
            LOG.info("Liquibase change log table unlocker tool... ERROR!", e);
            return;
        }

        LOG.info("Liquibase change log table unlocker tool... DONE!");
    }
}