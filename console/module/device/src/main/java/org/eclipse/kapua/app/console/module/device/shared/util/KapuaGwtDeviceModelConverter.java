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
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionOption;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter.convertEntity;

public class KapuaGwtDeviceModelConverter {

    private KapuaGwtDeviceModelConverter() { }

    public static GwtDevice convertDevice(Device device)
            throws KapuaException {

        GwtDevice gwtDevice = new GwtDevice();
        gwtDevice.setId(KapuaGwtCommonsModelConverter.convertKapuaId(device.getId()));
        gwtDevice.setScopeId(KapuaGwtCommonsModelConverter.convertKapuaId(device.getScopeId()));
        gwtDevice.setGwtDeviceStatus(device.getStatus().toString());
        gwtDevice.setClientId(device.getClientId());
        gwtDevice.setDisplayName(device.getDisplayName());
        gwtDevice.setModelId(device.getModelId());
        gwtDevice.setSerialNumber(device.getSerialNumber());
        gwtDevice.setGroupId(KapuaGwtCommonsModelConverter.convertKapuaId(device.getGroupId()));
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

    public static GwtDeviceConnection convertDeviceConnection(DeviceConnection deviceConnection) {
        GwtDeviceConnection gwtDeviceConnection = new GwtDeviceConnection();

        //
        // Convert commons attributes
        convertEntity(deviceConnection, gwtDeviceConnection);

        //
        // Convert other attributes
        gwtDeviceConnection.setClientId(deviceConnection.getClientId());
        gwtDeviceConnection.setUserId(KapuaGwtCommonsModelConverter.convertKapuaId(deviceConnection.getUserId()));
        gwtDeviceConnection.setClientIp(deviceConnection.getClientIp());
        gwtDeviceConnection.setServerIp(deviceConnection.getServerIp());
        gwtDeviceConnection.setProtocol(deviceConnection.getProtocol());
        gwtDeviceConnection.setConnectionStatus(convertDeviceConnectionStatus(deviceConnection.getStatus()));
        gwtDeviceConnection.setOptlock(deviceConnection.getOptlock());

        // convertDeviceAssetChannel user coupling attributes
        gwtDeviceConnection.setReservedUserId(KapuaGwtCommonsModelConverter.convertKapuaId(deviceConnection.getReservedUserId()));
        if (deviceConnection.getUserCouplingMode() != null) {
            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.valueOf(deviceConnection.getUserCouplingMode().name());
            gwtDeviceConnection.setConnectionUserCouplingMode(gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : "");
        }
        gwtDeviceConnection.setAllowUserChange(deviceConnection.getAllowUserChange());

        //
        // Return converted entity
        return gwtDeviceConnection;
    }

    private static String convertDeviceConnectionStatus(DeviceConnectionStatus status) {
        return status.toString();
    }

    public static GwtDeviceAssets convertDeviceAssets(DeviceAssets assets) {
        GwtDeviceAssets gwtAssets = new GwtDeviceAssets();
        List<GwtDeviceAsset> gwtAssetsList = new ArrayList<GwtDeviceAsset>();
        for (DeviceAsset asset : assets.getAssets()) {
            gwtAssetsList.add(convertDeviceAsset(asset));
        }
        gwtAssets.setAssets(gwtAssetsList);
        return gwtAssets;
    }

    public static GwtDeviceAsset convertDeviceAsset(DeviceAsset asset) {
        GwtDeviceAsset gwtAsset = new GwtDeviceAsset();
        List<GwtDeviceAssetChannel> gwtChannelsList = new ArrayList<GwtDeviceAssetChannel>();
        gwtAsset.setName(asset.getName());
        for (DeviceAssetChannel channel : asset.getChannels()) {
            gwtChannelsList.add(convertDeviceAssetChannel(channel));
        }
        gwtAsset.setChannels(gwtChannelsList);
        return gwtAsset;
    }

    public static GwtDeviceAssetChannel convertDeviceAssetChannel(DeviceAssetChannel channel) {
        GwtDeviceAssetChannel gwtChannel = new GwtDeviceAssetChannel();
        gwtChannel.setName(channel.getName());
        gwtChannel.setError(channel.getError());
        gwtChannel.setTimestamp(channel.getTimestamp());
        gwtChannel.setMode(channel.getMode().toString());
        gwtChannel.setType(ObjectTypeConverter.toString(channel.getType()));
        gwtChannel.setValue(ObjectValueConverter.toString(channel.getValue()));
        return gwtChannel;
    }

    public static GwtDeviceConnectionOption convertDeviceConnectionOption(DeviceConnectionOption deviceConnectionOption) {
        GwtDeviceConnectionOption gwtDeviceConnectionOption = new GwtDeviceConnectionOption();

        //
        // Convert commons attributes
        convertEntity(deviceConnectionOption, gwtDeviceConnectionOption);

        // convertDeviceAssetChannel user coupling attributes
        gwtDeviceConnectionOption.setReservedUserId(KapuaGwtCommonsModelConverter.convertKapuaId(deviceConnectionOption.getReservedUserId()));
        if (deviceConnectionOption.getUserCouplingMode() != null) {
            GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.getEnumFromLabel(deviceConnectionOption.getUserCouplingMode().name());
            gwtDeviceConnectionOption.setConnectionUserCouplingMode(gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : "");
        }
        gwtDeviceConnectionOption.setAllowUserChange(deviceConnectionOption.getAllowUserChange());

        //
        // Return converted entity
        return gwtDeviceConnectionOption;
    }

}
