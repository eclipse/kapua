/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import java.util.Date;

import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Metric information schema implementation
 * 
 * @since 1.0
 *
 */
public class MetricInfoImpl implements MetricInfo
{
    private StorableId id;
    private String     account;
    private String     clientId;
    private String     channel;
    private String     name;
    private String     type;
    private Object     value;
    private StorableId firstPublishedMessageId;
    private Date       firstPublishedMessageTimestamp;
    private StorableId lastPublishedMessageId;
    private Date       lastPublishedMessageTimestamp;

    /**
     * Construct a metric information for the given scope
     * 
     * @param scope
     */
    public MetricInfoImpl(String scope)
    {
        this.account = scope;
    }

    /**
     * Construct a metric information for the given scope and storable identifier
     * 
     * @param scope
     * @param id
     */
    public MetricInfoImpl(String scope, StorableId id)
    {
        this(scope);
        this.id = id;
    }

    @Override
    public StorableId getId()
    {
        return id;
    }

    /**
     * Set the storable identifier
     * 
     * @param id
     */
    public void setId(StorableId id)
    {
        this.id = id;
    }

    @Override
    public String getAccount()
    {
        return account;
    }

    @Override
    public String getClientId()
    {
        return this.clientId;
    }

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    @Override
    public String getChannel()
    {
        return channel;
    }

    @Override
    public void setChannel(String channel)
    {
        this.channel = channel;
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
    public StorableId getFirstPublishedMessageId()
    {
        return firstPublishedMessageId;
    }

    @Override
    public void setFirstPublishedMessageId(StorableId firstPublishedMessageId)
    {
        this.firstPublishedMessageId = firstPublishedMessageId;
    }

    @Override
    public Date getFirstPublishedMessageTimestamp()
    {
        return firstPublishedMessageTimestamp;
    }

    @Override
    public void setFirstPublishedMessageTimestamp(Date firstPublishedMessageTimestamp)
    {
        this.firstPublishedMessageTimestamp = firstPublishedMessageTimestamp;
    }

    @Override
    public StorableId getLastPublishedMessageId()
    {
        return lastPublishedMessageId;
    }

    @Override
    public void setLastPublishedMessageId(StorableId lastPublishedMessageId)
    {
        this.lastPublishedMessageId = lastPublishedMessageId;
    }

    @Override
    public Date getLastPublishedMessageTimestamp()
    {
        return lastPublishedMessageTimestamp;
    }

    @Override
    public void setLastPublishedMessageTimestamp(Date lastPublishedMessageTimestamp)
    {
        this.lastPublishedMessageTimestamp = lastPublishedMessageTimestamp;
    }
}
