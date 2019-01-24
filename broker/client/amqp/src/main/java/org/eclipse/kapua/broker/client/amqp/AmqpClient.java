/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.amqpbridge.AmqpBridge;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import io.vertx.rxjava.core.eventbus.MessageProducer;

/**
 * Amqp client implementation for device command operation.
 *
 */
public class AmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClient.class);

    public static int maxWait = 3000;

    private String clientId;
    private DestinationTranslator destinationTranslator;

    private AmqpBridge bridge;
    private MessageProducer<JsonObject> producer;
    private MessageConsumer<JsonObject> consumer;
    protected Handler<io.vertx.core.eventbus.Message<Message>> messageHandler;

    /**
     * Instantiate the client using the provided {@link AmqpBridge}.
     * @param bridge
     * @param clientOptions
     */
    public AmqpClient(AmqpBridge bridge, ClientOptions clientOptions) {
        this.bridge = bridge;
        clientId = clientOptions.getString(AmqpClientOptions.CLIENT_ID);
        destinationTranslator = (DestinationTranslator)clientOptions.get(AmqpClientOptions.DESTINATION_TRANSLATOR);
    }

    /**
     * Return the client id
     * @return
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Set the message handler to be used for the incoming messages.
     * @param messageHandler
     */
    public void messageHandler(Handler<io.vertx.core.eventbus.Message<Message>> messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Send the message to the specified destination.
     * @param message
     * @param destination
     * @throws KapuaException
     */
    public void send(JsonObject message, String destination) throws KapuaException {
        String senderDestination = destination;
        if (destinationTranslator!=null) {
            senderDestination = destinationTranslator.translate(destination);
        }
        producer = AmqpBridgeFactory.createProducer(bridge, senderDestination);
        producer.send(message);
    }

    /**
     * Subscribe to the specified destination.
     * @param destination
     * @param messageHandler
     * @throws KapuaException
     */
    public void subscribe(String destination, Handler<io.vertx.rxjava.core.eventbus.Message<JsonObject>> messageHandler) throws KapuaException {
        String receiverDestination = destination;
        if (destinationTranslator!=null) {
            receiverDestination = destinationTranslator.translate(destination);
        }
        consumer = AmqpBridgeFactory.createConsumer(bridge, receiverDestination);
        consumer.handler(messageHandler);
    }

    /**
     * Cleanup the client so close the {@link MessageConsumer} and {@link MessageConsumer} if initialized.
     */
    public void clean() {
        CountDownLatch countDown = new CountDownLatch(1);
        long startTime = System.currentTimeMillis();
        if (consumer!=null) {
            consumer.unregister(ar -> {
                if (ar.succeeded()) {
                    logger.info("Unregister consumer DONE");
                }
                else {
                    logger.info("Unregister consumer ERROR", ar.cause());
                }
                //anyway the operation is done
                countDown.countDown();
            });
        }
        if (producer!=null) {
            producer.close();
        }
        boolean operationSucceed = true;
        try {
            operationSucceed = countDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("AMQPCLIENT: Wait interrupted");
        }
        if (operationSucceed) {
            logger.info("AMQPCLIENT: client cleanup {} after: {}ms {} DONE", bridge, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        else {
            logger.info("AMQPCLIENT: client cleanup {} after: {}ms {} FAILED", bridge, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
    }

}
