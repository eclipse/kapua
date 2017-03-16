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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.ChannelInfoCreator;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Channel information schema creator implementation
 * 
 * @since 1.0.0
 */
public class ChannelInfoCreatorImpl implements ChannelInfoCreator {

    private KapuaId scopeId;
    private String clientId;
    private String channel;
    private StorableId messageId;
    private Date messageTimestamp;

    /**
     * Construct a channel information creator for the given account
     * 
     * @param account
     * 
     * @since 1.0.0
     */
    public ChannelInfoCreatorImpl(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    protected void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId != null ? scopeId : null;
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String fullChannelName) {
        this.channel = fullChannelName;
    }

    @Override
    public StorableId getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(StorableId messageId) {
        this.messageId = messageId;
    }

    @Override
    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    @Override
    public void setMessageTimestamp(Date messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }
}
