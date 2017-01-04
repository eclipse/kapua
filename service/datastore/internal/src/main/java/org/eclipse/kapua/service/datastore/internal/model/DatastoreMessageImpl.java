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

import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Implementation of the message returned by the data store find services
 * 
 * @since 1.0
 *
 */
public class DatastoreMessageImpl extends KapuaMessageImpl<KapuaChannel, KapuaPayload> implements DatastoreMessage
{
    private StorableId datastoreId;
    private Date       timestamp;

    @Override
    public StorableId getDatastoreId()
    {
        return datastoreId;
    }

    /**
     * Set the datastore message identifier
     * 
     * @param id
     */
    public void setDatastoreId(StorableId id)
    {
        this.datastoreId = id;
    }

    @Override
    public Date getTimestamp()
    {
        return this.timestamp;
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
