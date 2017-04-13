/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.data;

import java.util.Date;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

/**
 * Kura device message implementation.
 * 
 * @since 1.0
 * 
 */
public class KuraDataMessage implements DeviceMessage<KuraDataChannel, KuraDataPayload>
{
    protected KuraDataChannel  channel;
    protected Date             timestamp;
    protected KuraDataPayload  payload;

    /**
     * Constructor
     */
    public KuraDataMessage()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param channel
     * @param timestamp
     * @param payload
     */
    public KuraDataMessage(KuraDataChannel channel, Date timestamp, KuraDataPayload payload)
    {
        this();
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    @Override
    public KuraDataChannel getChannel()
    {
        return channel;
    }

    @Override
    public KuraDataPayload getPayload()
    {
        return payload;
    }

    @Override
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * Set the message timestamp
     * 
     * @param timestamp
     */
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}
