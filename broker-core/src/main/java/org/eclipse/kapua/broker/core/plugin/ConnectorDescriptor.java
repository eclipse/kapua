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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import java.io.Serializable;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;

/**
 * Describes the protocol in terms of message type implementations.<br>
 * It's needed by the broker to translate the specific device protocol message domain to the kapua message domain.<br>
 * Each broker connector can be bound with only one device level protocol.
 *
 * @since 1.0
 */
public class ConnectorDescriptor implements Serializable {

    private static final long serialVersionUID = -7220383679289083726L;

    /**
     * Allowed message types
     */
    public static enum MessageType {
        /**
         * Application message type
         */
        APP,
        /**
         * Birth message type
         */
        BIRTH,
        /**
         * Disconnect message type
         */
        DISCONNECT,
        /**
         * Missing message type
         */
        MISSING,
        /**
         * Notify message type
         */
        NOTIFY,
        /**
         * Unmatched filtering message type
         */
        UNMATCHED,
        /**
         * Data message type
         */
        DATA
    }

    private final String connectorName;
    private final String deviceProtocolName;

    private final Map<MessageType, Class<DeviceMessage<?, ?>>> deviceClass;
    private final Map<MessageType, Class<KapuaMessage<?, ?>>> kapuaClass;

    /**
     * Constructs a new connector descriptor
     *
     * @param connectorName
     *            connector name (as defined in the activemq.xml)
     * @param deviceProtocolName
     *            device level protocol name (ie Kura)
     * @param deviceClass
     *            device level messages implementation classes
     * @param kapuaClass
     *            Kapua level messages implementation classes
     */
    public ConnectorDescriptor(String connectorName, String deviceProtocolName, Map<MessageType, Class<DeviceMessage<?, ?>>> deviceClass, Map<MessageType, Class<KapuaMessage<?, ?>>> kapuaClass) {
        this.connectorName = connectorName;
        this.deviceProtocolName = deviceProtocolName;
        this.deviceClass = deviceClass;
        this.kapuaClass = kapuaClass;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public String getDeviceProtocolName() {
        return deviceProtocolName;
    }

    public Class<DeviceMessage<?, ?>> getDeviceClass(MessageType messageType) throws KapuaException {
        return deviceClass.get(messageType);
    }

    public Class<KapuaMessage<?, ?>> getKapuaClass(MessageType messageType) throws KapuaException {
        return kapuaClass.get(messageType);
    }

}
