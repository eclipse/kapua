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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.bundle.internal;

import java.util.Date;

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
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestPayload;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponseMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponsePayload;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

/**
 * Device bundle service implementation.
 *
 * @since 1.0
 *
 */
@KapuaProvider
public class DeviceBundleManagementServiceImpl implements DeviceBundleManagementService {

    private static final Domain deviceManagementDomain = new DeviceManagementDomain();

    @SuppressWarnings({ "rawtypes", "unchecked" })
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
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.read, scopeId));

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
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(bundleRequestMessage, timeout);
        BundleResponseMessage responseMessage = (BundleResponseMessage) deviceApplicationCall.send();

        //
        // Parse the response
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

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceBundleAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.READ);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

        return deviceBundleList;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
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
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.execute, scopeId));

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
        // Do get
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(bundleRequestMessage, timeout);
        BundleResponseMessage responseMessage = (BundleResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceBundleAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.EXECUTE);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void stop(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notEmptyOrNull(bundleId, "bundleID");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.execute, scopeId));

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
        // Do get
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(bundleRequestMessage, timeout);
        BundleResponseMessage responseMessage = (BundleResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceBundleAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.EXECUTE);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);
    }

}
