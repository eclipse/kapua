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
package org.eclipse.kapua.transport.amqp.message;

import org.eclipse.kapua.transport.message.TransportPayload;

/**
 * Implementation of {@link TransportPayload} API for MQTT transport facade.
 * 
 * @since 1.0.0
 *
 */
public class AmqpPayload implements TransportPayload {

    /**
     * The raw body of this {@link AmqpPayload}.
     */
    private byte[] body;

    /**
     * Construct a {@link AmqpPayload} with the given parameter.
     * 
     * @param body
     *            The raw body to set for this {@link AmqpPayload}.
     * @since 1.0.0
     */
    public AmqpPayload(byte[] body) {
        this.body = body;
    }

    /**
     * Gets the raw body set for this {@link AmqpPayload}.
     * 
     * @return The raw body set for this {@link AmqpPayload}.
     * @since 1.0.0
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets the raw body set for this {@link AmqpPayload}.
     * 
     * @param body
     *            the raw body set for this {@link AmqpPayload}.
     * @since 1.0.0
     */
    public void setBody(byte[] body) {
        this.body = body;
    }
}
