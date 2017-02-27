/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.broker.core;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_PASSWORD;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_USERNAME;

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

    private static Logger logger = LoggerFactory.getLogger(KapuaBrokerSecurityPlugin.class);

    public Broker installPlugin(Broker broker) throws Exception {
        logger.info(">> installPlugin {}", KapuaBrokerSecurityPlugin.class.getName());

        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(DB_USERNAME);
        String dbPassword = config.getString(DB_PASSWORD);
        new KapuaLiquibaseClient(resolveJdbcUrl(), dbUsername, dbPassword).update();

        try {
            // initialize shiro context for broker plugin from shiro ini file
            URL shiroIniUrl = getClass().getResource("/shiro.ini");
            String shiroIniStr = ResourceUtils.readResource(shiroIniUrl);
            Ini shiroIni = new Ini();
            shiroIni.load(shiroIniStr);

            IniSecurityManagerFactory factory = new IniSecurityManagerFactory(shiroIni);
            org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);

            // install the filters
            broker = new KapuaSecurityBrokerFilter(broker);
            return broker;
        } catch (Throwable t) {
            logger.error("Error in plugin installation.", t);
            throw new SecurityException(t);
        }
    }

}
