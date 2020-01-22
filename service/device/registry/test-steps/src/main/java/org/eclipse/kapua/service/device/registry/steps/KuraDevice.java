/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.qa.common.Suppressed;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
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

    /**
     * Topics that Kura device is listening to.
     */
    private String deployPackages;
    private String deployV2ExecDownloadPackage;
    private String uninstallPackage;
    private String deployBundles;
    private String deployConf;
    private String putConf;
    private String cmdExec;
    private String deployV2ExecStart34;
    private String deployV2ExecStart95;
    private String deployV2ExecStop77;
    private String readAssets;
    private String writeAsset;

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
    private String clientId;

    public boolean bundleStateChanged;
    public boolean configurationChanged;
    public boolean packageListChanged;
    public boolean packageListChangedAfterUninstall;
    public boolean assetStateChanged;

    public KuraDevice() {
        deployPackages = "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/packages";
        deployV2ExecDownloadPackage = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/download";
        uninstallPackage = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/uninstall";
        deployBundles = "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/bundles";
        deployConf = "$EDC/kapua-sys/rpione3/CONF-V1/GET/configurations";
        putConf = "$EDC/kapua-sys/rpione3/CONF-V1/PUT/configurations";
        cmdExec = "$EDC/kapua-sys/rpione3/CMD-V1/EXEC/command";
        deployV2ExecStart34 = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/start/34";
        deployV2ExecStart95 = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/start/95";
        deployV2ExecStop77 = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/stop/77";

        readAssets = "$EDC/kapua-sys/rpione3/ASSET-V1/EXEC/read";

        writeAsset = "$EDC/kapua-sys/rpione3/ASSET-V1/EXEC/write";

        clientId = "rpione3";

        mqttClientSetup();
    }

    public String getClientId() {
        return this.clientId;
    }

    public void addMoreThanOneDeviceToKuraMock(String name){
        clientId = name;
        mqttClientSetupForMoreDevices();
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
    public void mqttClientSetup() {
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

    private void mqttClientSetupForMoreDevices() {
        /*
         * mqttClient is meant to simulate Kura device for sending messages,
         * while subscribedClient is meant to receive messages from Kura device side.
         */
        try {
            deployPackages = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/GET/packages";
            deployV2ExecDownloadPackage = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/EXEC/download";
            uninstallPackage = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/EXEC/uninstall";
            deployBundles = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/GET/bundles";
            deployConf = "$EDC/kapua-sys/" + clientId + "/CONF-V1/GET/configurations";
            putConf = "$EDC/kapua-sys/" + clientId + "/CONF-V1/PUT/configurations";
            cmdExec = "$EDC/kapua-sys/" + clientId + "/CMD-V1/EXEC/command";
            deployV2ExecStart34 = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/EXEC/start/34";
            deployV2ExecStart95 = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/EXEC/start/95";
            deployV2ExecStop77 = "$EDC/kapua-sys/" + clientId + "/DEPLOY-V2/EXEC/stop/77";

            readAssets = "$EDC/kapua-sys/" + clientId + "/ASSET-V1/EXEC/read";

            writeAsset = "$EDC/kapua-sys/" + clientId + "/ASSET-V1/EXEC/write";

            mqttClient = new MqttClient(BROKER_URI, clientId,
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
    public void sendMessageFromFile(String topic, int qos, boolean retained, String fileName) throws MqttException, IOException, URISyntaxException {
        byte[] payload = Files.readAllBytes(Paths.get(getClass().getResource(fileName).toURI()));

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

        if (topic.equals(deployPackages)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(packageListChanged == true ? "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_packages_updated_list.mqtt" : (packageListChangedAfterUninstall == true ? "/mqtt/KapuaPoolClient-id_DEPLOY_V2_REPLY_package_list_after_uninstall.mqtt" : "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_packages_initial_list.mqtt")).toURI()));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(deployV2ExecDownloadPackage)) {
            callbackParam = extractCallback(payload);
            KuraPayload kuraPayloadInitial = new KuraPayload();
            kuraPayloadInitial.readFromByteArray(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            KuraPayload customKuraPayload1 = new KuraPayload();
            customKuraPayload1.setTimestamp(new Date());
            customKuraPayload1.getMetrics().put("response.code", 200);
            responsePayload = customKuraPayload1.toByteArray();
            mqttClient.publish(responseTopic, responsePayload, 0, false);
            Thread.sleep(100);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/NOTIFY/" + clientId + "/download";
            KuraPayload customKuraPayload2 = new KuraPayload();
            customKuraPayload2.setTimestamp(new Date());
            customKuraPayload2.getMetrics().put("job.id", kuraPayloadInitial.getMetrics().get("job.id"));
            customKuraPayload2.getMetrics().put("client.id", clientId);
            customKuraPayload2.getMetrics().put("dp.download.progress", 50);
            customKuraPayload2.getMetrics().put("dp.download.size", 20409);
            customKuraPayload2.getMetrics().put("dp.download.status", "IN_PROGRESS");
            customKuraPayload2.getMetrics().put("dp.download.index", 0);
            responsePayload = customKuraPayload2.toByteArray();
            mqttClient.publish(responseTopic, responsePayload , 0, false);
            Thread.sleep(100);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/NOTIFY/" + clientId + "/download";
            KuraPayload customKuraPayload3 = new KuraPayload();
            customKuraPayload3.setTimestamp(new Date());
            customKuraPayload3.getMetrics().put("job.id", kuraPayloadInitial.getMetrics().get("job.id"));
            customKuraPayload3.getMetrics().put("client.id", clientId);
            customKuraPayload3.getMetrics().put("dp.download.progress", 100);
            customKuraPayload3.getMetrics().put("dp.download.size", 20409);
            customKuraPayload3.getMetrics().put("dp.download.status", "COMPLETED");
            customKuraPayload3.getMetrics().put("dp.download.index", 0);
            responsePayload = customKuraPayload3.toByteArray();
            mqttClient.publish(responseTopic, responsePayload, 0, false);
            Thread.sleep(100);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/NOTIFY/" + clientId + "/install";
            KuraPayload customKuraPayload4 = new KuraPayload();
            customKuraPayload4.setTimestamp(new Date());
            customKuraPayload4.getMetrics().put("dp.name", "Example Publisher-1.0.300.dp");
            customKuraPayload4.getMetrics().put("job.id", kuraPayloadInitial.getMetrics().get("job.id"));
            customKuraPayload4.getMetrics().put("dp.install.progress", 100);
            customKuraPayload4.getMetrics().put("dp.install.status", "COMPLETED");
            customKuraPayload4.getMetrics().put("client.id", clientId);
            responsePayload = customKuraPayload4.toByteArray();
            mqttClient.publish(responseTopic, responsePayload, 0, false);

            packageListChanged = true;
        } else if (topic.equals(uninstallPackage)) {
            callbackParam = extractCallback(payload);
            KuraPayload kuraPayloadInitial = new KuraPayload();
            kuraPayloadInitial.readFromByteArray(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            KuraPayload customKuraPayload = new KuraPayload();

            customKuraPayload.setTimestamp(new Date());
            customKuraPayload.getMetrics().put("response.code", 200);
            responsePayload = customKuraPayload.toByteArray();
            mqttClient.publish(responseTopic, responsePayload, 0, false);
            Thread.sleep(5000);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/NOTIFY/" + clientId + "/uninstall";
            KuraPayload customKuraPayload2 = new KuraPayload();

            customKuraPayload2.setTimestamp(new Date());
            customKuraPayload2.getMetrics().put("job.id", kuraPayloadInitial.getMetrics().get("job.id"));
            customKuraPayload2.getMetrics().put("dp.name", "org.eclipse.kura.example.beacon");
            customKuraPayload2.getMetrics().put("dp.uninstall.progress", 100);
            customKuraPayload2.getMetrics().put("dp.uninstall.status", "COMPLETED");
            customKuraPayload2.getMetrics().put("client.id", clientId);
            responsePayload = customKuraPayload2.toByteArray();
            mqttClient.publish(responseTopic, responsePayload , 0, false);

            packageListChangedAfterUninstall = true;
        } else if (topic.equals(deployBundles)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(bundleStateChanged == true ? "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_bundles_updated_state.mqtt" : "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_bundles_inital_state.mqtt").toURI()));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(deployConf)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CONF-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource( configurationChanged == true ? "/mqtt/KapuaPool-client-id_CONF-V1_REPLY_req-id_updated_configurations.mqtt" : "/mqtt/KapuaPool-client-id_CONF-V1_REPLY_req-id_inital_configurations.mqtt").toURI()));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
        }
        else if (topic.equals(putConf)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CONF-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_CONF-V1_PUT_configurations.mqtt").toURI()));

            configurationChanged = true;
            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(cmdExec)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CMD-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_CMD-V1_REPLY_req-id_command.mqtt").toURI()));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(deployV2ExecStart34) || topic.equals(deployV2ExecStart95)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_DEPLOY-V2_EXEC_START_bundle_id.mqtt").toURI()));

            bundleStateChanged = true;
            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(deployV2ExecStop77)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/DEPLOY-V2/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_DEPLOY-V2_EXEC_STOP_bundle_id.mqtt").toURI()));

            bundleStateChanged = true;
            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(readAssets)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/ASSET-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(assetStateChanged == true ? "/mqtt/KapuaPool-client-id_ASSET-V1_READ_req-id_assets_updated_assets.mqtt" : "/mqtt/KapuaPool-client-id_ASSET-V1_READ_req-id_assets.mqtt").toURI()));

            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else if (topic.equals(writeAsset)) {
            callbackParam = extractCallback(payload);

            responseTopic = "$EDC/" + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/ASSET-V1/REPLY/" + callbackParam.getRequestId();
            responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_ASSET-V1_WRITE_req-id_assets.mqtt").toURI()));

            assetStateChanged = true;
            mqttClient.publish(responseTopic, responsePayload, 0, false);
        } else {
            logger.error("Kapua Mock Device unhandled topic: " + topic);
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
