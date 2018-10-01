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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageProducer;

/**
 * Implements an {@link EventBusService} client.
 * <p>
 * It is used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusClient {

    private MessageProducer<EventBusServerRequest> producer;

    private EventBusClient(MessageProducer<EventBusServerRequest> producer, EventBusClientConfig configs) {
        this.producer = producer;
    }

    public static EventBusClient create(EventBus eventBus, EventBusClientConfig configs) {
        EventBusClient producer = new EventBusClient(eventBus.sender(configs.getAddress()), configs);
        return producer;
    }

    public void sendRequest(EventBusServerRequest request, Handler<AsyncResult<EventBusServerResponse>> result) {
        producer.send(request, replyEvent -> {
            if (replyEvent.succeeded()) {
                Object reply = replyEvent.result().body();
                EventBusServerResponse response = EventBusServerResponse.create(reply);
                if (response != null) {
                    result.handle(Future.succeededFuture(response));
                } else {
                    result.handle(Future.failedFuture(new EventBusServerResponseException(EventBusServerResponse.INTERNAL_ERROR)));
                }
            } else {
                result.handle(Future.failedFuture(replyEvent.cause()));
            }
        });
    }
}
