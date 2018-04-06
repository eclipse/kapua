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

import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Abstract AMQP connector
 *
 * @param <M> Converter message type (optional)
 */
public abstract class AbstractAmqpSource<M> implements MessageSource<M> {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractAmqpSource.class);

    protected Vertx vertx;
    protected MessageFilter<M> filter;
    protected MessageHandler<M> messageHandler;
    private boolean connected;

    /**
     * Default protected constructor
     * @param vertx instance
     */
    protected AbstractAmqpSource(Vertx vertx) {
        this.vertx = vertx;
    }

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public void messageFilter(MessageFilter<M> filter) {
        this.filter = filter;
    }

    @Override
    public void messageHandler(MessageHandler<M> handler) {
        this.messageHandler = handler;
    }

    protected boolean isProcessMessage(MessageContext<M> message) {
        return filter.matches(message);
    }

    /**
     * AQMP connection logic
     * @param connectFuture
     */
    protected abstract void connect(final Future<Void> connectFuture);

    /**
     * AQMP disconnection logic
     * @param disconnectFuture
     */
    protected abstract void disconnect(final Future<Void> disconnectFuture);

    protected abstract Map<String, Object> getMessageParameters(Message message) throws KapuaException;

    protected byte[] extractBytePayload(Section body) throws KapuaException {
        logger.debug("Received message with body: {}", body);
        if (body instanceof Data) {
            Binary data = ((Data) body).getValue();
            logger.debug("Received data message: {}", body);
            return data != null ? data.getArray() : new byte[0];
        } else if (body instanceof AmqpValue) {
            String content = (String) ((AmqpValue) body).getValue();
            logger.debug("Received message with content: {}", content);
            return content != null ? content.getBytes() : new byte[0];
        } else {
            logger.warn("Received message with unknown message type! ({})", body != null ? body.getClass() : "null");
            throw new KapuaConverterException(KapuaErrorCodes.INTERNAL_ERROR);
        }
    }

}
