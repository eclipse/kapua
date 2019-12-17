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

import org.eclipse.kapua.service.commons.BuilderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PfxOptions;
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
    private Router mainRouter;

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

        @Override
        public void register(BuilderRegistry aRegistry) {
            Objects.requireNonNull(aRegistry, "aRegistry");
            aRegistry.register(config.getName(), this);
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

        mainRouter = Router.router(vertx);
        Router rootRouter = mainRouter;
        if (config.getRootPath() != null) {
            rootRouter = Router.router(vertx);
        }

        rootRouter.route().handler(BodyHandler.create());
        rootRouter.route().handler(CorsHandler.create(""));
        // TODO Make handler pluggable
        rootRouter.route().blockingHandler(HttpServiceHandlers::authenticationHandler);
        registerRoutes(rootRouter);
        rootRouter.route().failureHandler(HttpServiceHandlers::failureHandler);
        mainRouter.mountSubRouter(config.getRootPath(), rootRouter);

        if (config == null || config.getEndpoint() == null) {
            throw new IllegalStateException("Invalid http service configuration");
        }

        HttpServerOptions serverOpts = new HttpServerOptions()
                .setPort(config.getEndpoint().getPort())
                .setHost(config.getEndpoint().getBindAddress());

        // TLS
        serverOpts.setSsl(config.getEndpoint().isSsl());
        serverOpts
                .removeEnabledSecureTransportProtocol("SSLv2Hello")
                .removeEnabledSecureTransportProtocol("TLSv1")
                .removeEnabledSecureTransportProtocol("TLSv1.1");

        if (config.getEndpoint().getKeyStorePath() != null) {
            serverOpts.setPfxKeyCertOptions(new PfxOptions()
                    .setPath(config.getEndpoint().getKeyStorePath())
                    .setPassword(config.getEndpoint().getKeyStorePassword()));
            logger.info("PFX KeyStore loaded: {}", config.getEndpoint().getKeyStorePath());
        }

        // Mutual Auth
        serverOpts.setClientAuth(config.getEndpoint().getClientAuth());
        if (config.getEndpoint().getTrustStorePath() != null) {
            serverOpts.setPfxTrustOptions(new PfxOptions()
                    .setPath(config.getEndpoint().getTrustStorePath())
                    .setPassword(config.getEndpoint().getTrustStorePassword()));
            logger.info("PFX TrustStore loaded: {}", config.getEndpoint().getTrustStorePath());
        }

        server = vertx.createHttpServer(serverOpts)
                .requestHandler(mainRouter)
                .listen(listenReq -> {
                    if (listenReq.succeeded()) {
                        logger.info("HTTP service started {}, address {}, port {}, {}",
                                config.getName(),
                                config.getEndpoint().getBindAddress(),
                                config.getEndpoint().getPort(),
                                config.getEndpoint().isSsl() ? "Secure mode" : "Insecure mode");
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

    private void registerRoutes(@NotNull Router aRouter) {
        Objects.requireNonNull(aRouter, "param: router");

        for (HttpController controller : controllers) {
            controller.registerRoutes(aRouter);
        }
    }
}
