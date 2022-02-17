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
package org.eclipse.kapua.transport.mqtt.pooling;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.eclipse.kapua.transport.mqtt.MqttClient;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientCleanException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientTerminateException;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSetting;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
public class MqttClientPool extends GenericObjectPool<MqttClient> {

    private static final Logger LOG = LoggerFactory.getLogger(MqttClientPool.class);

    /**
     * Singleton instances of {@link MqttClientPool} by their host.
     *
     * @since 1.0.0
     */
    private static final Map<String, MqttClientPool> MQTT_CLIENT_POOL_BY_HOST = new HashMap<>();

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
     * @param serverURI The {@link java.net.URI} in {@link String} form for which get the {@link MqttClientPool}.
     * @return The singleton instance of {@link MqttClientPool}.
     * @since 1.1.0
     */
    public static MqttClientPool getInstance(String serverURI) {
        return MQTT_CLIENT_POOL_BY_HOST.computeIfAbsent(serverURI, k -> new MqttClientPool(new PooledMqttClientFactory(serverURI)));
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
        try {
            // Clean up callback
            kapuaClient.clean();

            // Return object to pool
            super.returnObject(kapuaClient);
        } catch (MqttClientCleanException mcce) {
            LOG.error("Error while returning MqttClient ({}) to the pool. Terminating...", kapuaClient.getClientId());
            try {
                kapuaClient.terminateClient();
                LOG.error("Error while returning MqttClient ({}) to the pool. Terminating... DONE! Error was: {}", kapuaClient.getClientId(), mcce.getMessage());
            } catch (MqttClientTerminateException mcte) {
                LOG.error("Error while returning MqttClient ({}) to the pool. Terminating... ERROR! Error was: {}", kapuaClient.getClientId(), mcce.getMessage(), mcte);
            }
        }
    }
}
