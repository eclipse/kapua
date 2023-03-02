/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaOptimisticLockingException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.storage.TxContext;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link DeviceEventService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class DeviceEventServiceImpl
        implements DeviceEventService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceEventServiceImpl.class);

    private static final int MAX_RETRY = 3;
    private static final double MAX_WAIT = 200d;

    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final TxManager txManager;
    private final DeviceRepository deviceRepository;
    private final DeviceEventFactory entityFactory;
    private final DeviceEventRepository repository;

    @Inject
    public DeviceEventServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TxManager txManager,
            DeviceRepository deviceRepository,
            DeviceEventFactory entityFactory,
            DeviceEventRepository deviceEventRepository) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.txManager = txManager;
        this.deviceRepository = deviceRepository;
        this.entityFactory = entityFactory;
        this.repository = deviceEventRepository;
    }

    // Operations

    @Override
    public DeviceEvent create(DeviceEventCreator deviceEventCreator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceEventCreator, "deviceEventCreator");
        ArgumentValidator.notNull(deviceEventCreator.getScopeId(), "deviceEventCreator.scopeId");
        ArgumentValidator.notNull(deviceEventCreator.getDeviceId(), "deviceEventCreator.deviceId");
        ArgumentValidator.notNull(deviceEventCreator.getReceivedOn(), "deviceEventCreator.receivedOn");
        ArgumentValidator.notEmptyOrNull(deviceEventCreator.getResource(), "deviceEventCreator.eventType");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.write, deviceEventCreator.getScopeId()));
        return txManager.executeWithResult(tx -> {
            //
            // Check that device exists
            final Device device = deviceRepository.find(tx, deviceEventCreator.getScopeId(), deviceEventCreator.getDeviceId());
            if (device == null) {
                throw new KapuaEntityNotFoundException(Device.TYPE, deviceEventCreator.getDeviceId());
            }

            // Create the event
            DeviceEvent newEvent = entityFactory.newEntity(deviceEventCreator.getScopeId());
            newEvent.setDeviceId(device.getId());
            newEvent.setReceivedOn(deviceEventCreator.getReceivedOn());
            newEvent.setSentOn(deviceEventCreator.getSentOn());
            newEvent.setResource(deviceEventCreator.getResource());
            newEvent.setAction(deviceEventCreator.getAction());
            newEvent.setResponseCode(deviceEventCreator.getResponseCode());
            newEvent.setEventMessage(deviceEventCreator.getEventMessage());
            newEvent.setPosition(deviceEventCreator.getPosition());

            final DeviceEvent created = repository.create(tx, newEvent);
            updateLastEventOnDevice(tx, device, created);

            return newEvent;
        });

    }

    @Override
    public DeviceEvent find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.read, scopeId));

        return txManager.executeWithResult(tx -> repository.find(tx, scopeId, entityId));
    }

    @Override
    public DeviceEventListResult query(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.read, query.getScopeId()));

        return txManager.executeWithResult(tx -> repository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.read, query.getScopeId()));

        return txManager.executeWithResult(tx -> repository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceEventId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceEventId, "deviceEvent.id");
        ArgumentValidator.notNull(scopeId, "deviceEvent.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.delete, scopeId));

        txManager.executeNoResult(tx -> repository.delete(tx, scopeId, deviceEventId));
    }


    /**
     * Updates the {@link Device#getLastEventId()} with the given {@link DeviceEvent}.
     *
     * @param tx
     * @param deviceEvent The {@link DeviceEvent} that needs to be set.
     * @throws KapuaException If {@link Device} does not exist or updating the entity causes an error that is not {@link KapuaOptimisticLockingException} which is ignored.
     * @since 1.2.0
     */
    private void updateLastEventOnDevice(TxContext tx, Device device, DeviceEvent deviceEvent) throws KapuaException {
        int retry = 0;
        do {
            retry++;
            try {
                if (device == null) {
                    throw new KapuaEntityNotFoundException(Device.TYPE, deviceEvent.getDeviceId());
                }

                if (device.getLastEvent() == null ||
                        device.getLastEvent().getReceivedOn().before(deviceEvent.getReceivedOn())) {
                    device.setLastEventId(deviceEvent.getId());
                    deviceRepository.update(tx, device);
                }
                break;
            } catch (KapuaOptimisticLockingException e) {
                LOG.warn("Concurrent update for device: {} - Event id: {} Attempt: {}/{}. {}", deviceEvent.getDeviceId(), deviceEvent.getId(), retry, MAX_RETRY, retry < MAX_RETRY ? "Retrying..." : "Skipping update!");

                if (retry < MAX_RETRY) {
                    try {
                        Thread.sleep((long) (Math.random() * MAX_WAIT));
                    } catch (InterruptedException e1) {
                        LOG.warn("Error while waiting retry: {}", e.getMessage());
                    }
                }
            }
        }
        while (retry < MAX_RETRY);
    }

}
