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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonClientOptions;
import io.vertx.proton.ProtonConnection;

public abstract class AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAmqpClient.class);

    protected boolean connected;
    protected AtomicInteger reconnectionFaultCount = new AtomicInteger();
    protected Long reconnectTaskId;

    protected ClientOptions clientOptions;
    protected Vertx vertx;
    protected Context context;
    protected ProtonClient client;
    protected ProtonConnection connection;

    protected AbstractAmqpClient(Vertx vertx, ClientOptions clientOptions) {
        this.vertx = vertx;
        this.clientOptions = clientOptions;
        this.context = vertx.getOrCreateContext();
    }

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    protected abstract void registerAction(ProtonConnection connection, Future<Object> future);

    public void disconnect(Future<Void> stopFuture) {
        //TODO disable reconnection in the meanwhile
        if (connection != null) {
            connection.close();
            connection = null;
            stopFuture.complete();
        }
    }

    public void connect(Future<Void> startFuture) {
        String brokerHost = clientOptions.getString(AmqpClientOptions.BROKER_HOST);
        Integer brokerPort = clientOptions.getInt(AmqpClientOptions.BROKER_PORT, null);
        Objects.requireNonNull(brokerHost);
        Objects.requireNonNull(brokerPort);
        logger.info("Connecting to broker {}:{}...", brokerHost, brokerPort);
        // make sure connection is already closed
        if (connection != null && !connection.isDisconnected()) {
            if (!startFuture.isComplete()) {
                startFuture.fail("Unable to connect: still connected");
            }
            return;
        }

        client = ProtonClient.create(vertx);
        ProtonClientOptions options = new ProtonClientOptions();
        Integer connectTimeout = clientOptions.getInt(AmqpClientOptions.CONNECT_TIMEOUT, null);
        Integer idleTimeout = clientOptions.getInt(AmqpClientOptions.IDLE_TIMEOUT, null);
        Integer waitBetweenReconnect = clientOptions.getInt(AmqpClientOptions.WAIT_BETWEEN_RECONNECT, null);

        if (connectTimeout != null) {
            options.setConnectTimeout(connectTimeout);
        }
        if (idleTimeout != null) {
            //check if zero disables the timeout and heartbeat
            options.setIdleTimeout(idleTimeout);//no activity for t>idleTimeout will close the connection (in seconds)
            options.setHeartbeat(idleTimeout * 1000 / 2);//no activity for t>2*heartbeat will close connection (in milliseconds)
        }
        options.setReconnectAttempts(1);//the reconnect attempts are managed externally
        if (waitBetweenReconnect != null) {
            options.setReconnectInterval(waitBetweenReconnect);
        }
        //TODO do we need to set some other parameter?
        client.connect(
                options,
                brokerHost,
                brokerPort,
                clientOptions.getString(AmqpClientOptions.USERNAME),
                clientOptions.getString(AmqpClientOptions.PASSWORD),
                asynchResult ->{
                    if (asynchResult.succeeded()) {
                        logger.info("Connecting to broker {}:{}... Creating receiver... DONE", brokerHost, brokerPort);
                        connection = asynchResult.result();
                        connection.openHandler((event) -> {
                            if (event.succeeded()) {
                                connection = event.result();
                                //TODO remove execute blocking
                                context.executeBlocking(future -> registerAction(connection, future), result -> {
                                    if (result.succeeded()) {
                                        logger.debug("Starting connector...DONE");
                                        setConnected(true);
                                        startFuture.complete();
                                    } else {
                                        logger.warn("Starting connector...FAIL [message:{}]", result.cause().getMessage());
                                        startFuture.fail(asynchResult.cause());
                                        setConnected(false);
                                        notifyConnectionLost();
                                    }
                                });
                            }
                            else {
                                startFuture.fail("Cannot establish connection!");
                                setConnected(false);
                                notifyConnectionLost();
                            }
                        });
                        connection.open();
                    } else {
                        logger.error("Cannot register ActiveMQ connection! ", asynchResult.cause().getCause());
                        if (!startFuture.isComplete()) {
                            startFuture.fail(asynchResult.cause());
                        }
                        setConnected(false);
                        notifyConnectionLost();
                    }
                });
    }

    protected void notifyConnectionLost() {
        logger.info("Notify disconnection...");
        if (reconnectTaskId == null) {
            if (reconnectTaskId == null) {
                long backOff = evaluateBackOff();
                logger.info("Notify disconnection... Start new task {}", backOff);
                reconnectTaskId = vertx.setTimer(backOff, new Handler<Long>() {

                    @Override
                    public void handle(Long obj) {
                        Future<Void> future = Future.future();
                        future.setHandler(result -> {
                            reconnectTaskId = null;
                            if (result.succeeded()) {
                                logger.info("Establish connection retry {}... SUCCESS", reconnectionFaultCount.get());
                                reconnectionFaultCount.set(0);
                            } else {
                                logger.info("Establish connection retry {}... FAILURE", reconnectionFaultCount.get(), result.cause());
                                if (reconnectionFaultCount.incrementAndGet() > clientOptions.getInt(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1) && 
                                        clientOptions.getInt(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1)>-1) {
                                    logger.error("Maximum reconnection attempts reached. Exiting...");
                                    System.exit(clientOptions.getInt(AmqpClientOptions.EXIT_CODE, -1));
                                };
                                //schedule a new task
                                notifyConnectionLost();
                            }
                        });
                        connect(future);
                        logger.info("Started new connection donw");
                    }
                });
            }
            else {
                logger.info("Another reconnect operation is enqueed. No action will be taken!");
            }
        }
        else {
            logger.info("Another reconnect operation is enqueed. No action will be taken!");
        }
        logger.info("Notify disconnection... DONE");
    }

    private long evaluateBackOff() {
        //TODO change algorithm to something exponential
        return (1 + reconnectionFaultCount.get()) * 3000;
    }

}
