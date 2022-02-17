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
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

import java.util.Date;

/**
 * {@link DeviceMessage} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @param <C> The {@link KuraChannel} type.
 * @param <P> The {@link KuraPayload} type.
 * @since 1.0.0
 */
public class KuraMessage<C extends KuraChannel, P extends KuraPayload> implements DeviceMessage<C, P> {

    protected C channel;
    protected Date timestamp;
    protected P payload;

    /**
     * Constructor
     *
     * @since 1.0.0
     */
    public KuraMessage() {
        super();
    }

    /**
     * Constructor.
     *
     * @param channel   The {@link KuraChannel}.
     * @param timestamp The timestamp.
     * @param payload   The {@link KuraPayload}.
     * @see org.eclipse.kapua.service.device.call.message.DeviceMessage
     * @since 1.0.0
     */
    public KuraMessage(C channel, Date timestamp, P payload) {
        this();

        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    @Override
    public C getChannel() {
        return channel;
    }

    @Override
    public P getPayload() {
        return payload;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void setChannel(C channel) {
        this.channel = channel;
    }

    @Override
    public void setPayload(P payload) {
        this.payload = payload;
    }
}
