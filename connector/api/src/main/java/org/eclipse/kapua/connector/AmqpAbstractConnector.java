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
package org.eclipse.kapua.connector;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * Abstract AMQP connector
 */
public abstract class AmqpAbstractConnector<P> extends AbstractConnector<byte[], P> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpAbstractConnector.class);

    protected static final String KEY_MAX_RECONNECTION_ATTEMPTS = "maxReconnectionAttempts";
    protected static final String KEY_EXIT_CODE = "exitCode";

    private AtomicInteger reconnectionFaultCount = new AtomicInteger();
    private Long reconnectTaskId;
    private int maxReconnectionAttempts;
    private int exitCode = -1;

    /**
     * Default protected constructor
     * @param Vertx instance
     * @param converter message instance
     * @param processor processor instance
     */
    protected AmqpAbstractConnector(Vertx vertx, Converter<byte[], P> converter, Processor<P> processor) {
        super(vertx, converter, processor);
    }

    /**
     * Constructor with no message converter
     * @param Vertx instance
     * @param processor processor instance
     */
    protected AmqpAbstractConnector(Vertx vertx, Processor<P> processor) {
        this(vertx, null, processor);
    }

    protected void setConfiguration(Map<String, Object> configuration) {
        logger.info("Configuration");
        Integer tmp = (Integer)configuration.get(KEY_MAX_RECONNECTION_ATTEMPTS);
        maxReconnectionAttempts = tmp != null ? (int) tmp : -1;
        logger.info("Maximum reconnection attemps {}", maxReconnectionAttempts);
        tmp = (Integer)configuration.get(KEY_EXIT_CODE);
        exitCode = tmp != null ? (int) tmp : -1;
        logger.info("Exit code {}", exitCode);
    }

    /**
     * AQMP connection logic
     * @param connectFuture
     */
    protected abstract void connect(final Future<Void> connectFuture);

    /**
     * AQMP disconnection logic
     * @param connectFuture
     */
    protected abstract void disconnect(final Future<Void> connectFuture);

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
                                if (reconnectionFaultCount.incrementAndGet() > maxReconnectionAttempts && maxReconnectionAttempts>-1) {
                                    logger.error("Maximum reconnection attempts reached. Exiting...");
                                    System.exit(exitCode);
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

    @Override
    protected MessageContext<byte[]> convert(MessageContext<?> message) throws KapuaConverterException {
        //this cast is safe since this implementation is using the AMQP connector
        Message msg = (Message)message.getMessage();
        return new MessageContext<byte[]>(
                extractBytePayload(msg.getBody()),
                getMessageParameters(msg));

        // By default, the receiver automatically accepts (and settles) the delivery
        // when the handler returns, if no other disposition has been applied.
        // To change this and always manage dispositions yourself, use the
        // setAutoAccept method on the receiver.
    }

    protected abstract Map<String, Object> getMessageParameters(Message message) throws KapuaConverterException;

    private byte[] extractBytePayload(Section body) throws KapuaConverterException {
        logger.info("Received message with body: {}", body);
        if (body instanceof Data) {
            Binary data = ((Data) body).getValue();
            logger.info("Received DATA message");
            return data.getArray();
        } else if (body instanceof AmqpValue) {
            String content = (String) ((AmqpValue) body).getValue();
            logger.info("Received message with content: {}", content);
            return content.getBytes();
        } else {
            logger.warn("Recevide message with unknown message type! ({})", body != null ? body.getClass() : "NULL");
            // TODO use custom exception
            throw new KapuaConverterException(KapuaErrorCodes.INTERNAL_ERROR);
        }
    }

}
