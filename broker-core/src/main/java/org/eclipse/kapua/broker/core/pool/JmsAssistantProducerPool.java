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
 *******************************************************************************/
package org.eclipse.kapua.broker.core.pool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is needed by {@link org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter} to handle a vm connection.<BR>
 * Indeed this bundle is instantiated during the broker startup then if {@link org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter} try to instantiate a connection receive an error from the
 * broker. (the vm factory couldn't
 * reach the broker)<BR>
 * Then this class is needed to instantiate only a connection to be useful for the filter when it need
 * ({@link org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter#addConnection(org.apache.activemq.broker.ConnectionContext, org.apache.activemq.command.ConnectionInfo) add connection} and
 * {@link org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter#removeConnection(org.apache.activemq.broker.ConnectionContext, org.apache.activemq.command.ConnectionInfo, Throwable) remove
 * connection}).<BR>
 * <BR>
 *
 * <b>NOTE:<BR>
 * with virtual topic support the destinations are removed! The message destination will be coded inside send method!
 * </b>
 * 
 * @since 1.0
 */
public class JmsAssistantProducerPool extends GenericObjectPool<JmsAssistantProducerWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(JmsAssistantProducerPool.class);

    public enum DESTINATIONS {
        /**
         * To be used to send messages without known destination.
         * Otherwise the inactive monitor will not be able to remove the destination because it has a producer!
         */
        NO_DESTINATION
    }

    private static Map<DESTINATIONS, JmsAssistantProducerPool> pools;

    static {
        pools = new HashMap<JmsAssistantProducerPool.DESTINATIONS, JmsAssistantProducerPool>();
        logger.info("Create pools for broker assistants (kapua server instance)");
        logger.info("Create Service pool...");
        // TODO parameter to be added to configuration
        // pools.put(DESTINATIONS.KAPUA_SERVICE,
        // new JmsAssistantProducerPool(new JmsAssistantProducerWrapperFactory(KapuaEnvironmentConfig.getInstance().getString(KapuaEnvironmentConfigKeys.SERVICE_QUEUE_NAME))));
        logger.info("Create NoDestination pool...");
        pools.put(DESTINATIONS.NO_DESTINATION,
                new JmsAssistantProducerPool(new JmsAssistantProducerWrapperFactory(null)));
        logger.info("Create pools... done.");
    }

    /**
     * Create a JmsAssistantProducerPool from the given factory
     * 
     * @param factory
     */
    protected JmsAssistantProducerPool(JmsAssistantProducerWrapperFactory factory) {
        super(factory);
        // TODO parameter to be added to configuration
        // int totalMaxSize = KapuaEnvironmentConfig.getInstance().getString(KapuaEnvironmentConfigKeys.POOL_TOTAL_MAX_SIZE);
        // int maxSize = KapuaEnvironmentConfig.getInstance().getString(KapuaEnvironmentConfigKeys.POOL_MAX_SIZE);
        // int minSize = KapuaEnvironmentConfig.getInstance().getString(KapuaEnvironmentConfigKeys.POOL_MIN_SIZE);
        int totalMaxSize = 25;
        int maxSize = 25;
        int minSize = 10;

        GenericObjectPoolConfig jmsPoolConfig = new GenericObjectPoolConfig();
        jmsPoolConfig.setMaxTotal(totalMaxSize);
        jmsPoolConfig.setMaxIdle(maxSize);
        jmsPoolConfig.setMinIdle(minSize);
        logger.info("Set test on return to true for JmsAssistantProducerPool");
        jmsPoolConfig.setTestOnReturn(true);
        logger.info("Set test on borrow to true for JmsAssistantProducerPool");
        jmsPoolConfig.setTestOnBorrow(true);
        logger.info("Set block when exausted to true for JmsAssistantProducerPool");
        jmsPoolConfig.setBlockWhenExhausted(true);

        setConfig(jmsPoolConfig);
    }

    /**
     * Return a JmsAssistantProducerPool for the given destination
     * 
     * @param destination
     * @return
     */
    public static JmsAssistantProducerPool getIOnstance(DESTINATIONS destination) {
        return pools.get(destination);
    }

    /**
     * Close all connection pools
     */
    public static void closePools() {
        if (pools != null) {
            logger.info("Close NoDestination pool...");
            pools.get(DESTINATIONS.NO_DESTINATION).close();
            logger.info("Close pools... done.");
        } else {
            logger.warn("Cannot close producer pools... Pools not initialized!");
        }
    }

}
