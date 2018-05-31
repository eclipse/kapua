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
package org.eclipse.kapua.broker.client.amqp;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;
import io.vertx.proton.ProtonLinkOptions;
import io.vertx.proton.ProtonSender;

public class AmqpSender extends AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpSender.class);
    private ProtonSender protonSender;
    private String destination;

    public AmqpSender(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
    }

    protected void registerAction(ProtonConnection connection, Future<Object> future) {
        try {
            logger.info("Register sender for destination {}...", destination);
            if (connection.isDisconnected()) {
                future.fail("Cannot register sender since the connection is not opened!");
            }
            else {
                ProtonLinkOptions senderOptions = new ProtonLinkOptions();
                // The client ID is set implicitly into the queue subscribed
                protonSender = connection.open().createSender(destination, senderOptions);
                protonSender.openHandler(ar -> {
                   if (ar.succeeded()) {
                       logger.info("Register sender for destination {}... DONE", destination);
                       setConnected(true);
                       future.complete();
                   }
                   else {
                       logger.info("Register sender for destination {}... ERROR...", destination, ar.cause());
                       setConnected(false);
                       notifyConnectionLost();
                       future.fail(ar.cause());
                   }
                });
                protonSender.open();
                logger.info("Register sender for destination {}... DONE", destination);
            }
        }
        catch(Exception e) {
            future.fail(e);
        }
    }

    public void send(Message message, Handler<ProtonDelivery> deliveryHandler) {
        message.setAddress(destination);
        protonSender.send(message, deliveryHandler);
        //TODO check if its better to create a new message like
//        import org.apache.qpid.proton.Proton;
//        Message msg = Proton.message();
//        msg.setBody(message.getBody());
//        msg.setAddress(destination);
//        protonSender.send(msg, deliveryHandler);
    }

}
