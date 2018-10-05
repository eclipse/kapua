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

public abstract class AbstractAmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAmqpClient.class);

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
    private ClientOptions clientOptions;
    private Vertx vertx;
    private ProtonClient client;
    private ProtonConnection connection;

    private ProtonMessageHandler messageHandler;
    private ProtonReceiver receiver;

    private ProtonSender sender;

    protected AbstractAmqpClient(Vertx vertx, ClientOptions clientOptions) {
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
        logger.info("Created client {}", client);
    }

    public String getClientId() {
        return clientId;
    }

    protected void createSender(String destination, boolean autoSettle, ProtonQoS qos) {
        try {
            logger.info("Register sender for destination {}... (client: {})", destination, client);
            if (connection.isDisconnected()) {
                logger.warn("Cannot register sender since the connection is not opened!");
                notifyConnectionLost();
            }
            else {
                ProtonLinkOptions senderOptions = new ProtonLinkOptions();
                // The client ID is set implicitly into the destination subscribed
                sender = connection.open().createSender(destination, senderOptions);

                //default values not changeable by config
                sender.setQoS(qos);
                sender.setAutoSettle(autoSettle);
                logger.info("Setting auto accept: {} - QoS: {}", autoSettle, qos);

                sender.openHandler(ar -> {
                   if (ar.succeeded()) {
                       logger.info("Register sender for destination {}... DONE (client: {})", destination, client);
                       setConnected();
                   }
                   else {
                       logger.info("Register sender for destination {}... ERROR... (client: {})", destination, client, ar.cause());
                       notifyConnectionLost();
                   }
                });
                sender.closeHandler(snd -> {
                    logger.warn("Sender is closed! (client: {})", client, snd.cause());
                    notifyConnectionLost();
                });
                sender.open();
                logger.info("Register sender for destination {}... DONE (client: {})", destination, client);
            }
        }
        catch(Exception e) {
            notifyConnectionLost();
        }
    }

    protected void send(Message message, String destination, Handler<ProtonDelivery> deliveryHandler) {
        message.setAddress(destination);
        sender.send(message, deliveryHandler);
        //TODO check if its better to create a new message like
//        import org.apache.qpid.proton.Proton;
//        Message msg = Proton.message();
//        msg.setBody(message.getBody());
//        msg.setAddress(destination);
//        protonSender.send(msg, deliveryHandler);
    }

    protected void createReceiver(String destination, int prefetch, boolean autoAccept, ProtonQoS qos) {
        try {
            logger.info("Register consumer for destination {}... (client: {})", destination, client);

            if (connection.isDisconnected()) {
                logger.warn("Cannot register consumer since the connection is not opened!");
                notifyConnectionLost();
            }
            else {
                // The client ID is set implicitly into the destination subscribed
                receiver = connection.createReceiver(destination);

                //default values not changeable by config
                receiver.setAutoAccept(autoAccept);
                receiver.setQoS(qos);
                receiver.setPrefetch(prefetch);
                logger.info("Setting auto accept: {} - QoS: {} - prefetch: {}", autoAccept, qos, prefetch);

                receiver.handler(messageHandler);
                receiver.openHandler(ar -> {
                    if(ar.succeeded()) {
                        logger.info("Succeeded establishing consumer link! (client: {})", client);
                        setConnected();
                    }
                    else {
                        logger.warn("Cannot establish link! (client: {})", client, ar.cause());
                        notifyConnectionLost();
                    }
                });
                receiver.closeHandler(recv -> {
                    logger.warn("Receiver is closed! (client: {})", client, recv.cause());
                    notifyConnectionLost();
                });
                receiver.open();
                logger.info("Register consumer for destination {}... DONE (client: {})", destination, client);
            }
        }
        catch(Exception e) {
            notifyConnectionLost();
        }
    }

    public void messageHandler(ProtonMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    protected void doAfterConnect() {
        //do nothing (to be overwritten by children, if needed)
    }

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected() {
        connected = true;
    }

    protected void setDisconnected() {
        connected = false;
    }

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

    protected void clean() {
        if (sender!=null) {
            logger.info("Closing sender {} for client {}", sender, client);
            sender.close();
            sender = null;
            logger.info("Closing sender for client {} DONE", client);
        }
        if (receiver!=null) {
            logger.info("Closing receiver {} for client {}", receiver, client);
            receiver.close();
            receiver = null;
            logger.info("Closing receiver for client {} DONE", client);
        }
    }

    public void connect(Future<Void> startFuture) {
        logger.info("Connecting to broker {}:{}... (client: {})", brokerHost, brokerPort, client);
        // make sure connection is already closed
        if (connection != null && !connection.isDisconnected()) {
            logger.warn("Unable to connect: still connected");
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
                                doAfterConnect();
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
