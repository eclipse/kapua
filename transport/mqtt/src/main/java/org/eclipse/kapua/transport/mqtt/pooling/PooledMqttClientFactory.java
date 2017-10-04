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

import java.net.URI;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.mqtt.MqttClient;
import org.eclipse.kapua.transport.mqtt.MqttClientConnectionOptions;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSetting;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSettingKeys;
import org.eclipse.kapua.transport.mqtt.setting.MqttClientSetting;
import org.eclipse.kapua.transport.mqtt.setting.MqttClientSettingKeys;
import org.eclipse.kapua.transport.utils.ClientIdGenerator;

/**
 * Pooled object factory for {@link MqttClientPool}.
 * 
 * @since 1.0.0
 *
 */
public class PooledMqttClientFactory extends BasePooledObjectFactory<MqttClient> {

    private final String brokerUri;

    public PooledMqttClientFactory(String brokerUri) {
        this.brokerUri = brokerUri;
    }

    /**
     * Creates the {@link MqttClient} for the {@link MqttClientPool}.
     * 
     * <p>
     * The client is initialized and connected. In case of any failure on connect operation, an exception is thrown and the the created client is destroyed.
     * </p>
     * 
     * @throws Exception
     *             FIXME [javadoc] document exception.
     * @since 1.0.0
     */
    @Override
    public MqttClient create()
            throws Exception {
        //
        // User pwd generation
        MqttClientSetting mqttClientSettings = MqttClientSetting.getInstance();
        MqttClientPoolSetting mqttClientPoolSettings = MqttClientPoolSetting.getInstance();

        String username = mqttClientSettings.getString(MqttClientSettingKeys.TRANSPORT_CREDENTIAL_USERNAME);
        char[] password = mqttClientSettings.getString(MqttClientSettingKeys.TRANSPORT_CREDENTIAL_PASSWORD).toCharArray();
        String clientId = ClientIdGenerator.getInstance().next(mqttClientPoolSettings.getString(MqttClientPoolSettingKeys.CLIENT_POOL_CLIENT_ID_PREFIX));
        URI brokerURI = URI.create(brokerUri);

        //
        // Get new client and connection options
        MqttClientConnectionOptions connectionOptions = new MqttClientConnectionOptions();
        connectionOptions.setClientId(clientId);
        connectionOptions.setUsername(username);
        connectionOptions.setPassword(password);
        connectionOptions.setEndpointURI(brokerURI);

        //
        // Connect client
        MqttClient kapuaClient = new MqttClient();
        try {
            kapuaClient.connectClient(connectionOptions);
        } catch (KapuaException ke) {
            kapuaClient.terminateClient();
            throw ke;
        }

        return kapuaClient;
    }

    /**
     * Wraps the given {@link MqttClient} into a {@link DefaultPooledObject}.
     * 
     * @param mqttClient
     *            The object to wrap for {@link BasePooledObjectFactory}.
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
     * @param pooledMqttClient
     *            The object to validate.
     * 
     * @since 1.0.0
     */
    @Override
    public boolean validateObject(PooledObject<MqttClient> pooledMqttClient) {
        MqttClient mqttClient = pooledMqttClient.getObject();
        return (mqttClient != null && mqttClient.isConnected());
    }

    /**
     * Destroys the given {@link MqttClient} pooled object.
     * <p>
     * Before calling super implementation {@link BasePooledObjectFactory#destroyObject(PooledObject)} it tries to clean up the {@link MqttClient}.
     * </p>
     * 
     * @param pooledMqttClient
     *            The pooled object to destroy.
     * 
     * @since 1.0.0.
     */
    @Override
    public void destroyObject(PooledObject<MqttClient> pooledMqttClient)
            throws Exception {
        MqttClient mqttClient = pooledMqttClient.getObject();
        if (mqttClient != null) {
            if (mqttClient.isConnected()) {
                mqttClient.disconnectClient();
            }
            mqttClient.terminateClient();
        }
        super.destroyObject(pooledMqttClient);
    }

}
