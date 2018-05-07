/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.server.util;

import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import java.util.Optional;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.ConsoleJAXBContextProvider;
import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.service.scheduler.quartz.SchedulerServiceInit;

import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleListener.class);

    private ServiceModuleBundle moduleBundle;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        logger.info("Initialize Console JABContext Provider");
        JAXBContextProvider consoleProvider = new ConsoleJAXBContextProvider();
        XmlUtil.setContextProvider(consoleProvider);

        SystemSetting config = SystemSetting.getInstance();
        if (config.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            logger.info("Initialize Liquibase embedded client.");
            String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
            String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
            String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));

            // initialize driver
            try {
                Class.forName(config.getString(SystemSettingKey.DB_JDBC_DRIVER));
            } catch (ClassNotFoundException e) {
                logger.warn("Could not find jdbc driver: {}", config.getString(SystemSettingKey.DB_JDBC_DRIVER));
            }

            logger.debug("Starting Liquibase embedded client update - URL: {}, user/pass: {}/{}", new Object[] { JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword });
            new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
        }

        // start quarz scheduler
        logger.info("Starting job scheduler...");
        try {
            SchedulerServiceInit.initialize();
        } catch (KapuaException e) {
            logger.error("Cannot start scheduler service: {}", e.getMessage(), e);
        }
        logger.info("Starting job scheduler... DONE");

        // Start service modules
        try {
            logger.info("Starting service modules...");
            if (moduleBundle == null) {
                moduleBundle = new ServiceModuleBundle();
            }
            moduleBundle.startup();
            logger.info("Starting service modules...DONE");
        } catch (KapuaException e) {
            logger.error("Cannot start service modules: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        // stop event modules
        try {
            logger.info("Stopping service modules...");
            if (moduleBundle != null) {
                moduleBundle.shutdown();;
                moduleBundle = null;
            }
            logger.info("Stopping service modules...DONE");
        } catch (KapuaException e) {
            logger.error("Cannot stop service modules: {}", e.getMessage(), e);
        }       

        // stop quarz scheduler
        logger.info("Stopping job scheduler...");
        SchedulerServiceInit.close();
        logger.info("Stopping job scheduler... DONE");
    }

}
