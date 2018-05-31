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

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonMessageHandler;
import io.vertx.proton.ProtonQoS;
import io.vertx.proton.ProtonReceiver;

public class AmqpConsumer extends AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpConsumer.class);
    private ProtonMessageHandler messageHandler;

    public AmqpConsumer(Vertx vertx, ClientOptions clientOptions, ProtonMessageHandler messageHandler) {
        super(vertx, clientOptions);
        this.messageHandler = messageHandler;
    }

    protected void registerAction(ProtonConnection connection, Future<Object> future) {
        try {
            String destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
            logger.info("Register consumer for destination {}...", destination);

            if (connection.isDisconnected()) {
                future.fail("Cannot register consumer since the connection is not opened!");
            }
            else {
                // The client ID is set implicitly into the queue subscribed
                ProtonReceiver receiver = connection.createReceiver(destination);
                receiver.setAutoAccept((boolean)clientOptions.get(AmqpClientOptions.AUTO_ACCEPT));
                receiver.setQoS((ProtonQoS)clientOptions.get(AmqpClientOptions.QOS));
                receiver.handler(messageHandler).open();
                logger.info("Register consumer for queue {}... DONE", destination);
                future.complete();
            }
        }
        catch(Exception e) {
            future.fail(e);
        }
    }

}
