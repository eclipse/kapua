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
package org.eclipse.kapua.transport.amqpproton.pooling;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.amqpproton.AmqpClient;
import org.eclipse.kapua.transport.amqpproton.pooling.setting.AmqpClientPoolSetting;
import org.eclipse.kapua.transport.amqpproton.pooling.setting.AmqpClientPoolSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Client pool for {@link AmqpClient} objects.
 * <p>
 * This serves to optimize communication at the transport level of Kapua.
 * Client borrowed from this pool are already connected and ready to publish and subscribe.
 * </p>
 * 
 * @since 1.0.0
 *
 */
public class AmqpClientPool extends GenericObjectPool<AmqpClient> {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClientPool.class);
    /**
     * Singleton instance of {@link AmqpClientPool}
     */
    private static Map<String, AmqpClientPool> mqttClientPoolInstances = new HashMap<>();

    /**
     * Initialize a {@link AmqpClientPool} with the according configuration sourced from {@link AmqpClientPoolSetting}.
     * 
     * @since 1.0.0
     */
    private AmqpClientPool(PooledAmqpClientFactory factory) {
        super(factory);

        AmqpClientPoolSetting config = AmqpClientPoolSetting.getInstance();
        GenericObjectPoolConfig clientPoolConfig = new GenericObjectPoolConfig();
        clientPoolConfig.setMinIdle(config.getInt(AmqpClientPoolSettingKeys.CLIENT_POOL_SIZE_IDLE_MIN));
        clientPoolConfig.setMaxIdle(config.getInt(AmqpClientPoolSettingKeys.CLIENT_POOL_SIZE_IDLE_MAX));
        clientPoolConfig.setMaxTotal(config.getInt(AmqpClientPoolSettingKeys.CLIENT_POOL_SIZE_TOTAL_MAX));

        clientPoolConfig.setMaxWaitMillis(config.getInt(AmqpClientPoolSettingKeys.CLIENT_POOL_BORROW_WAIT_MAX));

        clientPoolConfig.setTestOnReturn(config.getBoolean(AmqpClientPoolSettingKeys.CLIENT_POOL_ON_RETURN_TEST));
        clientPoolConfig.setTestOnBorrow(config.getBoolean(AmqpClientPoolSettingKeys.CLIENT_POOL_ON_BORROW_TEST));

        clientPoolConfig.setTestWhileIdle(config.getBoolean(AmqpClientPoolSettingKeys.CLIENT_POOL_WHEN_IDLE_TEST));
        clientPoolConfig.setBlockWhenExhausted(config.getBoolean(AmqpClientPoolSettingKeys.CLIENT_POOL_WHEN_EXAUSTED_BLOCK));

        clientPoolConfig.setTimeBetweenEvictionRunsMillis(config.getLong(AmqpClientPoolSettingKeys.CLIENT_POOL_EVICTION_INTERVAL));

        setConfig(clientPoolConfig);
    }

    /**
     * Gets the singleton instance of {@link AmqpClientPool}.
     * 
     * @param nodeUri
     * @return The singleton instance of {@link AmqpClientPool}.
     * @since 1.0.0
     */
    public static AmqpClientPool getInstance(String nodeUri) {
        AmqpClientPool mqttClientPool = mqttClientPoolInstances.get(nodeUri);
        if (mqttClientPool == null) {
            mqttClientPool = new AmqpClientPool(new PooledAmqpClientFactory(nodeUri));
            mqttClientPoolInstances.put(nodeUri, mqttClientPool);
        }

        return mqttClientPool;
    }

    /**
     * Returns a borrowed object to the pool.
     * <p>
     * Before calling super implementation {@link GenericObjectPool#returnObject(Object)} the {@link AmqpClient} is cleaned by invoking the {@link AmqpClient#clean()}.
     * </p>
     * 
     * @since 1.0.0
     */
    @Override
    public void returnObject(AmqpClient kapuaClient) {
        //
        // Clean up callback
        try {
            kapuaClient.clean();

            //
            // Return object to pool
            super.returnObject(kapuaClient);
        } catch (KapuaException e) {
            try {
                kapuaClient.terminateClient();
            } catch (KapuaException e1) {
                // FIXME: Manage exception
            }
            logger.error("Exception while returning object to the pool: {}", e.getMessage(), e);
        }
    }
}
