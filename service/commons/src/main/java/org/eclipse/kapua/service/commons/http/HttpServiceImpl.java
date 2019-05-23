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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HttpServiceImpl implements HttpService {

    private static Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);

    private Vertx vertx;
    private HttpServer server;
    private HttpServiceConfig config;
    private Set<HttpEndpoint> enpoints = new HashSet<>();
    private Handler<RoutingContext> authHandler;

    public static class Builder implements HttpService.Builder {

        private Vertx vertx;
        private Set<HttpEndpoint> enpoints = new HashSet<>();
        private Handler<RoutingContext> authHandler;
        private HttpServiceConfig config;

        public Builder(Vertx vertx) {
            this(vertx, new HttpServiceConfig());
        }

        public Builder(Vertx vertx, HttpServiceConfig config) {
            this.vertx = vertx;
            this.config = config;
        }

        @Override
        public void authHandler(Handler<RoutingContext> handler) {
            this.authHandler = handler;
        }

        @Override
        public void addEndpoint(HttpEndpoint endpoint) {
            enpoints.add(endpoint);
        }

        @Override
        public void setServiceConfig(HttpServiceConfig config) {
            this.config = config;
        }

        @Override
        public HttpService build() {
            return new HttpServiceImpl(this);
        }
    }

    private HttpServiceImpl(Builder builder) {
        this.vertx = builder.vertx;
        this.enpoints = new HashSet<>(builder.enpoints);
        this.config = builder.config;
        this.authHandler = builder.authHandler;
    }

    @Override
    public Set<HttpEndpoint> getEndpoints() {
        return Collections.unmodifiableSet(enpoints);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);
        registerRoutes(router);
        server = vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(config.getPort(), config.getBindAddress(), listenReq -> {
                        if (listenReq.succeeded()) {
                            logger.info("REST endpoint listening on port {} host {}", 
                                    config.getPort(), 
                                    config.getBindAddress());
                            startFuture.complete();
                        } else {
                            logger.error("Error starting REST endpoint on port {} host {}: {}", 
                                    config.getPort(), config.getBindAddress(), 
                                    listenReq.cause().getMessage());
                            startFuture.fail((listenReq.cause()));
                        }
                    });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        // Stop the deployment
        if (server != null) {
            server.close(close -> {
                if (close.succeeded()) {
                    stopFuture.complete();
                } else {
                    stopFuture.fail(close.cause());
                }
            });
            return;
        }
        stopFuture.handle(Future.succeededFuture());
    }

    private void registerRoutes(Router router) {
        if (this.authHandler != null) {
            router.route().blockingHandler(this.authHandler);
        }

        for (HttpEndpoint endpoint:enpoints) {
            endpoint.registerRoutes(router);
        }
    }
}
