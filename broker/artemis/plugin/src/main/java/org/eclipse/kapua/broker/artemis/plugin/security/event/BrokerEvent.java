/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis.plugin.security.event;

import org.eclipse.kapua.client.security.context.SessionContext;
import org.eclipse.kapua.model.id.KapuaId;

public class BrokerEvent {

    public enum EventType {
        disconnectClientByClientId,
        disconnectClientByConnectionId
    }

    private EventType eventType;
    private String username;
    private KapuaId scopeId;
    private String clientId;
    private String connectionId;
    private String oldConnectionId;

    public BrokerEvent(EventType eventType, SessionContext sessionContext, SessionContext oldSessionContext) {
        this.eventType = eventType;
        this.username = sessionContext.getUsername();
        this.scopeId = sessionContext.getScopeId();
        this.clientId = sessionContext.getClientId();
        this.connectionId = sessionContext.getConnectionId();
        this.oldConnectionId = oldSessionContext.getConnectionId();
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getUsername() {
        return username;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getOldConnectionId() {
        return oldConnectionId;
    }

}
