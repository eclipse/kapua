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
 *******************************************************************************/
package org.eclipse.kapua.transport.message.jms;

import org.eclipse.kapua.transport.message.TransportPayload;

/**
 * Implementation of {@link TransportPayload} API for JMS transport facade.
 *
 * @since 1.0.0
 */
public class JmsPayload implements TransportPayload {

    /**
     * The raw body of this {@link JmsPayload}.
     *
     * @since 1.0.0
     */
    private byte[] body;

    /**
     * Construct a {@link JmsPayload} with the given parameter.
     *
     * @param body The raw body to set for this {@link JmsPayload}.
     * @since 1.0.0
     */
    public JmsPayload(byte[] body) {
        this.body = body;
    }

    /**
     * Gets the raw body set for this {@link JmsPayload}.
     *
     * @return The raw body set for this {@link JmsPayload}.
     * @since 1.0.0
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets the raw body set for this {@link JmsPayload}.
     *
     * @param body the raw body set for this {@link JmsPayload}.
     * @since 1.0.0
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * Says whether or not the {@link #getBody()} has value.
     * <p>
     * Checks for {@code null} and size equals to 0
     *
     * @return {@code true} if {@link #getBody()} is not {@code null} and {@link #getBody()}{@code length > 0}, {@code false} otherwise.
     */
    public boolean hasBody() {
        return getBody() != null && getBody().length > 0;
    }
}
