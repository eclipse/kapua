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
package org.eclipse.kapua.transport.amqpproton;

import org.apache.qpid.proton.Proton;
import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.proton.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.proton.DestinationTranslator;
import org.eclipse.kapua.broker.client.amqp.proton.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.amqpproton.message.AmqpMessage;
import org.eclipse.kapua.transport.amqpproton.message.AmqpPayload;
import org.eclipse.kapua.transport.amqpproton.message.AmqpTopic;
import org.eclipse.kapua.transport.amqpproton.setting.AmqpClientSetting;
import org.eclipse.kapua.transport.amqpproton.setting.AmqpClientSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class that wrap a {@link org.eclipse.kapua.broker.client.amqp.proton.AmqpClient} and
 * adds some utility methods to manage the operations at the transport level
 * in Kapua.
 *
 * @since 1.0.0
 */
public class AmqpClient {

    private final static String VT_PREFIX_PATTERN = "topic://VirtualTopic.%s";
    /**
     * The wrapped proton-j client.
     */
    private org.eclipse.kapua.broker.client.amqp.proton.AmqpClient amqpClient;
    private ClientOptions clientOptions;
    private static final Logger logger = LoggerFactory.getLogger(AmqpClient.class);

    public AmqpClient() {
        clientOptions = new ClientOptions();
        clientOptions.put(AmqpClientOptions.AUTO_ACCEPT, false);
        clientOptions.put(AmqpClientOptions.QOS, 1);
        clientOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_CONNECT_TIMEOUT));
        clientOptions.put(AmqpClientOptions.EXIT_CODE, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.EXIT_CODE));
        clientOptions.put(AmqpClientOptions.IDLE_TIMEOUT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_IDLE_TIMEOUT));
        clientOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_IDLE_TIMEOUT));
        clientOptions.put(AmqpClientOptions.PREFETCH_MESSAGES, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_PREFETCH_MESSAGES));
        clientOptions.put(AmqpClientOptions.WAIT_BETWEEN_RECONNECT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_WAIT_BETWEEN_RECONNECT));
        if (AmqpClientSetting.getInstance().getBoolean(AmqpClientSettingKeys.TRANSPORT_MQTT_VT_ENABLED)) {
            //enable it if the device is connected through ActiveMQ MQTT connector with VirtualTopic enabled
            clientOptions.put(AmqpClientOptions.DESTINATION_TRANSLATOR, new DestinationTranslator() {

                @Override
                public String translate(String destination) {
                    return String.format(VT_PREFIX_PATTERN, destination.replaceAll("/", "."));
                }
            });
        }
    }

    private ClientOptions wrapOptions(TransportClientConnectOptions options) {
        clientOptions.put(AmqpClientOptions.BROKER_HOST, options.getEndpointURI().getHost());
        //the connection port is a deployment parameter so read it from configuration
        clientOptions.put(AmqpClientOptions.BROKER_PORT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_CONNECTION_PORT));
        clientOptions.put(AmqpClientOptions.PASSWORD, options.getPassword());
        clientOptions.put(AmqpClientOptions.USERNAME, options.getUsername());
        clientOptions.put(AmqpClientOptions.CLIENT_ID, options.getClientId());
        return clientOptions;
    }

    /**
     * Connects the {@link AmqpClient#amqpClient} according to the given {@link TransportClientConnectOptions}.
     *
     * @param options The connection options to use
     * @throws AmqpClientException When connect fails.
     * @since 1.0.0
     */
    public void connectClient(TransportClientConnectOptions options)
            throws KapuaException {
        try {
            if (amqpClient != null) {
                throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_ALREADY_CONNECTED,
                        null,
                        (Object[]) null);

            }

            amqpClient = new org.eclipse.kapua.broker.client.amqp.proton.AmqpClient(wrapOptions(options));
            amqpClient.connect();
            //since the AMQP client connect is asynchronous and Apache commons pool will test the borrowed client soon as it will be returned from this method we need to wait for the connection to be completed
            //otherwise the result will be an error since the borrowed client will be invalid (see org.eclipse.kapua.broker.client.amqp.AmqpClient for detail)
            long currentTime = System.currentTimeMillis();
            synchronized (amqpClient) {
                try {
                    amqpClient.wait(AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_CONNECT_TIMEOUT));
                } catch (InterruptedException e) {
                    logger.error("Interrupted: {}", e.getMessage(), e);
                }
                logger.info("Acquired connected client after {}ms", (System.currentTimeMillis() - currentTime));
            }
        } catch (IOException e) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_CONNECT_ERROR,
                    e,
                    options.getEndpointURI().toString(),
                    options.getClientId(),
                    options.getUsername());
        }
    }

    /**
     * Disconnects the {@link AmqpClient#amqpClient}.
     * <p>
     * Before disconnecting cleaning of subscriptions is attempted.
     * </p>
     *
     * @throws KapuaException When disconnect fails.
     * @since 1.0.0
     */
    public void disconnectClient()
            throws KapuaException {
        try {
            getAmqpClient().disconnect();
        } catch (AmqpClientException e) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_DISCONNECT_ERROR, e, (Object) null);
        }
    }

    /**
     * Disconnects the {@link AmqpClient#amqpClient}.
     * <p>
     * Before termination {@link AmqpClient#disconnectClient()} is invoked to clean up appended subscriptions and close connection.
     * </p>
     *
     * @throws KapuaException Whne close fails.
     * @since 1.0.0
     */
    public void terminateClient()
            throws KapuaException {
        try {
            if (getAmqpClient().isConnected()) {
                disconnectClient();
            }
            amqpClient = null;
        } catch (AmqpClientException e) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_TERMINATE_ERROR, e, (Object) null);
        }
    }

    /**
     * Checks if this {@link AmqpClient} is connected.
     *
     * @return {@code true} if connected, {@code false} otherwise.
     * @since 1.0.0
     */
    public boolean isConnected() {
        try {
            return getAmqpClient().isConnected();
        } catch (KapuaException e) {
            // FIXME: add log
            return false;
        }
    }

    //
    // Message management
    //

    /**
     * Publish a {@link AmqpMessage}.
     * <p>
     * QoS of publishing is set to 0.
     * </p>
     *
     * @param amqpMessage The {@link AmqpMessage} to publish.
     * @throws KapuaException When publish fails.
     * @since 1.0.0
     */
    public void publish(AmqpMessage amqpMessage)
            throws KapuaException {
        AmqpTopic amqpTopic = amqpMessage.getRequestTopic();
        AmqpPayload amqpPayload = amqpMessage.getPayload();
        try {
            getAmqpClient().send(createMesage(amqpPayload.getBody(), amqpTopic.getTopic()), amqpTopic.getTopic());
        } catch (KapuaException e) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_SUBSCRIBE_ERROR, e, amqpTopic.toString());
        }
    }

    /**
     * Subscribes this client to the given {@link AmqpTopic}.
     *
     * @param amqpTopic The {@link AmqpTopic} to subscribe to.
     * @param amqpClientConsumerHandler
     * @throws KapuaException When subscribe fails.
     * @since 1.0.0
     */
    public void subscribe(AmqpTopic amqpTopic, AmqpClientConsumerHandler amqpClientConsumerHandler)
            throws KapuaException {
        try {
            getAmqpClient().subscribe(amqpTopic.getTopic(), amqpClientConsumerHandler);
        } catch (KapuaException e) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_SUBSCRIBE_ERROR, e, amqpTopic.getTopic());
        }
    }

    /**
     * Cleans this client from any callback set and unsubscribes from all {@link AmqpTopic} subscribed.
     *
     * @throws KapuaException When any of the clean operations fails.
     */
    public void clean()
            throws KapuaException {
        try {
            getAmqpClient().clean();
        } catch (KapuaException e) {
            terminateClient();
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_CLEAN_ERROR, e, (Object) null);
        }

    }

    //
    // Utilty
    //

    /**
     * Gets the clientId of the wrapped {@link AmqpClient#amqpClient}.
     *
     * @return The clientId of the wrapped {@link AmqpClient#amqpClient}.
     */
    public String getClientId() {
        try {
            return getAmqpClient().getClientId();
        } catch (KapuaException e) {
            return null;
        }
    }

    /**
     * Gets the reference to the wrapped {@link AmqpClient#amqpClient} ready to be used.
     *
     * @return The wrapped {@link AmqpClient#amqpClient}.
     * @throws KapuaException If client has never been connected using {@link AmqpClient#connectClient(TransportClientConnectOptions)}.
     */
    private synchronized org.eclipse.kapua.broker.client.amqp.proton.AmqpClient getAmqpClient() throws KapuaException {
        if (amqpClient == null) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_NOT_CONNECTED, null, (Object) null);
        }

        return amqpClient;
    }

    private Message createMesage(byte[] payload, String destination) {
      Message msg = Proton.message();
      Section s = new Data(new Binary(payload));
      msg.setBody(s);
      msg.setAddress(destination);
      return msg;
    }

}
