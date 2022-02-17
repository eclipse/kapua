/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.server.util;

import com.google.common.base.MoreObjects;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.ConsoleJAXBContextProvider;
import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.scheduler.quartz.SchedulerServiceInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @since 1.0.0
 */
public class ConsoleListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleListener.class);

    private static final SystemSetting SYSTEM_SETTING = SystemSetting.getInstance();

    private ServiceModuleBundle moduleBundle;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        try {
            LOG.info("Initialize Console JABContext Provider...");
            JAXBContextProvider consoleProvider = new ConsoleJAXBContextProvider();
            XmlUtil.setContextProvider(consoleProvider);
            LOG.info("Initialize Console JABContext Provider... DONE!");
        } catch (Exception e) {
            LOG.error("Initialize Console JABContext Provider... ERROR! Error: {}", e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }

        if (SYSTEM_SETTING.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            try {
                String dbUsername = SYSTEM_SETTING.getString(SystemSettingKey.DB_USERNAME);
                String dbPassword = SYSTEM_SETTING.getString(SystemSettingKey.DB_PASSWORD);
                String schema = MoreObjects.firstNonNull(
                        SYSTEM_SETTING.getString(SystemSettingKey.DB_SCHEMA_ENV),
                        SYSTEM_SETTING.getString(SystemSettingKey.DB_SCHEMA)
                );

                // Loading JDBC Driver
                String jdbcDriver = SYSTEM_SETTING.getString(SystemSettingKey.DB_JDBC_DRIVER);
                try {
                    Class.forName(jdbcDriver);
                } catch (ClassNotFoundException e) {
                    LOG.warn("Could not find jdbc driver: {}. Subsequent DB operation failures may occur...", SYSTEM_SETTING.getString(SystemSettingKey.DB_JDBC_DRIVER));
                }

                // Starting Liquibase Client
                new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, schema).update();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        // Start Quartz scheduler
        try {
            LOG.info("Starting Quartz scheduler...");
            SchedulerServiceInit.initialize();
            LOG.info("Starting Quartz scheduler... DONE!");
        } catch (Exception e) {
            LOG.error("Starting Quartz scheduler... ERROR! Error: {}", e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }

        // Start service modules
        try {
            LOG.info("Starting service modules...");
            if (moduleBundle == null) {
                moduleBundle = new ServiceModuleBundle();
            }
            moduleBundle.startup();
            LOG.info("Starting service modules... DONE!");
        } catch (Exception e) {
            LOG.error("Starting service modules... ERROR! Error: {}", e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
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

        // Stop Quartz scheduler
        try {
            LOG.info("Stopping Quartz scheduler...");
            SchedulerServiceInit.close();
            LOG.info("Stopping Quartz scheduler... DONE");
        } catch (Exception e) {
            LOG.info("Stopping Quartz scheduler... ERROR! Error: {}", e.getMessage(), e);
        }
    }

}
