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
package org.eclipse.kapua.broker.client.amqp.proton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.qpid.proton.Proton;
import org.apache.qpid.proton.amqp.messaging.Source;
import org.apache.qpid.proton.amqp.messaging.Target;
import org.apache.qpid.proton.amqp.transport.ErrorCondition;
import org.apache.qpid.proton.engine.BaseHandler;
import org.apache.qpid.proton.engine.Connection;
import org.apache.qpid.proton.engine.Delivery;
import org.apache.qpid.proton.engine.Event;
import org.apache.qpid.proton.engine.Receiver;
import org.apache.qpid.proton.engine.Sender;
import org.apache.qpid.proton.engine.Session;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.message.impl.MessageImpl;
import org.apache.qpid.proton.reactor.FlowController;
import org.apache.qpid.proton.reactor.Handshaker;
import org.apache.qpid.proton.reactor.Reactor;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.proton.ClientOptions.AmqpClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmqpClient extends BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClient.class);

    private final static String ID_PATTERN = "%s_%s";

    private DestinationTranslator destinationTranslator;

    private Thread threadClient;
    protected boolean connected;
    protected AtomicInteger reconnectionFaultCount = new AtomicInteger();
    protected Long reconnectTaskId;
    private ClientOptions options;
    private String clientId;
    private Reactor r;
    private Connection conn;
    private Session ssn;
    private Sender snd;
    private Receiver rec;
    private ConsumerHandler consumerHandler;

    //internal fields used by the publisher
    private int nextTag;
    protected ByteArrayOutputStream current = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];

    public AmqpClient(ClientOptions options) throws IOException {
        this.options = options;
        clientId = options.getString(AmqpClientOptions.CLIENT_ID);
        Object tmp = options.get(AmqpClientOptions.DESTINATION_TRANSLATOR);
        if (tmp == null) {
            logger.debug("No destiantion translator defined.");
        }
        else if (tmp instanceof DestinationTranslator) {
            destinationTranslator = (DestinationTranslator) tmp;
        }
        else {
            KapuaException.internalError(String.format("The destination translator must be null or an instance of DestinationTranslator! found {}", tmp));
        }
        logger.info("Creating client: {}", clientId);
        add(new Handshaker());
        add(new FlowController());
        r = Proton.reactor(this);
    }

    @Override
    public void onReactorInit(Event event) {
        conn = event.getReactor().connectionToHost(options.getString(AmqpClientOptions.BROKER_HOST), options.getInt(AmqpClientOptions.BROKER_PORT, 5672), this);
    }

    @Override
    public void onConnectionInit(Event event) {
        conn = event.getConnection();
        conn.setContainer(clientId);
        conn.open();

        ssn = conn.session();
        ssn.open();
    }

    public void subscribe(String destination, ConsumerHandler consumerHandler) {
        logger.info("Subscribe to {}", destination);
        this.consumerHandler = consumerHandler;
        rec = ssn.receiver(getReceiverId());
        Source source = new Source();
        source.setAddress(getAddress(destination));
        rec.setSource(source);
        Target t = new Target();
        t.setAddress(getAddress(destination));
        rec.setTarget(t);
        rec.flow(10);
        rec.open();
    }

    @Override
    public void onTransportError(Event event) {
        ErrorCondition condition = event.getTransport().getCondition();
        if (condition != null) {
            logger.error("Error: " + condition.getDescription());
        } else {
            logger.error("Error (no description returned).");
        }
    }

    @Override
    public void onDelivery(Event event) {
        Receiver receiver = (Receiver)event.getLink();
        logger.info("Received delivery - Receiver: {} - from link: {} - equals: {} - consumer handler {}", rec, receiver, rec==receiver, consumerHandler);
        Delivery delivery = receiver.current();
        if (delivery != null) {
            int count;
            while ((count = receiver.recv(buffer, 0, buffer.length)) > 0) {
                current.write(buffer, 0, count);
            }
            if (delivery.isPartial()) {
                return;
            }
            byte[] data = current.toByteArray();
            current.reset();
            Message msg = Proton.message();
            msg.decode(data, 0, data.length);
            receiver.advance();
            logger.debug("Received message from {}", msg.getAddress());

            try {
                if (consumerHandler != null) {
                    consumerHandler.consumeMessage(delivery, msg);
                }
                else {
                    logger.warn("Message received for a null consumer handler!");
                }
            } catch (Exception e) {
                logger.error("Message processing error: {}", e.getMessage(), e);
            }
            finally {
                delivery.settle();
            }
            //close the receiver since the pattern used by the "device command" sends one message as request and wait for only one message as reply
            receiver.close();
            //no needing to call free() for both receiver and sender since it's called implicitly by the close method!
            //receiver.free();
        }
    }

    public void send(Message message, String destination) throws KapuaException {
        snd = ssn.sender(getSenderId());
        logger.debug("Send message to {} - snd {}", destination, snd);
        Target target = new Target();
        target.setAddress(getAddress(destination));
        snd.setTarget(target);
        Source source = new Source();
        source.setAddress(getAddress(destination));
        snd.setSource(source);
        message.setAddress(message.getAddress());
        snd.open();

        int bufferSize = 1024;
        byte[] encodedMessage = new byte[bufferSize];
        MessageImpl msg = (MessageImpl) message;
        int len = msg.encode2(encodedMessage, 0, bufferSize);

        // looks like the message is bigger than our initial buffer, lets resize and try again.
        if (len > encodedMessage.length) {
          encodedMessage = new byte[len];
          msg.encode(encodedMessage, 0, len);
        }
        byte[] tag = String.valueOf(nextTag++).getBytes();
        Delivery dlv = snd.delivery(tag);
        snd.send(encodedMessage, 0, len);
        dlv.settle();
        snd.advance();
        snd.close();
        //no needing to call free() for both receiver and sender since it's called implicitly by the close method!
        //snd.free();
        snd = null;
        logger.info("Send message to {} - snd {} DONE", destination, snd);
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public void onLinkLocalOpen(Event e) {
        super.onLinkLocalOpen(e);
        logger.debug("onLinkLocalOpen! {}", e);
    }

    @Override
    public void onLinkLocalClose(Event e) {
        super.onLinkLocalClose(e);
        if (e.getContext() instanceof Receiver) {
            //if the closing object is the Receiver then set to null also the consumer handler
            logger.info("Cleaning consumer handler...");
            consumerHandler = null;
        }
        logger.debug("onLinkLocalClose! {}", e);
    }

    @Override
    public void onLinkLocalDetach(Event e) {
        super.onLinkLocalDetach(e);
        logger.debug("onLinkLocalDetach! {}", e);
    }

    @Override
    public void onLinkRemoteOpen(Event e) {
        super.onLinkRemoteOpen(e);
        logger.debug("onLinkRemoteOpen! {}", e);
    }

    @Override
    public void onLinkRemoteDetach(Event e) {
        super.onLinkRemoteDetach(e);
        logger.debug("onLinkRemoteDetach! {}", e);
    }

    @Override
    public void onLinkRemoteClose(Event e) {
        super.onLinkRemoteClose(e);
        logger.debug("onLinkRemoteClose! {}", e);
    }

    @Override
    public void onConnectionLocalClose(Event e) {
        super.onConnectionLocalClose(e);
        logger.debug("onConnectionLocalClose! {}", e);
    }

    @Override
    public void onConnectionRemoteClose(Event e) {
        super.onConnectionRemoteClose(e);
        logger.debug("onConnectionRemoteClose! {}", e);
    }

    @Override
    public void onConnectionLocalOpen(Event e) {
        super.onConnectionLocalOpen(e);
        logger.debug("onConnectionLocalOpen! {}", e);
    }

    @Override
    public void onConnectionRemoteOpen(Event e) {
        super.onConnectionRemoteOpen(e);
        setConnected(true);
        logger.debug("onConnectionRemoteOpen! {}", e);
    }

    @Override
    public void onConnectionUnbound(Event e) {
        super.onConnectionUnbound(e);
        logger.debug("onConnectionUnbound! {}", e);
        notifyConnectionLost();
    }

    @Override
    public void onConnectionBound(Event e) {
        super.onConnectionBound(e);
        logger.debug("onConnectionBound! {}", e);
    }

    @Override
    public void onConnectionFinal(Event e) {
        super.onConnectionFinal(e);
        logger.debug("onConnectionFinal! {}", e);
    }

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected(boolean connected) {
        synchronized (this) {
            this.connected = connected;
            this.notify();
        }
    }

    public void clean() {
        logger.info("Clean client {} - snd {} - rec {}", clientId, snd, rec);
        if (snd!=null) {
            snd.close();
            //no needing to call free() for both receiver and sender since it's called implicitly by the close method!
            //snd.free();
            snd = null;
        }
        if (rec!=null) {
            //no needing to call free() for both receiver and sender since it's called implicitly by the close method!
            //rec.free();
            rec.close();
            rec = null;
        }
    }

    public void disconnect() {
        logger.info("Disconnecting client '{}'...", clientId);
        clean();
        if (ssn!=null) {
            logger.info("Freeing and closing session for client id '{}'...", clientId);
            ssn.free();
            ssn.close();
        }
        if (conn!=null) {
            logger.info("Freeing and closing connection for client id '{}'...", clientId);
            conn.free();
            conn.close();
        }
        if (r != null) {
            r.stop();
        }
        synchronized (this) {
            threadClient = null;
        }
        logger.info("Disconnecting client '{}'... DONE", clientId);
        connected = false;
    }

    public void connect() {
        synchronized (this) {
            if (threadClient!=null) {
                KapuaException.internalError("Cannot start the client since another instance is still running!");
            }
            else {
                threadClient = new Thread(() -> {
                    logger.info("Starting client: {}", clientId);
                    r.run();
                    logger.info("Closing client: {}", clientId);
                });
                threadClient.start();
            }
        }
    }

    protected void notifyConnectionLost() {
        disconnect();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disconnect();
    }

    private String getAddress(String destination) {
        if (destinationTranslator!=null) {
            return destinationTranslator.translate(destination);
        }
        else {
            return destination;
        }
    }

    private String getReceiverId() {
        return String.format(ID_PATTERN, clientId, "rec");
    }

    private String getSenderId() {
        return String.format(ID_PATTERN, clientId, "snd");
    }
}
