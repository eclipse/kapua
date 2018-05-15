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
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

/**
 * Utility {@code abstract} {@link Class} used to provide utility methods to all implementation of {@link org.eclipse.kapua.service.device.management.DeviceManagementService}s.
 *
 * @since 1.0.0
 */
public abstract class AbstractDeviceManagementServiceImpl {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceEventService DEVICE_EVENT_SERVICE = LOCATOR.getService(DeviceEventService.class);
    private static final DeviceEventFactory DEVICE_EVENT_FACTORY = LOCATOR.getFactory(DeviceEventFactory.class);

    /**
     * Creates a {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent} extracting data from the given {@link KapuaRequestMessage} and {@link KapuaResponseMessage}.
     * <p>
     * This operation is performed using {@link KapuaSecurityUtils#doPrivileged(ThrowingRunnable)} since {@link org.eclipse.kapua.service.device.registry.event.DeviceEventDomain} isn't a required
     * permission to use any of the {@link org.eclipse.kapua.service.device.management.DeviceManagementService}s.
     *
     * @param scopeId         The scopeId in which to create the {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent}
     * @param deviceId        The {@link org.eclipse.kapua.service.device.registry.Device} id for which the {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent} is created
     * @param requestMessage  The {@link KapuaRequestMessage} of the {@link org.eclipse.kapua.service.device.management.DeviceManagementService} operation from which to extract data.
     * @param responseMessage The {@link KapuaResponseMessage} of the {@link org.eclipse.kapua.service.device.management.DeviceManagementService} operation from which to extract data.
     * @throws KapuaException If the creation of the {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent} fails for some reasons
     * @since 1.0.0
     */
    protected void createDeviceEvent(KapuaId scopeId, KapuaId deviceId, KapuaRequestMessage<?, ?> requestMessage,
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

        KapuaSecurityUtils.doPrivileged(() -> DEVICE_EVENT_SERVICE.create(deviceEventCreator));
    }
}
