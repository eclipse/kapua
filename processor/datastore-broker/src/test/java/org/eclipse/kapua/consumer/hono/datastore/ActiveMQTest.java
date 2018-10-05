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
package org.eclipse.kapua.consumer.hono.datastore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQTest {

    protected final static Logger logger = LoggerFactory.getLogger(ActiveMQTest.class);

    private final static String SERVER_URL = "tcp://192.168.33.10:1883";
    private final static String CLIENT_ID = "client-1";
    private final static String TOPIC_SUB = "kapua-sys/" + CLIENT_ID + "/#";
    private final static String TOPIC_PUB = "kapua-sys/" + CLIENT_ID + "/data/topic1";

    @Test
    @Ignore
    public void test() throws MqttException, InterruptedException {
        MqttClient client = new MqttClient(SERVER_URL, CLIENT_ID);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("kapua-sys");
        options.setPassword("kapua-password".toCharArray());
        client.setCallback(new MqttCallback());
        client.connect(options);
        client.subscribe(TOPIC_SUB);
        for(int i=0; i<100; i++) {
            client.publish(TOPIC_PUB, getMqttMessage());
            Thread.sleep(1000);
        }
    }

    private MqttMessage getMqttMessage() {
        MqttMessage message = new MqttMessage();
        KuraDataMessage kdm = new KuraDataMessage();
        KuraDataChannel kdc = new KuraDataChannel();
        kdc.setClientId(CLIENT_ID);
        kdc.setScope("kapua-sys");
        List<String> channelPart = new ArrayList<>();
        channelPart.add("data");
        channelPart.add("data1");
        kdc.setSemanticChannelParts(channelPart);
        kdm.setChannel(kdc);
        KuraDataPayload kdp = new KuraDataPayload();
        kdp.getMetrics().put("metric1", new Integer(1));
        kdp.setBody("this is the body!".getBytes());
        kdm.setPayload(kdp);
        message.setPayload(kdm.getPayload().toByteArray());
        return message;
    }

    private class MqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

        @Override
        public void connectionLost(Throwable t) {
            logger.info("Connection lost: {}", t.getMessage(), t);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            logger.info("Messsage delivered!");
        }

        @Override
        public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
            logger.info("Message received: ", arg1.toString());
        }

    }
}
