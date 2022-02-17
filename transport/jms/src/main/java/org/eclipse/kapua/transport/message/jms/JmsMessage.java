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
package org.eclipse.kapua.transport.message.jms;

import org.eclipse.kapua.transport.message.TransportMessage;

import java.util.Date;

/**
 * Implementation of {@link TransportMessage} API for JMS transport facade.
 */
public class JmsMessage implements TransportMessage<JmsTopic, JmsPayload> {

    private static final long serialVersionUID = 1L;

    /**
     * The topic of this {@link JmsMessage}.
     *
     * @since 1.0.0
     */
    private JmsTopic topic;

    /**
     * The receivedOn of this {@link JmsMessage}.
     *
     * @since 1.0.0
     */
    private Date receivedOn;

    /**
     * The payload of this {@link JmsMessage}.
     *
     * @since 1.0.0
     */
    private JmsPayload payload;

    /**
     * Construct a {@link JmsMessage} with the given parameters.
     *
     * @param topic      The {@link JmsTopic} to set for this {@link JmsMessage}.
     * @param receivedOn The received on to set for this {@link JmsMessage}.
     * @param payload    The {@link JmsPayload} to set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public JmsMessage(JmsTopic topic, Date receivedOn, JmsPayload payload) {
        setTopic(topic);
        setReceivedOn(receivedOn);
        setPayload(payload);
    }

    /**
     * Gets the {@link JmsTopic} set for this {@link JmsMessage}.
     *
     * @return The {@link JmsTopic} set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public JmsTopic getTopic() {
        return topic;
    }

    /**
     * Sets the {@link JmsTopic} set for this {@link JmsMessage}.
     *
     * @param topic The {@link JmsTopic} to set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public void setTopic(JmsTopic topic) {
        this.topic = topic;
    }

    /**
     * Gets the received on set for this {@link JmsMessage}.
     *
     * @return The received on set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public Date getReceivedOn() {
        return receivedOn;
    }

    /**
     * Sets the received on set for this {@link JmsMessage}.
     *
     * @param receivedOn The received on to set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    /**
     * Gets the {@link JmsPayload} set for this {@link JmsMessage}.
     *
     * @return The {@link JmsPayload} set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public JmsPayload getPayload() {
        if (payload == null) {
            payload = new JmsPayload(new byte[0]);
        }

        return payload;
    }

    /**
     * Sets the {@link JmsPayload} set for this {@link JmsMessage}.
     *
     * @param payload The {@link JmsPayload} to set for this {@link JmsMessage}.
     * @since 1.0.0
     */
    public void setPayload(JmsPayload payload) {
        this.payload = payload;
    }

    /**
     * Gets the {@link JmsMessage} fields concatenated in a user-friendly {@link String}.
     *
     * @return The {@link JmsMessage} fields concatenated in a user-friendly {@link String}.
     * @since 1.2.0
     */
    @Override
    public String toString() {
        String[] toStringTokens = new String[3];

        toStringTokens[0] = getReceivedOn() != null ? getReceivedOn().toString() : null;
        toStringTokens[1] = getTopic() != null ? getTopic().toString() : null;
        toStringTokens[2] = getPayload() != null ? getPayload().toString() : null;

        return String.join(", ", toStringTokens);
    }
}
