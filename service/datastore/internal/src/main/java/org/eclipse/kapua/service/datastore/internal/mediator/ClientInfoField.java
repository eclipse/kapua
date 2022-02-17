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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link ClientInfo} schema
 *
 * @since 1.0.0
 */
public enum ClientInfoField implements StorableField {

    /**
     * Scope id
     *
     * @since 1.0.0
     */
    SCOPE_ID(ClientInfoSchema.CLIENT_SCOPE_ID),

    /**
     * Client identifier
     *
     * @since 1.0.0
     */
    CLIENT_ID(ClientInfoSchema.CLIENT_ID),

    /**
     * Timestamp
     *
     * @since 1.0.0
     */
    TIMESTAMP(ClientInfoSchema.CLIENT_TIMESTAMP),

    /**
     * Message identifier
     *
     * @since 1.0.0
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
