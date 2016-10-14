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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura;

import java.util.Date;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

public class KuraMessage<C extends KuraChannel, P extends KuraPayload> implements DeviceMessage<C, P>
{
    protected C    channel;
    protected Date timestamp;
    protected P    payload;

    public KuraMessage()
    {
        super();
    }

    public KuraMessage(C channel, Date timestamp, P payload)
    {
        this();
        this.channel = channel;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    @Override
    public C getChannel()
    {
        return channel;
    }

    @Override
    public P getPayload()
    {
        return payload;
    }

    @Override
    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}
