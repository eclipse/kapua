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
 * @param <P> Processor message type
 */
public abstract class AbstractAmqpSource<M> implements MessageSource<M> {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractAmqpSource.class);

    protected Vertx vertx;
    private boolean connected;

    /**
     * Default protected constructor
     * @param vertx instance
     * @param converter message instance
     * @param processorMap processor map instances
     * @param errorProcessorMap error processor map instances
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
            logger.debug("Received DATA message");
            return data.getArray();
        } else if (body instanceof AmqpValue) {
            String content = (String) ((AmqpValue) body).getValue();
            logger.debug("Received message with content: {}", content);
            return content.getBytes();
        } else {
            logger.warn("Recevide message with unknown message type! ({})", body != null ? body.getClass() : "NULL");
            throw new KapuaConverterException(KapuaErrorCodes.INTERNAL_ERROR);
        }
    }

}
