/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.Date;

/**
 * {@link MetricInfo} implementation.
 *
 * @since 1.0.0
 */
public class MetricInfoImpl implements MetricInfo {

    private static final long serialVersionUID = 1L;

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


    public MetricInfoImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.0.0
     */
    public MetricInfoImpl(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    @Override
    public StorableId getId() {
        return id;
    }

    @Override
    public void setId(StorableId id) {
        this.id = id;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
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
