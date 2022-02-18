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
import org.eclipse.kapua.service.datastore.model.ChannelInfoCreator;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.Date;

/**
 * {@link ChannelInfoCreator} implementation.
 *
 * @since 1.0.0
 */
public class ChannelInfoCreatorImpl implements ChannelInfoCreator {

    private KapuaId scopeId;
    private String clientId;
    private String name;
    private StorableId messageId;
    private Date messageTimestamp;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.0.0
     */
    public ChannelInfoCreatorImpl(KapuaId scopeId) {
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
