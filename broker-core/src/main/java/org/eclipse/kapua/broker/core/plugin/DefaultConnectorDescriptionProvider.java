/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 *
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
class DefaultConnectorDescriptionProvider implements ConnectorDescriptorProvider {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConnectorDescriptionProvider.class);

    public final static String DEFAULT_TRANSPORT_PROTOCOL = "MQTT";

    private final Map<String, ConnectorDescriptor> configuration = new HashMap<>();
    private final ConnectorDescriptor defaultDescriptor;

    DefaultConnectorDescriptionProvider() {
        if (!BrokerSetting.getInstance().getBoolean(BrokerSettingKey.DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR, false)) {
            defaultDescriptor = createDefaultDescriptor();
        } else {
            defaultDescriptor = null;
        }

        try {
            loadConfigurations(configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ConnectorDescriptor getDescriptor(final String connectorName) {
        return configuration.getOrDefault(connectorName, defaultDescriptor);
    }

    private static void loadConfigurations(final Map<String, ConnectorDescriptor> configuration) throws Exception {
        final String uri = BrokerSetting.getInstance().getString(BrokerSettingKey.CONFIGURATION_URI, null);

        if (uri == null || uri.isEmpty()) {
            return;
        }

        final Properties p = new Properties();

        final URL url = new URL(uri);
        try (final InputStream in = url.openStream()) {
            p.load(in);
        }

        final String transports = p.getProperty("transports", "");
        for (String transport : transports.split("\\s*,\\s*")) {
            if (transport.isEmpty()) {
                continue;
            }
            ConnectorDescriptor desc = loadFromProperties(p, transport);
            if (desc != null) {
                configuration.put(transport, desc);
            }
        }
    }

    private static ConnectorDescriptor loadFromProperties(Properties p, String transport) throws ClassNotFoundException {

        final Map<MessageType, Class<? extends DeviceMessage<?, ?>>> deviceClasses = new HashMap<>();
        final Map<MessageType, Class<? extends KapuaMessage<?, ?>>> kapuaClasses = new HashMap<>();

        String transportProtocol = p.getProperty(String.format("%s.transport_protocol", transport));

        for (MessageType mt : MessageType.values()) {

            {
                final String key = String.format("%s.device.%s", transport, mt.name());
                final String clazzName = p.getProperty(key);
                if (clazzName != null && !clazzName.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Class<? extends DeviceMessage<?, ?>> clazz = (Class<? extends DeviceMessage<?, ?>>) Class.forName(clazzName).asSubclass(DeviceMessage.class);
                    deviceClasses.put(mt, clazz);
                } else {
                    logger.info("No class mapping for key {}", key);
                }
            }

            {
                final String key = String.format("%s.kapua.%s", transport, mt.name());
                final String clazzName = p.getProperty(key);
                if (clazzName != null && !clazzName.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Class<? extends KapuaMessage<?, ?>> clazz = (Class<? extends KapuaMessage<?, ?>>) Class.forName(clazzName).asSubclass(KapuaMessage.class);
                    kapuaClasses.put(mt, clazz);
                } else {
                    logger.info("No class mapping for key {}", key);
                }
            }

        }

        return new ConnectorDescriptor(transportProtocol, deviceClasses, kapuaClasses);
    }

    /**
     * Create the default Kura connector description
     *
     * @return the default instance, never returns {@code null}
     */
    private static ConnectorDescriptor createDefaultDescriptor() {
        final Map<MessageType, Class<? extends DeviceMessage<?, ?>>> deviceClass = new HashMap<>();
        deviceClass.put(MessageType.APP, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class);
        deviceClass.put(MessageType.BIRTH, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class);
        deviceClass.put(MessageType.DISCONNECT, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage.class);
        deviceClass.put(MessageType.MISSING, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage.class);
        deviceClass.put(MessageType.NOTIFY, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraNotifyMessage.class);
        deviceClass.put(MessageType.UNMATCHED, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedMessage.class);
        deviceClass.put(MessageType.DATA, org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage.class);

        final Map<MessageType, Class<? extends KapuaMessage<?, ?>>> kapuaClass = new HashMap<>();
        kapuaClass.put(MessageType.APP, org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class);
        kapuaClass.put(MessageType.BIRTH, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class);
        kapuaClass.put(MessageType.DISCONNECT, org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage.class);
        kapuaClass.put(MessageType.MISSING, org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage.class);
        kapuaClass.put(MessageType.NOTIFY, org.eclipse.kapua.message.device.lifecycle.KapuaNotifyMessage.class);
        kapuaClass.put(MessageType.UNMATCHED, org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage.class);
        kapuaClass.put(MessageType.DATA, org.eclipse.kapua.message.device.data.KapuaDataMessage.class);

        return new ConnectorDescriptor(DEFAULT_TRANSPORT_PROTOCOL, deviceClass, kapuaClass);
    }

}