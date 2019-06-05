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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectTest {

    protected static Logger logger = LoggerFactory.getLogger(ConnectTest.class);

    private ConnectTest() {
    }

    public static void main(String argv[]) throws Exception {
        ConnectTest connectTest = new ConnectTest();
//        connectTest.testBase();
        connectTest.testStealingLink();
//        connectTest.testLifecycleAndTelemetry();
    }

    private void testBase() throws MqttException, InterruptedException {
        step("kapua-broker", "kapua-password", "client", new String[] {"kapua-sys/client/topic1", "$EDC/kapua-sys/client/MQTT/BIRTH"}, 0);
        step("kapua-broker", "kapua-password", "client", new String[] {"topic1/topic2", "kapua-sys/client"}, 1);
        step("kapua-broker", "kapua-password", "client", new String[] {"topic1/topic2", "kapua-sys/client"}, 2);
        step("kapua-broker", "kapua-password-wrong", "client", new String[] {"topic1/topic2", "kapua-sys/client"}, 0);
        step("kapua-broker-wrong", "kapua-password", "client", new String[] {"topic1/topic2", "kapua-sys/client"}, 0);
        Thread.sleep(3600);
    }

    private void testStealingLink() throws MqttException, InterruptedException {
        MqttClient[] mqttClient = new MqttClient[3];
        boolean[] one = new boolean[] {true, false, false};
        boolean[] two = new boolean[] {false, true, false};
        boolean[] three = new boolean[] {false, false, true};
        int iterations = 3;
        while(iterations-->0) {
            mqttClient[0] = connect("kapua-broker", "kapua-password", "client");
            checkConnection(mqttClient, one);
            mqttClient[1] = connect("kapua-broker", "kapua-password", "client");
            checkConnection(mqttClient, two);
            mqttClient[2] = connect("kapua-broker", "kapua-password", "client");
            checkConnection(mqttClient, three);
        }
        for (int i=0; i<mqttClient.length; i++) {
            if (mqttClient[i]!=null && mqttClient[i].isConnected()) {
                mqttClient[i].disconnect();
            }
        }
        Thread.sleep(2000);
    }

    private void testLifecycleAndTelemetry() throws MqttException, InterruptedException {
        String account = "kapua-sys";
        String username = "kapua-broker";
        String clientId = "client";
        String birthTopic = "$EDC/" + account+ "/" + clientId + "/MQTT/BIRTH";
        String telemetryTopic = account+ "/" + clientId + "/topic_1";
        MqttClient mqttClient = connect(username, "kapua-password", clientId);
        int iteration = 10;
        mqttClient.publish(birthTopic, 0, getMessage("string_body", buildBirthMetrics("", "")).toByteArray());
        for (int i=0; i<iteration; i++) {
            mqttClient.publish(telemetryTopic, 0, getMessage("string_body_" + i, buildMetrics(String.valueOf(i))).toByteArray());
            Thread.sleep(300);
        }
        Thread.sleep(2000);
        mqttClient.disconnect();
    }

    private void checkConnection(MqttClient[] mqttClient, boolean[] connectionStatus) {
        for (int i=0; i<mqttClient.length; i++) {
            if (mqttClient[i]==null) {
                Assert.assertFalse("Bad connection status for client " + i + " (expected " + connectionStatus[i] + ")", connectionStatus[i]);
            }
            else {
                Assert.assertEquals("Bad connection status for client " + i + " (expected " + connectionStatus[i] + ")", connectionStatus[i], mqttClient[i].isConnected());
            }
        }
    }

    private void step(String username, String password, String clientId, String topics[], int qos) throws MqttException, InterruptedException {
        MqttClient mqttClient = connect(username, password, clientId);
        doPubSub(mqttClient, topics, qos);
        Thread.sleep(3000);
        disconnect(mqttClient);
    }

    private MqttClient connect(String username, String password, String clientId) throws MqttException, InterruptedException {
        MqttClient client = new MqttClient("localhost", 8883, clientId, username, password);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        Properties properties = new Properties();
        properties.put("com.ibm.ssl.keyStore", "/Users/riccardomodanese/dev/git/kapua_riccardo/broker/artemis/plugin/src/test/resources/certificates/client.ks");
        properties.put("com.ibm.ssl.keyStorePassword", "changeit");
        properties.put("com.ibm.ssl.trustStore", "/Users/riccardomodanese/dev/git/kapua_riccardo/broker/artemis/plugin/src/test/resources/certificates/client.ts");
        properties.put("com.ibm.ssl.trustStorePassword", "changeit");
        options.setSSLProperties(properties);

        client.connect(options);
        return client;
    }

    private void doPubSub(MqttClient client, String topics[], int qos) throws MqttException, InterruptedException {
        for (String topic : topics) {
            client.subscribe(topic, qos);
            client.publish("not_valid/" + topic, qos, "test message");
            client.publish(topic, qos, "test message");
        }
        Thread.sleep(3000);
        client.disconnect();
    }

    private void disconnect(MqttClient client) throws MqttException {
        client.disconnect();
    }

    public KuraPayload getMessage(String body, Map<String, Object> metrics) throws MqttException {
        KuraPayload payload = new KuraPayload();
        payload.setBody(null);
        payload.setMetrics(metrics);
        payload.setTimestamp(new Date());
        KuraPayload payloadKura = new KuraPayload();
        payloadKura.setBody((body + dataFormat.format(new Date())).getBytes());
        payloadKura.setMetrics(metrics);
        return payloadKura;
    }

    public Map<String, Object> buildMetrics(String value) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("metric_str_1", "string_" + value);
        metrics.put("metric_long_" + value, new Long(12345));
        metrics.put("metric_boolean_" + value, true);
        metrics.put("metric_int_" + value, new Integer(999));
        return metrics;
    }

    public Map<String, Object> buildBirthMetrics(String prefix, String suffix) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put(UPTIME, prefix + "uptime" + suffix);
        metrics.put(DISPLAY_NAME, prefix + "displayName" + suffix);
        metrics.put(MODEL_NAME, prefix + "modelName" + suffix);
        metrics.put(MODEL_ID, prefix + "modelId" + suffix);
        metrics.put(PART_NUMBER, prefix + "partNumber" + suffix);
        metrics.put(SERIAL_NUMBER, prefix + "serialNumber" + suffix);
        metrics.put(FIRMWARE, prefix + "firmware" + suffix);
        metrics.put(FIRMWARE_VERSION, prefix + "firmwareVersion" + suffix);
        metrics.put(BIOS, prefix + "bios" + suffix);
        metrics.put(BIOS_VERSION, prefix + "biosVersion" + suffix);
        metrics.put(OS, prefix + "os" + suffix);
        metrics.put(OS_VERSION, prefix + "osVersion" + suffix);
        metrics.put(JVM_NAME, prefix + "jvmName" + suffix);
        metrics.put(JVM_VERSION, prefix + "jvmVersion" + suffix);
        metrics.put(JVM_PROFILE, prefix + "jvmProfile" + suffix);
        metrics.put(APPLICATION_FRAMEWORK, prefix + "applicationFramework" + suffix);
        metrics.put(APPLICATION_FRAMEWORK_VERSION, prefix + "applicationFrameworkVersion" + suffix);
        metrics.put(CONNECTION_INTERFACE, prefix + "connectionInterface" + suffix);
        metrics.put(CONNECTION_IP, prefix + "connectionIp" + suffix);
        metrics.put(ACCEPT_ENCODING, prefix + "acceptEncoding" + suffix);
        metrics.put(APPLICATION_IDS, prefix + "applicationIdentifiers" + suffix);
        metrics.put(AVAILABLE_PROCESSORS, prefix + "availableProcessors" + suffix);
        metrics.put(TOTAL_MEMORY, prefix + "totalMemory" + suffix);
        metrics.put(OS_ARCH, prefix + "osArch" + suffix);
        metrics.put(OSGI_FRAMEWORK, prefix + "osgiFramework" + suffix);
        metrics.put(OSGI_FRAMEWORK_VERSION, prefix + "osgiFrameworkVersion" + suffix);
        metrics.put(MODEM_IMEI, prefix + "modemImei" + suffix);
        metrics.put(MODEM_IMSI, prefix + "modemImsi" + suffix);
        metrics.put(MODEM_ICCID, prefix + "modemIccid" + suffix);
        return metrics;
    }

    public static DateFormat dataFormat = new SimpleDateFormat("dd/MM/YYYY mm:HH:ss");
    public static final String UPTIME = "uptime";
    public static final String DISPLAY_NAME = "display_name";
    public static final String MODEL_NAME = "model_name";
    public static final String MODEL_ID = "model_id";
    public static final String PART_NUMBER = "part_number";
    public static final String SERIAL_NUMBER = "serial_number";
    public static final String AVAILABLE_PROCESSORS = "available_processors";
    public static final String TOTAL_MEMORY = "total_memory";
    public static final String FIRMWARE = "firmware";
    public static final String FIRMWARE_VERSION = "firmware_version";
    public static final String BIOS = "bios";
    public static final String BIOS_VERSION = "bios_version";
    public static final String OS = "os";
    public static final String OS_VERSION = "os_version";
    public static final String OS_ARCH = "os_arch";
    public static final String JVM_NAME = "jvm_name";
    public static final String JVM_VERSION = "jvm_version";
    public static final String JVM_PROFILE = "jvm_profile";
    public static final String ESF_VERSION = "esf_version";
    public static final String KURA_VERSION = "kura_version";
    public static final String ESF_KURA_VERSION = "esf_kura_version";
    public static final String APPLICATION_FRAMEWORK = "application_framework";
    public static final String APPLICATION_FRAMEWORK_VERSION = "application_framework_version";
    public static final String OSGI_FRAMEWORK = "osgi_framework";
    public static final String OSGI_FRAMEWORK_VERSION = "osgi_framework_version";
    public static final String CONNECTION_INTERFACE = "connection_interface";
    public static final String CONNECTION_IP = "connection_ip";
    public static final String ACCEPT_ENCODING = "accept_encoding";
    public static final String APPLICATION_IDS = "application_ids";
    public static final String MODEM_IMEI = "modem_imei";
    public static final String MODEM_IMSI = "modem_imsi";
    public static final String MODEM_ICCID = "modem_iccid";
}
