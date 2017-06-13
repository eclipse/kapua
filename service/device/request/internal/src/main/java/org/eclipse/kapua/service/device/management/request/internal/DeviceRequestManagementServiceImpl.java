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
package org.eclipse.kapua.service.device.management.request.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.request.DeviceRequestManagementService;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import java.util.Date;

@KapuaProvider
public class DeviceRequestManagementServiceImpl implements DeviceRequestManagementService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final Domain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();
    private static final GenericRequestFactory FACTORY = LOCATOR.getFactory(GenericRequestFactory.class);

    @Override public KapuaResponseMessage exec(
            GenericRequestMessage requestInput,
            Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(requestInput, "requestInput");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_DOMAIN, Actions.execute, requestInput.getScopeId()));

        //
        // Prepare the request
        GenericRequestChannel genericRequestChannel = FACTORY.newRequestChannel();
        genericRequestChannel.setAppName(requestInput.getChannel().getAppName());
        genericRequestChannel.setVersion(requestInput.getChannel().getVersion());
        genericRequestChannel.setMethod(requestInput.getChannel().getMethod());
        genericRequestChannel.setResources(requestInput.getChannel().getResources());

        GenericRequestPayload genericRequestPayload = FACTORY.newRequestPayload();
        genericRequestPayload.setMetrics(requestInput.getPayload().getMetrics());
        genericRequestPayload.setBody(requestInput.getPayload().getBody());

        GenericRequestMessage genericRequestMessage = FACTORY.newRequestMessage();
        genericRequestMessage.setScopeId(requestInput.getScopeId());
        genericRequestMessage.setDeviceId(requestInput.getDeviceId());
        genericRequestMessage.setCapturedOn(new Date());
        genericRequestMessage.setChannel(genericRequestChannel);
        genericRequestMessage.setPayload(genericRequestPayload);
        genericRequestMessage.setPosition(requestInput.getPosition());

        //
        // Do exec
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(genericRequestMessage, timeout);
        KapuaResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(requestInput.getScopeId(), requestInput.getDeviceId(), responseMessage.getReceivedOn(), requestInput.getChannel().getAppName().getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.EXECUTE);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

        return responseMessage;
    }
}
