/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core.vertx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.commons.core.Application;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ConfigurationImpl;
import org.eclipse.kapua.commons.core.ConfigurationSourceFactoryImpl;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.ObjectContextImpl;
import org.eclipse.kapua.commons.core.spi.ConfigurationSourceFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

/*
 * Base class to implement a Vertx application.
 * <p>
 * It requires a {@link ObjectContextConfiguration} that is used to create 
 * the {@link ObjectContext} and creates the {@link Configuration}.
 * <p>
 * It deploys the main Verticle provided by the implementor. 
 */
public abstract class VertxApplication<M extends AbstractMainVerticle> implements Application {

    static {
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.SLF4JLogDelegateFactory");
    }

    private static Logger logger = LoggerFactory.getLogger(VertxApplication.class); 

    private static final String BANNER_FILE_SUFFIX = "-banner.txt";
    private static final String CONFIG_FILE_SUFFIX = "-config.json";
    private static final String METRIC_REGISTRY_NAME = "com-eurotech-could";
    private static final String DEFAULT_APP_NAME = "vertx-application";

    private static final String PROP_VERTX_METRICS_ROOT = "vertx.metrics-root";
    private static final String PROP_VERTX_WARNING_EXCEPTION_TIME = "vertx.warning-exception-time";
    private static final String PROP_VERTX_BLOCKED_THREAD_CHECK_INTERVAL = "vertx.blocked-thread-check-interval";
    private static final String PROP_VERTX_STARTUP_TIMEOUT = "vertx.startup-timeout";

    private Vertx vertx;

    /**
     * Returns the name of the application which is used later on (e.g. to retrieve 
     * the configuration file). Implementation classes should override this method
     * if not it  returns a default application name {@link DEFAULT_APP_NAME}.
     * 
     * @return the name of the application
     */
    @Override
    public String getName() {
        return DEFAULT_APP_NAME;
    }

    public abstract Class<M> getMainVerticle();

    /**
     * This method is called by the implementation classes to start the application.
     * <p>
     * It invokes the method {@link #initialize(EnvironmentSetup)} to execute actions 
     * that need to be performed before starting the actual execution
     * After, it invokes the method {@link #run(Environment, Configuration)} to start 
     * the actual execution.
     * 
     * @param args command line parameters
     * @throws Exception 
     */
    public void run(String[] args) throws Exception {
        EnvironmentImpl env = null;
        ConfigurationImpl config = null;

        printBanner(getName() + BANNER_FILE_SUFFIX);

        ConfigurationSourceFactory configSourceFactory = getConfigurationSourceFactory();
        config = new ConfigurationImpl();
        config.setSource(configSourceFactory.create(this.getClass().getClassLoader(), getName() + CONFIG_FILE_SUFFIX));

        MetricRegistry metricRegistry = null;
        DropwizardMetricsOptions metrOpts = new DropwizardMetricsOptions();
        metrOpts.setEnabled(Boolean.parseBoolean(config.getProperty("vertx.metrics-enabled")));
        String metricsRoot = config.getProperty(PROP_VERTX_METRICS_ROOT);
        metricRegistry = SharedMetricRegistries.getOrCreate(METRIC_REGISTRY_NAME);
        try {
            SharedMetricRegistries.setDefault(METRIC_REGISTRY_NAME, metricRegistry);
        }
        catch (IllegalStateException e) {
            logger.warn("Default metric registry already set.");
        }
        metrOpts.setRegistryName(METRIC_REGISTRY_NAME);
        metrOpts.setBaseName(metricsRoot + ".vertx");

        VertxOptions opts = new VertxOptions();
        opts.setWarningExceptionTime(Long.parseLong(config.getProperty(PROP_VERTX_WARNING_EXCEPTION_TIME)));
        opts.setBlockedThreadCheckInterval(Long.parseLong(config.getProperty(PROP_VERTX_BLOCKED_THREAD_CHECK_INTERVAL)));
        opts.setMetricsOptions(metrOpts);
        vertx = Vertx.vertx(opts);

        env = new EnvironmentImpl(vertx, config);
        this.initialize(env);

        List<AbstractModule> modules = new ArrayList<>();
        if (env.getBeanContextConfig() != null) {
            modules.add(env.getBeanContextConfig());
        }
        final EnvironmentImpl finalEnv = env;
        final ConfigurationImpl finalConfig = config;
        modules.add(new AbstractModule() {

            @Override
            protected void configure() {
                bind(EventBusProvider.class).toInstance(new EventBusProvider() {

                    @Override
                    public EventBus get() {
                        // There is one event bus object per vertx instance
                        return vertx.eventBus();
                    }

                });
                bind(Vertx.class).toInstance(vertx);
                if (finalEnv.getMetricRegistry() != null) {
                    bind(MetricRegistry.class).toInstance(finalEnv.getMetricRegistry());
                }
                bind(Configuration.class).toInstance(finalConfig);
                for(String key:finalConfig.getKeys()) {
                    bind(String.class).annotatedWith(Names.named(key)).toInstance(finalConfig.getProperty(key));
                }
            }

        });

        Injector injector = Guice.createInjector(modules);

        ObjectContextImpl contextImpl = new ObjectContextImpl();
        contextImpl.setInjector(injector);
        env.setBeanContext(contextImpl);
        env.setMetricRegistry(metricRegistry);

        this.run(env, config);
    }

    /**
     * Initialize the application.
     * <p>
     * This is called by the {@link #run(String[])} method. Don't call it yourself. 
     * <p>
     * Implementor may override this function to execute custom initialization
     * logic. Implementor has to override this method to provide the 
     * {@link ObjectContextConfig}.
     * <p>
     * @param setup environment for initialization.
     * @throws Exception
     */
    public void initialize(EnvironmentSetup setup) throws Exception {
    }

    /**
     * Run the application.
     * <p>
     * This is called by the {@link #run(String[])} method. Don't call it yourself.
     * <p>
     * An implementation class usually don't need to override this method since the
     * default implementation deploys the main verticle which is supposed to execute
     * the application logic.
     * <p> 
     * If the start of the main verticle take more time than established by a startup
     * timeout the start is aborted and the application closes.
     * <p>
     * @param environment environment for initialization.
     * @param config application configuration.
     * @throws Exception
     */
    public void run(Environment environment, Configuration config) throws Exception {
        this.deployMainVerticle(environment, config, this.getMainVerticle());
    }

    /**
     * Shutdown the application.
     * <p>
     * May be called by the {@link #run(String[])} method, e.g. if the application cant'
     * startup within the established timeout. May be called externally as well. If 
     * the shutdown doesn't complete within a shutdown timeout, the operation is aborted 
     * and the application is terminated.
     * <p>
     * @param timeout timeout waiting for shutdown
     */
    public void shutdown(long timeout) throws Exception {
        long actualTimeout = timeout;
        if (timeout ==-1) {
            actualTimeout = Long.MAX_VALUE;
        }

        if (vertx != null) {
            Future<Void> closeFuture = Future.future();
            CountDownLatch stoppedSignal = new CountDownLatch(1);
            vertx.close(ar -> {
                if (ar.succeeded()) {
                    closeFuture.complete();
                    logger.error("Closing Vertx...DONE");
                } else {
                    closeFuture.fail(ar.cause());
                    logger.error("Closing Vertx...FAILED", ar.cause());
                }
                stoppedSignal.countDown();
            });

            boolean touchedZero = stoppedSignal.await(actualTimeout, TimeUnit.MILLISECONDS);
            if (closeFuture.failed() || !touchedZero) {
                throw new Exception("Closing Vertx...FAILED", closeFuture.cause());
            }
        }
    }

    /**
     * @throws Exception 
     * 
     */
    public void shutdown() throws Exception {
        this.shutdown(-1);
    }

    private void deployMainVerticle(Environment env, Configuration config, Class<M> clazz) throws Exception {
        logger.trace("Deploying verticle {}...", this.getClass());
        CountDownLatch startedSignal = new CountDownLatch(1);
        Future<Void> deployFuture = Future.future();
        M m = env.getBeanContext().getInstance(clazz);
        vertx.deployVerticle(m, ar -> {
            if (ar.succeeded()) {
                deployFuture.complete();
                logger.trace("Deploying verticle {}...DONE", this.getClass());
            } else {
                deployFuture.fail(ar.cause());
                logger.error("Deploying verticle {}...FAILED", this.getClass(), ar.cause());
            }
            startedSignal.countDown();
        });

        long startupTimeout = Long.parseLong(config.getProperty(PROP_VERTX_STARTUP_TIMEOUT));
        boolean touchedZero = startedSignal.await(startupTimeout, TimeUnit.MILLISECONDS);
        if (deployFuture.failed()) {
            throw new Exception(String.format("Error occured while deploying verticle %s", clazz.getName()), deployFuture.cause());
        } else if (!touchedZero) {
            throw new Exception(String.format("Error occured while deploying verticle %s [Start timeout expired]", clazz.getName()));
        }
    }

    private ConfigurationSourceFactory getConfigurationSourceFactory() {
        ConfigurationSourceFactory configSourceFactory = null;
        ServiceLoader<ConfigurationSourceFactory> factoryLoader = ServiceLoader.load(ConfigurationSourceFactory.class);
        if (factoryLoader.iterator().hasNext()) {
            configSourceFactory = factoryLoader.iterator().next();
            logger.info("Custom configuration factory found: " + configSourceFactory.getClass());
        } else {
            logger.debug("No custom configuration factory found, using: " + ConfigurationSourceFactoryImpl.class);
            configSourceFactory = new ConfigurationSourceFactoryImpl();
        }
        return configSourceFactory;
    }

    private void printBanner(String fileName) {

        try {
            InputStream bannerStream = this.getClass().getClassLoader().getResourceAsStream(fileName);                   
            if (bannerStream == null) {
                logger.warn("Banner {} is missing", fileName);
                return;
            }

            System.out.println();
            System.out.println();

            int c = bannerStream.read();
            while( c >= 0) {
                System.out.print((char)c);
                c = bannerStream.read();
            }
            System.out.println();
            System.out.println();
            bannerStream.close();
        } catch (IOException e) {
            logger.warn("Failed to print banner {}...", fileName, e);
        }
    }
}
