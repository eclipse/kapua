/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator;

import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.kapua.kura.simulator.payload.Message;
import org.eclipse.kapua.kura.simulator.topic.Topic;
import org.eclipse.kapua.kura.simulator.util.Hex;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A transport implementation based on MQTT
 */
public class MqttAsyncTransport extends AbstractMqttTransport implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(MqttAsyncTransport.class);

    private final MqttAsyncClient client;

    private final MqttConnectOptions connectOptions;

    private Runnable onConnected;

    private Runnable onDisconnected;

    public MqttAsyncTransport(final GatewayConfiguration configuration) throws MqttException {
        super(configuration);

        final MemoryPersistence persistence = new MemoryPersistence();
        final String plainBrokerUrl = plainUrl(configuration.getBrokerUrl());
        this.client = new MqttAsyncClient(plainBrokerUrl, configuration.getClientId(), persistence);
        this.client.setCallback(new MqttCallback() {

            @Override
            public void messageArrived(final String topic, final MqttMessage message) throws Exception {
            }

            @Override
            public void deliveryComplete(final IMqttDeliveryToken token) {
            }

            @Override
            public void connectionLost(final Throwable cause) {
                handleDisconnected();
            }
        });
        this.connectOptions = createConnectOptions(configuration.getBrokerUrl());
    }

    @Override
    public void connect() {
        try {
            this.client.connect(this.connectOptions, null, new IMqttActionListener() {

                @Override
                public void onSuccess(final IMqttToken asyncActionToken) {
                    handleConnected();
                }

                @Override
                public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                    logger.warn("Failed to connect", exception);
                }
            });
        } catch (final MqttException e) {
            logger.warn("Failed to initiate connect", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            this.client.disconnect(null, new IMqttActionListener() {

                @Override
                public void onSuccess(final IMqttToken asyncActionToken) {
                    handleDisconnected();
                }

                @Override
                public void onFailure(final IMqttToken asyncActionToken, final Throwable exception) {
                    logger.warn("Failed to disconnect", exception);
                }
            });
        } catch (final MqttException e) {
            logger.warn("Failed to initiatate disconnect", e);
        }
    }

    @Override
    public void close() throws MqttException {
        try {
            this.client.disconnect(0).waitForCompletion();
        } finally {
            this.client.close();
        }
    }

    @Override
    public void subscribe(final Topic topic, final Consumer<Message> consumer) {
        Objects.requireNonNull(consumer);

        try {
            this.client.subscribe(topic.render(this.topicContext), 0, null, null, new IMqttMessageListener() {

                @Override
                public void messageArrived(final String topic, final MqttMessage mqttMessage) throws Exception {
                    logger.debug("Received MQTT message from {}", topic);
                    consumer.accept(new Message(Topic.fromString(topic), mqttMessage.getPayload(),
                            MqttAsyncTransport.this.topicContext));
                }
            });
        } catch (final MqttException e) {
            if (e.getReasonCode() != MqttException.REASON_CODE_CLIENT_NOT_CONNECTED) {
                logger.warn("Failed to subscribe to: {}", topic, e);
            }
        }
    }

    @Override
    public void unsubscribe(final Topic topic) {
        try {
            this.client.unsubscribe(topic.render(this.topicContext));
        } catch (final MqttException e) {
            if (e.getReasonCode() != MqttException.REASON_CODE_CLIENT_NOT_CONNECTED) {
                logger.warn("Failed to unsubscribe: {}", topic, e);
            }
        }
    }

    @Override
    public void whenConnected(final Runnable runnable) {
        this.onConnected = runnable;
    }

    @Override
    public void whenDisconnected(final Runnable runnable) {
        this.onDisconnected = runnable;
    }

    protected void handleConnected() {
        final Runnable runnable = this.onConnected;
        if (runnable != null) {
            runnable.run();
        }
    }

    protected void handleDisconnected() {
        final Runnable runnable = this.onDisconnected;
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public void sendMessage(final Topic topic, final byte[] payload) {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending message - topic: {}, payload: {}", topic, Hex.toHex(payload, 256));
        }

        try {
            final String fullTopic = topic.render(this.topicContext);
            logger.debug("Full topic: {}", fullTopic);

            this.client.publish(fullTopic, payload, 0, false);
        } catch (final Exception e) {
            logger.warn("Failed to send out message", e);
            throw new RuntimeException(e);
        }
    }

}
