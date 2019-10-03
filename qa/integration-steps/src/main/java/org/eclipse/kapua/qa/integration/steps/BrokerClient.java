/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.integration.steps;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrokerClient implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(BrokerClient.class);

    private MqttClient mqttClient;

    private MemoryPersistence persistence;

    private MqttConnectOptions connOpts = new MqttConnectOptions();

    private String broker;

    private int recivedMsgCnt;

    public BrokerClient(String broker, int port, String clientId, String user, String pass) throws MqttException {

        this.broker = "tcp://" + broker + ":" + port;
        persistence = new MemoryPersistence();
        mqttClient = new MqttClient(this.broker, clientId, persistence);
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

    public void disconnect() throws MqttException {

        mqttClient.disconnect(2000);
    }

    public void subscribe(String topic, int qos) throws MqttException {
        logger.info("Subscribing to topic: " + topic);
        mqttClient.subscribe(topic, qos);
        logger.info("Subscribed to topic: " + topic);
    }

    public void publish(String topic, int qos, String content) throws MqttException {

        logger.info("Publishing message: " + content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        mqttClient.publish(topic, message);
        logger.info("Message published.");
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
        logger.info("Mqtt message arrived.");
        recivedMsgCnt++;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("Mqtt delivery complete");
    }

}
