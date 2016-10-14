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

import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;

public class AssetInfoImpl implements AssetInfo
{
    private StorableId id;
    private String scope;
    private String asset;
    private Date lastMessageTimestamp;
    private StorableId lastMessageId;
   
    public AssetInfoImpl(String scope, StorableId id)
    {
        this.scope = scope;
        this.id = id;
    }

    @Override
    public String getScope()
    {
        return scope;
    }
    
    @Override
    public StorableId getId()
    {
        return id;
    }

    @Override
    public String getAsset()
    {
        return asset;
    }

    public void setAsset(String asset)
    {
        this.asset = asset;
    }

    @Override
    public StorableId getLastMessageId()
    {
        return lastMessageId;
    }

    public void setLastMessageId(StorableId lastMessageId)
    {
        this.lastMessageId = lastMessageId;
    }

    @Override
    public Date getLastMessageTimestamp()
    {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Date lastMessageTimestamp)
    {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
