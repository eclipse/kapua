/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.message.mqtt;

import java.util.Date;

import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.pubsub.PubSubTransportMessage;

/**
 * Implementation of {@link TransportMessage} API for MQTT transport facade.
 */
public class MqttMessage implements PubSubTransportMessage<MqttTopic, MqttPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * The request topic of this {@link MqttMessage}.
     */
    private MqttTopic requestTopic;

    /**
     * The response topic of this {@link MqttMessage}.
     */
    private MqttTopic responseTopic;

    /**
     * The timestamp of this {@link MqttMessage}.
     */
    private Date timestamp;

    /**
     * The payload of this {@link MqttPayload}.
     */
    private MqttPayload payload;

    /**
     * Construct a {@link MqttMessage} with the given parameters.
     * 
     * @param requestTopic
     *            The request {@link MqttTopic} to set for this {@link MqttMessage}.
     * @param responseTopic
     *            The response {@link MqttTopic} to set for this {@link MqttMessage}.
     * @param requestPayload
     *            The request {@link MqttPayload} to set for this {@link MqttMessage}.
     */
    public MqttMessage(MqttTopic requestTopic, MqttTopic responseTopic, MqttPayload requestPayload) {
        this(requestTopic, (Date) null, requestPayload);
        this.responseTopic = responseTopic;
    }

    /**
     * Construct a {@link MqttMessage} with the given parameters.
     * 
     * @param requestTopic
     *            The request {@link MqttTopic} to set for this {@link MqttMessage}.
     * @param receivedOn
     *            The timestamp to set for this {@link MqttMessage}.
     * @param requestPayload
     *            The request {@link MqttPayload} to set for this {@link MqttMessage}.
     */
    public MqttMessage(MqttTopic requestTopic, Date receivedOn, MqttPayload requestPayload) {
        this.requestTopic = requestTopic;
        this.timestamp = receivedOn;
        this.payload = requestPayload;
    }

    /**
     * Gets the request {@link MqttTopic} set for this {@link MqttMessage}.
     * 
     * @return The request {@link MqttTopic} set for this {@link MqttMessage}.
     */
    public MqttTopic getRequestTopic() {
        return requestTopic;
    }

    /**
     * Sets the request {@link MqttTopic} set for this {@link MqttMessage}.
     * 
     * @param requestTopic
     *            The request {@link MqttTopic} to set for this {@link MqttMessage}.
     */
    public void setRequestTopic(MqttTopic requestTopic) {
        this.requestTopic = requestTopic;
    }

    /**
     * Gets the response {@link MqttTopic} set for this {@link MqttMessage}.
     * 
     * @return The response {@link MqttTopic} set for this {@link MqttMessage}.
     */
    public MqttTopic getResponseTopic() {
        return responseTopic;
    }

    /**
     * Sets the response {@link MqttTopic} set for this {@link MqttMessage}.
     * 
     * @param responseTopic
     *            The response {@link MqttTopic} to set for this {@link MqttMessage}.
     */
    public void setResponseTopic(MqttTopic responseTopic) {
        this.responseTopic = responseTopic;
    }

    /**
     * Gets the timestamp set for this {@link MqttMessage}.
     * 
     * @return The timestamp set for this {@link MqttMessage}.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp set for this {@link MqttMessage}.
     * 
     * @param timestamp
     *            The timestamp to set for this {@link MqttMessage}.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the {@link MqttPayload} set for this {@link MqttMessage}.
     * 
     * @return The {@link MqttPayload} set for this {@link MqttMessage}.
     */
    public MqttPayload getPayload() {
        return payload;
    }

    /**
     * Sets the {@link MqttPayload} set for this {@link MqttMessage}.
     * 
     * @param payload
     *            The {@link MqttPayload} to set for this {@link MqttMessage}.
     */
    public void setPayload(MqttPayload payload) {
        this.payload = payload;
    }

    public MqttTopic getChannel( ) {
        return requestTopic;
    }

    public void setChannel(MqttTopic topic) {
        this.requestTopic = topic;
    }
}
