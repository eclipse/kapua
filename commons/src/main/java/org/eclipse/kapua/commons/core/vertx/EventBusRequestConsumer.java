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

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

/**
 * Implements an {@link EventBus} request consumer.
 * <p>
 * It is commonly used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusRequestConsumer {

    private MessageConsumer<EBRequest> consumer;
    private EBRequestHandler requestHandler;

    protected EventBusRequestConsumer(MessageConsumer<EBRequest> producer, EBClientConfig configs) {
        this.consumer = producer;
        this.consumer.handler(this::handle);
    }

    public static EventBusRequestConsumer create(EventBus eventBus, EBClientConfig configs) {
        EventBusRequestConsumer consumer = new EventBusRequestConsumer(eventBus.consumer(configs.getAddress()), configs);
        return consumer;
    }

    public EBRequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(EBRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    private <T> void handle(Message<T> message) {
        if (!(message.body() instanceof JsonObject)) {
            message.reply(EBResponse.create(EBResponse.INTERNAL_ERROR, new JsonObject().put("message", "Bad request")));
        } else {
            JsonObject body = (JsonObject)message.body();
            EBRequest request = EBRequest.create(body.getString(EBRequest.ACTION), body.getJsonObject(EBRequest.BODY));
            // TODO Validate request object
            requestHandler.handle(request, ar -> {
                if (ar.succeeded()) {
                    message.reply(ar.result());
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
}
