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
package org.eclipse.kapua.processor.error.amqp.activemq;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.apps.api.HealthCheckable;
import org.eclipse.kapua.broker.client.amqp.AmqpSender;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.Status;
import io.vertx.proton.ProtonHelper;

public class ErrorProcessor implements Processor<Message>, HealthCheckable {

    private static final Logger logger = LoggerFactory.getLogger(ErrorProcessor.class);

    private AmqpSender sender;

    public ErrorProcessor(Vertx vertx, ClientOptions clientOptions) {
        sender = new AmqpSender(vertx, clientOptions);
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
        // nothing to do
        stopFuture.complete();
    }

    @Override
    public Status getStatus() {
        if (sender.isConnected()) {
            return Status.OK();
        }
        else {
            return Status.KO();
        }
    }

    @Override
    public boolean isHealty() {
        return sender.isConnected();
    }
}
