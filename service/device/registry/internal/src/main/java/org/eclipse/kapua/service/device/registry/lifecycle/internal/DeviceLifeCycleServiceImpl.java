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
package org.eclipse.kapua.service.device.registry.lifecycle.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;

/**
 * {@link DeviceLifeCycleService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceLifeCycleServiceImpl implements DeviceLifeCycleService {

    @Override
    public void birth(KapuaId connectionId, KapuaBirthMessage message)
            throws KapuaException {
        KapuaBirthPayload payload = message.getPayload();
        KapuaBirthChannel channel = message.getChannel();
        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        //
        // Device update
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device;
        if (deviceId == null) {
            String clientId = channel.getClientId();

            DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);
            DeviceCreator deviceCreator = deviceFactory.newCreator(scopeId, clientId);

            deviceCreator.setDisplayName(payload.getDisplayName());
            deviceCreator.setSerialNumber(payload.getSerialNumber());
            deviceCreator.setModelId(payload.getModelId());
            deviceCreator.setImei(payload.getModemImei());
            deviceCreator.setImsi(payload.getModemImsi());
            deviceCreator.setIccid(payload.getModemIccid());
            deviceCreator.setBiosVersion(payload.getBiosVersion());
            deviceCreator.setFirmwareVersion(payload.getFirmwareVersion());
            deviceCreator.setOsVersion(payload.getOsVersion());
            deviceCreator.setJvmVersion(payload.getJvmVersion());
            deviceCreator.setOsgiFrameworkVersion(payload.getContainerFrameworkVersion());
            deviceCreator.setApplicationFrameworkVersion(payload.getApplicationFrameworkVersion());
            deviceCreator.setConnectionInterface(payload.getConnectionInterface());
            deviceCreator.setConnectionIp(payload.getConnectionIp());
            deviceCreator.setApplicationIdentifiers(payload.getApplicationIdentifiers());
            deviceCreator.setAcceptEncoding(payload.getAcceptEncoding());

            // issue #57
            deviceCreator.setConnectionId(connectionId);

            device = deviceRegistryService.create(deviceCreator);
        } else {
            device = deviceRegistryService.find(scopeId, deviceId);
            device.setDisplayName(payload.getDisplayName());
            device.setSerialNumber(payload.getSerialNumber());
            device.setModelId(payload.getModelId());
            device.setImei(payload.getModemImei());
            device.setImsi(payload.getModemImsi());
            device.setIccid(payload.getModemIccid());
            device.setBiosVersion(payload.getBiosVersion());
            device.setFirmwareVersion(payload.getFirmwareVersion());
            device.setOsVersion(payload.getOsVersion());
            device.setJvmVersion(payload.getJvmVersion());
            device.setOsgiFrameworkVersion(payload.getContainerFrameworkVersion());
            device.setApplicationFrameworkVersion(payload.getApplicationFrameworkVersion());
            device.setConnectionInterface(payload.getConnectionInterface());
            device.setConnectionIp(payload.getConnectionIp());
            device.setApplicationIdentifiers(payload.getApplicationIdentifiers());
            device.setAcceptEncoding(payload.getAcceptEncoding());

            // issue #57
            device.setConnectionId(connectionId);

            deviceRegistryService.update(device);
        }

        //
        // Event create
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, device.getId(), message.getReceivedOn(), "BIRTH");

        deviceEventCreator.setEventMessage(payload.toDisplayString());
        // TODO check this change
        deviceEventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        deviceEventCreator.setSentOn(message.getSentOn());

        KapuaPosition position = message.getPosition();
        if (position != null) {
            deviceEventCreator.setPosition(position);
        }

        deviceEventService.create(deviceEventCreator);
    }

    @Override
    public void death(KapuaId connectionId, KapuaDisconnectMessage message)
            throws KapuaException {
        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        //
        // Device update
        KapuaLocator locator = KapuaLocator.getInstance();

        //
        // Event create
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, message.getReceivedOn(), "DEATH");

        deviceEventCreator.setReceivedOn(message.getReceivedOn());
        // TODO check this change
        deviceEventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        deviceEventCreator.setSentOn(message.getSentOn());

        KapuaPosition position = message.getPosition();
        if (position != null) {
            deviceEventCreator.setPosition(position);
        }

        deviceEventService.create(deviceEventCreator);
    }

    @Override
    public void missing(KapuaId connectionId, KapuaMissingMessage message)
            throws KapuaException {
        KapuaPayload payload = message.getPayload();
        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        //
        // Device update
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.find(scopeId, deviceId);

        //
        // Event create
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, device.getId(), message.getReceivedOn(), "MISSING");

        deviceEventCreator.setEventMessage(payload.toDisplayString());
        // TODO check this change
        deviceEventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        deviceEventCreator.setSentOn(message.getReceivedOn());

        KapuaPosition position = message.getPosition();
        if (position != null) {
            deviceEventCreator.setPosition(position);
        }

        deviceEventService.create(deviceEventCreator);

    }

    @Override
    public void applications(KapuaId connectionId, KapuaAppsMessage message)
            throws KapuaException {
        KapuaPayload payload = message.getPayload();
        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        //
        // Device update
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.find(scopeId, deviceId);

        //
        // Event create
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, device.getId(), message.getReceivedOn(), "APPLICATION");

        deviceEventCreator.setEventMessage(payload.toDisplayString());
        // TODO check this change
        deviceEventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        deviceEventCreator.setSentOn(message.getReceivedOn());

        KapuaPosition position = message.getPosition();
        if (position != null) {
            deviceEventCreator.setPosition(position);
        }

        deviceEventService.create(deviceEventCreator);
    }
}
