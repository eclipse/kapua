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
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;

/**
 * Implements an {@link EventBus} request producer.
 * <p>
 * It is commonly used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusRequestProducer {

    private MessageProducer<EBRequest> producer;

    private EventBusRequestProducer(MessageProducer<EBRequest> producer, EBClientConfig configs) {
        this.producer = producer;
    }

    public static EventBusRequestProducer create(EventBus eventBus, EBClientConfig configs) {
        EventBusRequestProducer producer = new EventBusRequestProducer(eventBus.sender(configs.getAddress()), configs);
        return producer;
    }

    public void sendRequest(EBRequest request, Handler<AsyncResult<EBResponse>> result) {
        producer.send(request, replyEvent -> {
            if (replyEvent.succeeded()) {
                Object reply = replyEvent.result().body();
                EBResponse response = checkAndGetResponse(reply);
                if (response != null) {
                    result.handle(Future.succeededFuture(response));
                } else {
                    result.handle(Future.failedFuture(new EBResponseException(EBResponse.INTERNAL_ERROR)));
                }
            } else {
                result.handle(Future.failedFuture(replyEvent.cause()));
            }
        });
    }

    private EBResponse checkAndGetResponse(Object candidate) {
        if (candidate == null || !(candidate instanceof JsonObject)) {
            return null;
        }
        JsonObject response = (JsonObject) candidate;
        if (!response.containsKey(EBResponse.STATUS_CODE)) {
            return null;
        }
        int resultCode = response.getInteger(EBResponse.STATUS_CODE);
        JsonObject body = null;
        if (response.containsKey(EBResponse.BODY)) {
            body = response.getJsonObject(EBResponse.BODY);
        }
        return EBResponse.create(resultCode, body);
    }
}
