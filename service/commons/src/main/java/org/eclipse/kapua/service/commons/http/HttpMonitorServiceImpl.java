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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.commons.HealthChecker;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;

public class HttpMonitorServiceImpl implements HttpMonitorService {

    private Vertx vertx;
    private HttpService service;
    private HttpMonitorServiceConfig config;
    private Set<HealthChecker> livenessCheckers;
    private Set<HealthChecker> readinessCheckers;

    public static class Builder implements HttpMonitorService.Builder {

        private Vertx vertx;
        private HttpMonitorServiceConfig config;
        private Set<HealthChecker> livenessCheckers = new HashSet<>();
        private Set<HealthChecker> readinessCheckers = new HashSet<>();

        public Builder(Vertx vertx) {
            this(vertx, new HttpMonitorServiceConfig());
        }

        public Builder(Vertx vertx, HttpMonitorServiceConfig config) {
            this.vertx = vertx;
            this.config = config;
        }

        @Override
        public void addLivenessChecker(HealthChecker checker) {
            livenessCheckers.add(checker);
        }

        @Override
        public void addReadinessChecker(HealthChecker checker) {
            readinessCheckers.add(checker);
        }

        @Override
        public void setServerConfig(HttpMonitorServiceConfig config) {
            this.config = config;
        }

        @Override
        public HttpMonitorService build() {
            return new HttpMonitorServiceImpl(this);
        }
    }

    private HttpMonitorServiceImpl(Builder builder) {
        this.vertx = builder.vertx;
        this.config = builder.config;
        this.livenessCheckers = builder.livenessCheckers;
        this.readinessCheckers = builder.readinessCheckers;
    }

    @Override
    public Set<HealthChecker> getLivenessCheckers() {
        return Collections.unmodifiableSet(livenessCheckers);
    }

    @Override
    public Set<HealthChecker> getReadinessCheckers() {
        return Collections.unmodifiableSet(readinessCheckers);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Set<HttpEndpoint> endpoints = new HashSet<>();

        // Add health checks endpoint
        if (config.isHealthCheckEnable()) {
            HealthCheckHandler livenessHandler = HealthCheckHandler.create(vertx);
            registerLivenessCheckers(livenessHandler);
            HealthCheckHandler readinessHandler = HealthCheckHandler.create(vertx);
            registerReadynessCheckers(readinessHandler);
            HealthChecksEndpoint healthCheckEndpoint = HealthChecksEndpoint.create(livenessHandler, readinessHandler);
            endpoints.add(healthCheckEndpoint);
        }

        // Configure Metrics
        if (config.isMetricsEnable()) {
            MetricsEndpoint meter = MetricsEndpoint.create(MetricsService.create(vertx));
            endpoints.add(meter);
        }

        try {
            HttpService.Builder builder = HttpService.builder(vertx, config.getHttp());
            for (HttpEndpoint endpoint : endpoints) {
                builder.addEndpoint(endpoint);
            }
            service = builder.build();
            service.start(startFuture);
        } catch (Exception e) {
            startFuture.fail(e);
        }
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        // Stop the deployment
        if (service != null) {
            service.stop(stopFuture);
            return;
        }
        stopFuture.complete();
    }

    private void registerLivenessCheckers(HealthCheckHandler handler) {
        for (HealthChecker checker : livenessCheckers) {
            checker.registerChecks(vertx, handler);
        }
    }

    private void registerReadynessCheckers(HealthCheckHandler handler) {
        for (HealthChecker checker : readinessCheckers) {
            checker.registerChecks(vertx, handler);
        }
    }

    private static class HealthChecksEndpoint implements HttpEndpoint {

        private static final String HEALTHCHECK_LIVENESS_PATH = "/alive";
        private static final String HEALTHCHECK_READINESS_PATH = "/ready";
        private static final String BASE_PATH = "/monitoring";

        private HealthCheckHandler livenessHandler;
        private HealthCheckHandler readinessHandler;

        private HealthChecksEndpoint(HealthCheckHandler livenessHandler, HealthCheckHandler readinessHandler) {
            this.livenessHandler = livenessHandler;
            this.readinessHandler = readinessHandler;
        }

        @Override
        public void registerRoutes(Router router) {
            router.get(HEALTHCHECK_READINESS_PATH).handler(readinessHandler);
            router.get(HEALTHCHECK_LIVENESS_PATH).handler(livenessHandler);
        }

        @Override
        public String getBasePath() {
            return BASE_PATH;
        }

        public static HealthChecksEndpoint create(HealthCheckHandler livenessHandler, HealthCheckHandler readynessHandler) {
            return new HealthChecksEndpoint(livenessHandler, readynessHandler);
        }
    }

    private static class MetricsEndpoint implements HttpEndpoint {

        private static final String METRICS_BASENAME_PATH = "/metrics/:base";
        private static final String METRICS_BASE_NAME_PARAM = "base";
        private static final String BASE_PATH = "/monitoring";

        private MetricsService service;

        private MetricsEndpoint(MetricsService service) {
            this.service = service;
        }

        @Override
        public void registerRoutes(Router router) {
            router.get(METRICS_BASENAME_PATH).handler(ctx -> {
                String baseName = ctx.request().getParam(METRICS_BASE_NAME_PARAM);
                ctx.response().end(this.service.getMetricsSnapshot(baseName).toBuffer());
            });
        }

        @Override
        public String getBasePath() {
            return BASE_PATH;
        }

        public static MetricsEndpoint create(MetricsService service) {
            return new MetricsEndpoint(service);
        }
    }
}
