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

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ServiceLoader;

import org.eclipse.kapua.commons.core.Application;
import org.eclipse.kapua.commons.core.BeanContextImpl;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ConfigurationImpl;
import org.eclipse.kapua.commons.core.ConfigurationSourceFactoryImpl;
import org.eclipse.kapua.commons.core.spi.ConfigurationSourceFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public abstract class VertxApplication<M extends AbstractMainVerticle> implements Application {

    static {
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.SLF4JLogDelegateFactory");
    }

    private static Logger logger = LoggerFactory.getLogger(VertxApplication.class); 

    private static final String METRIC_REGISTRY_NAME = "com-eurotech-could";
    private static final String DEFAULT_APP_NAME = "vertx-application";

    private Vertx vertx;

    public String getName() {
        return DEFAULT_APP_NAME;
    }

    public Vertx getVertx() {
        return this.vertx;
    }

    public void run(String[] args) {        

        printBanner(getName() + "-banner.txt");

        ConfigurationSourceFactory configSourceFactory = getConfigurationSourceFactory();
        ConfigurationImpl config = new ConfigurationImpl();
        config.setSource(configSourceFactory.create(this.getClass().getClassLoader(), getName() + "-config.json"));

        MetricRegistry metricRegistry = null;
        DropwizardMetricsOptions metrOpts = new DropwizardMetricsOptions();
        metrOpts.setEnabled(Boolean.parseBoolean(config.getProperty("vertx.metrics-enabled")));
        String metricsRoot = config.getProperty("vertx.metrics-root");
        metricRegistry = SharedMetricRegistries.getOrCreate(METRIC_REGISTRY_NAME);
        SharedMetricRegistries.setDefault(METRIC_REGISTRY_NAME, metricRegistry);
        metrOpts.setRegistryName(METRIC_REGISTRY_NAME);
        metrOpts.setBaseName(metricsRoot + ".vertx");

        VertxOptions opts = new VertxOptions();
        opts.setWarningExceptionTime(Long.parseLong(config.getProperty("vertx.warning-exception-time")));
        opts.setBlockedThreadCheckInterval(Long.parseLong(config.getProperty("vertx.blocked-thread-check-interval")));
        opts.setMetricsOptions(metrOpts);
        vertx = Vertx.vertx(opts);

        EnvironmentImpl env = new EnvironmentImpl(vertx, config);
        this.initialize(env);
        Injector injector = Guice.createInjector(
                env.getBeanContextConfig(),
                new AbstractModule() {

                    @Override
                    protected void configure() {
                        bind(Environment.class).toInstance(env);
                        bind(Configuration.class).toInstance(config);
                        for(String key:config.getKeys()) {
                            bind(String.class).annotatedWith(Names.named(key)).toInstance(config.getProperty(key));
                        }
                    }

                });
        BeanContextImpl contextImpl = new BeanContextImpl();
        contextImpl.setInjector(injector);
        env.setBeanContext(contextImpl);
        env.setMetricRegistry(metricRegistry);

        run(env, config);
    }

    protected void deployMainVerticle(Environment environment, Class<M> clazz) {
        this.getVertx().deployVerticle(environment.getBeanContext().getInstance(clazz));
    }

    public abstract void initialize(EnvironmentSetup setup);

    public abstract void run(Environment environment, Configuration config);

    private ConfigurationSourceFactory getConfigurationSourceFactory() {
        ConfigurationSourceFactory configSourceFactory = null;
        ServiceLoader<ConfigurationSourceFactory> factoryLoader = ServiceLoader.load(ConfigurationSourceFactory.class);
        if (factoryLoader.iterator().hasNext()) {
            configSourceFactory = factoryLoader.iterator().next();
            logger.info("Custom configuration factory found: " + configSourceFactory.getClass());
        } else {
            logger.info("No custom configuration factory found, using: " + ConfigurationSourceFactoryImpl.class);
            configSourceFactory = new ConfigurationSourceFactoryImpl();
        }
        return configSourceFactory;
    }

    private void printBanner(String fileName) {

        try {
            InputStream bannerStream = this.getClass().getClassLoader().getResourceAsStream(fileName);                   
            if (bannerStream == null) {
                logger.warn("Banner is missing");
                return;
            }

            System.out.println();
            System.out.println();

            int c = bannerStream.read();
            while( c > 0) {
                System.out.print((char)c);
                c = bannerStream.read();
            }
            System.out.println();
            System.out.println();
            bannerStream.close();
        } catch (IOException e) {
            logger.warn("Failed to print banner...", e);
        }
    }
}