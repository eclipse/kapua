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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

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
    public enum MessageType {
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
         * Data message type
         */
        DATA
    }

    private final Map<MessageType, Class<? extends DeviceMessage<?, ?>>> deviceClass;
    private final Map<MessageType, Class<? extends KapuaMessage<?, ?>>> kapuaClass;

    private String transportProtocol;

    /**
     * Constructs a new connector descriptor
     *
     * @param deviceClass device level messages implementation classes
     * @param kapuaClass  Kapua level messages implementation classes
     */
    public ConnectorDescriptor(String transportProtocol, Map<MessageType, Class<? extends DeviceMessage<?, ?>>> deviceClass, Map<MessageType, Class<? extends KapuaMessage<?, ?>>> kapuaClass) {
        Objects.requireNonNull(deviceClass);
        Objects.requireNonNull(kapuaClass);
        Objects.requireNonNull(transportProtocol);

        this.deviceClass = new EnumMap<>(deviceClass);
        this.kapuaClass = new EnumMap<>(kapuaClass);
        this.transportProtocol = transportProtocol;
    }

    public Class<? extends DeviceMessage<?, ?>> getDeviceClass(MessageType messageType) {
        return this.deviceClass.get(messageType);
    }

    public Class<? extends KapuaMessage<?, ?>> getKapuaClass(MessageType messageType) {
        return this.kapuaClass.get(messageType);
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

}
