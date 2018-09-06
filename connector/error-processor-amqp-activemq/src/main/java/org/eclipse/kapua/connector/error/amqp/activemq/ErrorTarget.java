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
package org.eclipse.kapua.connector.error.amqp.activemq;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.AmqpSender;
import org.eclipse.kapua.connector.KapuaProcessorException;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.MessageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonHelper;

public abstract class ErrorTarget implements MessageTarget<Message> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorTarget.class);

    private AmqpSender sender;

    public static ErrorTarget getProcessorWithNoFilter(Vertx vertx, AmqpSender sender) {
        return new ErrorTarget(vertx, sender) {
            @Override
            public boolean isProcessDestination(MessageContext<Message> message) {
                return true;
            }
        };
    }

    public ErrorTarget(Vertx vertx, AmqpSender sender) {
        this.sender = sender;
    }

    @Override
    public void start(Future<Void> startFuture) {
        sender.connect(startFuture);
    }

    @Override
    public void process(MessageContext<Message> message, Handler<AsyncResult<Void>> result) throws KapuaProcessorException {
        sender.send(message.getMessage(), delivery -> {
            ProtonHelper.accepted(delivery, true);
        });
        result.handle(Future.succeededFuture());
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        sender.disconnect(stopFuture);
    }
}
