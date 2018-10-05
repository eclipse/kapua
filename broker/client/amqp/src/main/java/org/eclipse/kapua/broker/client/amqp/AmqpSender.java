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

import io.vertx.core.Vertx;
import io.vertx.proton.ProtonQoS;

public class AmqpSender extends AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpSender.class);

    //TODO make them configurable if needed
    private final static boolean AUTO_SETTLE = true;
    private final static ProtonQoS QOS = ProtonQoS.AT_LEAST_ONCE;

    private String destination;

    public AmqpSender(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
    }

    public void send(Message message) {
        super.send(message, destination, ar -> {
            logger.debug("Message sent to destination: {} - Message: {}", destination, message);
        });
    }

    @Override
    protected void doAfterConnect() {
        createSender(destination, AUTO_SETTLE, QOS);
    }

}
