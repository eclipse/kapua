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

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;

/**
 * Base class to implement an {@link HttpService}
 * <p>
 * It exposes two predefined http endpoints, one to execute health 
 * checks and one to retrieve metrics.
 *
 */
public abstract class AbstractHttpService implements HttpService {

    private static Logger logger = LoggerFactory.getLogger(AbstractHttpService.class);

    private static final String HEALTH_PATH = "/health";
    private static final String METRICS_PATH = "/metrics";
    private static final String METRICS_BASE_NAME_PARAM = "baseName";

    private List<HttpServiceAdapter> routeAdapters = new ArrayList<>();
    private List<HealthCheckAdapter> healthCheckAdapters = new ArrayList<>();

    private Vertx vertx;
    private HttpServiceConfig config;
    private HttpServer httpServer;
    private Router router;
    private HealthCheckHandler healthCheckHandler;

    private MetricsService metricsService;

    protected AbstractHttpService(Vertx aVertx, HttpServiceConfig aConfig) {
        vertx = aVertx;
        config = aConfig;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        logger.trace("Starting service...");
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

            String metricPrefix = config.getMetricsRoot();
            String baseName = ctx.request().getParam(METRICS_BASE_NAME_PARAM);
            if (baseName != null) {
                metricPrefix = baseName;
            }
            ctx.response().end(this.metricsService.getMetricsSnapshot(metricPrefix).toBuffer());
        });

        for(HttpServiceAdapter adapter:routeAdapters) {
            adapter.register(router);
        }

        for(HealthCheckAdapter adapter:healthCheckAdapters) {
            adapter.register(healthCheckHandler);
        }

        HttpServerOptions opts = new HttpServerOptions();
        opts.setHost(config.getHost());
        opts.setPort(config.getPort());
        this.httpServer = vertx.createHttpServer(opts);

        this.httpServer.requestHandler(router::accept);

        this.httpServer.listen(ar -> {
            if (ar.succeeded()) {
                logger.info("Http server listening on port {}", this.httpServer.actualPort());
                logger.trace("Starting service...DONE");
                startFuture.complete();
            } else {
                logger.error("Starting service...FAILED", ar.cause());
                startFuture.fail(ar.cause());
            }
        });        
   }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        if (httpServer != null) {
            httpServer.close(ar -> {
                if (ar.succeeded()) {
                    logger.info("Http server closed!");
                    stopFuture.complete();
                } else {
                    logger.error("Error closing http server!");
                    stopFuture.fail(ar.cause());
                }
            });        
        } else {
            stopFuture.complete();
        }
    }

    @Override
    public void register(HttpServiceAdapter endpoint) {
        this.routeAdapters.add(endpoint);
    }

    @Override
    public void register(HealthCheckAdapter provider) {
        this.healthCheckAdapters.add(provider);
    }
}
