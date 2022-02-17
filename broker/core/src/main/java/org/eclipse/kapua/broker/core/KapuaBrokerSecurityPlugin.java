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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter;

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
        logger.info("Installing Kapua broker plugin...");

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
        } catch (Exception e) {
            logger.error("Error in plugin installation.", e);
            throw new SecurityException(e);
        }
    }

}
