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

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonClientOptions;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonDelivery;
import io.vertx.proton.ProtonLinkOptions;
import io.vertx.proton.ProtonMessageHandler;
import io.vertx.proton.ProtonQoS;
import io.vertx.proton.ProtonReceiver;
import io.vertx.proton.ProtonSender;
import io.vertx.proton.ProtonSession;

/**
 * Create and handle a Vertx Proton connection (reconnecting it if something wrong happens)
 *
 */
public class AmqpConnection {

    private static final Logger logger = LoggerFactory.getLogger(AmqpConnection.class);

    private Vertx vertx;

    private boolean disconnecting;
    private boolean connected;
    private AtomicInteger reconnectionFaultCount = new AtomicInteger();
    private Long reconnectTaskId;

    private String brokerHost;
    private Integer brokerPort;
    private String clientId;
    private Integer waitBetweenReconnect;
    private Integer connectTimeout;
    private Integer idleTimeout;
    private ProtonClientOptions options;
    private ProtonClient client;
    private ClientOptions clientOptions;

    protected ProtonConnection connection;

    public AmqpConnection(Vertx vertx, ClientOptions clientOptions) {
        logger.info("Creating connection. Vertx: {}", vertx);
        this.vertx = vertx;
        this.clientOptions = clientOptions;
        brokerHost = clientOptions.getString(AmqpClientOptions.BROKER_HOST);
        brokerPort = clientOptions.getInt(AmqpClientOptions.BROKER_PORT, null);
        clientId = clientOptions.getString(AmqpClientOptions.CLIENT_ID);
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
        options.setMaxFrameSize(1024*1024);
        logger.info("Created client {}", client);
    }

    public String getClientId() {
        return clientId;
    }

    public ProtonSession createSession() {
        return connection.createSession();
    }

    protected void setDisconnected() {
        connected = false;
    }

    protected void setConnected() {
        connected = true;
    }

    public Boolean isConnected() {
        return connected;
    }

    protected ProtonReceiver createReceiver(ProtonSession session, String destination, int prefetch, boolean autoAccept, ProtonQoS qos, CountDownLatch receiverCountDown, ProtonMessageHandler messageHandler) {
        ProtonReceiver receiver = null;
        try {
            logger.info("Register consumer for destination {}... (session: {})", destination, session);
            ProtonLinkOptions options = new ProtonLinkOptions();
            options.setLinkName(String.format("%s-rcv", clientId));
            // The client ID is set implicitly into the destination subscribed
            receiver = session.createReceiver(destination, options);

            //default values not changeable by config
            receiver.setAutoAccept(autoAccept);
            receiver.setQoS(qos);
            receiver.setPrefetch(prefetch);
            logger.info("Setting auto accept: {} - QoS: {} - prefetch: {}", autoAccept, qos, prefetch);

            receiver.handler(messageHandler);
            receiver.openHandler(ar -> {
                if(ar.succeeded()) {
                    logger.info("Succeeded establishing consumer link! (session: {})", session);
                    if (receiverCountDown != null) {
                        receiverCountDown.countDown();
                    }
                }
                else {
                    logger.warn("Cannot establish link! (session: {})", session, ar.cause());
                    notifyConnectionLost();
                }
            });
            receiver.closeHandler(recv -> {
                logger.warn("Receiver is closed! (session: {})", session, recv.cause());
                notifyConnectionLost();
            });
            receiver.open();
            logger.info("Register consumer for destination {}... DONE (session: {})", destination, session);
        }
        catch(Exception e) {
            notifyConnectionLost();
        }
        return receiver;
    }

    protected ProtonSender createSender(ProtonSession session, String destination, boolean autoSettle, ProtonQoS qos, CountDownLatch senderCountDown) {
        ProtonSender sender = null;
        try {
            logger.info("Register sender for destination {}... (session: {})", destination, session);
            ProtonLinkOptions senderOptions = new ProtonLinkOptions();
            senderOptions.setLinkName(String.format("%s-snd", clientId));
            // The client ID is set implicitly into the destination subscribed
            sender = session.createSender(destination, senderOptions);

            //default values not changeable by config
            sender.setQoS(qos);
            sender.setAutoSettle(autoSettle);
            logger.info("Setting auto accept: {} - QoS: {}", autoSettle, qos);

            sender.openHandler(ar -> {
               if (ar.succeeded()) {
                   logger.info("Register sender for destination {}... DONE (session: {})", destination, session);
                   if (senderCountDown != null) {
                       senderCountDown.countDown();
                   }
               }
               else {
                   logger.info("Register sender for destination {}... FAILED... (session: {})", destination, session, ar.cause());
                   notifyConnectionLost();
               }
            });
            sender.closeHandler(snd -> {
                logger.warn("Sender is closed! (session: {})", session, snd.cause());
                notifyConnectionLost();
            });
            sender.open();
            logger.info("Register sender for destination {}... DONE (session: {})", destination, session);
        }
        catch(Exception e) {
            notifyConnectionLost();
        }
        return sender;
    }

    protected void send(ProtonSender sender, Message message, String destination, Handler<ProtonDelivery> deliveryHandler) {
        message.setAddress(destination);
        sender.send(message, deliveryHandler);
        //TODO check if its better to create a new message like
//        import org.apache.qpid.proton.Proton;
//        Message msg = Proton.message();
//        msg.setBody(message.getBody());
//        msg.setAddress(destination);
//        protonSender.send(msg, deliveryHandler);
    }

    public void connect(Future<Void> startFuture) {
        logger.info("Connecting to broker {}:{}... (client: {})", brokerHost, brokerPort, client);
        // make sure connection is already closed
        if (connection != null && !connection.isDisconnected()) {
            logger.warn("Unable to connect: still connected");
            //in any case complete with a positive result the future
            if (!startFuture.isComplete()) {
                startFuture.complete();
            }
            return;
        }
        String username = clientOptions.getString(AmqpClientOptions.USERNAME);
        String password = null;
        Object tmp = clientOptions.get(AmqpClientOptions.PASSWORD);
        if (tmp instanceof String) {
            password = (String) tmp;
        }
        else if (tmp instanceof char[]) {
            password = String.valueOf((char[]) tmp);
        }
        else if (tmp!=null) {
            password = tmp.toString();
        }
        client.connect(
            options,
            brokerHost,
            brokerPort,
            username,
            password,
            asynchResult ->{
                if (asynchResult.succeeded()) {
                    connection = asynchResult.result();
                    logger.info("Connecting to broker {}:{}... DONE (client: {})", brokerHost, brokerPort, client);
                    connection.openHandler(event -> {
                        if (event.succeeded()) {
                            connection = event.result();
                            doAfterConnect(startFuture);
                        }
                        else {
                            notifyConnectionLost();
                        }
                    });
                    connection.disconnectHandler(conn -> {
                        logger.warn("Client ({}) is closed!", client);
                        notifyConnectionLost();
                    });
                    connection.closeHandler(conn -> {
                        logger.warn("Client ({}) is closed!", client);
                        notifyConnectionLost();
                    });
                    connection.open();
                } else {
                    logger.error("Cannot register ActiveMQ connection! (client: {})", asynchResult.cause().getCause(), client);
                    notifyConnectionLost();
                }
            });
    }

    protected void doAfterConnect(Future<Void> startFuture) {
        //in any case complete with a positive result the future
        if (!startFuture.isComplete()) {
            startFuture.complete();
        }
    }

    protected void notifyConnectionLost() {
        logger.info("Notify disconnection... (client: {})", client);
        setDisconnected();
        doReconnect();
    }

    protected void doReconnect() {
        if (disconnecting) {
            logger.info("Notify disconnection... shutdown in progress - skipping reconnection! (client: {})", client);
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

    public void disconnect(Future<Void> stopFuture) {
        disconnecting = true;
        logger.info("Closing connection {} for client {}", connection, client);
        if (connection != null) {
            connection.disconnect();
            connection.closeHandler(event -> {
                if (event.succeeded()) {
                    if (!stopFuture.isComplete()) {
                        stopFuture.complete();
                    }
                    logger.info("Closing connection {} for client {} DONE", connection, client);
                }
                else {
                    logger.info("Closing connection {} for client {} ERROR {}", connection, client, (event.cause()!=null ? event.cause().getCause() : "N/A"), event.cause());
                }
            });
            connection = null;
        }
    }
}
