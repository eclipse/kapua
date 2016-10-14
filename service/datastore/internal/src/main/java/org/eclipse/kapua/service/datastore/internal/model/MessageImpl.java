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
package org.eclipse.kapua.service.datastore.internal.model;

import java.util.Date;

import org.eclipse.kapua.service.datastore.model.Message;
import org.eclipse.kapua.service.datastore.model.Payload;
import org.eclipse.kapua.service.datastore.model.StorableId;

public class MessageImpl implements Message
{
    private StorableId  id;
    private String   topic;
    private Date    timestamp;
    private Date    receivedOn;
    private Payload payload;

    public MessageImpl()
    {
    }

    public MessageImpl(StorableId id)
    {
        this.id = id;
    }

    public MessageImpl(StorableId id, Date timestamp, String topic)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.topic = topic;
    }

    @Override
    public StorableId getId()
    {
        return this.id;
    }

    @Override
    public String getTopic()
    {
        return this.topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    @Override
    public Date getTimestamp()
    {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public Date getReceivedOn()
    {
        return this.receivedOn;
    }

    public void setReceivedOn(Date receivedOn)
    {
        this.receivedOn = receivedOn;
    }

    public void setId(StorableId id)
    {
        this.id = id;
    }

    @Override
    public Payload getPayload()
    {
        return this.payload;
    }

    public void setPayload(Payload payload)
    {
        this.payload = payload;
    }
}
