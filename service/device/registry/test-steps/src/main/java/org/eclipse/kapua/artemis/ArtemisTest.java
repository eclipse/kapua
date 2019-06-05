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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.artemis;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtemisTest {

    private static final Logger logger = LoggerFactory.getLogger(ArtemisTest.class);

    private static final long SLEEP_TIME = 500;
    private static final String SERVER_URL = "tcp://192.168.33.10:1883";
    private static final String SERVER_URL_2 = "tcp://192.168.33.10:1884";
    private static final String CLIENT_ID = "client-id";
    private static final String CLIENT_ID_2 = "client-id-2";
    private static final String CLIENT_ID_3 = "client-id-3";
    private static final String CLIENT_ID_1884 = "client-id-1884";

    public ArtemisTest() {
    }

//    @Test
//    public void testBasicConnectivity() throws Exception {
//        for(int i=0; i<20; i++) {
//            doTestInternal();
//        }
//    }

    @Test
    public void testBasicConnectivityLoop() throws Exception {
        for(int i=0; i<20; i++) {
            logger.info("###################################");
            logger.info("ITERATION {}", i);
            logger.info("###################################");
            testBasicConnectivity();
        }
    }

    @Test
    public void testAcceptors() throws Exception {
        MqttClient client = getClient(SERVER_URL, CLIENT_ID);
        MqttConnectOptions options = getOptions("user", "password");
        client.connect(options);
        Thread.sleep(SLEEP_TIME);
        client.publish("ACCEPTOR.ADD.brokerNetwork", new MqttMessage());
        Thread.sleep(SLEEP_TIME);
        client.publish("CONNECTOR.ADD.brokerNetwork", new MqttMessage());
    }

    @Test
    public void testBasicConnectivity() throws Exception {
        MqttClient client = getClient(SERVER_URL, CLIENT_ID);
        MqttConnectOptions options = getOptions("user", "password");
        client.connect(options);
        MqttClient client2 = getClient(SERVER_URL, CLIENT_ID_2);
        MqttConnectOptions options2 = getOptions("user-2", "password");
        client2.connect(options2);
        MqttClient client3 = getClient(SERVER_URL, CLIENT_ID_3);
        MqttConnectOptions options3 = getOptions("user-3", "password");
        client3.connect(options3);
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client2 should be connected!", client2.isConnected());
        Assert.assertTrue("Client3 should be connected!", client3.isConnected());
        client.publish("DISCONNECT/ACCOUNT/" + CLIENT_ID_2, new MqttMessage());
        client.publish("DISCONNECT/ACCOUNT/" + CLIENT_ID_3, new MqttMessage());
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client2 should be disconnected!", !client2.isConnected());
        Assert.assertTrue("Client3 should be disconnected!", !client3.isConnected());
        client2.connect(options2);
        client3.connect(options3);
        client.disconnect();
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client2 should be connected!", client2.isConnected());
        Assert.assertTrue("Client3 should be connected!", client3.isConnected());
        for(int i=0; i<1; i++) {
            doTestInternal();
            Assert.assertTrue("Client2 should be connected!", client2.isConnected());
            Assert.assertTrue("Client3 should be connected!", client3.isConnected());
        }
        client2.publish("ACCEPTOR.ADD.mqtt2", new MqttMessage());
        Thread.sleep(SLEEP_TIME);
        MqttClient client1884 = getClient(SERVER_URL_2, CLIENT_ID_1884);
        MqttConnectOptions options1884 = getOptions("user1884", "password1884");
        client1884.connect(options1884);
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client should be connected!", client1884.isConnected());
        client2.publish("ACCEPTOR.REMOVE.mqtt2", new MqttMessage());
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client should be disconnected!", !client1884.isConnected());
        client2.disconnect();
        client3.disconnect();
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client2 should be disconnected!", !client2.isConnected());
        Assert.assertTrue("Client3 should be disconnected!", !client3.isConnected());
    }

    private void doTestInternal() throws Exception {
        MqttClient client = getClient(SERVER_URL, CLIENT_ID);
        MqttClient client1 = getClient(SERVER_URL, CLIENT_ID);
        MqttConnectOptions options = getOptions("user", "password");
        client.connect(options);
        client.subscribe("#");
        client.publish("testTopic", new MqttMessage());
        Assert.assertTrue("Client should be connected!", client.isConnected());
        client.disconnect();
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client should be disconnected!", !client.isConnected());

        client.connect(options);
        client1.connect(getOptions("user-1", "password2"));
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client should be disconnected!", !client.isConnected());
        Assert.assertTrue("Client1 should be connected!", client1.isConnected());
        client1.disconnect();
        Thread.sleep(SLEEP_TIME);
        Assert.assertTrue("Client should be disconnected!", !client.isConnected());
        Assert.assertTrue("Client1 should be disconnected!", !client1.isConnected());
    }

    private static MqttClient getClient(String serverUrl, String clientId) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient client = new MqttClient(serverUrl, clientId, persistence);
        client.setCallback(new MqttCallback(clientId));
        return client;
    }

    private static MqttConnectOptions getOptions(String username, String password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        return options;
    }

}