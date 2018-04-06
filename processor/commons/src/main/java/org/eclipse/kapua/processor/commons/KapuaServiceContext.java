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
package org.eclipse.kapua.processor.commons;

import java.util.Optional;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

public class KapuaServiceContext {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaServiceContext.class);

    private ServiceModuleBundle moduleBundle;
    private KapuaLiquibaseClient liquibaseClient;
    private JAXBContextProvider jaxbContextProvider;

    private KapuaServiceContext() {}

    public static KapuaServiceContext create(SystemSetting config, JAXBContextProvider provider) throws KapuaException {
        KapuaServiceContext ctx = new KapuaServiceContext();
        if (config.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            LOG.info("Initialize Liquibase embedded client.");
            try {
                // initialize driver
                Class.forName(config.getString(SystemSettingKey.DB_JDBC_DRIVER));
            } catch (ClassNotFoundException e) {
                throw KapuaException.internalError(e);
            }

            String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
            String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
            String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));

            LOG.debug("Starting Liquibase embedded client update - URL: {}, user/pass: {}/{}", JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword);

            ctx.liquibaseClient = new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema));
            ctx.liquibaseClient.update();
        }

        // Start service modules
        LOG.info("Starting service modules...");
        if (ctx.moduleBundle == null) {
            ctx.moduleBundle = new ServiceModuleBundle();
        }
        ctx.moduleBundle.startup();

        // Set JAXB Context
        ctx.jaxbContextProvider = provider;
        XmlUtil.setContextProvider(provider);

        LOG.info("Starting service modules...DONE");
        return ctx;
    }

    public void close() throws KapuaException {
        // shutdown event modules
        LOG.info("Stopping service modules...");
        if (moduleBundle != null) {
            moduleBundle.shutdown();
            moduleBundle = null;
        }
        liquibaseClient = null;
        jaxbContextProvider = null;
        LOG.info("Stopping service modules...DONE");
    }
}
