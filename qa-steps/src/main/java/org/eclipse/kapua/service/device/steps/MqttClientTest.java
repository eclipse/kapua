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

import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Topic listener class that implements implementation of mqtt listener that listens on
 * $EDC topics and records them onto local file system.
 * This files are than used for mocked Kura device responses in integration tests.
 * <p>
 * FIXME Make it friendlier for usage. Suggestions please!
 * FIXME Describe process of recording kapua - kura communication and how to use recrded files.
 */
public class MqttClientTest extends Assert {

    /**
     * Mqtt broker URI.
     */
    private static final String BROKER_URI = "tcp://localhost:1883";

    /**
     * Broker system user-name.
     */
    private static final String BROKER_USER = "kapua-sys";

    /**
     * Broker system user password.
     */
    private static final String BROKER_PASSWORD = "kapua-password";

    /**
     * Kapua default account name.
     */
    private static final String DEFAULT_ACCOUNT = "kapua-sys";

    /**
     * Kapua system topic filter under which all kapua messages reside.
     */
    private static final String EDC_TOPIC_FILTER = "$EDC/#";

    /**
     * Default testing QoS is 0 - fire and forget.
     */
    private static final int DEFAULT_QOS = 0;

    /**
     * Time the test is waiting for messages to record them to file.
     * This is in millis - 2 minutes.
     */
    private static final int WAIT_FOR_MESSAGE_MILLIS = 120_000;

    @Test
    @Ignore
    public void clientTopicListener() throws Exception {

        MqttClient client = new MqttClient(BROKER_URI, MqttClient.generateClientId(),
                new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(BROKER_USER);
        options.setPassword(BROKER_PASSWORD.toCharArray());
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String fileName = topicTranslator(topic);
                writeMessageToFile(fileName, message.getPayload());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

        client.connect(options);
        client.subscribe(EDC_TOPIC_FILTER, DEFAULT_QOS);
        Thread.sleep(WAIT_FOR_MESSAGE_MILLIS);
    }

    /**
     * Write received message to file for later replay.
     *
     * @param fileName file name that is formatted by with topic in name
     * @param payload  byte array of topic data
     * @throws IOException on file being written
     */
    private void writeMessageToFile(String fileName, byte[] payload) throws IOException {

        FileOutputStream fileOs = new FileOutputStream(fileName);

        try {
            fileOs.write(payload);
        } finally {
            fileOs.close();
        }
    }

    /**
     * Restructure topic for use in file name.
     *
     * @param topic MQTT $EDC topic name
     * @return topic name restructured for usage in file name
     */
    private String topicTranslator(String topic) {
        String translated;

        translated = topic.substring(topic.indexOf(DEFAULT_ACCOUNT) + DEFAULT_ACCOUNT.length() + 1);
        translated = translated.replace('/', '_');

        return translated;
    }
}
