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

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

/**
 * Dispatches incoming {@link EventBusServerRequest} to registered handlers.
 * <p>
 * Each handler is identified by an action that has to be unique
 * across all the registered handlers. Responses are sent back to the sender
 * replying to the message.
 * 
 *
 */
public class EventBusDispatcher {

    private Map<String, Handler<Message<EventBusServerRequest>>> handlers = new HashMap<>();
    private Vertx vertx;
    private EventBusServer server;

    private EventBusDispatcher(Vertx aVertx, EventBusServer aServer) {
        vertx = aVertx;
        server = aServer;
        server.setRequestHandler(this::handle);
    }

    public static EventBusDispatcher dispatcher(Vertx aVertx, EventBusServer aServer) {
        return new EventBusDispatcher(aVertx, aServer);
    }

    public void registerHandler(String action, Handler<Message<EventBusServerRequest>> handler) {
        if (!handlers.containsKey(action)) {
            handlers.put(action, handler);
        } // TODO Handle the case when the handler is discarded
    }

    public void registerBlockingHandler(String action, Handler<Message<EventBusServerRequest>> handler) {
        if (!handlers.containsKey(action)) {
            handlers.put(action, message -> { 
                vertx.executeBlocking(fut -> {
                    handler.handle(message);
                    fut.complete();
                }, 
                ar -> {});
            });
        } // TODO Handle the case when the handler is discarded
    }

    private void handle(Message<EventBusServerRequest> message) {
        if (message == null || message.body() == null) {
            message.fail(EventBusServerResponse.BAD_REQUEST, null);
            return;
        }
        EventBusServerRequest request = EventBusServerRequest.create(message.body());
        String action = request.getAction();
        if (action == null || action.isEmpty()) {
            message.fail(EventBusServerResponse.BAD_REQUEST, null);
            return;
        }
        if (handlers == null || handlers.size() == 0 || !handlers.containsKey(action)) {
            message.fail(EventBusServerResponse.NOT_FOUND, null);
            return;
        }
        handlers.get(action).handle(message);
    }
}
