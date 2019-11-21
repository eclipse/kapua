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
package org.eclipse.kapua.service.commons.http;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.HealthCheckProvider;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;

public class HttpMonitorServiceImpl implements HttpMonitorService {

    private Vertx vertx;
    private HttpService service;
    private HttpMonitorServiceConfig config;
    private Set<HealthCheckProvider> providers = new HashSet<>();

    public static class Builder implements HttpMonitorServiceContext, HttpMonitorServiceBuilder {

        private Vertx vertx;
        private HttpMonitorServiceConfig config;
        private Set<HealthCheckProvider> providers = new HashSet<>();

        public Builder(Vertx vertx) {
            this(vertx, new HttpMonitorServiceConfig());
        }

        public Builder(Vertx vertx, HttpMonitorServiceConfig config) {
            this.vertx = vertx;
            this.config = config;
        }

        @Override
        public Builder addHealthCheckProviders(Set<HealthCheckProvider> someProviders) {
            providers.addAll(someProviders);
            return this;
        }

        @Override
        public Builder addHealthCheckProvider(HealthCheckProvider aProvider) {
            providers.add(aProvider);
            return this;
        }

        @Override
        public HttpMonitorServiceContext getContext() {
            return this;
        }

        @Override
        public HttpMonitorService build() {
            Objects.requireNonNull(vertx, "member: vertx");
            Objects.requireNonNull(config, "member: config");
            return new HttpMonitorServiceImpl(this);
        }
    }

    private HttpMonitorServiceImpl(Builder builder) {
        Objects.requireNonNull(builder, "param: builder");
        vertx = builder.vertx;
        config = builder.config;
        providers = builder.providers;
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "param: startFuture");

        Set<HttpController> controllers = new HashSet<>();

        // Add health checks controllers
        if (config.isHealthCheckEnable()) {
            HealthCheckHandler livenessHandler = HealthCheckHandler.create(vertx);
            HealthCheckHandler readynessHandler = HealthCheckHandler.create(vertx);
            registerCheckers(livenessHandler, readynessHandler);

            HealthCheckController healthCheckController = HealthCheckController.create(livenessHandler, readynessHandler);
            controllers.add(healthCheckController);
        }

        // Configure Metrics
        if (config.isMetricsEnable()) {
            MetricsController meter = MetricsController.create(MetricsService.create(vertx));
            controllers.add(meter);
        }

        try {
            HttpServiceConfig httpServiceConfig = new HttpServiceConfig();
            httpServiceConfig.setName(config.getName() + "httpService");
            httpServiceConfig.setInstances(1);
            httpServiceConfig.setEndpoint(config.getEndpoint());
            HttpServiceBuilder builder = HttpService.builder(vertx, httpServiceConfig);
            for (HttpController controller : controllers) {
                builder.getContext().addController(controller);
            }
            service = builder.build();
            service.start(startFuture);
        } catch (Exception e) {
            startFuture.fail(e);
        }
    }

    @Override
    public void stop(@NotNull Future<Void> stopFuture) throws Exception {
        Objects.requireNonNull(stopFuture, "param: stopFuture");

        // Stop the deployment
        if (service != null) {
            service.stop(stopFuture);
            return;
        }
        stopFuture.complete();
    }

    private void registerCheckers(HealthCheckHandler livenessHandler, HealthCheckHandler readynessHandler) {
        for (HealthCheckProvider provider : providers) {
            provider.registerLivenessCheckers(livenessHandler);
            provider.registerReadynessCheckers(readynessHandler);
        }
    }

    private static class HealthCheckController implements HttpController {

        private static final String HEALTHCHECK_LIVENESS_PATH = "/alive";
        private static final String HEALTHCHECK_READINESS_PATH = "/ready";
        private static final String BASE_PATH = "/monitoring";

        private HealthCheckHandler livenessHandler;
        private HealthCheckHandler readinessHandler;

        private HealthCheckController(HealthCheckHandler livenessHandler, HealthCheckHandler readinessHandler) {
            this.livenessHandler = livenessHandler;
            this.readinessHandler = readinessHandler;
        }

        @Override
        public void registerRoutes(@NotNull Router router) {
            router.get(HEALTHCHECK_READINESS_PATH).handler(readinessHandler);
            router.get(HEALTHCHECK_LIVENESS_PATH).handler(livenessHandler);
        }

        @Override
        public String getPath() {
            return BASE_PATH;
        }

        public static HealthCheckController create(HealthCheckHandler livenessHandler, HealthCheckHandler readynessHandler) {
            return new HealthCheckController(livenessHandler, readynessHandler);
        }
    }

    private static class MetricsController implements HttpController {

        private static final String METRICS_BASENAME_PATH = "/metrics/:base";
        private static final String METRICS_BASE_NAME_PARAM = "base";
        private static final String BASE_PATH = "/monitoring";

        private MetricsService service;

        private MetricsController(MetricsService service) {
            this.service = service;
        }

        @Override
        public void registerRoutes(@NotNull Router router) {
            router.get(METRICS_BASENAME_PATH).handler(ctx -> {
                String baseName = ctx.request().getParam(METRICS_BASE_NAME_PARAM);
                ctx.response().end(this.service.getMetricsSnapshot(baseName).toBuffer());
            });
        }

        @Override
        public String getPath() {
            return BASE_PATH;
        }

        public static MetricsController create(MetricsService service) {
            return new MetricsController(service);
        }
    }
}
