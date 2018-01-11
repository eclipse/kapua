/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import org.eclipse.kapua.qa.utils.Suppressed;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Device that connects to MQTT broker and listens for messages as kapua-sys user
 * and allows another client to connect and publish and subscribe to messages.
 * <p>
 * Used in Cucumber for writing Gherkin scenarios for broker service.
 */
public class MqttDevice {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MqttDevice.class);

    /**
     * URI of mqtt broker.
     */
    private static final String BROKER_URI = "tcp://localhost:1883";

    /**
     * Listening mqtt client name.
     */
    private static final java.lang.String LISTENER_NAME = "ListenerClient";

    /**
     * System user under which Device is listening for messages.
     */
    private static final String SYS_USER = "kapua-sys";

    /**
     * System user password while connecting to broker.
     */
    private static final String SYS_PASSWORD = "kapua-password";

    /**
     * Default quality of service - mqtt.
     */
    // TODO switch to qos 1????
    private static final int DEFAULT_QOS = 0;

    /**
     * Default retain flag is false.
     */
    private static final boolean DEFAULT_RETAIN = false;

    /**
     * No filter on topic.
     */
    private static final String NO_TOPIC_FILTER = "#";

    /**
     * Map of Mqtt client for sending messages.
     */
    private Map<String, MqttClient> mqttClients;

    /**
     * Mqtt client for listening from messages.
     */
    private MqttClient subscribedClient;

    /**
     * Map for storing received messages that clients are listening to.
     * It is Map of Maps, first key is clientId. Key in second map is
     * topic on which message was received.
     */
    private Map<String, Map<String, String>> clientReceivedMqttMessage;

    /**
     * Map for storing received messages that Listener is listening to.
     */
    private Map<String, String> listenerReceivedMqttMessage;

    public MqttDevice() {

        mqttClients = new HashMap<>();
    }

    /**
     * Connect subscriber to mqtt broker.
     */
    public void mqttSubscriberConnect() {

        MqttConnectOptions subscriberOpts = new MqttConnectOptions();
        subscriberOpts.setUserName(SYS_USER);
        subscriberOpts.setPassword(SYS_PASSWORD.toCharArray());
        try {
            subscribedClient = new MqttClient(BROKER_URI, LISTENER_NAME,
                    new MemoryPersistence());
            subscribedClient.connect(subscriberOpts);
            subscribedClient.subscribe(NO_TOPIC_FILTER, DEFAULT_QOS);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        subscribedClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.info("Listener connection to broker lost. {}", throwable.getMessage(), throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                logger.info("Message arrived in Listener with topic: {}", topic);
                // exclude the connect messages sent by the broker (that may affect the tests)
                // this messages can be received by this callback before the listenerReceivedMqttMessage is properly initialized. So a check for null should be performed
                // TODO manage this client in a better way, so the list of the received messages should be internal and exposed as getter to the caller.
                if (listenerReceivedMqttMessage != null) {
                    if (!topic.contains("MQTT/CONNECT")) {
                        listenerReceivedMqttMessage.clear();
                        listenerReceivedMqttMessage.put(topic, new String(mqttMessage.getPayload()));
                    } else {
                        logger.info("Received CONNECT message. The message will be discarded!");
                    }
                } else {
                    logger.info("Received message map is null. The message is not stored!");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                logger.info("Listener message delivery complete.");
            }
        });
    }

    /**
     * Disconnect Device mqtt subscriber that listens on mqtt broker.
     */
    public void mqttSubscriberDisconnect() {

        try {
            try (final Suppressed<Exception> s = Suppressed.withException()) {
                s.run(subscribedClient::disconnect);
                s.run(subscribedClient::close);
            }
        } catch (final Exception e) {
            logger.warn("Failed during cleanup of subscriber Paho resources", e);
        }
    }


    /**
     * Connect mqtt client that sends and listens to messages.
     *
     * @param clientId    mqtt client identifier
     * @param userName    user with which client connects to broker
     * @param password    password for connection
     * @param topicFilter filter for topics client is listening to
     * @throws MqttException
     */
    public void mqttClientConnect(String clientId, String userName, String password, String topicFilter)
            throws MqttException {

        MqttConnectOptions clientOpts = new MqttConnectOptions();
        clientOpts.setUserName(userName);
        clientOpts.setPassword(password.toCharArray());
        MqttClient mqttClient = null;
        mqttClient = new MqttClient(BROKER_URI, clientId,
                new MemoryPersistence());
        mqttClient.connect(clientOpts);
        if ((topicFilter != null) && (topicFilter.length() > 0)) {
            mqttClient.subscribe(topicFilter, DEFAULT_QOS);
        }

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.info("Client connection to broker lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                logger.info("Message arrived in client with topic: " + topic);

                clientReceivedMqttMessage.clear();
                Map<String, String> topicPayload = new HashMap<>();
                topicPayload.put(topic, new String(mqttMessage.getPayload()));
                clientReceivedMqttMessage.put(clientId, topicPayload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                logger.info("Client message delivery complete.");
            }
        });

        mqttClients.put(clientId, mqttClient);
    }

    /**
     * Disconnect Device mqtt client that listens and sends messages to mqtt broker.
     */
    public void mqttClientsDisconnect() {

        for (Map.Entry<String, MqttClient> mqttClient : mqttClients.entrySet()) {
            try {
                try (final Suppressed<Exception> s = Suppressed.withException()) {
                    s.run(mqttClient.getValue()::disconnect);
                    s.run(mqttClient.getValue()::close);
                }
            } catch (final Exception e) {
                logger.warn("Failed during cleanup of client Paho resources", e);
            }
        }
    }

    /**
     * Send string message to topic, also provide map where message received
     * from broker is stored.
     *
     * @param clientId    id of client with which message is sent.
     * @param payload     simple string payload
     * @param topic       topic described as string e.g. /foo/bar
     * @param clientMqttMessage object where client received message is stored
     * @param listenerMqttMessage object where listener received message is stored
     */
    public void mqttClientPublishString(String clientId, String payload, String topic,
                                        Map<String, Map<String, String>> clientMqttMessage,
                                        Map<String, String> listenerMqttMessage) {

        this.clientReceivedMqttMessage = clientMqttMessage;
        this.listenerReceivedMqttMessage = listenerMqttMessage;
        MqttClient mqttClient = mqttClients.get(clientId);
        try {
            mqttClient.publish(topic, payload.getBytes(), DEFAULT_QOS, DEFAULT_RETAIN);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
