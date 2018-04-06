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
package org.eclipse.kapua.transport.amqpproton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.qpid.proton.amqp.Binary;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.engine.Delivery;
import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.amqp.proton.ConsumerHandler;
import org.eclipse.kapua.transport.amqpproton.message.AmqpMessage;
import org.eclipse.kapua.transport.amqpproton.message.AmqpPayload;
import org.eclipse.kapua.transport.amqpproton.message.AmqpTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic implementation of {@link ConsumerHandler} interface.
 * <p>
 * This generic implementation is meant to be used in the transport layer of Kapua
 * to receive responses from the device when a request is sent to the device.
 * It offers the capability of receive one or more responses.
 * </p>
 * 
 * @since 1.0.0
 */
public class AmqpClientConsumerHandler implements ConsumerHandler {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClientConsumerHandler.class);

    /**
     * List of received messages.
     * 
     * @since 1.0.0
     */
    private List<AmqpMessage> responses;

    /**
     * Number of response messages expected
     * 
     * @since 1.0.0
     */
    private int expectedResponses;

    /**
     * Construct a callback with the given response container and 1 as expected response messages
     * 
     * @param responses
     *            The container in which put the received messages
     * @since 1.0.0
     */
    public AmqpClientConsumerHandler(List<AmqpMessage> responses) {
        this(responses, 1);
    }

    /**
     * Construct a callback with the given response container and the given number of expected responses
     * 
     * @param responses
     *            The container in which put the received messages
     * @param expectedResponses
     *            The number of the expected responses to wait before notify observers.
     * @since 1.0.0
     */
    public AmqpClientConsumerHandler(List<AmqpMessage> responses, int expectedResponses) {
        this.responses = responses;
        this.expectedResponses = expectedResponses;
    }

    @Override
    public void consumeMessage(Delivery delivery, Message message) throws Exception {
        AmqpTopic amqpTopic = new AmqpTopic(message.getAddress());
        Section body = message.getBody();
        byte[] payload = null;
        if (body instanceof Data) {
            Binary data = ((Data) body).getValue();
            logger.info("Received DATA message");
            payload = data.getArray();
        } else if (body instanceof AmqpValue) {
            String content = (String) ((AmqpValue) body).getValue();
            logger.info("Received message with content: {}", content);
            payload = content.getBytes();
        } else {
            logger.warn("Received message with unknown message type! ({})", body != null ? body.getClass() : "NULL");
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }
        AmqpMessage amqpMessage = new AmqpMessage(amqpTopic,
                new Date(),
                new AmqpPayload(payload));

        //
        // Add to the received responses
        if (responses == null) {
            responses = new ArrayList<AmqpMessage>();
        }

        //
        // Convert MqttMessage to the given device-levelMessage
        responses.add(amqpMessage);

        //
        // notify if all expected responses arrived
        if (expectedResponses == responses.size()) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

}
