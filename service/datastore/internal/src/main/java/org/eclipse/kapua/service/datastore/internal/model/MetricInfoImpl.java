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
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Metric information schema implementation
 * 
 * @since 1.0.0
 */
public class MetricInfoImpl implements MetricInfo {

    private StorableId id;
    private KapuaId scopeId;
    private String clientId;
    private String channel;

    private String name;
    private Class<?> metricType;

    private StorableId firstMessageId;
    private Date firstMessageOn;
    private StorableId lastMessageId;
    private Date lastMessageOn;

    /**
     * Construct a metric information for the given scope
     * 
     * @param scopeId
     */
    public MetricInfoImpl(KapuaId scopeId) {
        setScopeId(scopeId);
    }

    /**
     * Construct a metric information for the given scope and storable identifier
     * 
     * @param scopeId
     * @param id
     */
    public MetricInfoImpl(KapuaId scopeId, StorableId id) {
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
     * 
     * @since 1.0.0
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
        return this.clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
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
    public Class<?> getMetricType() {
        return metricType;
    }

    @Override
    public void setMetricType(Class<?> metricType) {
        this.metricType = metricType;
    }

    @Override
    public StorableId getFirstMessageId() {
        return firstMessageId;
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
        return lastMessageId;
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
