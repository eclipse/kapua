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

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.eclipse.kapua.qa.common.Suppressed;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.elasticsearch.common.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtemisEmbeddedTest {

    private static final Logger logger = LoggerFactory.getLogger(ArtemisEmbeddedTest.class);

    private static final String CONFIGURATION_FILE = "file:///Users/riccardomodanese/dev/git/kapua_riccardo/assembly/broker-artemis/configurations/broker.xml";
    private static final String DEFAULT_DATA_DIRECTORY_PREFIX = "target/artemis" + UUIDs.randomBase64UUID();
    private static final String DEFAULT_DATA_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/data/journal";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private static Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private static EmbeddedActiveMQ server;

    private static final String SERVER_URL = "tcp://192.168.33.10:1883";
    private static final String CLIENT_ID = "client-id";

    public static void main(String argv[]) {
        new ArtemisEmbeddedTest().start();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static MqttClient getClient(String serverUrl, String clientId) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient client = new MqttClient(SERVER_URL, CLIENT_ID, persistence);
        client.setCallback(new MqttCallback(CLIENT_ID));
        return client;
    }

    private static MqttConnectOptions getOptions(String username, String password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        return options;
    }

    public ArtemisEmbeddedTest() {
    }

    public void start() {
        logger.info("Starting new instance of Event Broker");
        try {
            //start Artemis embedded
//            Configuration configuration = new ConfigurationImpl();
//            configuration.setPersistenceEnabled(false);
//            configuration.setJournalDirectory(DEFAULT_DATA_DIRECTORY);
//            configuration.setSecurityEnabled(false);
//            configuration.addAcceptorConfiguration("amqp", 
//                    "tcp://127.0.0.1:5672?protocols=AMQP");
//            configuration.addConnectorConfiguration("connector", "tcp://127.0.0.1:5672");
//            JMSConfiguration jmsConfig = new JMSConfigurationImpl();
//            ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl().setName("cf").setConnectorNames(Arrays.asList("connector")).setBindings("cf");
//            jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);
//
//            jmsServer = new EmbeddedJMS().setConfiguration(configuration).setJmsConfiguration(jmsConfig).start();

            server = new EmbeddedActiveMQ();
            server.setConfigResourcePath(CONFIGURATION_FILE);
            server.start();
            if (EXTRA_STARTUP_DELAY > 0) {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to start Event Broker", e);
        }
    }

    public void stop() {
        logger.info("Stopping Event Broker instance ...");
        try (final Suppressed<RuntimeException> s = Suppressed.withRuntimeException()) {
            // close all resources
            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);
            // shut down broker
            if (server != null) {
                server.stop();
                server = null;
            }
        } catch (Exception e) {
            logger.error("Failed to stop Event Broker", e);
        }
        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Stopping Event Broker instance ... done!");
    }

}