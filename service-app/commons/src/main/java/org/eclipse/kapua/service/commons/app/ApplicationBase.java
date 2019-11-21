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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.Service;
import org.eclipse.kapua.service.commons.ServiceBuilders;
import org.eclipse.kapua.service.commons.ServiceConfig;
import org.eclipse.kapua.service.commons.ServiceVerticle;
import org.eclipse.kapua.service.commons.Services;
import org.eclipse.kapua.service.commons.http.HttpMonitorService;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceBuilder;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

public class ApplicationBase<C extends Configuration> implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(ApplicationBase.class);

    private static final class ContextImpl implements Context {

        private Vertx vertx;

        private HttpMonitorServiceBuilder monitorServiceBuilder;

        private ServiceBuilders serviceBuilders;

        private ContextImpl(@NotNull Vertx aVertx, @NotNull HttpMonitorServiceBuilder aMonitorServiceBuilder, @NotNull ServiceBuilders someServiceBuilders) {
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

        public static ContextImpl create(@NotNull Vertx aVertx, @NotNull HttpMonitorServiceBuilder aMonitorServiceBuilder, @NotNull ServiceBuilders someServiceBuilders) {
            Objects.requireNonNull(aVertx, "param: aVertx");
            Objects.requireNonNull(aMonitorServiceBuilder, "param: aMonitorServiceBuilder");
            Objects.requireNonNull(someServiceBuilders, "param: someServiceBuilders");
            return new ContextImpl(aVertx, aMonitorServiceBuilder, someServiceBuilders);
        }
    }

    private Vertx vertx;
    private C configuration;
    private ServiceBuilderManager serviceBuilderManager;
    private ContextImpl context;
    private Services services;
    private HttpMonitorService monitorService;

    @Autowired
    public void setConfig(C aConfig) {
        Objects.requireNonNull(aConfig, "param: aConfig");
        configuration = aConfig;
    }

    @Autowired
    public void setServiceBuilderManager(ServiceBuilderManager aManager) {
        Objects.requireNonNull(aManager, "param: aManager");
        serviceBuilderManager = aManager;
    }

    @Override
    public final void run(ApplicationArguments args) throws Exception {

        logger.info("Starting application {}", configuration);

        vertx = createVertx(configuration.getVertxConfig());

        ServiceBuilders serviceBuilders = new ServiceBuilders();
        for (String name : configuration.getServiceConfigs().getConfigs().keySet()) {
            ServiceConfig serviceConfig = configuration.getServiceConfigs().getConfigs().get(name);
            serviceBuilders.getBuilders().put(name, serviceBuilderManager.create(vertx, serviceConfig));
        }

        HttpMonitorServiceBuilder httpMonitorBuilder = HttpMonitorService.builder(vertx, configuration.getHttpMonitorServiceConfig());
        context = ContextImpl.create(vertx, httpMonitorBuilder, serviceBuilders);
        Future<Void> startupFuture = Future.future();
        CountDownLatch startupLatch = new CountDownLatch(1);

        Future.succeededFuture().compose(map -> {
            try {
                Future<Void> runInternalReq = Future.future();
                runInternal(context, configuration, runInternalReq);
                return runInternalReq;
            } catch (Exception exc) {
                return Future.failedFuture(exc);
            }
        }).compose(map -> {

            // Create http services
            services = new Services();
            for (String name : configuration.getServiceConfigs().getConfigs().keySet()) {
                Service service = serviceBuilders.getBuilders().get(name).build();
                if (service instanceof HealthCheckProvider) {
                    httpMonitorBuilder.getContext().addHealthCheckProvider((HealthCheckProvider) service);
                }
                services.getServices().put(name, service);
            }

            // Start service monitoring
            monitorService = httpMonitorBuilder.build();

            Future<String> monitorDeployFuture = Future.future();
            vertx.deployVerticle(ServiceVerticle.create(monitorService), monitorDeployFuture);
            return monitorDeployFuture;
        }).compose(map -> {
            // Start services
            @SuppressWarnings("rawtypes")
            List<Future> deployFutures = new ArrayList<>();
            for (String name : configuration.getServiceConfigs().getConfigs().keySet()) {
                for (int i = 0; i < configuration.getServiceConfigs().getConfigs().get(name).getInstances(); i++) {
                    Future<String> userServiceDeployFuture = Future.future();
                    deployFutures.add(userServiceDeployFuture);
                    vertx.deployVerticle(ServiceVerticle.create(services.getServices().get(name)), userServiceDeployFuture);
                }
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

    private Vertx createVertx(VertxConfig config) {
        Objects.requireNonNull(config, "param: config");
        VertxConfig vertxConfig = config;
        MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(vertxConfig.getMetrics().getRegistryName());
        SharedMetricRegistries.setDefault(vertxConfig.getMetrics().getRegistryName(), metricRegistry);

        DropwizardMetricsOptions metrOpts = new DropwizardMetricsOptions();
        metrOpts.setEnabled(config.getMetrics().isEnable());
        metrOpts.setRegistryName(vertxConfig.getMetrics().getRegistryName());

        VertxOptions opts = new VertxOptions();
        opts.setWarningExceptionTime(vertxConfig.getWarningExceptionTime());
        opts.setBlockedThreadCheckInterval(vertxConfig.getBlockedThreadCheckInterval());
        opts.setMetricsOptions(metrOpts);
        return Vertx.vertx(opts);
    }
}
