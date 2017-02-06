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
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;

/**
 * {@link DeviceRegistryService} implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceRegistryServiceImpl extends AbstractKapuaService implements DeviceRegistryService {

    // Constructors
    /**
     * Constructor
     * 
     * @param deviceEntityManagerFactory
     */
    public DeviceRegistryServiceImpl(DeviceEntityManagerFactory deviceEntityManagerFactory) {
        super(deviceEntityManagerFactory);
    }

    /**
     * Constructor
     */
    public DeviceRegistryServiceImpl() {
        super(DeviceEntityManagerFactory.instance());
    }

    // Operations implementation
    @Override
    public Device create(DeviceCreator deviceCreator) throws KapuaException {
        DeviceValidation.validateCreatePreconditions(deviceCreator);
        return entityManagerSession.onTransactedInsert(entityManager -> DeviceDAO.create(entityManager, deviceCreator));
    }

    @Override
    public Device update(Device device) throws KapuaException {
        DeviceValidation.validateUpdatePreconditions(device);

        return entityManagerSession.onTransactedResult(entityManager -> {
            Device currentDevice = DeviceDAO.find(entityManager, device.getId());
            if (currentDevice == null) {
                throw new KapuaEntityNotFoundException(Device.TYPE, device.getId());
            }

            currentDevice.setStatus(device.getStatus());
            currentDevice.setDisplayName(device.getDisplayName());
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

            currentDevice.setConnectionId(device.getConnectionId());
            currentDevice.setLastEventId(device.getLastEventId());

            // Update
            return DeviceDAO.update(entityManager, currentDevice);
        });
    }

    @Override
    public Device find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        DeviceValidation.validateFindPreconditions(scopeId, entityId);
        return entityManagerSession.onResult(entityManager -> DeviceDAO.find(entityManager, entityId));
    }

    @Override
    public DeviceListResult query(KapuaQuery<Device> query) throws KapuaException {
        DeviceValidation.validateQueryPreconditions(query);
        return entityManagerSession.onResult(entityManager -> DeviceDAO.query(entityManager, query));
    }

    @Override
    public long count(KapuaQuery<Device> query) throws KapuaException {
        DeviceValidation.validateCountPreconditions(query);
        return entityManagerSession.onResult(entityManager -> DeviceDAO.count(entityManager, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceId) throws KapuaException {
        DeviceValidation.validateDeletePreconditions(scopeId, deviceId);
        entityManagerSession.onTransactedAction(entityManager -> DeviceDAO.delete(entityManager, deviceId));
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        DeviceValidation.validateFindByClientIdPreconditions(scopeId, clientId);

        DeviceQueryImpl query = new DeviceQueryImpl(scopeId);
        KapuaPredicate predicate = new AttributePredicate<>(DevicePredicates.CLIENT_ID, clientId);
        query.setPredicate(predicate);

        //
        // Query and parse result
        Device device = null;
        DeviceListResult result = query(query);
        if (!result.isEmpty()) {
            device = result.getFirstItem();
        }

        return device;
    }

}
