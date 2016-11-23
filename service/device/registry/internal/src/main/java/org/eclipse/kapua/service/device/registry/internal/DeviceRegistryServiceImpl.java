/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;

/**
 * Device registry service implementation.
 * 
 * @since 1.0
 *
 */
public class DeviceRegistryServiceImpl implements DeviceRegistryService {

    // Collaborator members
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final DeviceValidation deviceValidation;
    private final DeviceEntityManagerFactory deviceEntityManagerFactory;
    private final EntityManagerSession entityManagerSession;

    // Constructors
    /**
     * Constructor
     * 
     * @param authorizationService
     * @param permissionFactory
     * @param deviceEntityManagerFactory
     */
    public DeviceRegistryServiceImpl(AuthorizationService authorizationService, PermissionFactory permissionFactory, DeviceEntityManagerFactory deviceEntityManagerFactory) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.deviceValidation = new DeviceValidation(permissionFactory, authorizationService);
        this.deviceEntityManagerFactory = deviceEntityManagerFactory;
        this.entityManagerSession = new EntityManagerSession(deviceEntityManagerFactory);
    }

    /**
     * Constructor
     */
    public DeviceRegistryServiceImpl() {
        KapuaLocator locator = KapuaLocator.getInstance();
        authorizationService = locator.getService(AuthorizationService.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
        this.deviceValidation = new DeviceValidation(permissionFactory, authorizationService);
        deviceEntityManagerFactory = DeviceEntityManagerFactory.instance();
        this.entityManagerSession = new EntityManagerSession(deviceEntityManagerFactory);
    }

    // Operations implementation

    @Override
    public Device create(DeviceCreator deviceCreator) throws KapuaException {
        deviceValidation.validateCreatePreconditions(deviceCreator);

        return entityManagerSession.onEntityManagerInsert(entityManager -> {
            entityManager.beginTransaction();
            Device device = DeviceDAO.create(entityManager, deviceCreator);
            entityManager.commit();
            return DeviceDAO.find(entityManager, device.getId());
        });
    }

    @Override
    public Device update(Device device) throws KapuaException {
        deviceValidation.validateUpdatePreconditions(device);
        return entityManagerSession.onEntityManagerResult(entityManager -> {
            Device currentDevice = DeviceDAO.find(entityManager, device.getId());
            if (currentDevice == null) {
                throw new KapuaEntityNotFoundException(Device.TYPE, device.getId());
            }

            currentDevice.setStatus(device.getStatus());
            currentDevice.setDisplayName(device.getDisplayName());
            currentDevice.setLastEventOn(device.getLastEventOn());
            currentDevice.setLastEventType(device.getLastEventType());
            currentDevice.setSerialNumber(device.getSerialNumber());
            currentDevice.setModelId(device.getModelId());
            currentDevice.setImei(device.getImei());
            currentDevice.setImsi(device.getImsi());
            currentDevice.setIccid(device.getIccid());
            currentDevice.setBiosVersion(device.getBiosVersion());
            currentDevice.setFirmwareVersion(device.getFirmwareVersion());
            currentDevice.setOsVersion(device.getOsVersion());
            currentDevice.setJvmVersion(device.getJvmVersion());
            currentDevice.setOsgiFrameworkVersion(device.getOsgiFrameworkVersion());
            currentDevice.setApplicationFrameworkVersion(device.getApplicationFrameworkVersion());
            currentDevice.setApplicationIdentifiers(device.getApplicationIdentifiers());
            currentDevice.setAcceptEncoding(device.getAcceptEncoding());
            currentDevice.setCustomAttribute1(device.getCustomAttribute1());
            currentDevice.setCustomAttribute2(device.getCustomAttribute2());
            currentDevice.setCustomAttribute3(device.getCustomAttribute3());
            currentDevice.setCustomAttribute4(device.getCustomAttribute4());
            currentDevice.setCustomAttribute5(device.getCustomAttribute5());
            currentDevice.setCredentialsMode(device.getCredentialsMode());
            currentDevice.setPreferredUserId(device.getPreferredUserId());

            // Update
            entityManager.beginTransaction();
            DeviceDAO.update(entityManager, currentDevice);
            entityManager.commit();

            return DeviceDAO.find(entityManager, device.getId());
        });
    }

    @Override
    public Device find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        deviceValidation.validateFindPreconditions(scopeId, entityId);
        return entityManagerSession.onEntityManagerResult(entityManager -> DeviceDAO.find(entityManager, entityId));
    }

    @Override
    public DeviceListResult query(KapuaQuery<Device> query) throws KapuaException {
        deviceValidation.validateQueryPreconditions(query);
        return entityManagerSession.onEntityManagerResult(entityManager -> DeviceDAO.query(entityManager, query));
    }

    @Override
    public long count(KapuaQuery<Device> query) throws KapuaException {
        deviceValidation.validateCountPreconditions(query);
        return entityManagerSession.onEntityManagerResult(entityManager -> DeviceDAO.count(entityManager, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceId) throws KapuaException {
        deviceValidation.validateDeletePreconditions(scopeId, deviceId);
        entityManagerSession.onEntityManagerAction(entityManager -> {
            if (DeviceDAO.find(entityManager, deviceId) == null) {
                throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
            }

            entityManager.beginTransaction();
            DeviceDAO.delete(entityManager, deviceId);
            entityManager.commit();
        });
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        deviceValidation.validateFindByClientIdPreconditions(scopeId, clientId);

        DeviceQueryImpl query = new DeviceQueryImpl(scopeId);
        KapuaPredicate predicate = new AttributePredicate<String>(DevicePredicates.CLIENT_ID, clientId);
        query.setPredicate(predicate);

        //
        // Query and parse result
        Device device = null;
        DeviceListResult result = query(query);
        if (result.getSize() == 1) {
            device = result.getItem(0);
        }

        return device;
    }

}
