/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.annotated.AnnotatedApplication;
import org.eclipse.kapua.kura.simulator.app.command.SimpleCommandApplication;
import org.eclipse.kapua.kura.simulator.app.deploy.SimpleDeployApplication;
import org.eclipse.kapua.kura.simulator.generator.GeneratorScheduler;
import org.eclipse.kapua.kura.simulator.generator.Generators;

import org.eclipse.scada.utils.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class SingleTestApplication {

    private static final Logger logger = LoggerFactory.getLogger(SingleTestApplication.class);

    private SingleTestApplication() {
    }

    public static void main(final String[] args) throws Throwable {

        toInfinityAndBeyond();

        logger.info("Starting ...");

        final ScheduledExecutorService downloadExecutor = Executors
                .newSingleThreadScheduledExecutor(new NamedThreadFactory("DownloadSimulator"));

        final GatewayConfiguration configuration = new GatewayConfiguration(
                "tcp://kapua-broker:kapua-password@localhost:1883", "kapua-sys", "sim-1");

        try (final GeneratorScheduler scheduler = new GeneratorScheduler(Duration.ofSeconds(1))) {

            final Set<Application> apps = new HashSet<>();
            apps.add(new SimpleCommandApplication(s -> String.format("Command '%s' not found", s)));
            apps.add(AnnotatedApplication.build(new SimpleDeployApplication(downloadExecutor)));
            apps.add(Generators.simpleDataApplication("data-1", scheduler, "sine", Generators.sine(Duration.ofSeconds(120), 100, 0, null)));

            try (final MqttAsyncTransport transport = new MqttAsyncTransport(configuration);
                    final Simulator simulator = new Simulator(configuration, transport, apps);) {
                Thread.sleep(Long.MAX_VALUE);
                logger.info("Bye bye...");
            } finally {
                downloadExecutor.shutdown();
            }

        }

        logger.info("Exiting...");
    }

    /**
     * Redirect Paho logging to SLF4J
     */
    private static void toInfinityAndBeyond() {
        java.util.logging.LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("org.eclipse.paho.client.mqttv3").setLevel(Level.ALL);
    }
}
