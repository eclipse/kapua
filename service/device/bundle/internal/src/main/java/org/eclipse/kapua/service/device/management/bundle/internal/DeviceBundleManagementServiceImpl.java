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
package org.eclipse.kapua.service.device.management.bundle.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.bundle.internal.exception.BundleGetManagementException;
import org.eclipse.kapua.service.device.management.bundle.internal.exception.BundleStartManagementException;
import org.eclipse.kapua.service.device.management.bundle.internal.exception.BundleStopManagementException;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestPayload;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponseMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponsePayload;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.response.KapuaResponsePayload;

import java.util.Date;

/**
 * DeviceBundleManagementService implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceBundleManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceBundleManagementService {

    private static final Domain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();

    @Override
    public DeviceBundles get(KapuaId scopeId, KapuaId deviceId, Long timeout)
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
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        BundleRequestChannel bundleRequestChannel = new BundleRequestChannel();
        bundleRequestChannel.setAppName(DeviceBundleAppProperties.APP_NAME);
        bundleRequestChannel.setVersion(DeviceBundleAppProperties.APP_VERSION);
        bundleRequestChannel.setMethod(KapuaMethod.READ);

        BundleRequestPayload bundleRequestPayload = new BundleRequestPayload();

        BundleRequestMessage bundleRequestMessage = new BundleRequestMessage();
        bundleRequestMessage.setScopeId(scopeId);
        bundleRequestMessage.setDeviceId(deviceId);
        bundleRequestMessage.setCapturedOn(new Date());
        bundleRequestMessage.setPayload(bundleRequestPayload);
        bundleRequestMessage.setChannel(bundleRequestChannel);

        //
        // Do get
        DeviceCallExecutor<BundleRequestChannel, BundleRequestPayload, BundleRequestMessage, BundleResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(bundleRequestMessage, timeout);
        BundleResponseMessage responseMessage = (BundleResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, bundleRequestMessage, responseMessage);

        //
        // Check response
        if (responseMessage.getResponseCode().isAccepted()) {
            BundleResponsePayload responsePayload = responseMessage.getPayload();

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            String body = null;
            try {
                body = new String(responsePayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, responsePayload.getBody());
            }

            DeviceBundles deviceBundleList = null;
            try {
                deviceBundleList = XmlUtil.unmarshal(body, DeviceBundlesImpl.class);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION,
                        e,
                        body);
            }

            return deviceBundleList;
        } else {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new BundleGetManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    @Override
    public void start(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notEmptyOrNull(bundleId, "bundleId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.execute, scopeId));

        //
        // Prepare the request
        BundleRequestChannel bundleRequestChannel = new BundleRequestChannel();
        bundleRequestChannel.setAppName(DeviceBundleAppProperties.APP_NAME);
        bundleRequestChannel.setVersion(DeviceBundleAppProperties.APP_VERSION);
        bundleRequestChannel.setMethod(KapuaMethod.EXECUTE);
        bundleRequestChannel.setStart(true);
        bundleRequestChannel.setBundleId(bundleId);

        BundleRequestPayload bundleRequestPayload = new BundleRequestPayload();

        BundleRequestMessage bundleRequestMessage = new BundleRequestMessage();
        bundleRequestMessage.setScopeId(scopeId);
        bundleRequestMessage.setDeviceId(deviceId);
        bundleRequestMessage.setCapturedOn(new Date());
        bundleRequestMessage.setPayload(bundleRequestPayload);
        bundleRequestMessage.setChannel(bundleRequestChannel);

        //
        // Do start
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(bundleRequestMessage, timeout);
        BundleResponseMessage responseMessage = (BundleResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, bundleRequestMessage, responseMessage);

        //
        // Check response
        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new BundleStartManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

    @Override
    public void stop(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notEmptyOrNull(bundleId, "bundleId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.execute, scopeId));

        //
        // Prepare the request
        BundleRequestChannel bundleRequestChannel = new BundleRequestChannel();
        bundleRequestChannel.setAppName(DeviceBundleAppProperties.APP_NAME);
        bundleRequestChannel.setVersion(DeviceBundleAppProperties.APP_VERSION);
        bundleRequestChannel.setMethod(KapuaMethod.EXECUTE);
        bundleRequestChannel.setStart(false);
        bundleRequestChannel.setBundleId(bundleId);

        BundleRequestPayload bundleRequestPayload = new BundleRequestPayload();

        BundleRequestMessage bundleRequestMessage = new BundleRequestMessage();
        bundleRequestMessage.setScopeId(scopeId);
        bundleRequestMessage.setDeviceId(deviceId);
        bundleRequestMessage.setCapturedOn(new Date());
        bundleRequestMessage.setPayload(bundleRequestPayload);
        bundleRequestMessage.setChannel(bundleRequestChannel);

        //
        // Do stop
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(bundleRequestMessage, timeout);
        BundleResponseMessage responseMessage = (BundleResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, bundleRequestMessage, responseMessage);

        //
        // Check response
        if (!responseMessage.getResponseCode().isAccepted()) {
            KapuaResponsePayload responsePayload = responseMessage.getPayload();

            throw new BundleStopManagementException(responseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionStack());
        }
    }

}
