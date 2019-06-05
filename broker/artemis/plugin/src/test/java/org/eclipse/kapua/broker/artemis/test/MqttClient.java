/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.test;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClient implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(MqttClient.class);

    private org.eclipse.paho.client.mqttv3.MqttClient mqttClient;

    private MemoryPersistence persistence;

    private MqttConnectOptions connOpts = new MqttConnectOptions();

    private String broker;

    private int recivedMsgCnt;

    public MqttClient(String broker, int port, String clientId, String user, String pass) throws MqttException {
        this.broker = "ssl://" + broker + ":" + port;
        persistence = new MemoryPersistence();
        mqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(this.broker, clientId, persistence);
        connOpts.setCleanSession(true);
        connOpts.setUserName(user);
        connOpts.setPassword(pass.toCharArray());
    }

    public void connect() throws MqttException {
        logger.info("Connecting to broker: " + broker);
        mqttClient.setCallback(this);
        mqttClient.connect(connOpts);
        logger.info("Connected");
    }

    public void connect(MqttConnectOptions connOpts) throws MqttException {
        this.connOpts = connOpts;
        connect();
    }

    public void disconnect() throws MqttException {
        mqttClient.disconnect(2000);
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public void subscribe(String topic, int qos) throws MqttException {
        logger.info("Subscribing to topic: " + topic);
        mqttClient.subscribe(topic, qos);
        logger.info("Subscribed to topic: " + topic);
    }

    public void publish(String topic, int qos, byte[] content) throws MqttException {
        logger.info("Publishing message on topic: " + topic + " - " + content);
        MqttMessage message = new MqttMessage(content);
        message.setQos(qos);
        mqttClient.publish(topic, message);
        logger.info("Message on topic " + topic + " published.");
    }

    public void publish(String topic, int qos, String content) throws MqttException {
        publish(topic, qos, content.getBytes());
    }

    public void resetMsgCnt() {
        recivedMsgCnt = 0;
    }

    public int getRecivedMsgCnt() {
        return recivedMsgCnt;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Mqtt subscription connection lost.");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        logger.info("Mqtt message arrived: {} - {}", s, new String(mqttMessage.getPayload()));
        recivedMsgCnt++;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("Mqtt delivery complete");
    }

}
