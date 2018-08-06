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

    protected boolean disconnecting;
    protected boolean connected;
    protected AtomicInteger reconnectionFaultCount = new AtomicInteger();
    protected Long reconnectTaskId;

    private String brokerHost;
    private Integer brokerPort;
    private Integer waitBetweenReconnect;
    private Integer connectTimeout;
    private Integer idleTimeout;
    private ProtonClientOptions options;
    protected ClientOptions clientOptions;
    protected Vertx vertx;
    protected Context context;
    protected ProtonClient client;
    protected ProtonConnection connection;

    protected AbstractAmqpClient(Vertx vertx, ClientOptions clientOptions) {
        this.vertx = vertx;
        this.clientOptions = clientOptions;
        this.context = vertx.getOrCreateContext();
        brokerHost = clientOptions.getString(AmqpClientOptions.BROKER_HOST);
        brokerPort = clientOptions.getInt(AmqpClientOptions.BROKER_PORT, null);
        Objects.requireNonNull(brokerHost);
        Objects.requireNonNull(brokerPort);
        client = ProtonClient.create(vertx);
        waitBetweenReconnect = clientOptions.getInt(AmqpClientOptions.WAIT_BETWEEN_RECONNECT, null);
        connectTimeout = clientOptions.getInt(AmqpClientOptions.CONNECT_TIMEOUT, null);
        idleTimeout = clientOptions.getInt(AmqpClientOptions.IDLE_TIMEOUT, null);
        options = new ProtonClientOptions();
        //TODO add ssl parameters
        logger.info("Parameters: connect timeout: {} - idle timeout: {} - wait between reconnect: {}", connectTimeout, idleTimeout, waitBetweenReconnect);
        if (connectTimeout != null) {
            options.setConnectTimeout(connectTimeout);
        }
        if (idleTimeout != null) {
            //TODO check if zero disables the timeout and heartbeat -- comment on TCPSSLOptions.setIdleTimeout(): zero means don't timeout. Also, looks ignored if == 0 in TransportImpl.java
            options.setIdleTimeout(idleTimeout);//no activity for t>idleTimeout will close the connection (in seconds)
            options.setHeartbeat(idleTimeout * 1000 / 2);//no activity for t>2*heartbeat will close connection (in milliseconds)
        }
        options.setReconnectAttempts(1);//the reconnect attempts are managed externally
        if (waitBetweenReconnect != null) {
            options.setReconnectInterval(waitBetweenReconnect);
        }
        else {
            waitBetweenReconnect = new Integer(1000);
        }
        logger.info("Created client {}", client);
    }

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    protected abstract void registerAction(ProtonConnection connection);

    public void disconnect(Future<Void> stopFuture) {
        disconnecting = true;
        logger.info("Closing connection {} for client {}...", connection, client);
        if (connection != null) {
            connection.disconnect();
            logger.info("Closing connection {} for client {}... DONE", connection, client);
            connection = null;
        }
        //in any case complete with a positive result the future
        stopFuture.complete();
    }

    public void connect(Future<Void> startFuture) {
        logger.info("Connecting to broker {}:{}... (client: {})", brokerHost, brokerPort, client);
        // make sure connection is already closed
        if (connection != null && !connection.isDisconnected()) {
            logger.warn("Unable to connect: still connected");
            return;
        }
        client.connect(
                options,
                brokerHost,
                brokerPort,
                clientOptions.getString(AmqpClientOptions.USERNAME),
                clientOptions.getString(AmqpClientOptions.PASSWORD),
                asynchResult ->{
                    if (asynchResult.succeeded()) {
                        connection = asynchResult.result();
                        logger.info("Connecting to broker {}:{}... DONE (client: {})", brokerHost, brokerPort, client);
                        connection.openHandler(event -> {
                            if (event.succeeded()) {
                                connection = event.result();
                                registerAction(connection);
                            }
                            else {
                                notifyConnectionLost();
                            }
                        });
                        connection.disconnectHandler(conn -> {
                            logger.warn("Client ({}) is closed! attempting to restore it...", client);
                            notifyConnectionLost();
                        });
                        connection.closeHandler(conn -> {
                            logger.warn("Client ({}) is closed! attempting to restore it...", client);
                            notifyConnectionLost();
                        });
                        connection.open();
                    } else {
                        logger.error("Cannot register ActiveMQ connection! (client: {})", asynchResult.cause().getCause(), client);
                        notifyConnectionLost();
                    }
                });
        //in any case complete with a positive result the future
        if (!startFuture.isComplete()) {
            startFuture.complete();
        }
    }

    protected void notifyConnectionLost() {
        logger.info("Notify disconnection... (client: {})", client);
        setConnected(false);
        if (disconnecting) {
            logger.info("Notify disconnection... shutdown in progress - skipping reconection! (client: {})", client);
            return;
        }
        if (reconnectTaskId == null) {
            if (reconnectTaskId == null) {
                long backOff = evaluateBackOff();
                logger.info("Notify disconnection... Start new task {} (client: {})", backOff, client);
                reconnectTaskId = vertx.setTimer(backOff, new Handler<Long>() {

                    @Override
                    public void handle(Long obj) {
                        Future<Void> future = Future.future();
                        future.setHandler(result -> {
                            reconnectTaskId = null;
                            if (result.succeeded()) {
                                logger.info("Establish connection retry {}... SUCCESS (client: {})", reconnectionFaultCount.get(), client);
                                reconnectionFaultCount.set(0);
                            } else {
                                logger.info("Establish connection retry {}... FAILURE (client: {})", reconnectionFaultCount.get(), result.cause(), client);
                                if (reconnectionFaultCount.incrementAndGet() > clientOptions.getInt(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1) && 
                                        clientOptions.getInt(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1)>-1) {
                                    logger.error("Maximum reconnection attempts reached. Exiting... (client: {})", client);
                                    System.exit(clientOptions.getInt(AmqpClientOptions.EXIT_CODE, -1));
                                };
                                //schedule a new task
                                notifyConnectionLost();
                            }
                        });
                        connect(future);
                        logger.info("Started new connection done (client: {})", client);
                    }
                });
            }
            else {
                logger.info("Another reconnect operation is enqueed. No action will be taken! (client: {})", client);
            }
        }
        else {
            logger.info("Another reconnect operation is enqueed. No action will be taken! (client: {})", client);
        }
        logger.info("Notify disconnection... DONE client: {})", client);
    }

    private long evaluateBackOff() {
        return (1 + reconnectionFaultCount.get()) * waitBetweenReconnect + (long)((double)waitBetweenReconnect * Math.random());
    }

}
