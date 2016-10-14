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

import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.TopicInfo;

public class TopicInfoImpl implements TopicInfo
{
    private StorableId id;
    private String scope;
    private StorableId lastMsgId;
    private Date   lastMsgTimestamp;
    private String fullTopicName;

    public TopicInfoImpl(String scope, StorableId id)
    {
        this.scope= scope;
        this.id = id;
    }
    
    @Override
    public StorableId getId()
    {
        return id;
    }

    @Override
    public String getScope()
    {
        return scope;
    }

    @Override
    public StorableId getLastMessageId()
    {
        return this.lastMsgId;
    }

    public void setLastMessageId(StorableId lastMsgId)
    {
        this.lastMsgId = lastMsgId;
    }

    @Override
    public Date getLastMessageTimestamp()
    {
        return lastMsgTimestamp;
    }

    public void setLastMessageTimestamp(Date lastMsgTimestamp)
    {
        this.lastMsgTimestamp = lastMsgTimestamp;
    }

    @Override
    public String getFullTopicName()
    {
        return fullTopicName;
    }
    
    public void setFullTopicName(String fullTopicName)
    {
        this.fullTopicName = fullTopicName;
    }
}
