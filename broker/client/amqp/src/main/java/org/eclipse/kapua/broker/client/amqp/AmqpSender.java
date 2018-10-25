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
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonQoS;
import io.vertx.proton.ProtonSender;
import io.vertx.proton.ProtonSession;

public class AmqpSender extends AmqpConnection {

    private static final Logger logger = LoggerFactory.getLogger(AmqpSender.class);

    private final static boolean AUTO_SETTLE = true;
    private final static ProtonQoS QOS = ProtonQoS.AT_LEAST_ONCE;

    private ProtonSession session;
    private ProtonSender sender;
    private String destination;

    public AmqpSender(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
    }

    @Override
    protected void doAfterConnect(Future<Void> startFuture) {
        session = connection.createSession();
        session.openHandler(ar -> {
            if (ar.succeeded()) {
                sender = createSender(session, destination, AUTO_SETTLE, QOS, null);
                sender.openHandler(rec -> {
                    if (rec.succeeded()) {
                        logger.info("Created sender {}", sender);
                    }
                    else {
                        logger.info("Created sender {} ERROR", sender);
                        notifyConnectionLost();
                    }
                    super.doAfterConnect(startFuture);
                });
                sender.open();
            }
        });
        session.closeHandler(ar -> {
            logger.info("Closed sender session {}", sender);
            notifyConnectionLost();
        });
        session.open();
    }

    public void send(Message message) {
        super.send(sender, message, destination, ar -> {
            logger.debug("Message sent to destination: {} - Message: {}", destination, message);
        });
}
}
