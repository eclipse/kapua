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

import java.net.URI;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.transport.amqpproton.AmqpClient;
import org.eclipse.kapua.transport.amqpproton.AmqpClientConnectionOptions;
import org.eclipse.kapua.transport.amqpproton.pooling.setting.AmqpClientPoolSetting;
import org.eclipse.kapua.transport.amqpproton.pooling.setting.AmqpClientPoolSettingKeys;
import org.eclipse.kapua.transport.amqpproton.setting.AmqpClientSetting;
import org.eclipse.kapua.transport.amqpproton.setting.AmqpClientSettingKeys;
import org.eclipse.kapua.transport.utils.ClientIdGenerator;

/**
 * Pooled object factory for {@link AmqpClientPool}.
 * 
 * @since 1.0.0
 *
 */
public class PooledAmqpClientFactory extends BasePooledObjectFactory<AmqpClient> {

    private final String nodeUri;

    public PooledAmqpClientFactory(String nodeUri) {
        this.nodeUri = nodeUri;
    }

    /**
     * Creates the {@link AmqpClient} for the {@link AmqpClientPool}.
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
    public AmqpClient create()
            throws Exception {
        //
        // User pwd generation
        AmqpClientSetting amqpClientSettings = AmqpClientSetting.getInstance();
        AmqpClientPoolSetting amqpClientPoolSettings = AmqpClientPoolSetting.getInstance();

        String username = amqpClientSettings.getString(AmqpClientSettingKeys.TRANSPORT_CREDENTIAL_USERNAME);
        char[] password = amqpClientSettings.getString(AmqpClientSettingKeys.TRANSPORT_CREDENTIAL_PASSWORD).toCharArray();
        String clientId = ClientIdGenerator.getInstance().next(amqpClientPoolSettings.getString(AmqpClientPoolSettingKeys.CLIENT_POOL_CLIENT_ID_PREFIX));

        //
        // Get new client and connection options
        AmqpClientConnectionOptions connectionOptions = new AmqpClientConnectionOptions();
        connectionOptions.setClientId(clientId);
        connectionOptions.setUsername(username);
        connectionOptions.setPassword(password);
        connectionOptions.setEndpointURI(URI.create(nodeUri));

        //
        // Connect client
        AmqpClient kapuaClient = new AmqpClient();
        try {
            kapuaClient.connectClient(connectionOptions);
        } catch (KapuaException ke) {
            kapuaClient.terminateClient();
            throw ke;
        }

        return kapuaClient;
    }

    /**
     * Wraps the given {@link AmqpClient} into a {@link DefaultPooledObject}.
     * 
     * @param amqpClient
     *            The object to wrap for {@link BasePooledObjectFactory}.
     * @since 1.0.0
     */
    @Override
    public PooledObject<AmqpClient> wrap(AmqpClient amqpClient) {
        return new DefaultPooledObject<>(amqpClient);
    }

    /**
     * Validates status of the given {@link AmqpClient} pooled object.
     * 
     * <p>
     * Check performed for the client to be marked as valid are:
     * </p>
     * <ul>
     * <li>{@link AmqpClient} {@code != null}</li>
     * <li>{@link AmqpClient#isConnected()} {@code == true}</li>
     * </ul>
     * 
     * @param pooledAmqpClient
     *            The object to validate.
     * 
     * @since 1.0.0
     */
    @Override
    public boolean validateObject(PooledObject<AmqpClient> pooledAmqpClient) {
        AmqpClient amqpClient = pooledAmqpClient.getObject();
        return (amqpClient != null && amqpClient.isConnected());
    }

    /**
     * Destroys the given {@link AmqpClient} pooled object.
     * <p>
     * Before calling super implementation {@link BasePooledObjectFactory#destroyObject(PooledObject)} it tries to clean up the {@link AmqpClient}.
     * </p>
     * 
     * @param pooledAmqpClient
     *            The pooled object to destroy.
     * 
     * @since 1.0.0.
     */
    @Override
    public void destroyObject(PooledObject<AmqpClient> pooledAmqpClient)
            throws Exception {
        AmqpClient amqpClient = pooledAmqpClient.getObject();
        if (amqpClient != null) {
            if (amqpClient.isConnected()) {
                amqpClient.disconnectClient();
            }
            amqpClient.terminateClient();
        }
        super.destroyObject(pooledAmqpClient);
    }

}
