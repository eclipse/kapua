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
package org.eclipse.kapua.kura.simulator.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.kapua.kura.simulator.GatewayConfiguration;
import org.eclipse.kapua.kura.simulator.MqttAsyncTransport;
import org.eclipse.kapua.kura.simulator.Simulator;
import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.annotated.AnnotatedApplication;
import org.eclipse.kapua.kura.simulator.app.command.SimpleCommandApplication;
import org.eclipse.kapua.kura.simulator.app.deploy.SimpleDeployApplication;
import org.eclipse.kapua.kura.simulator.simulation.Configurations;
import org.eclipse.kapua.kura.simulator.simulation.JsonReader;
import org.eclipse.kapua.kura.simulator.simulation.Simulation;
import org.eclipse.scada.utils.concurrent.NamedThreadFactory;
import org.eclipse.scada.utils.str.StringReplacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.layout.TTLLLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

/**
 * This is a main class running a simple default simulator setup with multiple
 * instances
 */
public class SimulatorRunner {

    private static final Logger logger = LoggerFactory.getLogger(SimulatorRunner.class);

    private SimulatorRunner() {
    }

    public static void main(final String... args) throws Throwable {

        toInfinityAndBeyond();

        final Options opts = new Options();

        opts.addOption(
                Option.builder("n")
                        .longOpt("basename")
                        .hasArg().argName("BASENAME")
                        .desc("The base name of the simulator instance")
                        .build());

        opts.addOption(
                Option.builder()
                        .longOpt("name-factory")
                        .hasArg().argName("FACTORY")
                        .desc("The name factory to use")
                        .build());

        opts.addOption(
                Option.builder("c")
                        .longOpt("count")
                        .hasArg().argName("COUNT")
                        .type(Integer.class)
                        .desc("The number of instances to start")
                        .build());

        opts.addOption(
                Option.builder("a")
                        .longOpt("account-name")
                        .hasArg().argName("NAME")
                        .desc("The name of the account (defaults to 'kapua-sys')")
                        .build());

        opts.addOption(
                Option.builder("f")
                        .longOpt("simulation")
                        .hasArg().argName("URI")
                        .desc("The URI or path to a JSON file containing a simple simulation setup")
                        .build());

        opts.addOption(
                Option.builder("s")
                        .longOpt("seconds")
                        .hasArg().argName("SECONDS")
                        .type(Long.class)
                        .desc("Shutdown simulator after <SECONDS> seconds")
                        .build());

        opts.addOption("?", "help", false, null);

        {
            final OptionGroup broker = new OptionGroup();
            broker.setRequired(false);

            broker.addOption(
                    Option.builder("h")
                            .longOpt("broker-host")
                            .hasArg().argName("HOST")
                            .desc("Only the hostname of the broker, used for building the full URL")
                            .build());
            broker.addOption(
                    Option.builder("b")
                            .longOpt("broker")
                            .hasArg().argName("URL")
                            .desc("The full URL to the broker").build());

            opts.addOptionGroup(broker);
        }

        {
            final OptionGroup logging = new OptionGroup();
            logging.setRequired(false);

            logging.addOption(Option.builder("q").longOpt("quiet").desc("Suppress output").build());
            logging.addOption(Option.builder("v").longOpt("verbose").desc("Show more output").build());
            logging.addOption(Option.builder("d").longOpt("debug").desc("Show debug output").build());

            opts.addOptionGroup(logging);
        }

        final CommandLine cli;
        try {
            cli = new DefaultParser().parse(opts, args);
        } catch (final ParseException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
            return;
        }

        if (cli.hasOption('?')) {
            showHelp(opts);
            System.exit(0);
        }

        setupLogging(cli);

        final String basename = replace(cli.getOptionValue('n', env("KSIM_BASE_NAME", "sim-")));
        final String nameFactoryName = cli.getOptionValue("name-factory", env("KSIM_NAME_FACTORY", null));
        final int count = Integer.parseInt(replace(cli.getOptionValue('c', env("KSIM_NUM_GATEWAYS", "1"))));
        final String brokerHost = replace(cli.getOptionValue("h"));
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
                .newSingleThreadScheduledExecutor(new NamedThreadFactory("DownloadSimulator"));

        final List<AutoCloseable> close = new LinkedList<>();

        final NameFactory nameFactory = createNameFactory(nameFactoryName)
                .orElseGet(() -> NameFactories.prefixed(basename));

        final List<Simulation> simulations = createSimulations(cli);

        // auto close all simulations
        close.addAll(simulations);

        try {
            for (int i = 1; i <= count; i++) {

                final String name = nameFactory.generateName(i);
                logger.info("Creating instance #{} - {}", i, name);

                final GatewayConfiguration configuration = new GatewayConfiguration(broker, accountName, name);

                final Set<Application> apps = new HashSet<>();
                apps.add(new SimpleCommandApplication(s -> String.format("Command '%s' not found", s)));
                apps.add(AnnotatedApplication.build(new SimpleDeployApplication(downloadExecutor)));

                for (final Simulation sim : simulations) {
                    apps.add(sim.createApplication(name));
                }

                final MqttAsyncTransport transport = new MqttAsyncTransport(configuration);
                close.add(transport);
                final Simulator simulator = new Simulator(configuration, transport, apps);
                close.add(simulator);
            }

            Thread.sleep(TimeUnit.SECONDS.toMillis(shutdownAfter));
            logger.info("Bye bye...");
        } finally {
            downloadExecutor.shutdown();
            closeAll(close);
        }

        logger.info("Exiting...");
    }

    private static boolean hasText(final String text) {
        return text != null && !text.isEmpty();
    }

    private static List<Simulation> createSimulations(final CommandLine cli) throws IOException {

        String simulationConfiguration = System.getenv("KSIM_SIMULATION_CONFIGURATION");
        if (hasText(simulationConfiguration)) {
            logger.info("Using direct simulator configuration");
            return Configurations.createSimulations(JsonReader.parse(simulationConfiguration));
        }

        simulationConfiguration = System.getenv("KSIM_SIMULATION_URL");

        if (!hasText(simulationConfiguration)) {
            simulationConfiguration = cli.getOptionValue("simulation");
        }

        if (hasText(simulationConfiguration)) {
            logger.info("Loading simulator configuration from: {}", simulationConfiguration);

            try {
                final Path path = Paths.get(simulationConfiguration);
                try (final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    return Configurations.createSimulations(JsonReader.parse(reader));
                }

            } catch (final InvalidPathException e) {
                // ignore and re-try as URL
            }

            final URL url = new URL(simulationConfiguration);
            try (final InputStream in = url.openStream()) {
                return Configurations.createSimulations(JsonReader.parse(in, StandardCharsets.UTF_8));
            }
        }

        return Collections.emptyList();
    }

    /**
     * Show command line help
     *
     * @param opts
     *            configured options
     */
    private static void showHelp(final Options opts) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("SimulatorRunner",
                "Run Kura gateway simulator\n\n",
                opts,
                "\nThis application is the default main entry point for this library. Other entry points may be available.",
                true);
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
        java.util.logging.Logger.getLogger("org.eclipse.paho.client.mqttv3").setLevel(java.util.logging.Level.ALL);
    }

    private static void setupLogging(final CommandLine cli) {
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // reset first

        context.reset();

        // now configure

        final ConsoleAppender<ILoggingEvent> consoleAdapter = new ConsoleAppender<>();
        consoleAdapter.setContext(context);
        consoleAdapter.setName("console");
        final LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(context);

        final TTLLLayout layout = new TTLLLayout();

        layout.setContext(context);
        layout.start();
        encoder.setLayout(layout);

        consoleAdapter.setEncoder(encoder);
        consoleAdapter.start();

        final ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        if (cli.hasOption("d")) {
            rootLogger.setLevel(ch.qos.logback.classic.Level.ALL);
        } else if (cli.hasOption("v")) {
            rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);
        } else if (cli.hasOption("q")) {
            rootLogger.setLevel(ch.qos.logback.classic.Level.WARN);
        } else {
            rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);
        }
        rootLogger.addAppender(consoleAdapter);
    }
}
