/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.amqp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.qpid.amqp_1_0.jms.impl.BytesMessageImpl;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.client.message.MessageConstants;
import org.eclipse.kapua.message.internal.MessageErrorCodes;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AMQP consumer handler
 * 
 * @since 1.6.0
 */
public class AmqpClientConsumerHandler {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClientConsumerHandler.class);

    private static final int MAX_MESSAGE_SIZE = 4*1024*1024;

    /**
     * List of received messages.
     * 
     * @since 1.6.0
     */
    private List<AmqpMessage> responses;

    /**
     * Number of response messages expected
     * 
     * @since 1.6.0
     */
    private int expectedResponses;

    /**
     * Construct a callback with the given response container and 1 as expected response messages
     * 
     * @param responses
     *            The container in which put the received messages
     * @since 1.6.0
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
     * @since 1.6.0
     */
    public AmqpClientConsumerHandler(List<AmqpMessage> responses, int expectedResponses) {
        this.responses = responses;
        this.expectedResponses = expectedResponses;
    }

    public void consumeMessage(Message message, DestinationTranslator destinationTranslator) {
        try {
            consumeMessageInternal(message, destinationTranslator);
        } catch (KapuaException | JMSException e) {
            throw KapuaRuntimeException.internalError(e, "Error while processing message " + e.getMessage());
        }
        finally {
            try {
                message.acknowledge();
            } catch (JMSException e) {
                logger.warn("Acknowledge message error: {}", e.getMessage(), e);
            }
        }
    }

    private void consumeMessageInternal(Message message, DestinationTranslator destinationTranslator) throws JMSException, KapuaException {
        String topic = message.getObjectProperty(MessageConstants.PROPERTY_ORIGINAL_TOPIC).toString();
        AmqpTopic amqpTopic = new AmqpTopic(destinationTranslator.translateToClientDomain(topic));
        //the message must be loaded in memory to be converted into Kapua message so there is no need to read it chunk by chunk
        //just add a limit to the maximum size allowed
        BytesMessageImpl bytesMessageImpl = (BytesMessageImpl)message;
        int messageSize = (int)bytesMessageImpl.getBodyLength();
        if (messageSize>MAX_MESSAGE_SIZE) {
            throw new KapuaException(MessageErrorCodes.INVALID_MESSAGE, "Message size exceeding the maximum allowed: " + MAX_MESSAGE_SIZE);
        }
        byte[] payload = new byte[messageSize];
        int read = bytesMessageImpl.readBytes(payload);
        logger.debug("AMQP message received. Bytes read: {}", read);
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
