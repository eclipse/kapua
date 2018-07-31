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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;

/**
 * Creates an event bus request consumer for a defined address and dispatches 
 * incoming {@link EBRequest} to registered handlers.
 * <p>
 * Each {@link EBRequestHandler} is identified by an action that has to be unique
 * across all the registered handlers. Responses are sent back to the sender
 * replying to the message.
 * 
 *
 */
public class EBRequestDispatcher {

    private Map<String, EBRequestHandler> handlers = new HashMap<>();
    private EventBusRequestConsumer consumer;

    private EBRequestDispatcher(EventBus eventBus, String address) {
        EBClientConfig config = new EBClientConfig();
        config.setAddress(address);
        this.consumer = EventBusRequestConsumer.create(eventBus, config);
        this.consumer.setRequestHandler(this::handle);
    }

    public static EBRequestDispatcher dispatcher(EventBus eventBus, String address) {
        return new EBRequestDispatcher(eventBus, address);
    }

    public void registerHandler(String action, EBRequestHandler handler) {
        if (!handlers.containsKey(action)) {
            handlers.put(action, handler);
        } // TODO Handle the case when the handler is discarded
    }

    private <T> void handle(EBRequest request, Handler<AsyncResult<EBResponse>> responseEvent) {

        if (handlers == null || handlers.size() == 0) {
            responseEvent.handle(Future.failedFuture(new EBResponseException(EBResponse.NOT_FOUND)));
            return;
        }
        handlers.get(request.getAction()).handle(request, ar -> {
            if (ar.succeeded()) {
                responseEvent.handle(Future.succeededFuture(ar.result()));
            } else {
                if (ar.cause() instanceof EBResponseException) {
                    responseEvent.handle(Future.failedFuture(ar.cause()));
                    return;
                }
                responseEvent.handle(Future.failedFuture(new EBResponseException(EBResponse.INTERNAL_ERROR, ar.cause().getMessage(), ar.cause())));
            }
        });

    }
}
