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
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;

/**
 * Base class to implement an {@link EventBusService}.
 * <p>
 * It exposes two predefined event bus endpoints, one to execute health 
 * checks and one to retrieve metrics.
 */
public abstract class AbstractEventBusService implements EventBusService {

    private static Logger logger = LoggerFactory.getLogger(AbstractEventBusService.class);

    private static final String HEALTH_ADDRESS = "health";

    private Vertx vertx;
    private EventBusServiceConfig config;
    private EventBusServer eventBusServer;
    private HealthChecks healthChecks;
    private HealthCheckHandler healthCheckHandler;

    private EventBusDispatcher messageDispatcher;
    private List<EventBusServiceAdapter> handlerAdapters = new ArrayList<>();
    private List<HealthCheckAdapter> healthCheckAdapters = new ArrayList<>();

    protected AbstractEventBusService(Vertx aVertx, EventBusServiceConfig aConfig) {
        vertx = aVertx;
        config = aConfig;
    }

    @Override
    public void register(EventBusServiceAdapter provider) {
        this.handlerAdapters.add(provider);
    }

    @Override
    public void register(HealthCheckAdapter provider) {
        this.healthCheckAdapters.add(provider);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eventBus = vertx.eventBus();
        EventBusClientConfig clientConfig = new EventBusClientConfig();
        clientConfig.setAddress(config.getAddress());
        eventBusServer = EventBusServer.server(eventBus, clientConfig);
        messageDispatcher = EventBusDispatcher.dispatcher(vertx, eventBusServer);
        for(EventBusServiceAdapter adapter:handlerAdapters) {
            adapter.register(messageDispatcher);
        }

        healthChecks = HealthChecks.create(vertx);
        eventBus.consumer(config.getHealthCheckAddress(), message -> healthChecks.invoke(message::reply));

        healthCheckHandler = HealthCheckHandler.createWithHealthChecks(healthChecks);
        eventBus.consumer(HEALTH_ADDRESS, message -> healthChecks.invoke(message::reply));
        for(HealthCheckAdapter adapter:healthCheckAdapters) {
            adapter.register(healthCheckHandler);
        }
        eventBusServer.listen(clientConfig.getAddress());
        startFuture.complete();
    }

    public void stop(Future<Void> stopFuture) throws Exception {
        if (eventBusServer != null) {
            eventBusServer.close();
        }
        stopFuture.complete();
    }
}
