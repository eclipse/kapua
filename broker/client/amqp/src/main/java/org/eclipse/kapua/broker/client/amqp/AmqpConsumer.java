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

import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonMessageHandler;
import io.vertx.proton.ProtonQoS;
import io.vertx.proton.ProtonReceiver;

public class AmqpConsumer extends AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpConsumer.class);
    private ProtonMessageHandler messageHandler;
    private ProtonReceiver receiver;

    public AmqpConsumer(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
    }

    public void messageHandler(ProtonMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    protected void registerAction(ProtonConnection connection) {
        try {
            String destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
            logger.info("Register consumer for destination {}... (client: {})", destination, client);

            if (connection.isDisconnected()) {
                logger.warn("Cannot register consumer since the connection is not opened!");
                notifyConnectionLost();
            }
            else {
                // The client ID is set implicitly into the queue subscribed
                receiver = connection.createReceiver(destination);
                receiver.setAutoAccept((boolean)clientOptions.get(AmqpClientOptions.AUTO_ACCEPT));
                receiver.setQoS((ProtonQoS)clientOptions.get(AmqpClientOptions.QOS));
                Integer prefetch = clientOptions.getInt(AmqpClientOptions.PREFETCH_MESSAGES, 1);
                receiver.setPrefetch(prefetch);
                logger.info("Setting prefetch to {}", prefetch);
                receiver.handler(messageHandler);
                receiver.openHandler(ar -> {
                    if(ar.succeeded()) {
                        logger.info("Succeeded establishing consumer link! (client: {})", client);
                        setConnected(true);
                    }
                    else {
                        logger.warn("Cannot establish link! (client: {})", ar.cause(), client);
                        notifyConnectionLost();
                    }
                });
                receiver.closeHandler(recv -> {
                    logger.warn("Receiver is closed! attempting to restore it... (client: {})", client);
                    notifyConnectionLost();
                });
                receiver.open();
                logger.info("Register consumer for queue {}... DONE (client: {})", destination, client);
            }
        }
        catch(Exception e) {
            notifyConnectionLost();
        }
    }

}
