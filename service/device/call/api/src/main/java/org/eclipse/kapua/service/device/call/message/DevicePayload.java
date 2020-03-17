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
package org.eclipse.kapua.service.device.call.message;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.Payloads;
import org.eclipse.kapua.message.Payload;

import java.util.Date;
import java.util.Map;

/**
 * {@link DevicePayload} definition.
 *
 * @since 1.0.0
 */
public interface DevicePayload extends Payload {

    /**
     * Gets the timestamp.
     *
     * @return The timestamp.
     * @since 1.0.0
     */
    Date getTimestamp();

    /**
     * Sets the timestamp.
     *
     * @param timestamp The timestamp
     * @since 1.0.0
     */
    void setTimestamp(Date timestamp);

    /**
     * Get the {@link DevicePosition}
     *
     * @return A {@link DevicePosition} if present, or {@code null} otherwise
     * @since 1.0.0
     */
    DevicePosition getPosition();

    /**
     * Sets the {@link DevicePosition}
     *
     * @param position The {@link DevicePosition}
     * @since 1.0.0
     */
    void setPosition(DevicePosition position);

    /**
     * Gets the metrics.
     *
     * @return The metrics
     * @since 1.0.0
     */
    Map<String, Object> getMetrics();

    /**
     * Sets the metrics.
     *
     * @param metrics The metrics
     * @since 1.0.0
     */
    void setMetrics(Map<String, Object> metrics);
    
    /**
     * Gets the raw body.
     *
     * @return The raw body.
     * @since 1.0.0
     */
    byte[] getBody();

    /**
     * Sets the raw body.
     *
     * @param body The raw body.
     * @since 1.0.0
     */
    void setBody(byte[] body);

    //
    // Encode/Decode stuff
    //

    /**
     * Converts the {@link DevicePayload} to a protobuf encoded {@code byte[]}
     *
     * @return The protobuf encoding of {@code this} {@link DevicePayload}
     * @since 1.0.0
     */
    byte[] toByteArray();

    /**
     * Converts the given {@code byte[]} to a {@link DevicePayload}
     *
     * @param rawPayload The {@code byte[]} to convert
     * @throws KapuaException If the given {@code byte[]} is not properly formatted.
     * @since 1.0.0
     */
    void readFromByteArray(byte[] rawPayload)
            throws KapuaException;

    //
    // Display methods
    //

    /**
     * Returns a {@link String} for displaying the {@link DevicePayload} content.
     *
     * @return A {@link String} used for displaying the content of the {@link DevicePayload}, never returns {@code null}
     * @since 1.0.0
     */
    default String toDisplayString() {
        return Payloads.toDisplayString(getMetrics());
    }
}
