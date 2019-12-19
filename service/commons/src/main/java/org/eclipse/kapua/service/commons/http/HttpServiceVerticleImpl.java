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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.kapua.service.commons.AbstractServiceVerticle;
import org.eclipse.kapua.service.commons.BuilderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

class HttpServiceVerticleImpl extends AbstractServiceVerticle implements HttpServiceVerticle {

    private static Logger logger = LoggerFactory.getLogger(HttpServiceVerticleImpl.class);

    public static class Builder implements HttpServiceContext, HttpServiceVerticleBuilder {

        private Set<HttpController> controllers = new HashSet<>();
        private Handler<RoutingContext> authHandler;
        private HttpServiceConfig config;

        public Builder() {
            this(new HttpServiceConfig());
        }

        public Builder(HttpServiceConfig config) {
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
        public Builder setAuthHandler(Handler<RoutingContext> anHandler) {
            authHandler = anHandler;
            return this;
        }

        @Override
        public HttpServiceContext getContext() {
            return this;
        }

        @Override
        public void register(BuilderRegistry aRegistry) {
            Objects.requireNonNull(aRegistry, "aRegistry");
            aRegistry.register(config.getName(), this);
        }

        @Override
        public HttpServiceVerticleImpl build() {
            Objects.requireNonNull(config, "member: config");
            if (config.getInstances() < 1) {
                throw new IllegalArgumentException("Number of instances must be higher than zero");
            }
            return new HttpServiceVerticleImpl(this);
        }

        public HttpServiceConfig getConfig() {
            return config;
        }

        public Set<HttpController> getControllers() {
            return controllers;
        }

        public Handler<RoutingContext> getAuthHandler() {
            return authHandler;
        }
    }

    private Builder builder;
    private HttpService service;

    private HttpServiceVerticleImpl(Builder aBuilder) {
        builder = aBuilder;
    }

    @Override
    public String getName() {
        return builder.getConfig().getName();
    }

    @Override
    public void internalStart(Future<Void> startFuture) throws Exception {
        if (builder.getConfig().getInstances() < 1) {
            IllegalStateException exc = new IllegalStateException("Number of instances must be higher than zero");
            logger.warn("Error starting service {}: {}",
                    builder.getConfig().getName(),
                    exc.getMessage(),
                    exc);
            startFuture.fail(exc);
            return;
        }

        if (builder.getConfig().getInstances() == 1) {
            service = HttpService.create(vertx, builder.getConfig());
            service.addControllers(builder.getControllers());
            service.setAuthHandler(builder.getAuthHandler());
            try {
                Future<Void> localStartFuture = Future.future();
                localStartFuture.setHandler(startReq -> {
                    if (startReq.succeeded()) {
                        logger.info("HTTP service started, name {}, address {}, port {}, {}",
                                builder.getConfig().getName(),
                                builder.getConfig().getEndpoint().getBindAddress(),
                                builder.getConfig().getEndpoint().getPort(),
                                builder.getConfig().getEndpoint().isSsl() ? "Secure mode" : "Insecure mode");
                        startFuture.complete();
                    } else {
                        logger.warn("Error starting service, name {}: {}",
                                builder.getConfig().getName(),
                                startReq.cause().getMessage(),
                                startReq.cause());
                        startFuture.fail(startReq.cause());
                    }
                });
                service.start(localStartFuture);
            } catch (Exception e) {
                startFuture.fail(e);
            }
            return;
        }

        @SuppressWarnings("rawtypes")
        List<Future> deployFuts = new ArrayList<>();
        HttpServiceConfig singleInstanceConfig = HttpServiceConfig.from(builder.getConfig());
        singleInstanceConfig.setInstances(1);
        for (int i = 0; i < builder.getConfig().getInstances(); i++) {
            Future<Void> deployFut = Future.future();
            deployFuts.add(deployFut);
            vertx.deployVerticle(new Builder(singleInstanceConfig)
                    .addControllers(builder.getControllers())
                    .setAuthHandler(builder.getAuthHandler())
                    .build(), deployReq -> {
                        if (deployReq.succeeded()) {
                            deployFut.complete();
                        } else {
                            deployFut.fail(deployReq.cause());
                        }
                    });
        }
        CompositeFuture.all(deployFuts).setHandler(deployReqs -> {
            if (deployReqs.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(deployReqs.cause());
            }
        });
    }

    @Override
    public void internalStop(Future<Void> stopFuture) throws Exception {
        if (service != null) {
            Future<Void> localStopFuture = Future.future();
            localStopFuture.setHandler(stopReq -> {
                if (stopReq.succeeded()) {
                    logger.info("HTTP service stopped, name {}, address {}, port {}, {}",
                            builder.getConfig().getName(),
                            builder.getConfig().getEndpoint().getBindAddress(),
                            builder.getConfig().getEndpoint().getPort(),
                            builder.getConfig().getEndpoint().isSsl() ? "Secure mode" : "Insecure mode");
                    stopFuture.complete();
                } else {
                    logger.warn("Error stopping monitoring service, name {}: {}",
                            builder.getConfig().getName(),
                            stopReq.cause().getMessage(),
                            stopReq.cause());
                    stopFuture.fail(stopReq.cause());
                }
            });
            service.stop(localStopFuture);
            return;
        }
        stopFuture.complete();
    }
}
