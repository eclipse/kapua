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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.web;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @since 1.0.0
 */
public class RestApiListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(RestApiListener.class);

    //Injection not supported here, unfortunately
    private SystemSetting systemSetting = KapuaLocator.getInstance().getComponent(SystemSetting.class);
    private ServiceModuleBundle moduleBundle = KapuaLocator.getInstance().getComponent(ServiceModuleBundle.class);

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        LOG.warn("Initialized, systemSettings:{}, moduleBundle: {}", systemSetting, moduleBundle);

        if (systemSetting.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            try {
                String dbUsername = systemSetting.getString(SystemSettingKey.DB_USERNAME);
                String dbPassword = systemSetting.getString(SystemSettingKey.DB_PASSWORD);
                String schema = MoreObjects.firstNonNull(
                        systemSetting.getString(SystemSettingKey.DB_SCHEMA_ENV),
                        systemSetting.getString(SystemSettingKey.DB_SCHEMA)
                );

                // Loading JDBC Driver
                String jdbcDriver = systemSetting.getString(SystemSettingKey.DB_JDBC_DRIVER);
                try {
                    Class.forName(jdbcDriver);
                } catch (ClassNotFoundException e) {
                    LOG.warn("Could not find jdbc driver: {}. Subsequent DB operation failures may occur...", systemSetting.getString(SystemSettingKey.DB_JDBC_DRIVER));
                }

                // Starting Liquibase Client
                new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, schema).update();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        // Start service modules
        try {
            LOG.info("Starting service modules...");
            moduleBundle.startup();
            LOG.info("Starting service modules... DONE!");
        } catch (KapuaException e) {
            LOG.error("Starting service modules... ERROR! Error: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        // Stop event modules
        try {
            LOG.info("Stopping service modules...");
            if (moduleBundle != null) {
                moduleBundle.shutdown();
                moduleBundle = null;
            }
            LOG.info("Stopping service modules... DONE!");
        } catch (KapuaException e) {
            LOG.error("Stopping service modules... ERROR! Error: {}", e.getMessage(), e);
        }
    }

}
