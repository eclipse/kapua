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
package org.eclipse.kapua.broker.core;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.eclipse.kapua.broker.core.plugin.KapuaApplicationBrokerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Install {@link KapuaApplicationBrokerFilter} into activeMQ filter chain plugin.<BR>
 * <p>
 * Is called by activeMQ broker by configuring plugin tag inside broker tag into activemq.xml.<BR>
 * <BR>
 * <BR>
 * <p>
 *
 * <pre>
 * &lt;plugins&gt;
 *     &lt;bean xmlns="http://www.springframework.org/schema/beans" id="applicationFilter" class="org.eclipse.kapua.broker.core.KapuaApplicationBrokerFilter"/&gt;
 * &lt;/plugins&gt;
 * </pre>
 *
 * @since 1.0
 */
public class KapuaBrokerApplicationPlugin implements BrokerPlugin {

    private static final Logger logger = LoggerFactory.getLogger(KapuaBrokerApplicationPlugin.class);

    public Broker installPlugin(Broker broker) throws Exception {
        logger.info("Installing Kapua application broker plugin...");
        try {
            return new KapuaApplicationBrokerFilter(broker);
        } catch (Exception e) {
            logger.error("Error in plugin installation.", e);
            throw new SecurityException(e);
        }
    }

}
