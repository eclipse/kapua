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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;

/**
 * Base class to implement an {@link EBServer} verticle.
 *
 */
public abstract class AbstractEBServer extends AbstractVerticle implements EBServer {

    private static Logger logger = LoggerFactory.getLogger(AbstractEBServer.class);

    private static final String HEALTH_ADDRESS = "health";

    private EventBus eventBus;
    private HealthChecks healthChecks;
    private HealthCheckHandler healthCheckHandler;

    private EBRequestDispatcher messageDispatcher;
    private List<EBRequestHandlerProvider> handlerProviders = new ArrayList<>();
    private List<HealthCheckProvider> healthCheckProviders = new ArrayList<>();

    public abstract EBServerConfig getConfigs();

    @Override
    public void registerHandlerProvider(EBRequestHandlerProvider provider) {
        this.handlerProviders.add(provider);
    }

    @Override
    public void registerHealthCheckProvider(HealthCheckProvider provider) {
        this.healthCheckProviders.add(provider);
    }

    @Override
    public void start() throws Exception {
        super.start();

        eventBus = vertx.eventBus();
        messageDispatcher = EBRequestDispatcher.dispatcher(eventBus, getConfigs().getAddress());
        for(EBRequestHandlerProvider provider:handlerProviders) {
            provider.registerHandlers(messageDispatcher);
        }

        healthChecks = HealthChecks.create(vertx);
        eventBus.consumer(getConfigs().getHealthCheckAddress(), message -> healthChecks.invoke(message::reply));

        healthCheckHandler = HealthCheckHandler.createWithHealthChecks(healthChecks);
        eventBus.consumer(HEALTH_ADDRESS, message -> healthChecks.invoke(message::reply));
        for(HealthCheckProvider provider:healthCheckProviders) {
            provider.registerHealthChecks(healthCheckHandler);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (eventBus != null) {
            eventBus.close(ar -> {
                if (ar.succeeded()) {
                    logger.trace("EventBus closed");
                } else {
                    logger.error("Error occured while closing event bus: {}", ar.cause().getMessage(), ar.cause());
                }
            });
        }
    }
}
