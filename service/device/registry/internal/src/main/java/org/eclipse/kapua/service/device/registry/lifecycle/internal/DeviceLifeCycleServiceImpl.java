/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaOptimisticLockingException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaLifecycleMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceLifeCycleService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceLifeCycleServiceImpl implements DeviceLifeCycleService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceLifeCycleServiceImpl.class);

    private static final int MAX_RETRY = 3;
    private static final double MAX_WAIT = 500d;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceEventService DEVICE_EVENT_SERVICE = LOCATOR.getService(DeviceEventService.class);
    private static final DeviceEventFactory DEVICE_EVENT_FACTORY = LOCATOR.getFactory(DeviceEventFactory.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
    private static final DeviceFactory DEVICE_FACTORY = LOCATOR.getFactory(DeviceFactory.class);

    @Override
    public void birth(KapuaId connectionId, KapuaBirthMessage message) throws KapuaException {

        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        KapuaBirthPayload payload = message.getPayload();
        KapuaBirthChannel channel = message.getChannel();

        //
        // Device update
        Device device;
        if (deviceId == null) {
            String clientId = channel.getClientId();

            DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(scopeId, clientId);

            deviceCreator.setDisplayName(payload.getDisplayName());
            deviceCreator.setSerialNumber(payload.getSerialNumber());
            deviceCreator.setModelId(payload.getModelId());
            deviceCreator.setModelName(payload.getModelName());
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

            device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);
        } else {
            device = updateDeviceInfoFromMessage(scopeId, deviceId, message.getPayload(), connectionId);
        }

        //
        // Event create
        createLifecycleEvent(device, "BIRTH", message);
    }

    @Override
    public void applications(KapuaId connectionId, KapuaAppsMessage message) throws KapuaException {

        Device device = updateDeviceInfoFromMessage(message.getScopeId(), message.getDeviceId(), message.getPayload(), connectionId);

        //
        // Event create
        createLifecycleEvent(device, "APPLICATION", message);
    }

    @Override
    public void missing(KapuaId connectionId, KapuaMissingMessage message) throws KapuaException {
        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        //
        // Event create
        createLifecycleEvent(scopeId, deviceId, "MISSING", message);
    }

    @Override
    public void death(KapuaId connectionId, KapuaDisconnectMessage message) throws KapuaException {
        KapuaId scopeId = message.getScopeId();
        KapuaId deviceId = message.getDeviceId();

        //
        // Event create
        createLifecycleEvent(scopeId, deviceId, "DEATH", message);
    }

    /**
     * Updates the {@link Device} infos from the {@link KapuaBirthPayload} (or {@link org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload}.
     * <p>
     * Tries to update the {@link Device} a number of times to allow close device updates to be stored properly.
     *
     * @param scopeId      The {@link Device#getScopeId()} to update.
     * @param deviceId     The {@link Device#getId()} to update.
     * @param payload      The {@link KapuaBirthPayload} from which extract data.
     * @param connectionId The {@link DeviceConnection#getId()}
     * @return The updated {@link Device}.
     * @throws KapuaException If {@link Device} does not exists or {@link DeviceRegistryService#update(KapuaEntity)} causes an error.
     * @since 1.2.0
     */
    private Device updateDeviceInfoFromMessage(KapuaId scopeId, KapuaId deviceId, KapuaBirthPayload payload, KapuaId connectionId) throws KapuaException {

        Device device = null;

        int retry = 0;
        do {
            retry++;

            try {
                device = DEVICE_REGISTRY_SERVICE.find(scopeId, deviceId);

                if (device == null) {
                    throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
                }

                // If the BirthMessage does not contain a 'Display Name' keep the one registered on the DeviceRegistryService.
                if (!Strings.isNullOrEmpty(payload.getDisplayName())) {
                    device.setDisplayName(payload.getDisplayName());
                }

                device.setSerialNumber(payload.getSerialNumber());
                device.setModelId(payload.getModelId());
                device.setModelName(payload.getModelName());
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

                device = DEVICE_REGISTRY_SERVICE.update(device);
                break;
            } catch (KapuaOptimisticLockingException e) {
                LOG.warn("Concurrent update for device: {}... Attempt: {}/{}. {}", device.getClientId(), retry, MAX_RETRY, retry < MAX_RETRY ? "Retrying..." : "Raising exception!");

                if (retry < MAX_RETRY) {
                    try {
                        Thread.sleep((long) (Math.random() * MAX_WAIT));
                    } catch (InterruptedException e1) {
                        LOG.warn("Error while waiting retry: {}", e.getMessage());
                    }
                } else {
                    throw e;
                }
            }
        }
        while (retry < MAX_RETRY);

        return device;
    }


    /**
     * Creates a {@link DeviceEvent} from the {@link KapuaLifecycleMessage}.
     * <p>
     * Internally invokes {@link DeviceRegistryService#find(KapuaId, KapuaId)} with the given parameters and
     * then uses {@link #createLifecycleEvent(Device, String, KapuaLifecycleMessage)}
     *
     * @param scopeId  The {@link Device#getScopeId()} of the {@link Device} that generated the {@link KapuaLifecycleMessage}.
     * @param deviceId The {@link Device#getId()} of the {@link Device} that generated the {@link KapuaLifecycleMessage}.
     * @param resource The resource used to publish the {@link KapuaLifecycleMessage}
     * @param message  The {@link KapuaLifecycleMessage} from which to extranct data.
     * @throws KapuaException if storing the {@link DeviceEvent} throws a {@link KapuaException}
     * @since 1.2.0
     */
    private DeviceEvent createLifecycleEvent(@NotNull KapuaId scopeId, KapuaId deviceId, @NotNull String resource, @NotNull KapuaLifecycleMessage<?, ?> message) throws KapuaException {

        Device device = DEVICE_REGISTRY_SERVICE.find(scopeId, deviceId);

        return createLifecycleEvent(device, resource, message);
    }

    /**
     * Creates a {@link DeviceEvent} from the {@link KapuaLifecycleMessage}.
     *
     * @param device   The {@link Device} that generated the {@link KapuaLifecycleMessage}.
     * @param resource The resource used to publish the {@link KapuaLifecycleMessage}
     * @param message  The {@link KapuaLifecycleMessage} from which to extranct data.
     * @throws KapuaException if storing the {@link DeviceEvent} throws a {@link KapuaException}
     * @since 1.2.0
     */
    private DeviceEvent createLifecycleEvent(@NotNull Device device, @NotNull String resource, @NotNull KapuaLifecycleMessage<?, ?> message) throws KapuaException {

        DeviceEventCreator deviceEventCreator = DEVICE_EVENT_FACTORY.newCreator(device.getScopeId(), device.getId(), message.getReceivedOn(), resource);
        deviceEventCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        deviceEventCreator.setSentOn(message.getSentOn());

        if (message.getPayload() != null) {
            deviceEventCreator.setEventMessage(message.getPayload().toDisplayString());
        }

        KapuaPosition position = message.getPosition();
        if (position != null) {
            deviceEventCreator.setPosition(position);
        }

        return KapuaSecurityUtils.doPrivileged(() -> DEVICE_EVENT_SERVICE.create(deviceEventCreator));
    }
}
