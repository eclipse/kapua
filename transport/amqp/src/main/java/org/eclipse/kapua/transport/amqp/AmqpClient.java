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
package org.eclipse.kapua.transport.amqp;

import java.util.concurrent.TimeUnit;

import org.apache.qpid.proton.Proton;
import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.DestinationTranslator;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.transport.TransportClientConnectOptions;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;
import org.eclipse.kapua.transport.amqp.setting.AmqpClientSetting;
import org.eclipse.kapua.transport.amqp.setting.AmqpClientSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;

/**
 * Class that wrap a {@link org.eclipse.kapua.broker.client.amqp.proton.AmqpClient} and
 * adds some utility methods to manage the operations at the transport level
 * in Kapua.
 *
 */
public class AmqpClient {

    private final static String VT_PREFIX_PATTERN = "topic://VirtualTopic.%s";
    /**
     * The wrapped proton-j client.
     */
    private org.eclipse.kapua.broker.client.amqp.AmqpReceiverSender client;
    private ClientOptions clientOptions;
    private static final Logger logger = LoggerFactory.getLogger(AmqpClient.class);

    public AmqpClient() {
        clientOptions = new ClientOptions();
        clientOptions.put(AmqpClientOptions.CONNECT_TIMEOUT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_CONNECT_TIMEOUT));
        clientOptions.put(AmqpClientOptions.EXIT_CODE, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.EXIT_CODE));
        clientOptions.put(AmqpClientOptions.IDLE_TIMEOUT, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_IDLE_TIMEOUT));
        clientOptions.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, AmqpClientSetting.getInstance().getInt(AmqpClientSettingKeys.TRANSPORT_IDLE_TIMEOUT));
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
     * Connects the {@link AmqpClient#client} according to the given {@link TransportClientConnectOptions}.
     *
     * @param options The connection options to use
     * @throws AmqpClientException When connect fails.
     */
    public void connectClient(TransportClientConnectOptions options)
            throws KapuaException {
        if (client != null) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_ALREADY_CONNECTED,
                    null,
                    (Object[]) null);

        }

        client = new org.eclipse.kapua.broker.client.amqp.AmqpReceiverSender(wrapOptions(options));
        Future<Void> connectFuture = Future.future();
        connectFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                logger.info("Connected client {} to {} with username {}", options.getEndpointURI().toString(), options.getClientId(), options.getUsername());
            }
            else {
                logger.error("Cannot connect client {} to {} with username {}", options.getEndpointURI().toString(), options.getClientId(), options.getUsername());
            }
        });
        //since the AMQP client connect is asynchronous and Apache commons pool will test the borrowed client soon as it will be returned from this method we need to wait for the connection to be completed
        //otherwise the result will be an error since the borrowed client will be invalid (see org.eclipse.kapua.broker.client.amqp.AmqpClient for detail)
        long startTime = System.currentTimeMillis();
        client.connect(connectFuture);
        try {
            client.connectionTimeout.await(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("Acquired connected client after {} - {}ms - {}", System.currentTimeMillis(), (System.currentTimeMillis() - startTime), client.isConnected());
    }

    /**
     * Disconnects the {@link AmqpClient#client}.
     * <p>
     * Before disconnecting cleaning of subscriptions is attempted.
     * </p>
     *
     */
    public void disconnectClient()
        throws KapuaException {
        Future<Void> disconnectFuture = Future.future();
        String clientId = client.getClientId();
        disconnectFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                logger.debug("Disconnected client {}", clientId);
            }
            else {
                logger.error("Cannot disconnect client {}", clientId, ar.cause());
            }
        });
        client.disconnect(disconnectFuture);
    }

    /**
     * Disconnects the {@link AmqpClient#client}.
     * <p>
     * Before termination {@link AmqpClient#disconnectClient()} is invoked to clean up appended subscriptions and close connection.
     * </p>
     *
     * @throws KapuaException When close fails.
     */
    public void terminateClient()
            throws KapuaException {
        try {
            if (client.isConnected()) {
                disconnectClient();
            }
            client = null;
        } catch (AmqpClientException e) {
            throw new AmqpClientException(AmqpClientErrorCodes.CLIENT_TERMINATE_ERROR, e, (Object) null);
        }
    }

    /**
     * Checks if this {@link AmqpClient} is connected.
     *
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    public String getClientId() {
        return client.getClientId();
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
     */
    public void publish(AmqpMessage amqpMessage)
            throws KapuaException {
        AmqpTopic amqpTopic = amqpMessage.getRequestTopic();
        AmqpPayload amqpPayload = amqpMessage.getPayload();
        client.send(createMesage(amqpPayload.getBody(), amqpTopic.getTopic()), amqpTopic.getTopic());
    }

    /**
     * Subscribes this client to the given {@link AmqpTopic}.
     *
     * @param amqpTopic The {@link AmqpTopic} to subscribe to.
     * @param clientConsumerHandler
     * @throws KapuaException When subscribe fails.
     */
    public void subscribe(AmqpTopic amqpTopic, AmqpClientConsumerHandler clientConsumerHandler)
            throws KapuaException {
        client.subscribe(amqpTopic.getTopic(), clientConsumerHandler);
    }

    /**
     * Cleans this client from any callback set and unsubscribes from all {@link AmqpTopic} subscribed.
     */
    public void clean() { 
        client.clean();
    }

    //
    // Utilty
    //

    private Message createMesage(byte[] payload, String destination) {
      Message msg = Proton.message();
      Section s = new Data(new Binary(payload));
      msg.setBody(s);
      msg.setAddress(destination);
      return msg;
    }

}
