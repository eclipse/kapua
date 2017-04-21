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

import static java.time.Duration.ofSeconds;
import static org.eclipse.kapua.qa.utils.Suppressed.withRuntimeException;

import java.net.BindException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.eclipse.kapua.qa.utils.Suppressed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class EmbeddedBroker {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedBroker.class);

    /**
     * Embedded broker configuration file from classpath resources.
     */
    public static final String ACTIVEMQ_XML = "xbean:activemq.xml";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private BrokerService broker;

    private DBHelper database;

    @Inject
    public EmbeddedBroker(final DBHelper database) {
        this.database = database;
    }

    @Before
    public void start() {
        
        this.database.setup();
        
        logger.info("Starting new instance");

        try {
            // test if port is already open

            try (ServerSocket socket = new ServerSocket(1883)) {
            } catch (BindException e) {
                throw new IllegalStateException("Broker port is already in use");
            }

            // start the broker

            broker = BrokerFactory.createBroker(ACTIVEMQ_XML);
            broker.start();

            // wait for the broker

            if (!broker.waitUntilStarted(Duration.ofSeconds(20).toMillis())) {
                throw new IllegalStateException("Failed to start up broker in time");
            }

            if (EXTRA_STARTUP_DELAY > 0) {
                Thread.sleep(ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start broker", e);
        }
    }

    @After
    public void stop() {
        logger.info("Stopping instance ...");

        try (final Suppressed<RuntimeException> s = withRuntimeException()) {

            // close all resources

            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);

            // shut down broker

            if (broker != null) {
                broker.stop();
                broker.waitUntilStopped();
                broker = null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to stop broker", e);
        }

        logger.info("Stopping instance ... done!");
    }

}
