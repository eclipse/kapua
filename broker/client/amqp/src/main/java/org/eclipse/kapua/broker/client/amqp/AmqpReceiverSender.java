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

import java.util.concurrent.TimeUnit;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.proton.ProtonMessageHandler;
import io.vertx.proton.ProtonQoS;

public class AmqpReceiverSender extends AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpReceiverSender.class);

    private static Vertx vertx = Vertx.vertx();

    //TODO make them configurable if needed
    private final static Integer PREFETCH = new Integer(10);
    private final static boolean AUTO_ACCEPT = true;
    private final static ProtonQoS QOS = ProtonQoS.AT_LEAST_ONCE;

    private DestinationTranslator destinationTranslator;

    public AmqpReceiverSender(ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destinationTranslator = (DestinationTranslator)clientOptions.get(AmqpClientOptions.DESTINATION_TRANSLATOR);
    }

    public void send(Message message, String destination) {
        String senderDestination = destination;
        if (destinationTranslator!=null) {
            senderDestination = destinationTranslator.translate(destination);
        }
        long startTime = System.currentTimeMillis();
        createSender(senderDestination, true, QOS);
        try {
            senderTimeout.await(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("Sender ready after {} - {}ms", System.currentTimeMillis(), System.currentTimeMillis()-startTime);
        super.send(message, senderDestination, delivery -> {
            logger.info("MESSAGE SENT!!!! {} - {}", message);
        });
    }

    public void subscribe(String destination, ProtonMessageHandler messageHandler) {
        String receiverDestination = destination;
        if (destinationTranslator!=null) {
            receiverDestination = destinationTranslator.translate(destination);
        }
        messageHandler(messageHandler);
        long startTime = System.currentTimeMillis();
        createReceiver(receiverDestination, PREFETCH, AUTO_ACCEPT, QOS);
        try {
            receiverTimeout.await(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("Receiver ready after {} - {}ms", System.currentTimeMillis(), System.currentTimeMillis()-startTime);
    }

    public void clean() {
        super.clean();
    }

    @Override
    protected void doAfterConnect() {
        setConnected();
    }

}
