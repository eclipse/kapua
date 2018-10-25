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
import io.vertx.proton.ProtonMessageHandler;
import io.vertx.proton.ProtonQoS;
import io.vertx.proton.ProtonReceiver;
import io.vertx.proton.ProtonSession;

public class AmqpReceiver extends AmqpConnection {

    private static final Logger logger = LoggerFactory.getLogger(AmqpSender.class);

    private final static Integer PREFETCH = new Integer(10);
    private final static boolean AUTO_ACCEPT = false;
    private final static ProtonQoS QOS = ProtonQoS.AT_MOST_ONCE;

    private ProtonMessageHandler messageHandler;
    private ProtonSession session;
    private ProtonReceiver receiver;
    private String destination;

    public AmqpReceiver(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
    }

    public void messageHandler(ProtonMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    protected void doAfterConnect(Future<Void> startFuture) {
        session = connection.createSession();
        session.openHandler(ar -> {
            if (ar.succeeded()) {
                receiver = createReceiver(session, destination, PREFETCH, AUTO_ACCEPT, QOS, null, messageHandler);
                receiver.openHandler(rec -> {
                    if (rec.succeeded()) {
                        logger.info("Created receiver {}", receiver);
                    }
                    else {
                        logger.info("Created receiver {} ERROR", receiver);
                        notifyConnectionLost();
                    }
                    super.doAfterConnect(startFuture);
                });
                receiver.open();
            }
        });
        session.closeHandler(ar -> {
            logger.info("Closed receiver session {}", receiver);
            notifyConnectionLost();
        });
        session.open();
    }

}
