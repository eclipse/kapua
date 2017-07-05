/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.util;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.commons.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.commons.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

import static org.eclipse.kapua.app.console.commons.shared.util.KapuaGwtModelConverter.convertEntity;

public class KapuaGwtDeviceModelConverter {

    private KapuaGwtDeviceModelConverter() { }

    public static GwtDevice convertDevice(Device device)
            throws KapuaException {

        GwtDevice gwtDevice = new GwtDevice();
        gwtDevice.setId(KapuaGwtModelConverter.convertKapuaId(device.getId()));
        gwtDevice.setScopeId(KapuaGwtModelConverter.convertKapuaId(device.getScopeId()));
        gwtDevice.setGwtDeviceStatus(device.getStatus().toString());
        gwtDevice.setClientId(device.getClientId());
        gwtDevice.setDisplayName(device.getDisplayName());
        gwtDevice.setModelId(device.getModelId());
        gwtDevice.setSerialNumber(device.getSerialNumber());
        gwtDevice.setGroupId(KapuaGwtModelConverter.convertKapuaId(device.getGroupId()));
        gwtDevice.setFirmwareVersion(device.getFirmwareVersion());
        gwtDevice.setBiosVersion(device.getBiosVersion());
        gwtDevice.setOsVersion(device.getOsVersion());
        gwtDevice.setJvmVersion(device.getJvmVersion());
        gwtDevice.setOsgiVersion(device.getOsgiFrameworkVersion());
        gwtDevice.setAcceptEncoding(device.getAcceptEncoding());
        gwtDevice.setApplicationIdentifiers(device.getApplicationIdentifiers());
        gwtDevice.setIotFrameworkVersion(device.getApplicationFrameworkVersion());
        gwtDevice.setIccid(device.getIccid());
        gwtDevice.setImei(device.getImei());
        gwtDevice.setImsi(device.getImsi());
        gwtDevice.setCustomAttribute1(device.getCustomAttribute1());
        gwtDevice.setCustomAttribute2(device.getCustomAttribute2());
        gwtDevice.setCustomAttribute3(device.getCustomAttribute3());
        gwtDevice.setCustomAttribute4(device.getCustomAttribute4());
        gwtDevice.setCustomAttribute5(device.getCustomAttribute5());
        gwtDevice.setOptlock(device.getOptlock());

        // Last device event
        if (device.getLastEvent() != null) {
            DeviceEvent lastEvent = device.getLastEvent();

            gwtDevice.setLastEventType(lastEvent.getType());
            gwtDevice.setLastEventOn(lastEvent.getReceivedOn());

        }

        // Device connection
        gwtDevice.setConnectionIp(device.getConnectionIp());
        gwtDevice.setConnectionInterface(device.getConnectionInterface());
        if (device.getConnection() != null) {
            DeviceConnection connection = device.getConnection();
            gwtDevice.setClientIp(connection.getClientIp());
            gwtDevice.setGwtDeviceConnectionStatus(connection.getStatus().toString());
            gwtDevice.setDeviceUserId(connection.getUserId().toCompactId());
        }
        return gwtDevice;
    }

    public static GwtDeviceEvent convertDeviceEvent(DeviceEvent deviceEvent) {
        GwtDeviceEvent gwtDeviceEvent = new GwtDeviceEvent();
        gwtDeviceEvent.setDeviceId(deviceEvent.getDeviceId().toCompactId());
        gwtDeviceEvent.setSentOn(deviceEvent.getSentOn());
        gwtDeviceEvent.setReceivedOn(deviceEvent.getReceivedOn());
        gwtDeviceEvent.setEventType(deviceEvent.getResource());
        gwtDeviceEvent.setGwtActionType(deviceEvent.getAction().toString());
        gwtDeviceEvent.setGwtResponseCode(deviceEvent.getResponseCode().toString());
        String escapedMessage = KapuaSafeHtmlUtils.htmlEscape(deviceEvent.getEventMessage());
        gwtDeviceEvent.setEventMessage(escapedMessage);

        return gwtDeviceEvent;
    }

    public static GwtDeviceConnection convert(DeviceConnection deviceConnection) {
        GwtDeviceConnection gwtDeviceConnection = new GwtDeviceConnection();

        //
        // Convert commons attributes
        convertEntity(deviceConnection, gwtDeviceConnection);

        //
        // Convert other attributes
        gwtDeviceConnection.setClientId(deviceConnection.getClientId());
        gwtDeviceConnection.setUserId(convert(deviceConnection.getUserId()));
        gwtDeviceConnection.setClientIp(deviceConnection.getClientIp());
        gwtDeviceConnection.setServerIp(deviceConnection.getServerIp());
        gwtDeviceConnection.setProtocol(deviceConnection.getProtocol());
        gwtDeviceConnection.setConnectionStatus(convert(deviceConnection.getStatus()));
        gwtDeviceConnection.setOptlock(deviceConnection.getOptlock());

        //
        // Return converted entity
        return gwtDeviceConnection;
    }

    private static String convert(DeviceConnectionStatus status) {
        return status.toString();
    }
}
