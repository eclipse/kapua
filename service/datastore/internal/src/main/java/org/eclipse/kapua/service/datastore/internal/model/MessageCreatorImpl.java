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

import org.eclipse.kapua.service.datastore.model.MessageCreator;
import org.eclipse.kapua.service.datastore.model.Payload;

public class MessageCreatorImpl implements MessageCreator
{
    private String   topic;
    private Date    timestamp;
    private Date    receivedOn;
    private Payload payload;

    @Override
    public Date getTimestamp()
    {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public Date getReceivedOn()
    {
        return receivedOn;
    }

    @Override
    public void setReceivedOn(Date receivedOn)
    {
        this.receivedOn = receivedOn;
    }

    @Override
    public String getTopic()
    {
        return topic;
    }

    @Override
    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    @Override
    public Payload getPayload()
    {
        return this.payload;
    }

    @Override
    public void setPayload(Payload payload)
    {
        this.payload = payload;
    }

}
