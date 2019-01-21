/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.amqp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

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
public class AmqpClientConsumerHandler implements Handler<io.vertx.core.eventbus.Message<JsonObject>> {

    private static final Logger logger = LoggerFactory.getLogger(AmqpClientConsumerHandler.class);

    private final static String VT_TOPIC_PREFIX = "topic://VirtualTopic.";

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
    public void handle(io.vertx.core.eventbus.Message<JsonObject> event) {
        JsonObject message = event.body();
        if (message==null) {
            logger.warn("Received empty message from address {}", event.address());
        }
        else {
            try {
                JsonFactory factory = new JsonFactory();

                ObjectMapper mapper = new ObjectMapper(factory);
                JsonNode rootNode = mapper.readTree(message.toString());
                byte[] payload = null;
                Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
                while (fieldsIterator.hasNext()) {

                    Map.Entry<String,JsonNode> field = fieldsIterator.next();
                    if (field.getKey().equals(AmqpConstants.BODY)) {
                        payload = field.getValue().binaryValue();
                    }
                    logger.info("Key: {}  -  Value: {}", field.getKey(), field.getValue());
                }
                AmqpTopic amqpTopic = new AmqpTopic(event.address());
                //TODO get topic from message metrics (to check the consistency with the real address)
                AmqpMessage amqpMessage = new AmqpMessage(amqpTopic,
                      new Date(),
                      new AmqpPayload(payload));
//                Section body = message;
//                byte[] payload = null;
//                if (body instanceof Data) {
//                    Binary data = ((Data) body).getValue();
//                    logger.info("Received DATA message: size {}", data.getLength());
//                    payload = data.getArray();
//                } else if (body instanceof AmqpValue) {
//                    String content = (String) ((AmqpValue) body).getValue();
//                    logger.info("Received message with content: {}", content);
//                    payload = content.getBytes();
//                } else {
//                    logger.warn("Received message with unknown message type! ({})", body != null ? body.getClass() : "NULL");
//                    //TODO throw runtime exception?
//                    payload = new byte[0];
//                }
//                AmqpMessage amqpMessage = new AmqpMessage(amqpTopic,
//                        new Date(),
//                        new AmqpPayload(payload));
                //
                // Add to the received responses
                if (responses == null) {
                    responses = new ArrayList<AmqpMessage>();
                }
                //TODO FIXME why the message interchanges between AMQP-MQTT connectors (with MQTT virtual topic on) on AtiveMQ doesn't clean the topic://VirtualTopic prefix?
                if (amqpMessage.getChannel().getTopic().toString().startsWith(VT_TOPIC_PREFIX)) {
                    amqpMessage.setChannel(new AmqpTopic(amqpMessage.getChannel().getTopic().toString().substring(VT_TOPIC_PREFIX.length()).replaceAll("\\.", "/")));
                }
                //
                // Convert MqttMessage to the given device-levelMessage
                logger.info("Converted message {}", amqpMessage.getPayload());
                responses.add(amqpMessage);

                //
                // notify if all expected responses arrived
                if (expectedResponses == responses.size()) {
                    synchronized (this) {
                        notifyAll();
                    }
                }
            }
            catch (Exception e) {
                logger.error("Error handling message {}", message, e);
            }
        }
    }

}
