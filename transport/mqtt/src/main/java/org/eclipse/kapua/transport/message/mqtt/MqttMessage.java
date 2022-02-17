/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.transport.message.mqtt;

import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.pubsub.PubSubTransportMessage;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Implementation of {@link TransportMessage} API for {@link org.eclipse.kapua.transport.mqtt.MqttFacade}.
 *
 * @since 1.0.0
 */
public class MqttMessage implements PubSubTransportMessage<MqttTopic, MqttPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * The request topic of this {@link MqttMessage}.
     *
     * @since 1.0.0
     */
    private MqttTopic requestTopic;

    /**
     * The response topic of this {@link MqttMessage}.
     *
     * @since 1.0.0
     */
    private MqttTopic responseTopic;

    /**
     * The timestamp of this {@link MqttMessage}.
     *
     * @since 1.0.0
     */
    private Date timestamp;

    /**
     * The payload of this {@link MqttPayload}.
     *
     * @since 1.0.0
     */
    private MqttPayload payload;

    /**
     * Construct a {@link MqttMessage} with the given parameters.
     *
     * @param requestTopic   The request {@link MqttTopic} to set for this {@link MqttMessage}.
     * @param responseTopic  The response {@link MqttTopic} to set for this {@link MqttMessage}.
     * @param requestPayload The request {@link MqttPayload} to set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public MqttMessage(@NotNull MqttTopic requestTopic, @NotNull MqttTopic responseTopic, @NotNull MqttPayload requestPayload) {
        setRequestTopic(requestTopic);
        setResponseTopic(responseTopic);
        setPayload(requestPayload);
    }

    /**
     * Construct a {@link MqttMessage} with the given parameters.
     *
     * @param requestTopic   The request {@link MqttTopic} to set for this {@link MqttMessage}.
     * @param receivedOn     The timestamp to set for this {@link MqttMessage}.
     * @param requestPayload The request {@link MqttPayload} to set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public MqttMessage(@NotNull MqttTopic requestTopic, @NotNull Date receivedOn, @NotNull MqttPayload requestPayload) {
        setRequestTopic(requestTopic);
        setTimestamp(receivedOn);
        setPayload(requestPayload);
    }

    /**
     * Gets the request {@link MqttTopic} set for this {@link MqttMessage}.
     *
     * @return The request {@link MqttTopic} set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public MqttTopic getRequestTopic() {
        return requestTopic;
    }

    /**
     * Sets the request {@link MqttTopic} set for this {@link MqttMessage}.
     *
     * @param requestTopic The request {@link MqttTopic} to set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public void setRequestTopic(MqttTopic requestTopic) {
        this.requestTopic = requestTopic;
    }

    /**
     * Gets the response {@link MqttTopic} set for this {@link MqttMessage}.
     *
     * @return The response {@link MqttTopic} set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public MqttTopic getResponseTopic() {
        return responseTopic;
    }

    /**
     * Sets the response {@link MqttTopic} set for this {@link MqttMessage}.
     *
     * @param responseTopic The response {@link MqttTopic} to set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public void setResponseTopic(MqttTopic responseTopic) {
        this.responseTopic = responseTopic;
    }

    /**
     * Gets the timestamp set for this {@link MqttMessage}.
     *
     * @return The timestamp set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp set for this {@link MqttMessage}.
     *
     * @param timestamp The timestamp to set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the {@link MqttPayload} set for this {@link MqttMessage}.
     *
     * @return The {@link MqttPayload} set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public MqttPayload getPayload() {
        if (payload == null) {
            payload = new MqttPayload();
        }

        return payload;
    }

    /**
     * Sets the {@link MqttPayload} set for this {@link MqttMessage}.
     *
     * @param payload The {@link MqttPayload} to set for this {@link MqttMessage}.
     * @since 1.0.0
     */
    public void setPayload(MqttPayload payload) {
        this.payload = payload;
    }

    /**
     * Tells whether or not this request {@link MqttMessage} expect a response.
     *
     * @return {@code true} if this request {@link MqttMessage} expect a response, {@code false} otherwise.
     * @since 1.1.0
     */
    public boolean expectResponse() {
        return getResponseTopic() != null;
    }

    /**
     * Gets the {@link MqttMessage} fields concatenated in a user-friendly {@link String}.
     *
     * @return The {@link MqttMessage} fields concatenated in a user-friendly {@link String}.
     * @since 1.2.0
     */
    @Override
    public String toString() {
        String[] toStringTokens = new String[3];

        toStringTokens[0] = getTimestamp() != null ? getTimestamp().toString() : null;
        toStringTokens[1] = getRequestTopic() != null ? getRequestTopic().toString() : null;
        toStringTokens[2] = getPayload() != null ? getPayload().toString() : null;

        return String.join(", ", toStringTokens);
    }
}
