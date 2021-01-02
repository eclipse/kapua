/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
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
import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseException;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
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

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";
    private static final String DEVICE_ASSETS = "deviceAssets";

    @Override
    public DeviceAssets get(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceAssets, DEVICE_ASSETS);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

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
            throw new DeviceManagementRequestException(e, deviceAssets);
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
                throw new DeviceManagementResponseException(e, responsePayload);
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
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceAssets, DEVICE_ASSETS);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

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
            throw new DeviceManagementRequestException(e, deviceAssets);
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
                throw new DeviceManagementResponseException(e, responsePayload);
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
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceAssets, DEVICE_ASSETS);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

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
            throw new DeviceManagementRequestException(e, deviceAssets);
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
                throw new DeviceManagementResponseException(e, responsePayload);
            }

            return deviceAssetsResponse;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new AssetWriteManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }
}
