/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPosition;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class KapuaStartCluster {

    private static final int MAX_LOOP = 20;

    private KapuaStartCluster() {
    }

    public static void main(String argv[]) throws Exception {
        DockerSteps ds = new DockerSteps(null);
        ds.startFullDockerEnvironment();
        String adminClientId = "admin-test-client";
        MqttClient adminClient = connect(adminClientId, "kapua-sys", "kapua-password");
        String clientId = "test-client";
        MqttClient client = connect(clientId, "kapua-broker", "kapua-password");
        int loopCount = 0;
        while(loopCount++<MAX_LOOP) {
            sendMessageBirth(client, "kapua-sys", 0);
            sendMessage(client, "kapua-sys" + "/" + clientId + "/topic", 0);
            Thread.sleep(1000);
            sendMessageBirth(adminClient, "kapua-sys", 0);
            sendMessage(adminClient, "kapua-sys" + "/" + adminClientId + "/topic", 0);
        }
    }

    protected static MqttClient connect(String clientId, String username, String password) throws MqttException {
        MqttClient mqttClient = new MqttClient("tcp://localhost:1883", clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        mqttClient.connect(options);
        return mqttClient;
    }

    protected static void sendMessageBirth(MqttClient mqttClient, String accountName, int waitFor) throws MqttPersistenceException, MqttException, InterruptedException {
        int qos = 1;
        boolean retained = false;
        String topicStr = "$EDC/" + accountName + "/" + mqttClient.getClientId() + "/MQTT/BIRTH";
        MqttTopic topic = new MqttTopic(topicStr);
        KuraPayload kuraPayload = new KuraPayload();
        kuraPayload.setTimestamp(new Date());
        MqttPayload payload = new MqttPayload(kuraPayload.toByteArray());
        MqttMessage mqttMessage = new MqttMessage(topic, new Date(), payload);
        mqttClient.publish(topicStr, mqttMessage.getPayload().getBody(), qos, retained);
    }

    protected static void sendMessage(MqttClient mqttClient, String topic, int waitFor) throws MqttPersistenceException, MqttException, InterruptedException {
        int qos = 1;
        boolean retained = false;
        byte[] mqttPayload = getByteArray("meggase body".getBytes(), new Date(), getMetrics(), getPosition(45.234, -7.3456, 1.0, 5.4, 0.1, 23.5, new Date(), 3, 2));
        mqttClient.publish(topic, mqttPayload, qos, retained);
    }

    private static Map<String, Object> getMetrics() {
        return getMetrics(new Object[][] {new Object[] {"metric int", 123}, new Object[] {"metric boolean", true}, new Object[] {"metric string", "text"}});
    }

    protected static byte[] getByteArray(byte[] body, Date date, Map<String, Object> metrics, KuraPosition position) {
        KuraPayload kuraPayload = new KuraPayload();
        kuraPayload.setBody(body);
        kuraPayload.setTimestamp(date);
        kuraPayload.setMetrics(metrics);
        kuraPayload.setPosition(position);
        return kuraPayload.toByteArray();
    }

    protected static Map<String, Object> getMetrics(Object[][] metrics) {
        Map<String, Object> metricsMap = new HashMap<>();
        for (Object[] obj : metrics) {
            metricsMap.put((String)obj[0], obj[1]);
        }
        return metricsMap;
    }

    protected static KuraPosition getPosition(Double latitude, Double longitude, Double altitude, Double heading, Double precision, Double speed, Date timestamp, Integer satellites, Integer status) {
        KuraPosition position = new KuraPosition();
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setAltitude(altitude);
        position.setHeading(heading);
        position.setPrecision(precision);
        position.setSpeed(speed);
        position.setTimestamp(timestamp);
        position.setSatellites(satellites);
        position.setStatus(status);
        return position;
    }
}
