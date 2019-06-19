/*******************************************************************************
 * Copyright (c) 2017, 2018 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common.utils;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.Given;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.Suppressed;
import org.elasticsearch.common.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class EmbeddedEventBroker {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedEventBroker.class);

    private static final String DEFAULT_DATA_DIRECTORY_PREFIX = "target/artemis" + UUIDs.randomBase64UUID();
    private static final String DEFAULT_DATA_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/data/journal";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private static final boolean NO_EMBEDDED_SERVERS = Boolean.getBoolean("org.eclipse.kapua.qa.noEmbeddedServers");

    private static Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private DBHelper database;

    private static EmbeddedJMS jmsServer;

    @Inject
    public EmbeddedEventBroker(final DBHelper database) {
        this.database = database;
    }

    @Given("^Start Event Broker$")
    public void start() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        System.setProperty(SystemSettingKey.EVENT_BUS_URL.key(), "amqp://127.0.0.1:5672");
        database.setup();

        logger.info("Starting new instance of Event Broker");
        try {
            //start Artemis embedded
            Configuration configuration = new ConfigurationImpl();
            configuration.setPersistenceEnabled(false);
            configuration.setJournalDirectory(DEFAULT_DATA_DIRECTORY);
            configuration.setSecurityEnabled(false);
            configuration.addAcceptorConfiguration("amqp", 
                    "tcp://127.0.0.1:5672?protocols=AMQP");
            configuration.addConnectorConfiguration("connector", "tcp://127.0.0.1:5672");
            JMSConfiguration jmsConfig = new JMSConfigurationImpl();
            ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl().setName("cf").setConnectorNames(Arrays.asList("connector")).setBindings("cf");
            jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

            jmsServer = new EmbeddedJMS().setConfiguration(configuration).setJmsConfiguration(jmsConfig).start();

            if (EXTRA_STARTUP_DELAY > 0) {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to start Event Broker", e);
        }
    }

    @Given("^Stop Event Broker$")
    public void stop() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("Stopping Event Broker instance ...");
        try (final Suppressed<RuntimeException> s = Suppressed.withRuntimeException()) {
            // close all resources
            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);
            // shut down broker
            if (jmsServer != null) {
                jmsServer.stop();
                jmsServer = null;
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
