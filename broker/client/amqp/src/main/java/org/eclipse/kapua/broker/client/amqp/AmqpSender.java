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

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;
import io.vertx.proton.ProtonLinkOptions;
import io.vertx.proton.ProtonSender;

public class AmqpSender extends AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpSender.class);
    private ProtonSender sender;
    private String destination;

    public AmqpSender(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
    }

    protected void registerAction(ProtonConnection connection) {
        try {
            logger.info("Register sender for destination {}... (client: {})", destination, client);
            if (connection.isDisconnected()) {
                logger.warn("Cannot register sender since the connection is not opened!");
                notifyConnectionLost();
            }
            else {
                ProtonLinkOptions senderOptions = new ProtonLinkOptions();
                // The client ID is set implicitly into the queue subscribed
                sender = connection.open().createSender(destination, senderOptions);
                sender.openHandler(ar -> {
                   if (ar.succeeded()) {
                       logger.info("Register sender for destination {}... DONE (client: {})", destination, client);
                       setConnected(true);
                   }
                   else {
                       logger.info("Register sender for destination {}... ERROR... (client: {})", destination, ar.cause(), client);
                       notifyConnectionLost();
                   }
                });
                sender.closeHandler(snd -> {
                    logger.warn("Sender is closed! attempting to restore it... (client: {})", client);
                    notifyConnectionLost();
                });
                sender.open();
                logger.info("Register sender for destination {}... DONE (client: {})", destination, client);
            }
        }
        catch(Exception e) {
            notifyConnectionLost();
        }
    }

    public void send(Message message, Handler<ProtonDelivery> deliveryHandler) {
        message.setAddress(destination);
        sender.send(message, deliveryHandler);
        //TODO check if its better to create a new message like
//        import org.apache.qpid.proton.Proton;
//        Message msg = Proton.message();
//        msg.setBody(message.getBody());
//        msg.setAddress(destination);
//        protonSender.send(msg, deliveryHandler);
    }

}
