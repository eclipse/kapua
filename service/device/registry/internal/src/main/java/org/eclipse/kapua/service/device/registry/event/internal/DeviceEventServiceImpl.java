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
package org.eclipse.kapua.service.device.registry.event.internal;

import javax.persistence.OptimisticLockException;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DeviceEventService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceEventServiceImpl extends AbstractKapuaService implements DeviceEventService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceEventServiceImpl.class);

    private static final int MAX_ITERATION = 3;
    private static final double MAX_WAIT = 200d;

    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final DeviceRegistryService deviceRegistryService;

    /**
     * Constructor
     */
    public DeviceEventServiceImpl() {
        super(DeviceEntityManagerFactory.instance());
        KapuaLocator locator = KapuaLocator.getInstance();
        authorizationService = locator.getService(AuthorizationService.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
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

        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.write, deviceEventCreator.getScopeId()));

        // Check that device exists
        if (deviceRegistryService.find(deviceEventCreator.getScopeId(), deviceEventCreator.getDeviceId()) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, deviceEventCreator.getDeviceId());
        }

        // Create the event
        DeviceEvent deviceEvent = entityManagerSession.onTransactedInsert(entityManager -> DeviceEventDAO.create(entityManager, deviceEventCreator));
        int iteration = 0;
        do {
            try {
                Device device = deviceRegistryService.find(deviceEvent.getScopeId(), deviceEvent.getDeviceId());
                if (device != null) {
                    device.setLastEventId(deviceEvent.getId());
                    deviceRegistryService.update(device);
                }
                break;
            }
            catch (OptimisticLockException e) {
                LOG.warn("Concurrent update for device id {}... try again (if maximum attempts is not reach)", deviceEvent.getDeviceId());
                try {
                    Thread.sleep((long)(Math.random() * MAX_WAIT));
                } catch (InterruptedException e1) {
                    LOG.warn("Error while waiting {}", e.getMessage());
                }
            }
        }
        while(iteration++ < MAX_ITERATION);
        return deviceEvent;
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

        return entityManagerSession.onResult(em -> DeviceEventDAO.find(em, scopeId, entityId));
    }

    @Override
    public DeviceEventListResult query(KapuaQuery<DeviceEvent> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> DeviceEventDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<DeviceEvent> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_EVENT_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> DeviceEventDAO.count(em, query));
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

        entityManagerSession.onTransactedAction(em -> {
            if (DeviceEventDAO.find(em, scopeId, deviceEventId) == null) {
                throw new KapuaEntityNotFoundException(DeviceEvent.TYPE, deviceEventId);
            }

            DeviceEventDAO.delete(em, scopeId, deviceEventId);
        });
    }
}
