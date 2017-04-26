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

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.util.List;

import org.eclipse.kapua.qa.utils.Suppressed;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

public class KuraDevice implements MqttCallback {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(KuraDevice.class);

    /*
     * Topics that Kura device is listening to.
     */
    private static final String DEPLOY_V2_GET_PACKAGES = "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/packages";

    private static final String DEPLOY_V2_GET_BUNDLES = "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/bundles";

    private static final String CONF_V1_GET_CONFIGURATIONS = "$EDC/kapua-sys/rpione3/CONF-V1/GET/configurations";

    private static final String CMD_V1_EXEC = "$EDC/kapua-sys/rpione3/CMD-V1/EXEC/command";

    /*
     * Mqtt Broker configuration.
     */
    /**
     * URI of mqtt broker.
     */
    private static final String BROKER_URI = "tcp://localhost:1883";

    /**
     * Mocked Kura device client id.
     */
    private static final String CLIENT_ID = "rpione3";

    /**
     * User with which Mocked Kura device is connecting to Cloud service.
     */
    private static final String CLIENT_USER = "kapua-broker";

    /**
     * Mocked Kura device password while connecting to Cloud service.
     */
    private static final String CLIENT_PASSWORD = "kapua-password";

    /**
     * User under which Kura device is listening for messages.
     */
    private static final String SERVER_USER = "kapua-sys";

    /**
     * Password for system user under which Kura is listening form messages.
     */
    private static final String SERVER_PASSWORD = "kapua-password";

    /**
     * Account under which Kura device is registered.
     */
    private static final String CLIENT_ACCOUNT = "kapua-sys";

    /**
     * Kapua system topic and everything under it as mqtt filter.
     */
    private static final String TOPIC_FILTER = "$EDC/#";

    /**
     * Default quality of service - mqtt.
     */
    private static final int QOS = 0;

    /**
     * Mqtt client for sending messages form Mocked Kura device.
     */
    private MqttClient mqttClient;

    /**
     * Mqtt client form listening from messages on Mocked Kura device.
     */
    private MqttClient subscribedClient;

    public KuraDevice() {

        mqttClientSetup();
    }

    /**
     * Disconnect Mocked Kura device mqtt clients that listen and send messages
     * to mqtt broker.
     */
    public void mqttClientDisconnect() {
        try {
            try (final Suppressed<Exception> s = Suppressed.withException()) {
                s.run(mqttClient::disconnect);
                s.run(subscribedClient::disconnect);
                s.run(mqttClient::close);
                s.run(subscribedClient::close);
            }
        } catch (final Exception e) {
            logger.warn("Failed during cleanup of Paho resources", e);
        }
    }

    /**
     * Connect both listening and sending mqtt client of Mocked Kura device.
     */
    public void mqttClientConnect() {

        MqttConnectOptions clientOpts = new MqttConnectOptions();
        clientOpts.setUserName(CLIENT_USER);
        clientOpts.setPassword(CLIENT_PASSWORD.toCharArray());
        MqttConnectOptions serverOpts = new MqttConnectOptions();
        serverOpts.setUserName(SERVER_USER);
        serverOpts.setPassword(SERVER_PASSWORD.toCharArray());
        try {
            mqttClient.connect(clientOpts);
            subscribedClient.connect(serverOpts);
            subscribedClient.subscribe(TOPIC_FILTER, QOS);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepare client and server part of mocked mqtt.
     */
    private void mqttClientSetup() {
        /*
         * mqttClient is meant to simulate Kura device for sending messages,
         * while subscribedClient is meant to receive messages from Kura device side.
         */
        try {
            mqttClient = new MqttClient(BROKER_URI, CLIENT_ID,
                    new MemoryPersistence());
            subscribedClient = new MqttClient(BROKER_URI, MqttClient.generateClientId(),
                    new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }

        subscribedClient.setCallback(this);
    }

    /**
     * Sending data to mqtt broker. Data is read form file containing pre-recorded response.
     *
     * @param topic
     *            mqtt broker topic
     * @param qos
     *            mqtt QOS
     * @param retained
     *            is message retained (mqtt specific)
     * @param fileName
     *            name of file and path with pre-recorded response
     * @throws MqttException
     * @throws IOException
     */
    public void sendMessageFromFile(String topic, int qos, boolean retained, String fileName) throws MqttException, IOException {
        byte[] payload = readAllBytes(get(fileName));

        mqttClient.publish(topic, payload, qos, retained);
    }

    /**
     * Extraction of metrics form Kapua message payload.
     *
     * @param payload
     *            payload received from Kapua
     * @param metricKey
     *            string representing key of metric
     * @return string representation of metric value
     */
    private String getMetric(byte[] payload, String metricKey) {

        String value = null;
        KuraPayloadProto.KuraPayload kuraPayload = null;
        try {
            kuraPayload = KuraPayloadProto.KuraPayload.parseFrom(payload);
        } catch (InvalidProtocolBufferException e) {
            value = null;
        }
        if (kuraPayload == null) {
            return value;
        }

        List<KuraPayloadProto.KuraPayload.KuraMetric> metrics = kuraPayload.getMetricList();
        for (KuraPayloadProto.KuraPayload.KuraMetric metric : metrics) {
            String name = metric.getName();
            if (name.equals(metricKey)) {
                value = metric.getStringValue();
            }
        }

        return value;
    }

    /**
     * Ectraction of callback parameters form Kapua generated message stored as Metrics.
     *
     * @param payload
     *            Kapua message
     * @return tuple with client and request id
     */
    private CallbackParam extractCallback(byte[] payload) {

        CallbackParam callbackParam = new CallbackParam();
        String clientId = getMetric(payload, "requester.client.id");
        String requestId = getMetric(payload, "request.id");
        callbackParam.setClientId(clientId);
        callbackParam.setRequestId(requestId);

        return callbackParam;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Kapua Mock Device connection to broker lost.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.info("Message arrived in Kapua Mock Device with topic: " + topic);

        CallbackParam callbackParam = null;
        String responseTopic = null;
        byte[] responsePayload = null;
        byte[] payload = mqttMessage.getPayload();

        switch (topic) {
        case DEPLOY_V2_GET_PACKAGES:
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            responsePayload = readAllBytes(get(("src/test/resources/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_packages.mqtt")));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
            break;
        case DEPLOY_V2_GET_BUNDLES:
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            responsePayload = readAllBytes(get(("src/test/resources/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_bundles.mqtt")));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
            break;
        case CONF_V1_GET_CONFIGURATIONS:
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CONF-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = readAllBytes(get(("src/test/resources/mqtt/KapuaPool-client-id_CONF-V1_REPLY_req-id_configurations.mqtt")));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
            break;
        case CMD_V1_EXEC:
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CMD-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = readAllBytes(get(("src/test/resources/mqtt/KapuaPool-client-id_CMD-V1_REPLY_req-id_command.mqtt")));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
            break;
        default:
            logger.error("Kapua Mock Device unhandled topic: " + topic);
            break;
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("Kapua Mock Device message delivery complete.");
    }

    /**
     * Simple tuple for callback parameters.
     */
    private class CallbackParam {

        private String clientId;

        private String requestId;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }
}
