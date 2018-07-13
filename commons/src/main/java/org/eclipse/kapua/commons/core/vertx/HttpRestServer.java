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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.kapua.commons.core.Configuration;

public class HttpRestServer extends AbstractVerticle implements HttpRestService {

    private static Logger logger = LoggerFactory.getLogger(HttpRestServer.class);

    private static final String HEALTH_PATH = "/health";
    private static final String METRICS_PATH = "/metrics";
    private static final String METRICS_BASE_NAME_PARAM = "baseName";

    private List<HttpRouteProvider> endpoints = new ArrayList<>();
    private List<HealthCheckProvider> healthCheckProviders = new ArrayList<>();

    private HttpServer httpServer;
    private Router router;
    private HealthCheckHandler healthCheckHandler;

    private MetricsService metricsService;

    @Inject
    private Configuration config;

    @Inject
    @Named("vertx.metrics-root")
    private String metricsRoot;

    @Override
    public void start() throws Exception {
        logger.trace("Starting verticle...");
        super.start();

        this.router = Router.router(vertx);
        this.healthCheckHandler = HealthCheckHandler.create(vertx);
        this.router.get(HEALTH_PATH).handler(this.healthCheckHandler);

        this.metricsService = MetricsService.create(vertx);
        this.router.get(METRICS_PATH).handler(ctx -> {
            if (!vertx.isMetricsEnabled()) {
                logger.warn("Metrics are disabled");
                ctx.response().setStatusCode(404).end();
                return;
            }

            String metricPrefix = metricsRoot;
            String baseName = ctx.request().getParam(METRICS_BASE_NAME_PARAM);
            if (baseName != null) {
                metricPrefix = baseName;
            }
            ctx.response().end(this.metricsService.getMetricsSnapshot(metricPrefix).toBuffer());
        });

        for(HttpRouteProvider endpoint:endpoints) {
            endpoint.registerRoutes(router);
        }

        for(HealthCheckProvider provider:healthCheckProviders) {
            provider.registerHealthChecks(healthCheckHandler);
        }

        HttpServerOptions opts = new HttpServerOptions();
        opts.setHost(config.getProperty("http-server.host"));
        opts.setPort(Integer.parseInt(config.getProperty("http-server.port")));
        this.httpServer = vertx.createHttpServer(opts);

        this.httpServer.requestHandler(router::accept);

        this.httpServer.listen(ar -> {
            if (ar.succeeded()) {
                logger.info("Http server listening on port {}", this.httpServer.actualPort());
                logger.trace("Starting verticle...DONE");
            } else {
                logger.error("Starting verticle...FAILED", ar.cause());
            }
        });        
   }

    @Override
    public void stop() throws Exception {
        this.httpServer.close(ar -> {
            if (ar.succeeded()) {
                logger.info("Http server closed!");
            } else {
                logger.error("Error closing http server!");
            }
        });        
        super.stop();
    }

    @Override
    public void registerRouteProvider(HttpRouteProvider endpoint) {
        this.endpoints.add(endpoint);
    }

    @Override
    public void registerHealthCheckProvider(HealthCheckProvider provider) {
        this.healthCheckProviders.add(provider);
    }
}
