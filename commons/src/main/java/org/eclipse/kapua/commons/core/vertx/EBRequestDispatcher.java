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

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * Creates an event bus consumer for a defined address and dispatches incoming 
 * {@link EBRequest} to registered handlers.
 * <p>
 * Each {@link EBRequestHandler} is identified by an action that has to be unique
 * across all the registered handlers. Responses are sent back to the sender
 * replying to the message.
 * 
 *
 */
public class EBRequestDispatcher {

    private Map<String, EBRequestHandler> handlers = new HashMap<>();

    private EBRequestDispatcher(EventBus eventBus, String address) {
        eventBus.consumer(address, this::handle);
    }

    public static EBRequestDispatcher dispatcher(EventBus eventBus, String address) {
        return new EBRequestDispatcher(eventBus, address);
    }

    public void registerHandler(String action, EBRequestHandler handler) {
        if (!handlers.containsKey(action)) {
            handlers.put(action, handler);
        } // TODO Handle the case when the handler is discarded
    }

    private <T> void handle(Message<T> message) {

        if (handlers == null || handlers.size() == 0) {
            message.reply(EBResponse.create(EBResponse.NOT_FOUND, new JsonObject().put("message", "Not found")));
            return;
        }
        JsonObject request = (JsonObject)message.body();
        // TODO Validate request object
        handlers.get(request.getString(EBRequest.ACTION)).handle(request, ar -> {
            if (ar.succeeded()) {
                EBResponse response = EBResponse.create(EBResponse.OK, ar.result());
                message.reply(response);
            } else {
                if (ar.cause() instanceof EBResponseException) {
                    message.reply(EBResponse.create(EBResponse.NOT_FOUND, new JsonObject().put("message", ar.cause().getMessage())));
                    return;
                }
                message.reply(EBResponse.create(EBResponse.INTERNAL_ERROR, new JsonObject().put("message", ar.cause().getMessage())));
            }
        });

    }
}
