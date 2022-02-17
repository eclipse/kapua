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

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.eclipse.kapua.transport.mqtt.MqttClient;
import org.eclipse.kapua.transport.mqtt.MqttClientConnectionOptions;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientException;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientTerminateException;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSetting;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSettingKeys;
import org.eclipse.kapua.transport.mqtt.setting.MqttClientSetting;
import org.eclipse.kapua.transport.mqtt.setting.MqttClientSettingKeys;
import org.eclipse.kapua.transport.utils.ClientIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Pooled object factory for {@link MqttClientPool}.
 *
 * @since 1.0.0
 */
public class PooledMqttClientFactory extends BasePooledObjectFactory<MqttClient> {

    private static final Logger LOG = LoggerFactory.getLogger(PooledMqttClientFactory.class);
    private static final ClientIdGenerator CLIENT_ID_GENERATOR = ClientIdGenerator.getInstance();

    private final String serverURI;

    public PooledMqttClientFactory(String serverURI) {
        this.serverURI = serverURI;
    }

    /**
     * Creates the {@link MqttClient} for the {@link MqttClientPool}.
     *
     * <p>
     * The client is initialized and connected. In case of any failure on connect operation, an exception is thrown and the the created client is destroyed.
     * </p>
     *
     * @throws Exception FIXME [javadoc] document exception.
     * @since 1.0.0
     */
    @Override
    public MqttClient create() throws Exception {
        //
        // User pwd generation
        MqttClientSetting mqttClientSettings = MqttClientSetting.getInstance();
        MqttClientPoolSetting mqttClientPoolSettings = MqttClientPoolSetting.getInstance();

        String username = mqttClientSettings.getString(MqttClientSettingKeys.TRANSPORT_CREDENTIAL_USERNAME);
        char[] password = mqttClientSettings.getString(MqttClientSettingKeys.TRANSPORT_CREDENTIAL_PASSWORD).toCharArray();
        String clientId = CLIENT_ID_GENERATOR.next(mqttClientPoolSettings.getString(MqttClientPoolSettingKeys.CLIENT_POOL_CLIENT_ID_PREFIX));

        //
        // Get new client and connection options
        MqttClientConnectionOptions connectionOptions = new MqttClientConnectionOptions();
        connectionOptions.setClientId(clientId);
        connectionOptions.setUsername(username);
        connectionOptions.setPassword(password);
        connectionOptions.setEndpointURI(URI.create(serverURI));

        //
        // Connect client
        MqttClient kapuaClient = new MqttClient();
        try {
            kapuaClient.connectClient(connectionOptions);
        } catch (MqttClientException mce) {
            try {
                kapuaClient.terminateClient();
            } catch (MqttClientTerminateException mcte) {
                LOG.error("Unable to properly terminate MQTT client after failed connect attempt: {}", clientId, mcte);
            }

            throw mce;
        }

        return kapuaClient;
    }

    /**
     * Wraps the given {@link MqttClient} into a {@link DefaultPooledObject}.
     *
     * @param mqttClient The object to wrap for {@link BasePooledObjectFactory}.
     * @since 1.0.0
     */
    @Override
    public PooledObject<MqttClient> wrap(MqttClient mqttClient) {
        return new DefaultPooledObject<>(mqttClient);
    }

    /**
     * Validates status of the given {@link MqttClient} pooled object.
     *
     * <p>
     * Check performed for the client to be marked as valid are:
     * </p>
     * <ul>
     * <li>{@link MqttClient} {@code != null}</li>
     * <li>{@link MqttClient#isConnected()} {@code == true}</li>
     * </ul>
     *
     * @param pooledMqttClient The object to validate.
     * @since 1.0.0
     */
    @Override
    public boolean validateObject(PooledObject<MqttClient> pooledMqttClient) {
        if (pooledMqttClient == null) {
            return false;
        }

        MqttClient mqttClient = pooledMqttClient.getObject();
        return (mqttClient != null && mqttClient.isConnected());
    }

    /**
     * Destroys the given {@link MqttClient} pooled object.
     * <p>
     * Before calling super implementation {@link BasePooledObjectFactory#destroyObject(PooledObject)} it tries to clean up the {@link MqttClient}.
     * </p>
     *
     * @param pooledMqttClient The pooled object to destroy.
     * @since 1.0.0.
     */
    @Override
    public void destroyObject(PooledObject<MqttClient> pooledMqttClient) throws Exception {
        if (pooledMqttClient == null) {
            return;
        }

        MqttClient mqttClient = pooledMqttClient.getObject();
        try {
            if (mqttClient != null) {
                if (mqttClient.isConnected()) {
                    mqttClient.disconnectClient();
                }
                mqttClient.terminateClient();
            }
        } catch (MqttClientException mce) {
            LOG.warn("Error while cleaning MqttClient {} before destroying it... Removing it from the pool anyway!", mqttClient.getClientId(), mce);
        }

        super.destroyObject(pooledMqttClient);
    }

}
