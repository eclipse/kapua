/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.eclipse.kapua.kura.simulator.GatewayConfiguration;
import org.eclipse.kapua.kura.simulator.MqttAsyncTransport;
import org.eclipse.kapua.kura.simulator.Simulator;
import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.annotated.AnnotatedApplication;
import org.eclipse.kapua.kura.simulator.app.command.SimpleCommandApplication;
import org.eclipse.kapua.kura.simulator.app.deploy.SimpleDeployApplication;
import org.eclipse.kapua.kura.simulator.util.NameThreadFactory;
import org.eclipse.scada.utils.str.StringReplacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * This is a main class running a simple default simulator setup with multiple
 * instances
 */
public class SimulatorRunner {

    private static final Logger logger = LoggerFactory.getLogger(SimulatorRunner.class);

    public static void main(final String... args) throws Throwable {

        toInfinityAndBeyond();

        final Options opts = new Options();
        opts.addOption("n", "basename", true, "The base name of the simulator instance");
        opts.addOption(null, "name-factory", true, "The name factory to use");
        opts.addOption("c", "count", true, "The number of instances to start");
        opts.addOption("h", "broker-host", true, "The broker's host");
        opts.addOption("b", "broker", true, "The URL to the broker");
        opts.addOption("a", "account-name", true, "The name of the account");
        opts.addOption("s", "shutdown", true, "Shutdown simulator after x seconds");

        final CommandLine cli = new DefaultParser().parse(opts, args);

        final String basename = replace(cli.getOptionValue('n', env("KSIM_BASE_NAME", "sim-")));
        final String nameFactoryName = cli.getOptionValue("name-factory", env("KSIM_NAME_FACTORY", null));
        final int count = Integer.parseInt(replace(cli.getOptionValue('c', env("KSIM_NUM_GATEWAYS", "1"))));
        final String brokerHost = replace(cli.getOptionValue("bh"));
        final String broker = replace(cli.getOptionValue('b', createBrokerUrl(Optional.ofNullable(brokerHost))));
        final String accountName = replace(cli.getOptionValue('a', env("KSIM_ACCOUNT_NAME", "kapua-sys")));
        final long shutdownAfter = Long
                .parseLong(replace(cli.getOptionValue('s', Long.toString(Long.MAX_VALUE / 1_000L))));

        dumpEnv();

        logger.info("Starting simulation ...");
        logger.info("\tbasename : {}", basename);
        logger.info("\tname-factory : {}", nameFactoryName);
        logger.info("\tcount: {}", count);
        logger.info("\tbroker: {}", broker);
        logger.info("\taccount-name: {}", accountName);

        final ScheduledExecutorService downloadExecutor = Executors
                .newSingleThreadScheduledExecutor(new NameThreadFactory("DownloadSimulator"));

        final List<AutoCloseable> close = new LinkedList<>();

        final NameFactory nameFactory = createNameFactory(nameFactoryName)
                .orElseGet(() -> NameFactories.prefixed(basename));

        try {
            for (int i = 1; i <= count; i++) {

                final String name = nameFactory.generateName(i);
                logger.info("Creating instance #{} - {}", i, name);

                final GatewayConfiguration configuration = new GatewayConfiguration(broker, accountName, name);

                final Set<Application> apps = new HashSet<>();
                apps.add(new SimpleCommandApplication(s -> String.format("Command '%s' not found", s)));
                apps.add(AnnotatedApplication.build(new SimpleDeployApplication(downloadExecutor)));

                final MqttAsyncTransport transport = new MqttAsyncTransport(configuration);
                close.add(transport);
                final Simulator simulator = new Simulator(configuration, transport, apps);
                close.add(simulator);
            }

            Thread.sleep(shutdownAfter * 1_000L);
            logger.info("Bye bye...");
        } finally {
            downloadExecutor.shutdown();
            closeAll(close);
        }

        logger.info("Exiting...");
    }

    private static Optional<NameFactory> createNameFactory(final String nameFactoryName) throws Exception {
        if (nameFactoryName == null || nameFactoryName.isEmpty()) {
            return Optional.empty();
        }

        switch (nameFactoryName) {
        case "default":
            return Optional.empty();
        case "host:name":
            return Optional.of(NameFactories.hostname());
        case "host:addr":
            return Optional.of(NameFactories.hostnameAddress());
        case "host:iface:name":
            return Optional.of(NameFactories.mainInterfaceName());
        case "host:iface:index":
            return Optional.of(NameFactories.mainInterfaceIndex());
        case "host:iface:mac":
            return Optional.of(NameFactories.mainInterfaceAddress());
        default:
            throw new IllegalArgumentException(String.format("Unknown name factory '%s'", nameFactoryName));
        }
    }

    private static void dumpEnv() {

        final List<String> keys = System.getenv().keySet().stream().filter(key -> key.startsWith("KSIM_")).sorted()
                .collect(Collectors.toList());

        if (keys.isEmpty()) {
            logger.info("No KSIM_* env vars found");
            return;
        }

        logger.info("Dumping KSIM_* env vars:");
        for (final String key : keys) {
            logger.info("\t{} = '{}'", key, System.getenv(key));
        }
    }

    private static String createBrokerUrl(final Optional<String> hostFromCli) {
        final String broker = System.getenv("KSIM_BROKER_URL");
        if (broker != null && !broker.isEmpty()) {
            return broker;
        }

        final String proto = replace(env("KSIM_BROKER_PROTO", "tcp"));
        final String user = replace(env("KSIM_BROKER_USER", "kapua-broker"));
        final String password = replace(env("KSIM_BROKER_PASSWORD", "kapua-password"));
        final String host = hostFromCli.orElse(replace(env("KSIM_BROKER_HOST", "localhost")));
        final String port = replace(env("KSIM_BROKER_PORT", "1883"));

        final StringBuilder sb = new StringBuilder(128);

        sb.append(proto).append("://");

        if (user != null && !user.isEmpty()) {
            sb.append(user);
            if (password != null && !password.isEmpty()) {
                sb.append(':').append(password);
            }
            sb.append('@');
        }

        sb.append(host).append(':').append(port);

        return sb.toString();
    }

    private static String env(final String envName, final String defaultValue) {
        return System.getenv().getOrDefault(envName, defaultValue);
    }

    private static String replace(final String string) {
        return StringReplacer.replace(string, StringReplacer.newExtendedSource(System.getenv()),
                StringReplacer.DEFAULT_PATTERN);
    }

    private static void closeAll(final List<AutoCloseable> close) throws Throwable {
        final LinkedList<Throwable> errors = new LinkedList<>();

        for (final AutoCloseable c : close) {
            try {
                c.close();
            } catch (final Exception e) {
                errors.add(e);
            }
        }

        final Throwable e = errors.pollFirst();
        if (e != null) {
            errors.forEach(e::addSuppressed);
            throw e;
        }
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
