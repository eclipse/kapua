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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.DefaultBuilderRegistry;
import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.ServiceVerticle;
import org.eclipse.kapua.service.commons.ServiceVerticleBuilder;
import org.eclipse.kapua.service.commons.ServiceVerticleBuilders;
import org.eclipse.kapua.service.commons.ServiceVerticles;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceContext;
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
import io.vertx.core.Vertx;

public class BaseApplication<C extends Configuration> implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    private static final class ContextImpl implements Context {

        private Vertx vertx;

        private HttpMonitorServiceVerticleBuilder monitorServiceBuilder;

        private ServiceVerticleBuilders serviceBuilders;

        private ContextImpl(@NotNull Vertx aVertx, @NotNull HttpMonitorServiceVerticleBuilder aMonitorServiceBuilder, @NotNull ServiceVerticleBuilders someServiceBuilders) {
            vertx = aVertx;
            monitorServiceBuilder = aMonitorServiceBuilder;
            serviceBuilders = someServiceBuilders;
        }

        @Override
        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public HttpMonitorServiceContext getMonitorServiceContext() {
            return monitorServiceBuilder.getContext();
        }

        @Override
        public <C> C getServiceContext(String name, Class<C> clazz) {
            return clazz.cast(serviceBuilders.getBuilders().get(name).getContext());
        }

        public static ContextImpl create(@NotNull Vertx aVertx, @NotNull HttpMonitorServiceVerticleBuilder aMonitorServiceBuilder, @NotNull ServiceVerticleBuilders someServiceBuilders) {
            Objects.requireNonNull(aVertx, "param: aVertx");
            // Objects.requireNonNull(aMonitorServiceBuilder, "param: aMonitorServiceBuilder");
            Objects.requireNonNull(someServiceBuilders, "param: someServiceBuilders");
            return new ContextImpl(aVertx, aMonitorServiceBuilder, someServiceBuilders);
        }
    }

    private Vertx vertx;
    private C configuration;
    private ContextImpl context;
    private ServiceVerticles services;
    private HttpMonitorServiceVerticle monitorServiceVerticle;
    private Set<ObjectFactory<ServiceVerticleBuilder<?, ?>>> serviceBuilderFactories = new HashSet<>();
    private DefaultBuilderRegistry serviceBuilderRegistry = new DefaultBuilderRegistry();

    @Autowired
    public void setVertx(Vertx aVertx) {
        Objects.requireNonNull(aVertx, "param: aVertx");
        vertx = aVertx;
    }

    @Autowired
    public void setConfig(C aConfig) {
        Objects.requireNonNull(aConfig, "param: aConfig");
        configuration = aConfig;
    }

    @Autowired
    public void setServiceBuilderFactories(Set<ObjectFactory<ServiceVerticleBuilder<?, ?>>> aServiceBuilderFactories) {
        Objects.requireNonNull(serviceBuilderFactories, "param: serviceBuilderFactories");
        serviceBuilderFactories.addAll(aServiceBuilderFactories);
    }

    @Override
    public final void run(ApplicationArguments args) throws Exception {

        logger.info("Starting application {}", configuration);
        Future<Void> startupFuture = Future.future();
        CountDownLatch startupLatch = new CountDownLatch(1);

        Future.succeededFuture().compose(map -> {
            try {
                ServiceVerticleBuilders serviceBuilders = new ServiceVerticleBuilders();
                for (ObjectFactory<ServiceVerticleBuilder<?, ?>> factory : serviceBuilderFactories) {
                    factory.getObject().register(serviceBuilderRegistry);
                }

                for (String name : serviceBuilderRegistry.getNames()) {
                    serviceBuilders.getBuilders().put(name, serviceBuilderRegistry.get(name));
                }

                HttpMonitorServiceVerticleBuilder httpMonitorBuilder = null;
                HttpMonitorServiceConfig monitorConfig = configuration.getHttpMonitorServiceConfig();
                if (isMonitoringEnabled(monitorConfig)) {
                    httpMonitorBuilder = HttpMonitorServiceVerticle.builder(monitorConfig);
                }

                Future<Void> runInternalReq = Future.future();
                context = ContextImpl.create(vertx, httpMonitorBuilder, serviceBuilders);
                runInternal(context, configuration, runInternalReq);
                return runInternalReq;
            } catch (Exception exc) {
                return Future.failedFuture(exc);
            }
        }).compose(map -> {

            // Create services
            HttpMonitorServiceVerticleBuilder monitorServiceBuilder = context.monitorServiceBuilder;
            ServiceVerticleBuilders serviceBuilders = context.serviceBuilders;
            services = new ServiceVerticles();
            for (String name : serviceBuilders.getBuilders().keySet()) {
                ServiceVerticle service = context.serviceBuilders.getBuilders().get(name).build();
                if (monitorServiceBuilder != null && service instanceof HealthCheckProvider) {
                    monitorServiceBuilder.getContext().addHealthCheckProvider((HealthCheckProvider) service);
                }
                services.getVerticles().put(name, service);
            }

            // Start service monitoring
            if (monitorServiceBuilder != null) {
                monitorServiceVerticle = monitorServiceBuilder.build();
                Future<String> monitorDeployFuture = Future.future();
                vertx.deployVerticle(monitorServiceVerticle, monitorDeployFuture);
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
                vertx.deployVerticle(services.getVerticles().get(name), serviceDeployFuture);
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

        if (!startupLatch.await(configuration.getStartupTimeout(), TimeUnit.MILLISECONDS)) {
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

    protected void runInternal(Context context, C config) throws Exception {
    }

    protected void runInternal(Context context, C config, Future<Void> runFuture) throws Exception {
    }

    private boolean isMonitoringEnabled(HttpMonitorServiceConfig aMonitorConfig) {
        Objects.requireNonNull(aMonitorConfig);
        return aMonitorConfig.isHealthCheckEnable() || aMonitorConfig.isMetricsEnable();
    }
}
