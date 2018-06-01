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
import org.eclipse.kapua.converter.Converter;
import org.eclipse.kapua.converter.KapuaConverterException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;

/**
 * Abstract AMQP connector
 */
public abstract class AmqpAbstractConnector<P> extends AbstractConnector<byte[], P> {

    protected final static Logger logger = LoggerFactory.getLogger(AmqpAbstractConnector.class);

    /**
     * Default protected constructor
     * @param converter message converter instance
     * @param processor message processor instance
     */
    protected AmqpAbstractConnector(Vertx vertx, Converter<byte[], P> converter, Processor<P> processor) {
        super(vertx, converter, processor);
    }

    /**
     * Constructor with no message converter
     * @param processor
     */
    protected AmqpAbstractConnector(Vertx vertx, Processor<P> processor) {
        this(vertx, null, processor);
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
