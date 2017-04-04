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
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Channel information schema implementation
 * 
 * @since 1.0.0
 */
public class ChannelInfoImpl implements ChannelInfo {

    private StorableId id;
    private KapuaId scopeId;
    private String clientId;
    private String name;

    private StorableId firstMessageId;
    private Date firstMessageOn;
    private StorableId lastMessageId;
    private Date lastMessageOn;

    /**
     * Construct a channel information for the given scope
     * 
     * @param scope
     * 
     * @since 1.0.0
     */
    public ChannelInfoImpl(KapuaId scope) {
        setScopeId(scope);
    }

    /**
     * Construct a channel information for the given scope and storable identifier
     * 
     * @param scope
     * @param id
     * 
     * @since 1.0.0
     */
    public ChannelInfoImpl(KapuaId scopeId, StorableId id) {
        this(scopeId);
        this.id = id;
    }

    @Override
    public StorableId getId() {
        return id;
    }

    /**
     * Set the storable identifier
     * 
     * @param id
     */
    public void setId(StorableId id) {
        this.id = id;
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
        return clientId;
    }

    /**
     * Set the client identifier
     * 
     * @param clientId
     * 
     * @since 1.0.0
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public StorableId getFirstMessageId() {
        return this.firstMessageId;
    }

    @Override
    public void setFirstMessageId(StorableId firstMessageId) {
        this.firstMessageId = firstMessageId;
    }

    @Override
    public Date getFirstMessageOn() {
        return firstMessageOn;
    }

    @Override
    public void setFirstMessageOn(Date firstMessageOn) {
        this.firstMessageOn = firstMessageOn;
    }

    @Override
    public StorableId getLastMessageId() {
        return this.lastMessageId;
    }

    @Override
    public void setLastMessageId(StorableId lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    @Override
    public Date getLastMessageOn() {
        return lastMessageOn;
    }

    @Override
    public void setLastMessageOn(Date lastMessageOn) {
        this.lastMessageOn = lastMessageOn;
    }

}
