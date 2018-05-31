/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;

import com.google.common.base.MoreObjects;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Install {@link KapuaSecurityBrokerFilter} into activeMQ filter chain plugin.<BR>
 * <p>
 * Is called by activeMQ broker by configuring plugin tag inside broker tag into activemq.xml.<BR>
 * <BR>
 * <BR>
 * <p>
 * <pre>
 * &lt;plugins&gt;
 *     &lt;bean xmlns="http://www.springframework.org/schema/beans" id="kapuaFilter" class="org.eclipse.kapua.broker.core.KapuaSecurityBrokerFilter"/&gt;
 * &lt;/plugins&gt;
 * </pre>
 *
 * @since 1.0
 */
public class KapuaBrokerSecurityPlugin implements BrokerPlugin {

    private static final Logger logger = LoggerFactory.getLogger(KapuaBrokerSecurityPlugin.class);

    @Override
    public Broker installPlugin(final Broker broker) throws Exception {
        logger.info("Installing Kapua broker plugin.");

        SystemSetting config = SystemSetting.getInstance();
        if(config.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
            logger.debug("Starting Liquibase embedded client.");
            String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
            String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
            String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
            new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
        }

        try {
            // initialize shiro context for broker plugin from shiro ini file
            final URL shiroIniUrl = getClass().getResource("/shiro.ini");
            Ini shiroIni = new Ini();
            try (final InputStream input = shiroIniUrl.openStream()) {
                shiroIni.load(input);
            }

            SecurityManager securityManager = new IniSecurityManagerFactory(shiroIni).getInstance();
            SecurityUtils.setSecurityManager(securityManager);

            // install the filters
            return new KapuaSecurityBrokerFilter(broker);
        } catch (Throwable t) {
            logger.error("Error in plugin installation.", t);
            throw new SecurityException(t);
        }
    }

}
