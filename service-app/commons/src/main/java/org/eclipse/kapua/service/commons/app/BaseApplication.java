/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.service.commons.DefaultBuilderRegistry;
import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.ServiceVerticle;
import org.eclipse.kapua.service.commons.ServiceVerticleBuilder;
import org.eclipse.kapua.service.commons.ServiceVerticleBuilders;
import org.eclipse.kapua.service.commons.ServiceVerticles;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceVerticle;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceVerticleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * Base class that can be extended to implement your own Vertx based application. Derived classes can add
 * their own behavior by overriding methods {@link #init(InitContext,C)}/{@link #init(InitContext,C,Future)}
 * and/or {@link #run(Context,C)}/{@link #run(Context,C,Future)}.
 * The class holds a reference to the root Vertx instance that is used to deploy the service verticles.
 *
 * @param <C> the configuration class associated with your own Vertx based application
 */
public class BaseApplication<C extends Configuration> implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    private InitContext<C> initContext;
    private Context context;
    private ServiceVerticles services;
    private HttpMonitorServiceVerticle monitorServiceVerticle;
    private DefaultBuilderRegistry serviceBuilderRegistry = new DefaultBuilderRegistry();

    /**
     * Sets the initialization context of the application. Must be called before {@link #run(ApplicationArguments)} is run.
     * If the application instance is created using Spring Boot, the initialization context is injected using the configuration 
     * defined in {@link AbstractBeanProvider}. 
     * 
     * @param context the application initialization context
     */
    @Autowired
    public final void setInitContext(InitContext<C> context) {
        initContext = context;
    }

    /**
     * Run the application. If the application startup does not complete within the time configured in {@link BaseConfiguration#getStartupTimeout()}
     * an exception is thrown and application is expected to exit.
     * If monitoring is enabled all services implementing the HealthCheckProvider interface will add their health checks to the monitoring service.
     */
    @Override
    public final void run(ApplicationArguments args) throws Exception {

        logger.info("Starting application {}", initContext.getConfig());
        Future<Void> startupFuture = Future.future();
        CountDownLatch startupLatch = new CountDownLatch(1);

        Future.succeededFuture().compose(map -> {
            try {
                ServiceVerticleBuilders serviceBuilders = initContext.getServiceVerticleBuilders();
                Set<ObjectFactory<ServiceVerticleBuilder<?, ?>>> serviceBuilderFactories = initContext.getServiceBuilderFactories();
                if (serviceBuilderFactories != null && serviceBuilderFactories.size() > 0) {
                    for (ObjectFactory<ServiceVerticleBuilder<?, ?>> factory : serviceBuilderFactories) {
                        factory.getObject().register(serviceBuilderRegistry);
                    }
                }
                for (String name : serviceBuilderRegistry.getNames()) {
                    serviceBuilders.put(name, serviceBuilderRegistry.get(name));
                }

                Future<Void> initInternalReq = Future.future();
                init(initContext, initInternalReq);
                boolean isMonitoringEnabled = isMonitoringEnabled(initContext.getConfig().getHttpMonitorServiceConfig());
                HttpMonitorServiceVerticleBuilder httpMonitorBuilder = isMonitoringEnabled ? initContext.getMonitorServiceVerticleBuilder() : null;
                context = ContextImpl.create(initContext.getVertx(), httpMonitorBuilder, initContext);
                return initInternalReq;
            } catch (Exception exc) {
                return Future.failedFuture(exc);
            }
        }).compose(map -> {
            try {
                Future<Void> runInternalReq = Future.future();
                run(context, initContext.getConfig(), runInternalReq);
                return runInternalReq;
            } catch (Exception exc) {
                return Future.failedFuture(exc);
            }
        }).compose(map -> {

            // Create services
            HttpMonitorServiceVerticleBuilder monitorServiceBuilder = initContext.getMonitorServiceVerticleBuilder();
            ServiceVerticleBuilders serviceBuilders = initContext.getServiceVerticleBuilders();
            services = new ServiceVerticles();
            for (String name : serviceBuilders.getNames()) {
                ServiceVerticle service = serviceBuilders.get(name).build();
                if (monitorServiceBuilder != null && service instanceof HealthCheckProvider) {
                    monitorServiceBuilder.getContext().addHealthCheckProvider((HealthCheckProvider) service);
                }
                services.getVerticles().put(name, service);
            }

            // Start service monitoring
            if (monitorServiceBuilder != null) {
                monitorServiceVerticle = monitorServiceBuilder.build();
                Future<String> monitorDeployFuture = Future.future();
                initContext.getVertx().deployVerticle(monitorServiceVerticle, monitorDeployFuture);
                return monitorDeployFuture;
            } else {
                return Future.succeededFuture();
            }
        }).compose(map -> {
            // Start services
            @SuppressWarnings("rawtypes")
            List<Future> deployFutures = new ArrayList<>();
            for (String name : serviceBuilderRegistry.getNames()) {
                Future<String> serviceDeployFuture = Future.future();
                deployFutures.add(serviceDeployFuture);
                initContext.getVertx().deployVerticle(services.getVerticles().get(name), serviceDeployFuture);
            }

            // All services must be started before proceeding
            return CompositeFuture.all(deployFutures);
        }).setHandler(startup -> {
            if (startup.succeeded()) {
                startupFuture.complete();
            } else {
                startupFuture.fail(startup.cause());
            }
            startupLatch.countDown();
        });

        C configuration = initContext.getConfig();
        if (!startupLatch.await(initContext.getConfig().getStartupTimeout(), TimeUnit.MILLISECONDS)) {
            String msg = String.format("Timeout expired %ds", configuration.getStartupTimeout());
            logger.debug("Error running application {}: {}", configuration.getApplicationName(), msg);
            throw new Exception(msg);
        } else {
            if (startupFuture.succeeded()) {
                logger.info("Application {} running", configuration.getApplicationName());
            } else {
                logger.error("Error running application {}: {}", configuration.getApplicationName(), startupFuture.cause().getMessage());
                throw new Exception(startupFuture.cause());
            }
        }
    }

    /**
     * Do not call this method yourself. Override it if you expect that it will take a short time to execute
     * 
     * @param context
     * @param config
     * @throws Exception
     */
    public void init(InitContext<C> context) throws Exception {
    }

    /**
     * This method is called by {@link #run(ApplicationArguments)} before creation of service verticles. Override
     * this method if you want for example to add ServiceVerticleBuilders to the application.
     * Do not call this method yourself. Override it if you expect that initInternal will take long time to execute
     * 
     * @param context the initialization context of the application
     * @param initFuture a future that should be called when init is complete
     * @throws Exception
     */
    public void init(InitContext<C> context, Future<Void> initFuture) throws Exception {
        init(context);
        initFuture.complete();
    }

    /*
     * Do not call this method yourself. Override it if you expect that it will take a short time to execute
     */
    public void run(Context context, C config) throws Exception {
    }

    /**
     * This method is called by {@link #run(ApplicationArguments)} before deployment of service verticles. Override
     * this method if you want for example to add controllers to a specific service.
     * Do not call this method yourself. Override it if you expect that initInternal will take long time to execute
     * 
     * @param context the context of the application
     * @param config the configuration of the application
     * @param runFuture a future that should be called when run is complete
     * @throws Exception
     */
    public void run(Context context, C config, Future<Void> runFuture) throws Exception {
        run(context, config);
        runFuture.complete();
    }

    private boolean isMonitoringEnabled(HttpMonitorServiceConfig aMonitorConfig) {
        Objects.requireNonNull(aMonitorConfig);
        return aMonitorConfig.isHealthCheckEnable() || aMonitorConfig.isMetricsEnable();
    }
}
