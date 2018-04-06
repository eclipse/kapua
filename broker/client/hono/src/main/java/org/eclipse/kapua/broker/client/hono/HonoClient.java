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
package org.eclipse.kapua.broker.client.hono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.apache.qpid.proton.message.Message;
import org.eclipse.hono.client.MessageConsumer;
import org.eclipse.hono.client.impl.HonoClientImpl;
import org.eclipse.hono.config.ClientConfigProperties;
import org.eclipse.hono.util.MessageTap;
import org.eclipse.hono.util.TimeUntilDisconnectNotification;
import org.eclipse.kapua.broker.client.hono.ClientOptions.HonoClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonClientOptions;

public class HonoClient {

    private static final Logger logger = LoggerFactory.getLogger(HonoClient.class);

    protected ClientOptions clientOptions;
    protected boolean connected;
    protected AtomicInteger reconnectionFaultCount = new AtomicInteger();
    protected Long reconnectTaskId;

    protected Vertx vertx;
    private org.eclipse.hono.client.HonoClient honoClient;
    private Consumer<Message> messageConsumer;

    public HonoClient(Vertx vertx, Consumer<Message> messageConsumer) {
        this.vertx = vertx;
        this.messageConsumer = messageConsumer;
    }

    public void connect(final Future<Void> connectFuture) {
        String host = clientOptions.getString(HonoClientOptions.HOST);
        Integer port = clientOptions.getInt(HonoClientOptions.PORT, null);
        Objects.requireNonNull(host);
        Objects.requireNonNull(port);
        logger.info("Hono client - Connecting to {}:{}", host, port);
        if (honoClient != null) {
            //try to disconnect the client
            Future<Void> tmpFuture = Future.future();
            tmpFuture.setHandler(result -> {
                if (!result.succeeded()) {
                    logger.warn("Hono client - Cannot close connection... may be the connection was already closed!", result.cause());
                }
                else {
                    logger.debug("Hono client - Connection closed");
                }
            });
            disconnect(tmpFuture);
        }
        honoClient = new HonoClientImpl(vertx, getClientConfigProperties(host, port));
        //TODO handle subscription to multiple tenants ids
        honoClient.connect(
                getProtonClientOptions(),
                protonConnection -> notifyConnectionLost()
                ).compose(connectedClient -> {
                final Consumer<Message> telemetryHandler = MessageTap.getConsumer(
                        messageConsumer, this::handleCommandReadinessNotification);
                Future<MessageConsumer> futureConsumer = connectedClient.createTelemetryConsumer(
                        clientOptions.getString(HonoClientOptions.TENANT_ID),
                        telemetryHandler, closeHook -> {
                            String errorMesssage = "Hono client - remotely detached consumer link";
                            logger.error(errorMesssage);
                            if (!connectFuture.isComplete()) {
                                connectFuture.fail(errorMesssage);
                            }
                            notifyConnectionLost();
                            }
                        );
                return futureConsumer;
        }).setHandler(result -> {
            if (!result.succeeded()) {
                logger.error("Hono client - cannot create telemetry consumer for {}:{} - {}", host, port, result.cause());
                if (!connectFuture.isComplete()) {
                    connectFuture.fail(result.cause());
                }
                notifyConnectionLost();
            }
            else {
                logger.info("Hono client - Established connection to {}:{}", host, port);
                connectFuture.complete();
            }
        });
    }

    public void disconnect(final Future<Void> closeFuture) {
        if(honoClient!=null) {
            honoClient.shutdown(event -> {
                logger.info("Hono client - closing connection {}", event);
                if (!closeFuture.isComplete()) {
                    closeFuture.complete();
                }
            }
            );
        }
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
                                if (reconnectionFaultCount.incrementAndGet() > clientOptions.getInt(HonoClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1) 
                                        && clientOptions.getInt(HonoClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1)>-1) {
                                    logger.error("Maximum reconnection attempts reached. Exiting...");
                                    System.exit(clientOptions.getInt(HonoClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, -1));
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
        Integer waitBetweenReconnect = clientOptions.getInt(HonoClientOptions.WAIT_BETWEEN_RECONNECT, null);
        return (1 + reconnectionFaultCount.get()) * waitBetweenReconnect + (long)((double)waitBetweenReconnect * Math.random());
    }

    protected ClientConfigProperties getClientConfigProperties(String host, int port) {
        ClientConfigProperties props = new ClientConfigProperties();
        props.setHost(host);
        props.setPort(port);
        props.setUsername(clientOptions.getString(HonoClientOptions.USERNAME));
        props.setPassword(clientOptions.getString(HonoClientOptions.PASSWORD));
        props.setTrustStorePath(clientOptions.getString(HonoClientOptions.TRUSTSTORE_FILE));
        props.setHostnameVerificationRequired(false);
        return props;
    }

    protected ProtonClientOptions getProtonClientOptions() {
        ProtonClientOptions opts = new ProtonClientOptions();
        Integer connectTimeout = clientOptions.getInt(HonoClientOptions.CONNECT_TIMEOUT, null);
        Integer idleTimeout = clientOptions.getInt(HonoClientOptions.IDLE_TIMEOUT, null);
        Integer waitBetweenReconnect = clientOptions.getInt(HonoClientOptions.WAIT_BETWEEN_RECONNECT, null);

        if (connectTimeout != null) {
            opts.setConnectTimeout(connectTimeout);
        }
        if (idleTimeout != null) {
            //check if zero disables the timeout and heartbeat
            opts.setIdleTimeout(idleTimeout);//no activity for t>idleTimeout will close the connection (in seconds)
            opts.setHeartbeat(idleTimeout * 1000 / 2);//no activity for t>2*heartbeat will close connection (in milliseconds)
        }
        opts.setReconnectAttempts(1);//the reconnect attempts are managed externally
        if (waitBetweenReconnect != null) {
            opts.setReconnectInterval(waitBetweenReconnect);
        }
        //TODO do we need to set some other parameter?
        return opts;
    }

    protected void handleCommandReadinessNotification(final TimeUntilDisconnectNotification notification) {
    }

}
