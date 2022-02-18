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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.MetricInfoCreator;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.Date;

/**
 * {@link MetricInfoCreator} implementation.
 *
 * @since 1.0.0
 */
public class MetricInfoCreatorImpl<T> implements MetricInfoCreator<T> {

    private KapuaId scopeId;
    private String clientId;
    private String channel;

    private String name;
    private Class<T> metricType;

    private StorableId messageId;
    private Date messageTimestamp;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}
     * @since 1.0.0
     */
    public MetricInfoCreatorImpl(KapuaId scopeId) {
        setScopeId(scopeId);
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
    public Class<T> getMetricType() {
        return metricType;
    }

    @Override
    public void setMetricType(Class<T> metricType) {
        this.metricType = metricType;
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
