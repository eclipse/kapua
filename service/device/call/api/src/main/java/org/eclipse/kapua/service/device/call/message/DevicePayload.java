/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.Payloads;
import org.eclipse.kapua.message.Payload;

import java.util.Date;
import java.util.Map;

/**
 * Device {@link Payload} definition.
 *
 * @since 1.0
 */
public interface DevicePayload extends Payload {

    /**
     * Get the message timestamp
     *
     * @return
     */
    Date getTimestamp();

    /**
     * Get the {@link DevicePosition}
     *
     * @return A {@link DevicePosition} if present, or {@code null} otherwise
     */
    DevicePosition getPosition();

    /**
     * Get the metrics
     *
     * @return
     */
    Map<String, Object> getMetrics();

    /**
     * Get the message body
     *
     * @return
     */
    byte[] getBody();

    void setTimestamp(Date timestamp);

    void setPosition(DevicePosition position);

    void setMetrics(Map<String, Object> metrics);

    void setBody(byte[] body);

    //
    // Encode/Decode stuff
    //

    /**
     * Converts the {@link DevicePayload} to a protobuf encoded {@code byte[]}
     *
     * @return The protobuf encoding of {@code this} {@link DevicePayload}
     */
    byte[] toByteArray();

    /**
     * Converts the given {@code byte[]} to a {@link DevicePayload}
     *
     * @param rawPayload The {@code byte[]} to convert
     * @throws KapuaException If the given {@code byte[]} is not properly formatted.
     */
    void readFromByteArray(byte[] rawPayload)
            throws KapuaException;

    //
    // Display methods
    //

    /**
     * Returns a string for displaying the {@link DevicePayload} content.
     *
     * @return A {@link String} used for displaying the content of the {@link DevicePayload}, never returns {@code null}
     */
    default String toDisplayString() {
        return Payloads.toDisplayString(getMetrics());
    }
}
