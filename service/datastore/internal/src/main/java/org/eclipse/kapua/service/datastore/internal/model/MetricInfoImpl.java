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

import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;

public class MetricInfoImpl implements MetricInfo
{
    private StorableId id;
    private String scope;
    private String fullTopicName;
    private String name;
    private String type;
    private Object value;
    private StorableId lastMessageId;
    private Date lastMessageTimestamp;
    
    public MetricInfoImpl(String scope, StorableId id)
    {
        this.id = id;
        this.scope = scope;
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
    public String getFullTopicName()
    {
        return fullTopicName;
    }

    @Override
    public void setFullTopicName(String fullTopicName)
    {
        this.fullTopicName = fullTopicName;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public <T> T getValue(Class<T> clazz)
    {
        return clazz.cast(value);
    }

    @Override
    public <T> void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public StorableId getLastMessageId()
    {
        return lastMessageId;
    }

    @Override
    public void setLastMessageId(StorableId lastMessageId)
    {
        this.lastMessageId = lastMessageId;
    }

    @Override
    public Date getLastMessageTimestamp()
    {
        return lastMessageTimestamp;
    }

    @Override
    public void setLastMessageTimestamp(Date lastMessageTimestamp)
    {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
 }
