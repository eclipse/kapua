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

/**
 * Base class to implement an {@link EBServer} verticle.
 *
 */
public class AbstractEBServer extends AbstractVerticle implements EBServer {

    private static Logger logger = LoggerFactory.getLogger(AbstractEBServer.class);

    private EventBus eventBus;
    private EBRequestDispatcher messageDispatcher;
    private List<EBRequestHandlerProvider> handlerProviders = new ArrayList<>();

    private EBServerConfig configs;

    public EBServerConfig getConfigs() {
        return configs;
    }

    public void setConfigs(EBServerConfig configs) {
        this.configs = configs;
    }

    public void registerHandlerProvider(EBRequestHandlerProvider provider) {
        handlerProviders.add(provider);
    }

    @Override
    public void start() throws Exception {
        super.start();

        eventBus = vertx.eventBus();
        messageDispatcher = EBRequestDispatcher.dispatcher(eventBus, getConfigs().getAddress());
        for(EBRequestHandlerProvider provider:handlerProviders) {
            provider.registerHandlers(messageDispatcher);
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
