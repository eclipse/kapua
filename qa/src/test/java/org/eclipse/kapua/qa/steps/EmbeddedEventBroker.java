/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.qa.utils.Suppressed;
import org.elasticsearch.common.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class EmbeddedEventBroker {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedEventBroker.class);

    private static final String DEFAULT_DATA_DIRECTORY_PREFIX = "target/artemis" + UUIDs.randomBase64UUID();
    private static final String DEFAULT_DATA_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/data/journal";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private DBHelper database;

    private EmbeddedJMS jmsServer;

    @Inject
    public EmbeddedEventBroker(final DBHelper database) {
        this.database = database;
    }

    @Before(value = "@StartEventBroker")
    public void start() {

        System.setProperty(SystemSettingKey.EVENT_BUS_URL.key(), "amqp://127.0.0.1:5672");
        database.setup();

        logger.info("Starting new instance");
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

            //TODO to remove once the application life cycle will be implemented
            //init JmsEventBus
            ServiceEventBusManager.start();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start broker", e);
        }
    }

    @After(value = "@StopEventBroker")
    public void stop() {
        logger.info("Stopping instance ...");
        try (final Suppressed<RuntimeException> s = Suppressed.withRuntimeException()) {
            // close all resources
            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);
            // shut down broker
            if (jmsServer != null) {
                jmsServer.stop();
                jmsServer = null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to stop broker", e);
        }
        logger.info("Stopping instance ... done!");
    }

}
