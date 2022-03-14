/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.integration.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@Singleton
public class MqttClientPoolSteps extends TestBase {

    protected static final Logger logger = LoggerFactory.getLogger(MqttClientPoolSteps.class);

    private static final String POOL_DEVICES_NAME_PREFIX = "DEVICES_POOL_";
    private static final String POOL_EXECUTOR_SERVICES_NAME_PREFIX = "EXECUTOR_POOL_";
    private static final String POOL_MQTT_CLIENT_WRAPPER_NAME_PREFIX = "MQTT_CLIENT_WRAPPER_";

    @Inject
    public MqttClientPoolSteps(StepData stepData) {
        super(stepData);
    }

    @When("I prepare {int} client(s) with {string} as client id in a pool called {string} with username {string} password {string} and brokerUrl {string} and if connected disconnect after {int} second(s)")
    public void prepareClientsInPool(int clients, String clientId, String poolName, String username, String password, String brokerUrl, long waitBeforeDisconnect) throws MqttException {
        List<MqttClientCallable> tasks = new ArrayList<>();
        for (int i=0; i<clients; i++) {
            tasks.add(new MqttClientCallable(clientId, username, password, brokerUrl, waitBeforeDisconnect));
        }
        stepData.put(POOL_DEVICES_NAME_PREFIX + poolName, tasks);
    }

    @When("I connect the pool called {string}")
    public void iConnectThePool(String poolName) {
        List<MqttClientCallable> tasks = (List<MqttClientCallable>)stepData.get(POOL_DEVICES_NAME_PREFIX + poolName);
        ScheduledExecutorService es = Executors.newScheduledThreadPool(tasks.size());
        try {
            stepData.put(POOL_EXECUTOR_SERVICES_NAME_PREFIX + poolName, es);
            List<Future<MqttClientWrapper>> result = es.invokeAll(tasks);
            stepData.put(POOL_MQTT_CLIENT_WRAPPER_NAME_PREFIX + poolName, result);
        } catch (InterruptedException e) {
            logger.error("Connecting clients error: {}", e.getMessage());
        }
    }

    @Then("Only 1 client of the pool called {string} is still connected within {int} second(s)")
    public void onlyOneClientConnectedWithinSeconds(String poolName, int timeout) {
        List<Future<MqttClientWrapper>> result = (List<Future<MqttClientWrapper>>) stepData.get(POOL_MQTT_CLIENT_WRAPPER_NAME_PREFIX + poolName);
        AtomicInteger totalClientsChecked = new AtomicInteger();
        AtomicInteger connectedAtStartup = new AtomicInteger();
        AtomicInteger connectionLost = new AtomicInteger();
        AtomicInteger stillConnected = new AtomicInteger();
        result.forEach(mqttFut -> {
            totalClientsChecked.incrementAndGet();
            try {
                MqttClientWrapper mqttClientWrapper = mqttFut.get(timeout, TimeUnit.SECONDS);
                if (mqttClientWrapper.isConnectedAtStartup()) {
                    connectedAtStartup.incrementAndGet();
                }
                if (mqttClientWrapper.isStillConnected()) {
                    stillConnected.incrementAndGet();
                }
                if (mqttClientWrapper.isConnectionLost()) {
                    connectionLost.incrementAndGet();
                }
            } catch (Exception e) {
                logger.warn("Error while checking client status: {}", e.getMessage());
            }
        });
        ScheduledExecutorService es = (ScheduledExecutorService)stepData.get(POOL_EXECUTOR_SERVICES_NAME_PREFIX + poolName);
        es.shutdown();
        stepData.remove(POOL_DEVICES_NAME_PREFIX + poolName);
        stepData.remove(POOL_EXECUTOR_SERVICES_NAME_PREFIX + poolName);
        stepData.remove(POOL_MQTT_CLIENT_WRAPPER_NAME_PREFIX + poolName);
        logger.info("REPORT: {} - {} - {} - {}", totalClientsChecked.get(), connectedAtStartup.get(), connectionLost.get(), stillConnected.get());
        Assert.assertEquals("Bad client size", result.size(), totalClientsChecked.get());
        Assert.assertEquals("Bad client connected at startup", result.size(), connectedAtStartup.get());
        Assert.assertEquals("Bad client connection lost", result.size()-1, connectionLost.get());
        Assert.assertEquals("Bad still connected client size", 1, stillConnected.get());
    }
}

class MqttClientWrapper {

    private String clientId;
    private String username;
    private String password;
    private String brokerUrl;
    private MqttClient client;
    private MqttClientCallBack mqttClientCallBack;
    private MqttConnectOptions connectOptions;
    private long waitBeforeDisconnect;

    private boolean connectedAtStartup;
    private boolean connectionLost;
    private boolean stillConnected;

    public MqttClientWrapper(String clientId, String username, String password, String brokerUrl, long waitBeforeDisconnect) throws MqttException {
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.brokerUrl = brokerUrl;
        this.waitBeforeDisconnect = waitBeforeDisconnect;
        client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
        mqttClientCallBack = new MqttClientCallBack();
        client.setCallback(mqttClientCallBack);
        connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(username);
        connectOptions.setPassword(password.toCharArray());
    }

    public void suscribe(String topic) throws MqttException {
        client.subscribe(topic);
    }

    public void publish(String topic, byte[] data) throws MqttPersistenceException, MqttException {
        client.publish(topic, new MqttMessage(data));
    }

    public void connect() {
        try {
            client.connect(connectOptions);
        }
        catch (MqttException e) {
            connectionLost = true;//e.getReasonCode()==32109;
            MqttClientPoolSteps.logger.info("Connect error: {}", e.getMessage(), e);
        }
        connectedAtStartup = true;
        try {
            if (waitBeforeDisconnect>0) { 
                synchronized (client) {
                    client.wait(waitBeforeDisconnect);
                }
                if (client.isConnected()) {
                    stillConnected = true;
                }
                client.disconnect();
            }
        }
        catch (Exception e) {
            MqttClientPoolSteps.logger.info("Error: {}", e.getMessage());
        }
    }

    public void disconnect() throws MqttException {
        if (client==null) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR);
        }
        try {
            if (client.isConnected()) {
                client.disconnect();
            }
            client.close(true);
        }
        catch (Exception e) {
            MqttClientPoolSteps.logger.warn("Closing client error: {}", e.getMessage());
        }
    }

    public boolean isConnectedAtStartup() {
        return connectedAtStartup;
    }

    public boolean isConnectionLost() {
        return mqttClientCallBack.isConnectionLost() || connectionLost;
    }

    public boolean isStillConnected() {
        return stillConnected;
    }
}

class MqttClientCallBack implements MqttCallback {

    private boolean connectionLost;

    public MqttClientCallBack() {
    }

    @Override
    public void connectionLost(Throwable cause) {
        MqttClientPoolSteps.logger.info("Disconnected: {}", cause.getMessage());
        connectionLost = true;
    }

    public boolean isConnectionLost() {
        return connectionLost;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        MqttClientPoolSteps.logger.info("Received: {} - {}", topic, new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        MqttClientPoolSteps.logger.info("Delivered: {}", token.getTopics()[0]);
    }

}

class MqttClientCallable implements Callable<MqttClientWrapper> {

    private MqttClientWrapper mqttClientWrapper;

    public MqttClientCallable(String clientId, String username, String password, String brokerUrl, long waitBeforeDisconnect) {
        try {
            mqttClientWrapper = new MqttClientWrapper(clientId, username, password, brokerUrl, waitBeforeDisconnect);
        } catch (MqttException e) {
            MqttClientPoolSteps.logger.info("Error: {}", e.getMessage());
        }
    }

    @Override
    public MqttClientWrapper call() {
        mqttClientWrapper.connect();
        return mqttClientWrapper;
    }
}