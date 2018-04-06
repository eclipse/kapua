/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal.transport;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.message.transport.TransportChannel;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportMessageType;
import org.eclipse.kapua.message.transport.TransportPayload;
import org.eclipse.kapua.message.transport.TransportQos;

/**
 * Kapua transport message object reference implementation.
 */
public class TransportMessageImpl extends KapuaMessageImpl<TransportChannel, TransportPayload> implements TransportMessage {

    private static final long serialVersionUID = 7633228507593265455L;

    private String scopeName;
    private TransportMessageType messageType;
    private TransportQos transportQos;

    @Override
    public String getScopeName() {
        return scopeName;
    }

    @Override
    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    /**
     * Gets the message typeused in the transport message
     * 
     * @return
     */
    @Override
    public TransportMessageType getMessageType() {
        return messageType;
    }

    /**
     * Sets the message type used in the transport message
     */
    @Override
    public void setMessageType(TransportMessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public TransportQos getQoS() {
        return transportQos;
    }

    @Override
    public void setQoS(TransportQos transportQos) {
        this.transportQos = transportQos;
    }

}
