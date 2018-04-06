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

import java.util.Objects;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * Implements an {@link EventBus} request server.
 * <p>
 * It is used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusServer {

    private EventBus eventBus;
    private EventBusClientConfig config;

    private MessageConsumer<EventBusServerRequest> consumer;
    private Handler<Message<EventBusServerRequest>> requestHandler;

    protected EventBusServer(EventBus anEventBus, EventBusClientConfig aConfig) {
        eventBus = anEventBus;
        config = aConfig;
    }

    public static EventBusServer server(EventBus anEventBus, EventBusClientConfig aConfig) {
        EventBusServer server = new EventBusServer(anEventBus, aConfig);
        return server;
    }

    public void setRequestHandler(Handler<Message<EventBusServerRequest>> handler) {
        this.requestHandler = handler;
    }

    public void listen(String address) {
        consumer = eventBus.consumer(address);
        consumer.handler(this::handle);
    }

    public void close() {
        consumer.unregister();
        consumer = null;
    }

    private void handle(Message<EventBusServerRequest> message) {
        Objects.requireNonNull(message, "Invalid null message");
        if (requestHandler != null) {
            requestHandler.handle(message);
        } else {
            message.reply(EventBusServerResponse.create(EventBusServerResponse.NOT_FOUND));
        }
    }
}