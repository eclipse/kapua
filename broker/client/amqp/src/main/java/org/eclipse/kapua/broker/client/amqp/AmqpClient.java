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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.proton.ProtonDelivery;
import io.vertx.proton.ProtonLinkOptions;
import io.vertx.proton.ProtonMessageHandler;
import io.vertx.proton.ProtonQoS;
import io.vertx.proton.ProtonReceiver;
import io.vertx.proton.ProtonSender;
import io.vertx.proton.ProtonSession;

public class AmqpClient {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClient.class);

    public static int maxWait = 3000;

    private final static Integer PREFETCH = new Integer(10);
    private final static boolean AUTO_ACCEPT = true;
    private final static ProtonQoS QOS = ProtonQoS.AT_LEAST_ONCE;

    private boolean valid;
    private String clientId;
    private DestinationTranslator destinationTranslator;

    protected ProtonSession session;
    protected ProtonReceiver receiver;
    protected ProtonMessageHandler messageHandler;
    protected ProtonSender sender;

    public AmqpClient(ProtonSession session, ClientOptions clientOptions) {
        this.session = session;
        clientId = clientOptions.getString(AmqpClientOptions.CLIENT_ID);
        destinationTranslator = (DestinationTranslator)clientOptions.get(AmqpClientOptions.DESTINATION_TRANSLATOR);
        logger.info("Created client {} for session {}", clientId, session);
        validate();
    }

    public String getClientId() {
        return clientId;
    }

    public void messageHandler(ProtonMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private void invalidate() {
        valid = false;
    }

    private void validate() {
        valid = true;
    }

    private boolean isvalid() {
        return (session!=null ? session.getCondition().getCondition()==null && session.getRemoteCondition().getCondition()==null : true) &&
                (receiver!=null ? receiver.getCondition().getCondition()==null && receiver.getRemoteCondition().getCondition()==null : true) &&
                (sender!=null ? sender.getCondition().getCondition()==null && sender.getRemoteCondition().getCondition()==null : true) &&
                valid;
    }

    private void checkValidity() throws KapuaException {
        if (!isvalid()) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }
    }

    public void send(Message message, String destination) throws KapuaException {
        checkValidity();
        String senderDestination = destination;
        if (destinationTranslator!=null) {
            senderDestination = destinationTranslator.translate(destination);
        }
        long startTime = System.currentTimeMillis();
        CountDownLatch senderCountDown = new CountDownLatch(1);
        boolean operationSucceed = true;
        sender = createSender(session, senderDestination, true, QOS, senderCountDown);
        try {
            operationSucceed = senderCountDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("Wait interrupted");
        }
        if (operationSucceed && sender.isOpen()) {
            logger.info("Acquired open sender after: {}ms DONE", (System.currentTimeMillis() - startTime));
        }
        else {
            logger.info("Acquired open sender after: {}ms FAILED", (System.currentTimeMillis() - startTime));
            notifySenderError();
        }
        logger.info("Sender ready after: {}ms", System.currentTimeMillis() - startTime);
        message.setAddress(destination);
        sender.send(message, delivery -> {
            logger.info("MESSAGE SENT!!!! {} - {}", message);
        });
    }

    public void subscribe(String destination, ProtonMessageHandler messageHandler) throws KapuaException {
        checkValidity();
        String receiverDestination = destination;
        if (destinationTranslator!=null) {
            receiverDestination = destinationTranslator.translate(destination);
        }
        messageHandler(messageHandler);
        long startTime = System.currentTimeMillis();
        CountDownLatch receiverCountDown = new CountDownLatch(1);
        boolean operationSucceed = true;
        receiver = createReceiver(session, receiverDestination, PREFETCH, AUTO_ACCEPT, QOS, receiverCountDown, messageHandler);
        try {
            operationSucceed = receiverCountDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("Wait interrupted");
        }
        if (operationSucceed && receiver.isOpen()) {
            logger.info("Acquired open receiver after: {}ms DONE", (System.currentTimeMillis() - startTime));
        }
        else {
            logger.info("Acquired open receiver after: {}ms FAILED", (System.currentTimeMillis() - startTime));
            notifyReceiverError();
        }
        logger.info("Receiver ready after {}ms", System.currentTimeMillis() - startTime);
    }

    protected ProtonReceiver createReceiver(ProtonSession session, String destination, int prefetch, boolean autoAccept, ProtonQoS qos, CountDownLatch receiverCountDown, ProtonMessageHandler messageHandler) {
        ProtonReceiver receiver = null;
        try {
            logger.info("Register consumer for destination {}... (session: {})", destination, session);
            // The client ID is set implicitly into the destination subscribed
            receiver = session.createReceiver(destination);

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
                    notifyReceiverError();
                }
            });
            receiver.closeHandler(recv -> {
                logger.warn("Receiver is closed! (session: {})", session, recv.cause());
                notifyReceiverError();
            });
            receiver.open();
            logger.info("Register consumer for destination {}... OPENED (session: {})", destination, session);
        }
        catch(Exception e) {
            notifyReceiverError();
        }
        return receiver;
    }

    public void notifyReceiverError() {
        logger.info("Receiver ERROR");
        invalidate();
    }

    public void notifySenderError() {
        logger.info("Sender ERROR");
        invalidate();
    }

    protected ProtonSender createSender(ProtonSession session, String destination, boolean autoSettle, ProtonQoS qos, CountDownLatch senderCountDown) {
        ProtonSender sender = null;
        try {
            logger.info("Register sender for destination {}... (session: {})", destination, session);
            ProtonLinkOptions senderOptions = new ProtonLinkOptions();
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
                   notifySenderError();
               }
            });
            sender.closeHandler(snd -> {
                logger.warn("Sender is closed! (session: {})", session, snd.cause());
                notifySenderError();
            });
            sender.open();
            logger.info("Register sender for destination {}... OPENED (session: {})", destination, session);
        }
        catch(Exception e) {
            notifySenderError();
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

    public void clean() {
        long startTime = System.currentTimeMillis();
        CountDownLatch cleanCountDown;
        if (sender != null && receiver != null) {
            cleanCountDown = new CountDownLatch(2);
        }
        else if (sender == null && receiver == null) {
            cleanCountDown = new CountDownLatch(0);
        }
        else {
            cleanCountDown = new CountDownLatch(1);
        }
        if (sender!=null) {
            logger.info("Closing sender {} for session {}", sender, session);
            sender.closeHandler(ar -> {
                if(ar.succeeded()) {
                    logger.info("Sender close (session: {})", session, ar.cause());
                }
                else {
                    logger.warn("Sender close FAILED (session: {})", session, ar.cause());
                }
                if (cleanCountDown!=null) {
                    cleanCountDown.countDown();
                }
            });
            sender.close();
            sender = null;
            logger.info("Closing sender for session {} DONE", session);
        }
        if (receiver!=null) {
            logger.info("Closing receiver {} for session {}", receiver, session);
            receiver.closeHandler(ar -> {
                if(ar.succeeded()) {
                    logger.info("Receiver close (session: {})", session, ar.cause());
                }
                else {
                    logger.warn("Receiver close FAILED (session: {})", session, ar.cause());
                }
                if (cleanCountDown!=null) {
                    cleanCountDown.countDown();
                }
            });
            receiver.close();
            receiver = null;
            logger.info("Closing receiver for session {} DONE", session);
        }
        boolean operationSucceed = true;
        try {
            operationSucceed = cleanCountDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("Wait interrupted");
        }
        if (operationSucceed) {
            logger.info("Cleaned receiver/sender {} after: {}ms {} DONE", this, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        else {
            logger.info("Cleaned receiver/sender {} after: {}ms {} FAILED", this, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        if (session != null) {
            session.closeHandler(event -> {
                if(event.succeeded()) {
                    logger.info("Session close (session: {})", session, event.cause());
                    session = null;
                }
                else {
                    logger.warn("Session close FAILED (session: {})", session, event.cause());
                }
            });
            session.close();
            logger.info("Closing session {} for session {} DONE", session, session);
            session = null;
        }
        try {
            operationSucceed &= cleanCountDown.await(AmqpClient.maxWait, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.debug("Wait interrupted");
        }
        if (sender!=null && sender.isOpen()) {
            logger.warn("Cleaned sender {} - {} after: {}ms FAILED (sender not null or still connected)", sender, this, (System.currentTimeMillis() - startTime));
            notifySenderError();
        }
        else if (receiver!=null && receiver.isOpen()) {
            logger.warn("Cleaned receiver {} - {} after: {}ms FAILED (receiver not null or still connected)", receiver, this, (System.currentTimeMillis() - startTime));
            notifyReceiverError();
        }
        else if (session!=null) {
            logger.warn("Cleaned session {} - {} after: {}ms FAILED (session not null or still connected)", session, this, (System.currentTimeMillis() - startTime));
            notifyReceiverError();
        }
        else if (operationSucceed) {
            logger.info("Cleaned client {} after: {}ms {} DONE", this, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
        else {
            logger.info("Cleaned client {} after: {}ms {} FAILED", this, (System.currentTimeMillis() - startTime), Thread.currentThread().getId(), Thread.currentThread().getName());
        }
    }

}
