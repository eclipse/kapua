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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link ClientInfo} schema
 * 
 * @since 1.0
 *
 * @since 1.0.0
 */
public enum ClientInfoField implements StorableField {

    /**
     * Scope id
     */
    SCOPE_ID(ClientInfoSchema.CLIENT_SCOPE_ID),
    /**
     * Client identifier
     */
    CLIENT_ID(ClientInfoSchema.CLIENT_ID),
    /**
     * Timestamp
     */
    TIMESTAMP(ClientInfoSchema.CLIENT_TIMESTAMP),
    /**
     * Message identifier
     */
    MESSAGE_ID(ClientInfoSchema.CLIENT_MESSAGE_ID);

    private String field;

    private ClientInfoField(String name) {
        this.field = name;
    }

    @Override
    public String field() {
        return field;
    }

    /**
     * Get the client identifier (combining accountName and clientId).<br>
     * <b>If the id is null then it is generated</b>
     * 
     * @param id
     * @param scopeId
     * @param clientId
     * @return
     */
    public static String getOrDeriveId(StorableId id, KapuaId scopeId, String clientId) {
        if (id == null) {
            return DatastoreUtils.getHashCode(scopeId.toCompactId(), clientId);
        } else {
            return id.toString();
        }
    }

    /**
     * Get the client identifier (combining accountName and clientId).<br>
     * <b>If the id is null then it is generated</b>
     * 
     * @param id
     * @param clientInfo
     * @return
     */
    public static String getOrDeriveId(StorableId id, ClientInfo clientInfo) {
        return getOrDeriveId(id, clientInfo.getScopeId(), clientInfo.getClientId());
    }


}
