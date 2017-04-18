/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.xml.MetricTypeConverter;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestPayload;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponsePayload;
import org.eclipse.kapua.service.device.management.channel.ChannelMode;
import org.eclipse.kapua.service.device.management.channel.DeviceChannel;
import org.eclipse.kapua.service.device.management.channel.DeviceChannelFactory;
import org.eclipse.kapua.service.device.management.channel.DeviceChannels;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelRequestChannel;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelRequestMessage;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelRequestPayload;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelResponseMessage;
import org.eclipse.kapua.service.device.management.channel.message.internal.ChannelResponsePayload;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

/**
 * Device asset service implementation.
 *
 * @since 1.0
 *
 */
@KapuaProvider
public class DeviceAssetManagementServiceImpl implements DeviceAssetManagementService {

    private static final Domain deviceManagementDomain = new DeviceManagementDomain();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public DeviceAssets get(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.read, scopeId));

        //
        // Prepare the request
        AssetRequestChannel assetRequestChannel = new AssetRequestChannel();
        assetRequestChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        assetRequestChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);
        assetRequestChannel.setMethod(KapuaMethod.READ);

        AssetRequestPayload assetRequestPayload = new AssetRequestPayload();

        AssetRequestMessage assetRequestMessage = new AssetRequestMessage();
        assetRequestMessage.setScopeId(scopeId);
        assetRequestMessage.setDeviceId(deviceId);
        assetRequestMessage.setCapturedOn(new Date());
        assetRequestMessage.setPayload(assetRequestPayload);
        assetRequestMessage.setChannel(assetRequestChannel);

        //
        // Do get
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(assetRequestMessage, timeout);
        AssetResponseMessage responseMessage = (AssetResponseMessage) deviceApplicationCall.send();

        //
        // Parse the response
        AssetResponsePayload responsePayload = responseMessage.getPayload();

        DeviceAssetFactory deviceAssetFactory = KapuaLocator.getInstance().getFactory(DeviceAssetFactory.class);
        DeviceAssets deviceAssetList = deviceAssetFactory.newAssetListResult();
        for (Object entry : responsePayload.getProperties().values()){
            if (entry != null) {
                DeviceAsset deviceAsset = deviceAssetFactory.newDeviceAsset();
                deviceAsset.setName((String) entry);
                deviceAssetList.getAssets().add(deviceAsset);
            }
        }
        
        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceAssetAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.READ);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

        return deviceAssetList;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public DeviceChannels getChannels(KapuaId scopeId, KapuaId deviceId, String assetName, List<String> channelNames, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.read, scopeId));

        //
        // Prepare the request
        ChannelRequestChannel channelRequestChannel = new ChannelRequestChannel();
        channelRequestChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        channelRequestChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);
        channelRequestChannel.setMethod(KapuaMethod.READ);
        channelRequestChannel.setAssetName(assetName);

        ChannelRequestPayload assetRequestPayload = new ChannelRequestPayload();

        ChannelRequestMessage channelRequestMessage = new ChannelRequestMessage();
        channelRequestMessage.setScopeId(scopeId);
        channelRequestMessage.setDeviceId(deviceId);
        channelRequestMessage.setCapturedOn(new Date());
        channelRequestMessage.setPayload(assetRequestPayload);
        channelRequestMessage.setChannel(channelRequestChannel);

        //
        // Do get
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(channelRequestMessage, timeout);
        ChannelResponseMessage responseMessage = (ChannelResponseMessage) deviceApplicationCall.send();

        //
        // Parse the response
        ChannelResponsePayload responsePayload = responseMessage.getPayload();

        Map<String, DeviceChannel<?>> deviceChannelsMap = new HashMap<>();
        DeviceChannelFactory deviceChannelFactory = KapuaLocator.getInstance().getFactory(DeviceChannelFactory.class);
        
        try {
            for (Entry<String, Object> entry : responsePayload.getProperties().entrySet()){
                if (entry.getValue() != null) {

                    String[] tokens = entry.getKey().split("_");
                    String channelName = tokens[0];

                    DeviceChannel deviceChannel = deviceChannelsMap.get(channelName); 
                    if(deviceChannel == null) {                    
                        deviceChannel = deviceChannelFactory.newDeviceChannel();
                    }


                    deviceChannel.setName(channelName);
                    if ("type".equals(tokens[1])) {
                            deviceChannel.setType(MetricTypeConverter.fromString((String) entry.getValue()));
                    }
                    else if ("mode".equals(tokens[1])) {
                        deviceChannel.setMode(ChannelMode.valueOf((String) entry.getValue()));
                    }

                    deviceChannelsMap.put(channelName, deviceChannel);
                }
            }
        }
        catch (Exception e) {
            throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, responsePayload);
        }
        
        DeviceChannels deviceChannelList = deviceChannelFactory.newChannelListResult();
        deviceChannelList.getChannels().addAll(deviceChannelsMap.values());

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceAssetAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.READ);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

        return deviceChannelList;
    }
}
