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

import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Client information schema implementation
 * 
 * @since 1.0
 *
 */
public class ClientInfoImpl implements ClientInfo
{
    private StorableId id;
    private String     account;
    private String     clientId;
    private StorableId firstPublishedMessageId;
    private Date       firstPublishedMessageTimestamp;
    private StorableId lastPublishedMessageId;
    private Date       lastPublishedMessageTimestamp;

    /**
     * Construct a client information for the given account
     * 
     * @param account
     */
    public ClientInfoImpl(String account)
    {
        this.account = account;
    }

    /**
     * Construct a client information for the given account and storable identifier
     * 
     * @param account
     * @param id
     */
    public ClientInfoImpl(String account, StorableId id)
    {
        this(account);
        this.id = id;
    }

    @Override
    public String getAccount()
    {
        return account;
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
    public String getClientId()
    {
        return clientId;
    }

    @Override
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
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
