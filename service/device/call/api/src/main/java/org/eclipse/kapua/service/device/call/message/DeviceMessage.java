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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message;

import org.eclipse.kapua.message.Message;

import java.util.Date;

/**
 * Device {@link Message} definition.
 *
 * @param <C> {@link DeviceChannel} type
 * @param <P> {@link DevicePayload} type
 * @since 1.0
 */
public interface DeviceMessage<C extends DeviceChannel, P extends DevicePayload> extends Message<C, P> {

    /**
     * Get the {@link DeviceChannel}.
     *
     * @return The {@link DeviceChannel}.
     */
    C getChannel();

    /**
     * Sets the {@link DeviceChannel}.
     *
     * @param channel The {@link DeviceChannel} to set.
     */
    void setChannel(C channel);

    /**
     * Gets the {@link DevicePayload}.
     *
     * @return The {@link DevicePayload}.
     */
    P getPayload();

    /**
     * Sets the {@link DevicePayload}.
     *
     * @param payload The {@link DevicePayload} to set.
     */
    void setPayload(P payload);

    /**
     * Gets the message timestamp.
     *
     * @return The timestamp.
     */
    Date getTimestamp();

    /**
     * Sets the timestamp.
     *
     * @param timestamp The timestamp to set.
     */
    void setTimestamp(Date timestamp);
}
