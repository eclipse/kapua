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
package org.eclipse.kapua.transport.mqtt.pooling;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.mqtt.MqttClient;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSetting;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSettingKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * Client pool for {@link MqttClient} objects.
 * <p>
 * This serves to optimize communication at the transport level of Kapua.
 * Client borrowed from this pool are already connected and ready to publish and subscribe.
 * </p>
 * 
 * @since 1.0.0
 *
 */
public class MqttClientPool extends GenericObjectPool<MqttClient> {

    /**
     * Singleton instance of {@link MqttClientPool}
     */
    private static Map<String, MqttClientPool> mqttClientPoolInstances = new HashMap<>();

    /**
     * Initialize a {@link MqttClientPool} with the according configuration sourced from {@link MqttClientPoolSetting}.
     * 
     * @since 1.0.0
     */
    private MqttClientPool(PooledMqttClientFactory factory) {
        super(factory);

        MqttClientPoolSetting config = MqttClientPoolSetting.getInstance();
        GenericObjectPoolConfig clientPoolConfig = new GenericObjectPoolConfig();
        clientPoolConfig.setMinIdle(config.getInt(MqttClientPoolSettingKeys.CLIENT_POOL_SIZE_IDLE_MIN));
        clientPoolConfig.setMaxIdle(config.getInt(MqttClientPoolSettingKeys.CLIENT_POOL_SIZE_IDLE_MAX));
        clientPoolConfig.setMaxTotal(config.getInt(MqttClientPoolSettingKeys.CLIENT_POOL_SIZE_TOTAL_MAX));

        clientPoolConfig.setMaxWaitMillis(config.getInt(MqttClientPoolSettingKeys.CLIENT_POOL_BORROW_WAIT_MAX));

        clientPoolConfig.setTestOnReturn(config.getBoolean(MqttClientPoolSettingKeys.CLIENT_POOL_ON_RETURN_TEST));
        clientPoolConfig.setTestOnBorrow(config.getBoolean(MqttClientPoolSettingKeys.CLIENT_POOL_ON_BORROW_TEST));

        clientPoolConfig.setTestWhileIdle(config.getBoolean(MqttClientPoolSettingKeys.CLIENT_POOL_WHEN_IDLE_TEST));
        clientPoolConfig.setBlockWhenExhausted(config.getBoolean(MqttClientPoolSettingKeys.CLIENT_POOL_WHEN_EXAUSTED_BLOCK));

        clientPoolConfig.setTimeBetweenEvictionRunsMillis(config.getLong(MqttClientPoolSettingKeys.CLIENT_POOL_EVICTION_INTERVAL));

        setConfig(clientPoolConfig);
    }

    /**
     * Gets the singleton instance of {@link MqttClientPool}.
     * 
     * @return The singleton instance of {@link MqttClientPool}.
     * @since 1.0.0
     */
    public static MqttClientPool getInstance(String brokerUri) {
        MqttClientPool mqttClientPool = mqttClientPoolInstances.get(brokerUri);
        if (mqttClientPool == null) {
            mqttClientPool = new MqttClientPool(new PooledMqttClientFactory(brokerUri));
            mqttClientPoolInstances.put(brokerUri, mqttClientPool);
        }

        return mqttClientPool;
    }

    /**
     * Returns a borrowed object to the pool.
     * <p>
     * Before calling super implementation {@link GenericObjectPool#returnObject(Object)} the {@link MqttClient} is cleaned by invoking the {@link MqttClient#clean()}.
     * </p>
     * 
     * @since 1.0.0
     */
    @Override
    public void returnObject(MqttClient kapuaClient) {
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
            // FIXME: Manage exception
            e.printStackTrace();
        }
    }
}
