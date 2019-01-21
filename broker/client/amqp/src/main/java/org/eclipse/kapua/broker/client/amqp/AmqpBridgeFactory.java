/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.AmqpClient;
import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;

/**
 * Vertx Proton session factory.
 * Creates the connection for the specified endpoint, if doesn't exist, and bound a new session on this connection.
 * To be used for the device management operations.
 *
 */
public class AmqpBridgeFactory {

    private static final Logger logger = LoggerFactory.getLogger(AmqpBridgeFactory.class);

    private final static int DEFAULT_PORT = 5672;
    private static final Vertx VERTX = Vertx.vertx();
    private static Map<String, AmqpBridgeContainer> bridgeMap = new HashMap<>();
    private static Map<Integer, AmqpBridgeContainer> bridgeMapByBridge = new HashMap<>();

    private AmqpBridgeFactory() {
    }

    public static AmqpClient getInstance(String nodeUri, ClientOptions clientOptions) throws InterruptedException, KapuaException {
        return new AmqpClient(getAmqpBridge(nodeUri, clientOptions), clientOptions);
    }

    public static void cleanClient(AmqpClient client) {
        client.clean();
    }

    private static AmqpBridge getAmqpBridge(String nodeUri, ClientOptions clientOptions) throws InterruptedException, KapuaException {
        AmqpBridgeContainer amqpBridgeContainer = bridgeMap.get(nodeUri);
        if (amqpBridgeContainer != null) {
            return amqpBridgeContainer.getAmqpBridge();
        }
        else {
            synchronized (bridgeMap) {
                amqpBridgeContainer = bridgeMap.get(nodeUri);
                if (amqpBridgeContainer == null) {
                    final Ctx ctx = new Ctx();
                    long startTime = System.currentTimeMillis();
                    CountDownLatch countDown = new CountDownLatch(1);
                    Context currentContext = VERTX.getOrCreateContext();
                    String host = clientOptions.getString(AmqpClientOptions.BROKER_HOST);
                    int port = clientOptions.getInt(AmqpClientOptions.BROKER_PORT, DEFAULT_PORT);
                    currentContext.runOnContext(han -> {
                        ctx.setBridge(AmqpBridge.create(VERTX));
                        ctx.getBridge().start(host, port, res -> {
                            if (res.failed()) {
                                logger.error("BRIDGE: Return bridge to {}... ERROR", clientOptions.get(AmqpClientOptions.BROKER_HOST), res.cause());
                            }
                            else {
                                logger.info("BRIDGE: Return bridge to {}... DONE", clientOptions.get(AmqpClientOptions.BROKER_HOST));
                                countDown.countDown();
                            }
                        });
                    });
                    boolean operationSucceed = true;
                    try {
                        operationSucceed = countDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        logger.debug("BRIDGE: Wait interrupted");
                    }
                    if (operationSucceed) {
                        logger.info("BRIDGE: Bridge creation {} after: {}ms {} DONE", amqpBridgeContainer, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
                    }
                    else {
                        logger.info("BRIDGE: Bridge creation {} after: {}ms {} FAILED", amqpBridgeContainer, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
                    }
                    amqpBridgeContainer = new AmqpBridgeContainer(ctx.getBridge(), currentContext);
                    bridgeMap.put(nodeUri, amqpBridgeContainer);
                    bridgeMapByBridge.put(amqpBridgeContainer.getAmqpBridge().hashCode(), amqpBridgeContainer);
                }
            }
        }
        return amqpBridgeContainer.getAmqpBridge();
    }

    public static MessageProducer<JsonObject> createProducer(AmqpBridge amqpBridge, String destination) {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDown = new CountDownLatch(1);
        AmqpBridgeContainer amqpBridgeContainer = bridgeMapByBridge.get(amqpBridge.hashCode());
        final Context currentContext = amqpBridgeContainer.getContext();
        final Ctx ctx = new Ctx();
        currentContext.runOnContext(han -> {
            logger.info("Creating producer");
            try {
                ctx.setProducer(amqpBridge.createProducer(destination));
                logger.info("Creating producer DONE");
                countDown.countDown();
            }
            catch (Exception e) {
                logger.warn("Error: ", e);
            }
        });
//        currentContext.executeBlocking(fut -> {
//            logger.info("Creating producer");
//            try {
//                tmp.set(amqpBridge.createProducer(destination));
//                logger.info("Creating producer DONE");
//            }
//            catch (Exception e) {
//                logger.warn("Error: ", e);
//            }
//            fut.complete(tmp);
//            }, 
//            ar -> { logger.info("Completed", ar.result());
//            });
        boolean operationSucceed = true;
        try {
            operationSucceed = countDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("BRIDGE: Wait interrupted");
        }
        if (operationSucceed) {
            logger.info("BRIDGE: Producer creation {} after: {}ms {} DONE", amqpBridgeContainer, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        else {
            logger.info("BRIDGE: Producer creation {} after: {}ms {} FAILED", amqpBridgeContainer, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        return ctx.getProducer();
    }

    public static MessageConsumer<JsonObject> createConsumer(AmqpBridge amqpBridge, String destination) {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDown = new CountDownLatch(1);
        AmqpBridgeContainer amqpBridgeContainer = bridgeMapByBridge.get(amqpBridge.hashCode());
        final Context currentContext = amqpBridgeContainer.getContext();
        final Ctx ctx = new Ctx();
        currentContext.runOnContext(han -> {
            logger.info("Creating consumer");
            try {
                ctx.setConsumer(amqpBridge.createConsumer(destination));
                logger.info("Creating consumer DONE");
                countDown.countDown();
            }
            catch (Exception e) {
                logger.warn("Error: ", e);
            }
        });
        boolean operationSucceed = true;
        try {
            operationSucceed = countDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("BRIDGE: Wait interrupted");
        }
        if (operationSucceed) {
            logger.info("BRIDGE: Consumer creation {} after: {}ms {} DONE", amqpBridgeContainer, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        else {
            logger.info("BRIDGE: Consumer creation {} after: {}ms {} FAILED", amqpBridgeContainer, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        return ctx.getConsumer();
    }

    //TODO to be conected to the application lifecycle
    public static void cleanUp() {
        //TODO
    }

}

class Ctx {

    private MessageProducer<JsonObject> producer;
    private MessageConsumer<JsonObject> consumer;
    private AmqpBridge bridge;

    public void setBridge(AmqpBridge bridge) {
        this.bridge = bridge;
    }

    public void setProducer(MessageProducer<JsonObject> producer) {
        this.producer = producer;
    }

    public void setConsumer(MessageConsumer<JsonObject> consumer) {
        this.consumer = consumer;
    }

    public AmqpBridge getBridge() {
        return bridge;
    }

    public MessageProducer<JsonObject> getProducer() {
        return producer;
    }

    public MessageConsumer<JsonObject> getConsumer() {
        return consumer;
    }
}

class AmqpBridgeContainer {

    private AmqpBridge amqpBridge;
    private Context context;

    public AmqpBridgeContainer(AmqpBridge amqpBridge, Context context) {
        this.amqpBridge = amqpBridge;
        this.context = context;
    }

    public AmqpBridge getAmqpBridge() {
        return amqpBridge;
    }

    public Context getContext() {
        return context;
    }

}
