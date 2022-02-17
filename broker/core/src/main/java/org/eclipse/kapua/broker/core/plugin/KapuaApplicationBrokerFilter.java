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
package org.eclipse.kapua.broker.core.plugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMQ application filter plugin implementation (application context lifecycle filter).<br>
 * <br>
 * <p>
 * Filter to startup/shutdown proprly the application context.<br>
 * <br>
 * <p>
 * This filter is added inside ActiveMQ filter chain plugin by {@link org.eclipse.kapua.broker.core.KapuaBrokerApplicationPlugin}
 *
 * @since 1.0
 */
public class KapuaApplicationBrokerFilter extends BrokerFilter {

    private static final Logger logger = LoggerFactory.getLogger(KapuaApplicationBrokerFilter.class);

    // The following line must be done before any invocation of KapuaLocator.getInstance()
    private static ServiceModuleBundle application;

    public KapuaApplicationBrokerFilter(Broker next) throws KapuaException {
        super(next);
    }

    @Override
    public void start()
            throws Exception {
        logger.info(">>> Application broker filter: calling start...");
        synchronized (KapuaApplicationBrokerFilter.class) {
            if (application == null) {
                application = new ServiceModuleBundle() {

                };
            }
            application.startup();
        }
        super.start();
        logger.info(">>> Application broker filter: calling start... DONE");
    }

    @Override
    public void stop()
            throws Exception {
        logger.info(">>> Application broker filter: calling stop...");
        super.stop();
        synchronized (KapuaApplicationBrokerFilter.class) {
            if (application != null) {
                application.shutdown();
            }
        }
        logger.info(">>> Application broker filter: calling stop... DONE");
    }

}
