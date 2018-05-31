/*******************************************************************************
 * Copyright (c) 2017, 2018 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.broker.core.connector;

import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;

import java.util.EnumMap;
import java.util.Map;

/**
 * A default implementation of the {@link ConnectorDescriptorProvider} interface
 * <p>
 * This implementation tries to provide some ready-to-use defaults for the {@link ConnectorDescriptorProvider} system.
 * If nothing else is configured, then the provider will return settings which are targeting Eclipse Kura for
 * all transport names requested.
 * </p>
 * <h2>Extra configuration</h2>
 * <p>
 * It is possible to provide a URI to the system using the configuration key {@link BrokerSettingKey#CONFIGURATION_URI}
 * which has to point to a standard Java properties file of the following format:
 * </p>
 * <code><pre>
 * transports=foo,bar,baz
 * foo.device.APP=full.qualified.ClassName
 * foo.kapua.APP=full.qualified.ClassName
 * bar.device.APP=full.qualified.ClassName
 * bar.kapua.APP=full.qualified.ClassName
 * </pre></code>
 * <p>
 * The property {@code transports} holds a comma separated list of all transports by name. For each transport it will
 * look up all keys of {@code<transport>.[device|kapua].<MessageType>}, where {@code MessageType} are all values of
 * the {@link MessageType} enum. The value of each of this key must be a full qualified class name implementing
 * {@link DeviceMessage} for the "device" sub-key and {@link KapuaMessage} for the "kapua" sub-key.
 *
 * <h2>Disabling the default fallback</h2>
 * <p>
 * By default this implementation will return first from the configured transport name and then always
 * return a hard coded default provider. If this default provided should not be returned it is possible
 * to disable this by using the settings key {@link BrokerSettingKey#DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR}.
 * </p>
 */
public class DefaultConnectorDescriptionProvider extends ConnectorDescriptor {

    private static final long serialVersionUID = 2714832325902851833L;

    public static final String DEFAULT_TRANSPORT_PROTOCOL = "MQTT";

    private final static Map<MessageType, Class<? extends DeviceMessage<?, ?>>> DEVICE_CLASS;
    private final static Map<MessageType, Class<? extends KapuaMessage<?, ?>>> KAPUA_CLASS;

    static {
        DEVICE_CLASS = new EnumMap<>(MessageType.class);
        DEVICE_CLASS.put(MessageType.APP, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class);
        DEVICE_CLASS.put(MessageType.BIRTH, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class);
        DEVICE_CLASS.put(MessageType.DISCONNECT, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage.class);
        DEVICE_CLASS.put(MessageType.MISSING, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage.class);
        DEVICE_CLASS.put(MessageType.NOTIFY, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraNotifyMessage.class);
        DEVICE_CLASS.put(MessageType.UNMATCHED, org.eclipse.kapua.service.device.call.message.kura.others.KuraUnmatchedMessage.class);
        DEVICE_CLASS.put(MessageType.DATA, org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage.class);

        KAPUA_CLASS = new EnumMap<>(MessageType.class);
        KAPUA_CLASS.put(MessageType.APP, org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class);
        KAPUA_CLASS.put(MessageType.BIRTH, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class);
        KAPUA_CLASS.put(MessageType.DISCONNECT, org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage.class);
        KAPUA_CLASS.put(MessageType.MISSING, org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage.class);
        KAPUA_CLASS.put(MessageType.NOTIFY, org.eclipse.kapua.message.device.lifecycle.KapuaNotifyMessage.class);
        KAPUA_CLASS.put(MessageType.UNMATCHED, org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage.class);
        KAPUA_CLASS.put(MessageType.DATA, org.eclipse.kapua.message.device.data.KapuaDataMessage.class);
    }

    /**
     * Create the default {@link org.eclipse.kapua.service.device.call.kura.Kura} connector description
     *
     * @return the default instance, never returns {@code null}
     */
    public DefaultConnectorDescriptionProvider() {
        super(DEFAULT_TRANSPORT_PROTOCOL, DEVICE_CLASS, KAPUA_CLASS);
    }

}