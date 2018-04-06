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
 *******************************************************************************/
package org.eclipse.kapua.connector.hono;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SystemMessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(SystemMessageUtil.class);

    private static AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
    private static DeviceConnectionService deviceConnectionService = KapuaLocator.getInstance().getService(DeviceConnectionService.class);
    private static DeviceConnectionFactory deviceConnectionFactory = KapuaLocator.getInstance().getFactory(DeviceConnectionFactory.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SystemMessageUtil() {
    }

    public static SystemMessageBean getSystemContent(MessageContext<byte[]> message) throws KapuaException {
        String content = new String(message.getMessage());
        try {
            return MAPPER.readValue(content, SystemMessageBean.class);
        } catch (IOException e) {
            logger.error("Cannot read system message content '{}'", content, e);
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, "Cannot read system message content");
        }
    }

    /**
     * Set the device connection id in the message context properties (if found, no update otherwise)
     * @param message
     * @throws KapuaException
     */
    public static void updateDeviceConnectionId(MessageContext<?> message) throws KapuaException {
        Map<String, Object> parameters = message.getProperties();
        KapuaId scopeId = getScopeId((String)parameters.get(Properties.MESSAGE_SCOPE_NAME));
        DeviceConnection deviceConnection = getDeviceConnection(scopeId, (String)parameters.get(Properties.MESSAGE_CLIENT_ID));
        if (deviceConnection != null) {
            parameters.put(Properties.MESSAGE_CONNECTION_ID, Base64.getEncoder().encodeToString(SerializationUtils.serialize(deviceConnection.getId())));
        }
    }

    /**
     * Throw exception if device connection id is not found in the message properties
     * @param message
     * @throws KapuaException
     */
    public static void checkDeviceConnectionId(MessageContext<?> message) throws KapuaException {
        if (message.getProperties().get(Properties.MESSAGE_CONNECTION_ID) == null) {
            throw new KapuaException(KapuaErrorCodes.ENTITY_NOT_FOUND, "deviceConnectionId");
        }
    }

    /**
     * Create or update the device connection entity
     * @param message
     * @throws KapuaException
     */
    public static void createOrUpdateDeviceConnection(MessageContext<?> message) throws KapuaException {
        Map<String, Object> parameters = message.getProperties();
        String clientId = (String)parameters.get(Properties.MESSAGE_CLIENT_ID);
        KapuaId scopeId = getScopeId((String)parameters.get(Properties.MESSAGE_SCOPE_NAME));
        DeviceConnection deviceConnection = getDeviceConnection(scopeId, clientId);
        if (deviceConnection == null) {
            addConnection(scopeId, clientId);
        }
        else {
            updateDeviceConnection(scopeId, deviceConnection.getId(), DeviceConnectionStatus.CONNECTED);
        }
    }

    /**
     * Update the device connection status to disconnected
     * @param message
     * @throws KapuaException
     */
    public static void disconnectDeviceConnection(MessageContext<?> message) throws KapuaException {
        Map<String, Object> parameters = message.getProperties();
        String clientId = (String)parameters.get(Properties.MESSAGE_CLIENT_ID);
        KapuaId scopeId = getScopeId((String)parameters.get(Properties.MESSAGE_SCOPE_NAME));
        DeviceConnection deviceConnection = getDeviceConnection(scopeId, clientId);
        if (deviceConnection == null) {
            logger.error("Cannot find connection for client id {}", clientId);
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, "Cannot find connection for client id");
        }
        else {
            updateDeviceConnection(scopeId, deviceConnection.getId(), DeviceConnectionStatus.DISCONNECTED);
        }
    }

    private static void updateDeviceConnection(KapuaId scopeId, KapuaId connectionId, DeviceConnectionStatus status) throws KapuaException {
        DeviceConnection deviceConnection = getDeviceConnection(scopeId, connectionId);
        deviceConnection.setStatus(status);
        KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.update(deviceConnection));
    }

    private static DeviceConnection addConnection(KapuaId scopeId, String deviceId) throws KapuaException {
        DeviceConnectionCreator deviceConnectionCreator = deviceConnectionFactory.newCreator(scopeId);
        deviceConnectionCreator.setStatus(DeviceConnectionStatus.CONNECTED);
        deviceConnectionCreator.setClientId(deviceId);
        deviceConnectionCreator.setUserId(KapuaEid.ONE);
        deviceConnectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.INHERITED);
        deviceConnectionCreator.setAllowUserChange(false);
        //TODO to be filled with proper values
        deviceConnectionCreator.setClientIp("127.0.0.1");
        deviceConnectionCreator.setProtocol("MQTT");
        deviceConnectionCreator.setServerIp("127.0.0.1");
        return KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.create(deviceConnectionCreator));
    }

    private static DeviceConnection getDeviceConnection(KapuaId scopeId, KapuaId connectionId) throws KapuaException {
        return KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.find(scopeId, connectionId));
    }

    private static DeviceConnection getDeviceConnection(KapuaId scopeId, String deviceId) throws KapuaException {
        return KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(scopeId, deviceId));
    }

    private static KapuaId getScopeId(String tenantId) throws KapuaException {
        Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(tenantId));
        if (account == null) {
            throw new KapuaException(KapuaErrorCodes.ENTITY_NOT_FOUND, "scopeId");
        }
        return account.getId();
    }
}
