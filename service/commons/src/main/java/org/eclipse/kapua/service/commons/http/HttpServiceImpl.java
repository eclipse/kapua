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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class HttpServiceImpl implements HttpService {

    private static Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);

    private Vertx vertx;
    private HttpServer server;
    private HttpServiceConfig config;
    private Set<HttpController> controllers;

    public static class Builder implements HttpServiceContext, HttpServiceBuilder {

        private Vertx vertx;
        private Set<HttpController> controllers = new HashSet<>();
        private HttpServiceConfig config;

        public Builder(Vertx vertx) {
            this(vertx, new HttpServiceConfig());
        }

        public Builder(Vertx vertx, HttpServiceConfig config) {
            this.vertx = vertx;
            this.config = config;
        }

        @Override
        public Builder addControllers(Set<HttpController> someControllers) {
            controllers.addAll(someControllers);
            return this;
        }

        @Override
        public Builder addController(HttpController aController) {
            controllers.add(aController);
            return this;
        }

        @Override
        public HttpServiceContext getContext() {
            return this;
        }

        @Override
        public HttpService build() {
            Objects.requireNonNull(vertx, "member: vertx");
            Objects.requireNonNull(config, "member: config");
            return new HttpServiceImpl(this);
        }
    }

    private HttpServiceImpl(@NotNull Builder builder) {
        Objects.requireNonNull(builder, "param: builder");
        this.vertx = builder.vertx;
        this.controllers = new HashSet<>(builder.controllers);
        this.config = builder.config;
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public void registerLivenessCheckers(HealthCheckHandler anHandler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void registerReadynessCheckers(HealthCheckHandler anHandler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void start(@NotNull Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "param: startFuture");

        logger.debug("Starting HTTP service {}", config);

        Router router = Router.router(vertx);
        registerRoutes(router);
        // TODO router.route().failureHandler(HttpServiceHandlers.failureHandler());

        server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(config.getEndpoint().getPort(), config.getEndpoint().getBindAddress(), listenReq -> {
                    if (listenReq.succeeded()) {
                        logger.debug("HTTP service started {}", config.getName());
                        startFuture.complete();
                    } else {
                        logger.debug("Error starting service {}: {}", config.getName(), listenReq.cause().getMessage());
                        startFuture.fail((listenReq.cause()));
                    }
                });
    }

    @Override
    public void stop(@NotNull Future<Void> stopFuture) throws Exception {
        Objects.requireNonNull(stopFuture, "param: stopFuture");

        logger.debug("Stopping HTTP service {}", config);

        // Stop the deployment
        if (server != null) {
            server.close(closeReq -> {
                if (closeReq.succeeded()) {
                    logger.debug("HTTP service stopped {}", config.getName());
                    stopFuture.complete();
                } else {
                    logger.debug("Error stopping service {}: {}", config.getName(), closeReq.cause().getMessage());
                    stopFuture.fail(closeReq.cause());
                }
            });
            return;
        }
        stopFuture.handle(Future.succeededFuture());
    }

    private void registerRoutes(@NotNull Router router) {
        Objects.requireNonNull(router, "param: router");

        for (HttpController endpoint : controllers) {
            // TODO check duplicate basepath
            Router subRouter = Router.router(vertx);
            subRouter.route().handler(BodyHandler.create());
            subRouter.route().handler(CorsHandler.create(""));
            // TODO Put Service Event
            endpoint.registerRoutes(subRouter);
            subRouter.route().failureHandler(HttpServiceHandlers::failureHandler);
            router.mountSubRouter(endpoint.getPath(), subRouter);
        }
    }
}
