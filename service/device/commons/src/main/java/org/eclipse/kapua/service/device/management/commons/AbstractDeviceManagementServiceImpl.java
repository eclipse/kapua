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
package org.eclipse.kapua.service.device.management.commons;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

public abstract class AbstractDeviceManagementServiceImpl {

    protected static final Domain DEVICE_MANAGEMENT_DOMAIN = new DeviceManagementDomain();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceEventService DEVICE_EVENT_SERVICE = LOCATOR.getService(DeviceEventService.class);
    private static final DeviceEventFactory DEVICE_EVENT_FACTORY = LOCATOR.getFactory(DeviceEventFactory.class);

    protected <C extends KapuaRequestChannel, P extends KapuaRequestPayload> void createDeviceEvent(KapuaId scopeId, KapuaId deviceId, KapuaRequestMessage<C, P> requestMessage,
            KapuaResponseMessage responseMessage) throws KapuaException {

        DeviceEventCreator deviceEventCreator =
                DEVICE_EVENT_FACTORY.newCreator(
                        scopeId,
                        deviceId,
                        responseMessage.getReceivedOn(),
                        requestMessage.getChannel().getAppName().getValue());

        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(requestMessage.getChannel().getMethod());
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        DEVICE_EVENT_SERVICE.create(deviceEventCreator);
    }
}
