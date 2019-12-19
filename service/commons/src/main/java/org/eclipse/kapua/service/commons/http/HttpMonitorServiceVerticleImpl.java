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

import org.eclipse.kapua.service.commons.AbstractServiceVerticle;
import org.eclipse.kapua.service.commons.BuilderRegistry;
import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;

public class HttpMonitorServiceVerticleImpl extends AbstractServiceVerticle implements HttpMonitorServiceVerticle {

    private static Logger logger = LoggerFactory.getLogger(HttpMonitorServiceVerticleImpl.class);

    public static class Builder implements HttpMonitorServiceContext, HttpMonitorServiceVerticleBuilder {

        private HttpMonitorServiceConfig config;
        private Set<HealthCheckProvider> providers = new HashSet<>();

        public Builder() {
            this(new HttpMonitorServiceConfig());
        }

        public Builder(HttpMonitorServiceConfig config) {
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
        public HttpMonitorServiceVerticle build() {
            Objects.requireNonNull(config, "member: config");
            return new HttpMonitorServiceVerticleImpl(this);
        }

        @Override
        public void register(BuilderRegistry aRegistry) {
            Objects.requireNonNull(aRegistry, "aRegistry");
            aRegistry.register(config.getName(), this);
        }

        public HttpMonitorServiceConfig getConfig() {
            return config;
        }

        public Set<HealthCheckProvider> getProviders() {
            return providers;
        }
    }

    private HttpMonitorService service;
    private Builder builder;

    private HttpMonitorServiceVerticleImpl(Builder aBuilder) {
        Objects.requireNonNull(aBuilder, "param: builder");
        builder = aBuilder;
    }

    @Override
    public String getName() {
        return builder.getConfig().getName();
    }

    @Override
    public void internalStart(Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "param: startFuture");

        Future<Void> localStartFuture = Future.future();
        localStartFuture.setHandler(startReq -> {
            if (startReq.succeeded()) {
                logger.info("HTTP monitoring service started, name {}, address {}, port {}, {}",
                        builder.getConfig().getName(),
                        builder.getConfig().getEndpoint().getBindAddress(),
                        builder.getConfig().getEndpoint().getPort(),
                        builder.getConfig().getEndpoint().isSsl() ? "Secure mode" : "Insecure mode");
                startFuture.complete();
            } else {
                logger.warn("Error starting monitoring service, name {}: {}", builder.getConfig().getName(), startReq.cause().getMessage(), startReq.cause());
                startFuture.fail(startReq.cause());
            }
        });

        service = HttpMonitorService.create(vertx, builder.getConfig());
        service.addHealthCheckProviders(builder.getProviders());
        service.start(localStartFuture);
    }

    @Override
    public void internalStop(@NotNull Future<Void> stopFuture) throws Exception {
        Objects.requireNonNull(stopFuture, "param: stopFuture");

        // Stop the deployment
        if (service != null) {
            Future<Void> localStopFuture = Future.future();
            localStopFuture.setHandler(stopReq -> {
                if (stopReq.succeeded()) {
                    logger.info("HTTP monitoring service stopped, name {}, address {}, port {}, {}",
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
            service.stop(stopFuture);
            return;
        }
        stopFuture.complete();
    }
}
