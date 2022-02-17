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
package org.eclipse.kapua.broker.core.message;

import java.io.Serializable;

import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Platform message container.<BR>
 * Contains the message and additional information such as the connection id and the connector descriptor (useful for the message translation process)
 *
 * @param <M> Contained message type
 * @since 1.0
 */
public class CamelKapuaMessage<M extends KapuaMessage<?, ?>> implements Serializable {

    private static final long serialVersionUID = 6299705913639768816L;

    private M message;
    private KapuaId connectionId;
    private String datastoreId;
    private ConnectorDescriptor connectorDescriptor;

    /**
     * Constructs a new Camel Kapua message
     *
     * @param message             message
     * @param connectionId        connection id
     * @param connectorDescriptor connector descriptor
     */
    public CamelKapuaMessage(M message, KapuaId connectionId, ConnectorDescriptor connectorDescriptor) {
        this.connectionId = connectionId;
        this.message = message;
        this.connectorDescriptor = connectorDescriptor;
    }

    public M getMessage() {
        return message;
    }

    public void setMessage(M message) {
        this.message = message;
    }

    public KapuaId getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(KapuaId connectionId) {
        this.connectionId = connectionId;
    }

    public ConnectorDescriptor getConnectorDescriptor() {
        return connectorDescriptor;
    }

    public void setConnectorDescriptor(ConnectorDescriptor connectorDescriptor) {
        this.connectorDescriptor = connectorDescriptor;
    }

    public String getDatastoreId() {
        return datastoreId;
    }

    public void setDatastoreId(String datastoreId) {
        this.datastoreId = datastoreId;
    }

}
