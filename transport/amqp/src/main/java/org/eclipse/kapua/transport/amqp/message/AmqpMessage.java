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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.transport.amqp.message;

import java.util.Date;

import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.pubsub.PubSubTransportMessage;

/**
 * Implementation of {@link TransportMessage} API for MQTT transport facade.
 */
public class AmqpMessage implements PubSubTransportMessage<AmqpTopic, AmqpPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * The request topic of this {@link AmqpMessage}.
     */
    private AmqpTopic requestTopic;

    /**
     * The response topic of this {@link AmqpMessage}.
     */
    private AmqpTopic responseTopic;

    /**
     * The timestamp of this {@link AmqpMessage}.
     */
    private Date timestamp;

    /**
     * The payload of this {@link AmqpPayload}.
     */
    private AmqpPayload payload;

    /**
     * Construct a {@link AmqpMessage} with the given parameters.
     * 
     * @param requestTopic
     *            The request {@link AmqpTopic} to set for this {@link AmqpMessage}.
     * @param responseTopic
     *            The response {@link AmqpTopic} to set for this {@link AmqpMessage}.
     * @param requestPayload
     *            The request {@link AmqpPayload} to set for this {@link AmqpMessage}.
     */
    public AmqpMessage(AmqpTopic requestTopic, AmqpTopic responseTopic, AmqpPayload requestPayload) {
        this(requestTopic, (Date) null, requestPayload);
        this.responseTopic = responseTopic;
    }

    /**
     * Construct a {@link AmqpMessage} with the given parameters.
     * 
     * @param requestTopic
     *            The request {@link AmqpTopic} to set for this {@link AmqpMessage}.
     * @param receivedOn
     *            The timestamp to set for this {@link AmqpMessage}.
     * @param requestPayload
     *            The request {@link AmqpPayload} to set for this {@link AmqpMessage}.
     */
    public AmqpMessage(AmqpTopic requestTopic, Date receivedOn, AmqpPayload requestPayload) {
        this.requestTopic = requestTopic;
        this.timestamp = receivedOn;
        this.payload = requestPayload;
    }

    /**
     * Gets the request {@link AmqpTopic} set for this {@link AmqpMessage}.
     * 
     * @return The request {@link AmqpTopic} set for this {@link AmqpMessage}.
     */
    public AmqpTopic getRequestTopic() {
        return requestTopic;
    }

    /**
     * Sets the request {@link AmqpTopic} set for this {@link AmqpMessage}.
     * 
     * @param requestTopic
     *            The request {@link AmqpTopic} to set for this {@link AmqpMessage}.
     */
    public void setRequestTopic(AmqpTopic requestTopic) {
        this.requestTopic = requestTopic;
    }

    /**
     * Gets the response {@link AmqpTopic} set for this {@link AmqpMessage}.
     * 
     * @return The response {@link AmqpTopic} set for this {@link AmqpMessage}.
     */
    public AmqpTopic getResponseTopic() {
        return responseTopic;
    }

    /**
     * Sets the response {@link AmqpTopic} set for this {@link AmqpMessage}.
     * 
     * @param responseTopic
     *            The response {@link AmqpTopic} to set for this {@link AmqpMessage}.
     */
    public void setResponseTopic(AmqpTopic responseTopic) {
        this.responseTopic = responseTopic;
    }

    /**
     * Gets the timestamp set for this {@link AmqpMessage}.
     * 
     * @return The timestamp set for this {@link AmqpMessage}.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp set for this {@link AmqpMessage}.
     * 
     * @param timestamp
     *            The timestamp to set for this {@link AmqpMessage}.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the {@link AmqpPayload} set for this {@link AmqpMessage}.
     * 
     * @return The {@link AmqpPayload} set for this {@link AmqpMessage}.
     */
    public AmqpPayload getPayload() {
        return payload;
    }

    /**
     * Sets the {@link AmqpPayload} set for this {@link AmqpMessage}.
     * 
     * @param payload
     *            The {@link AmqpPayload} to set for this {@link AmqpMessage}.
     */
    public void setPayload(AmqpPayload payload) {
        this.payload = payload;
    }

    public AmqpTopic getChannel( ) {
        return requestTopic;
    }

    public void setChannel(AmqpTopic topic) {
        this.requestTopic = topic;
    }
}
