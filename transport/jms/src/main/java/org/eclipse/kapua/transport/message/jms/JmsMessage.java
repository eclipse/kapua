/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
        this.topic = topic;
        this.receivedOn = receivedOn;
        this.payload = payload;
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

}
