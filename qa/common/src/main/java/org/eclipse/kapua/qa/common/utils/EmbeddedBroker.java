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

import cucumber.api.java.en.Given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.eclipse.kapua.qa.common.Ports;
import org.eclipse.kapua.qa.common.Suppressed;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.elasticsearch.common.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class EmbeddedBroker {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedBroker.class);

    private static final String DEFAULT_DATA_DIRECTORY_PREFIX = "target/activemq/" + UUIDs.randomBase64UUID();
    private static final String DEFAULT_KAHA_DB_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/kahaDB";
    private static final String DEFAULT_DATA_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/data";
    private static final String KAHA_DB_DIRECTORY = "KAHA_DB_DIRECTORY";
    /**
     * Embedded broker configuration file from classpath resources.
     */
    public static final String ACTIVEMQ_XML = "xbean:activemq.xml";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private static final boolean NO_EMBEDDED_SERVERS = Boolean.getBoolean("org.eclipse.kapua.qa.noEmbeddedServers");

    private Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private static BrokerService broker;

    public EmbeddedBroker() {
    }

    @Given("^Start Broker$")
    public void start() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("Starting new Broker instance");
        try {
            // test if port is already open

            if (Ports.isPortOpen(1883)) {
                throw new IllegalStateException("Broker port is already in use");
            }

            // start the broker
            System.setProperty(KAHA_DB_DIRECTORY, DEFAULT_KAHA_DB_DIRECTORY);
            broker = BrokerFactory.createBroker(ACTIVEMQ_XML);
            broker.setDataDirectory(DEFAULT_DATA_DIRECTORY);
            logger.info("Setting ActiveMQ data directory to {}", broker.getBrokerDataDirectory());
            broker.start();

            // wait for the broker

            if (!broker.waitUntilStarted(Duration.ofSeconds(20).toMillis())) {
                throw new IllegalStateException("Failed to start up broker in time");
            }

            if (EXTRA_STARTUP_DELAY > 0) {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start broker", e);
        }
    }

    @Given("^Stop Broker$")
    public void stop() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("Stopping Broker instance ...");
        try (final Suppressed<RuntimeException> s = Suppressed.withRuntimeException()) {

            // close all resources

            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);

            // shut down broker

            if (broker != null) {
                broker.stop();
                broker.waitUntilStopped();
                broker = null;
            }

        } catch (Exception e) {
            logger.error("Failed to stop Broker!");
            e.printStackTrace();
        }

        DatastoreMediator.getInstance().clearCache();

        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Stopping Broker instance ... done!");
    }

}
