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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the connector descriptors from configurations
 * 
 * @since 1.0
 */
// TODO to choose where place configuration and how to load it
public class ConnectorDescriptorLoader
{

    private static Logger logger = LoggerFactory.getLogger(ConnectorDescriptorLoader.class);

    private final static String CONNECTOR_DESCRIPTOR_RESOURCE = "connectors.properties";

    /**
     * Statically create connector descriptor
     * 
     * @return
     */
    public static Map<String, ConnectorDescriptor> loadConnectorDescriptors()
    {
        Map<String, ConnectorDescriptor> connectorsDescriptorMap = new HashMap<String, ConnectorDescriptor>();
        URL configurationUrl = ConnectorDescriptorLoader.class.getClassLoader().getResource(CONNECTOR_DESCRIPTOR_RESOURCE);
        // TODO load parameters from the file

        Map<MessageType, Class<DeviceMessage<?, ?>>> deviceClass = new HashMap<>();
        deviceClass.put(MessageType.APP, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage"));
        deviceClass.put(MessageType.BIRTH, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage"));
        deviceClass.put(MessageType.DISCONNECT, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage"));
        deviceClass.put(MessageType.MISSING, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage"));
        deviceClass.put(MessageType.NOTIFY, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraNotifyMessage"));
        deviceClass.put(MessageType.UNMATCHED, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraUnmatchedMessage"));
        deviceClass.put(MessageType.DATA, getDeviceClazz("org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage"));

        Map<MessageType, Class<KapuaMessage<?, ?>>> kapuaClass = new HashMap<>();
        kapuaClass.put(MessageType.APP, getKapuaClazz("org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage"));
        kapuaClass.put(MessageType.BIRTH, getKapuaClazz("org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage"));
        kapuaClass.put(MessageType.DISCONNECT, getKapuaClazz("org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage"));
        kapuaClass.put(MessageType.MISSING, getKapuaClazz("org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage"));
        kapuaClass.put(MessageType.NOTIFY, getKapuaClazz("org.eclipse.kapua.message.device.lifecycle.KapuaNotifyMessage"));
        kapuaClass.put(MessageType.UNMATCHED, getKapuaClazz("org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage"));
        kapuaClass.put(MessageType.DATA, getKapuaClazz("org.eclipse.kapua.message.device.data.KapuaDataMessage"));

        connectorsDescriptorMap.put("mqtt", new ConnectorDescriptor("mqtt", "kura", deviceClass, kapuaClass));
        connectorsDescriptorMap.put("mqtts", new ConnectorDescriptor("mqtts", "kura", deviceClass, kapuaClass));
        
        connectorsDescriptorMap.put("ws", new ConnectorDescriptor("ws", "kura", deviceClass, kapuaClass));
        connectorsDescriptorMap.put("wss", new ConnectorDescriptor("wss", "kura", deviceClass, kapuaClass));
        
        return connectorsDescriptorMap;
    }

    /**
     * Return a specific Kapua message Class
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Class<KapuaMessage<?, ?>> getKapuaClazz(String clazz)
    {
        try {
            return (Class<KapuaMessage<?, ?>>) Class.forName(clazz);
        }
        catch (ClassNotFoundException e) {
            // TODO throw runtime exception!
            logger.error("Cannot instantiate class {}", e.getMessage(), e);
        }
        // TODO to remove once runtime exception will be thrown
        return null;
    }

    /**
     * Return a specific device message Class
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Class<DeviceMessage<?, ?>> getDeviceClazz(String clazz)
    {
        try {
            return (Class<DeviceMessage<?, ?>>) Class.forName(clazz);
        }
        catch (ClassNotFoundException e) {
            // TODO throw runtime exception!
            logger.error("Cannot instantiate class {}", e.getMessage(), e);
        }
        // TODO to remove once runtime exception will be thrown
        return null;
    }

}
