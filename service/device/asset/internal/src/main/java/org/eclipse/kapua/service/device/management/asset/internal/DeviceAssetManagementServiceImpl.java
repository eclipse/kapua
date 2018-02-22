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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.internal.exception.AssetGetManagementException;
import org.eclipse.kapua.service.device.management.asset.internal.exception.AssetReadManagementException;
import org.eclipse.kapua.service.device.management.asset.internal.exception.AssetWriteManagementException;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestChannel;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestPayload;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponsePayload;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.management.response.KapuaResponsePayload;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * {@link DeviceAssetManagementService} implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceAssetManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceAssetManagementService {

    @Override
    public DeviceAssets get(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(deviceAssets, "deviceAssets");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        AssetRequestChannel assetRequestChannel = new AssetRequestChannel();
        assetRequestChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        assetRequestChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);
        assetRequestChannel.setMethod(KapuaMethod.READ);

        AssetRequestPayload assetRequestPayload = new AssetRequestPayload();
        try {
            assetRequestPayload.setDeviceAssets(deviceAssets);
        } catch (JAXBException | UnsupportedEncodingException e) {
            throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_EXCEPTION, e, deviceAssets);
        }

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
        // Create event
        createDeviceEvent(scopeId, deviceId, assetRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            DeviceAssets deviceAssetsResponse;
            AssetResponsePayload responsePayload = responseMessage.getPayload();
            try {
                deviceAssetsResponse = responsePayload.getDeviceAssets();
            } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | UnsupportedEncodingException e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, responsePayload);
            }

            return deviceAssetsResponse;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new AssetGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    @Override
    public DeviceAssets read(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(deviceAssets, "deviceAssets");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        AssetRequestChannel assetRequestChannel = new AssetRequestChannel();
        assetRequestChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        assetRequestChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);
        assetRequestChannel.setMethod(KapuaMethod.EXECUTE);
        assetRequestChannel.setRead(true);

        AssetRequestPayload assetRequestPayload = new AssetRequestPayload();
        try {
            assetRequestPayload.setDeviceAssets(deviceAssets);
        } catch (JAXBException | UnsupportedEncodingException e) {
            throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_EXCEPTION, e, deviceAssets);
        }

        AssetRequestMessage assetRequestMessage = new AssetRequestMessage();
        assetRequestMessage.setScopeId(scopeId);
        assetRequestMessage.setDeviceId(deviceId);
        assetRequestMessage.setCapturedOn(new Date());
        assetRequestMessage.setPayload(assetRequestPayload);
        assetRequestMessage.setChannel(assetRequestChannel);

        //
        // Do read
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(assetRequestMessage, timeout);
        AssetResponseMessage responseMessage = (AssetResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, assetRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            DeviceAssets deviceAssetsResponse;
            AssetResponsePayload responsePayload = responseMessage.getPayload();
            try {
                deviceAssetsResponse = responsePayload.getDeviceAssets();
            } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | UnsupportedEncodingException e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, responsePayload);
            }

            return deviceAssetsResponse;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new AssetReadManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    @Override
    public DeviceAssets write(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(deviceAssets, "deviceAssets");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        AssetRequestChannel assetRequestChannel = new AssetRequestChannel();
        assetRequestChannel.setAppName(DeviceAssetAppProperties.APP_NAME);
        assetRequestChannel.setVersion(DeviceAssetAppProperties.APP_VERSION);
        assetRequestChannel.setMethod(KapuaMethod.EXECUTE);
        assetRequestChannel.setWrite(true);

        AssetRequestPayload assetRequestPayload = new AssetRequestPayload();
        try {
            assetRequestPayload.setDeviceAssets(deviceAssets);
        } catch (JAXBException | UnsupportedEncodingException e) {
            throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_EXCEPTION, e, deviceAssets);
        }

        AssetRequestMessage assetRequestMessage = new AssetRequestMessage();
        assetRequestMessage.setScopeId(scopeId);
        assetRequestMessage.setDeviceId(deviceId);
        assetRequestMessage.setCapturedOn(new Date());
        assetRequestMessage.setPayload(assetRequestPayload);
        assetRequestMessage.setChannel(assetRequestChannel);

        //
        // Do write
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(assetRequestMessage, timeout);
        AssetResponseMessage responseMessage = (AssetResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, assetRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            DeviceAssets deviceAssetsResponse;
            AssetResponsePayload responsePayload = responseMessage.getPayload();
            try {
                deviceAssetsResponse = responsePayload.getDeviceAssets();
            } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | UnsupportedEncodingException e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, responsePayload);
            }

            return deviceAssetsResponse;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new AssetWriteManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }
}
