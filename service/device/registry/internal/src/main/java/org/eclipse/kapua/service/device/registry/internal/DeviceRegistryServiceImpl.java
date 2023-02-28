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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceLinker;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceTransactedRepository;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * {@link DeviceRegistryService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class DeviceRegistryServiceImpl
        extends KapuaConfigurableServiceLinker
        implements DeviceRegistryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRegistryServiceImpl.class);
    private final DeviceTransactedRepository repository;
    private final DeviceFactory entityFactory;

    @Inject
    public DeviceRegistryServiceImpl(
            @Named("DeviceRegistryServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager,
            DeviceTransactedRepository repository,
            DeviceFactory entityFactory) {
        super(serviceConfigurationManager);
        this.repository = repository;
        this.entityFactory = entityFactory;
    }

    @Override
    public Device create(DeviceCreator deviceCreator)
            throws KapuaException {
        DeviceValidation.validateCreatePreconditions(deviceCreator);

        //
        // Check entity limit
        serviceConfigurationManager.checkAllowedEntities(deviceCreator.getScopeId(), "Devices");

        //
        // Check duplicate clientId
        DeviceQuery query = entityFactory.newQuery(deviceCreator.getScopeId());
        query.setPredicate(query.attributePredicate(DeviceAttributes.CLIENT_ID, deviceCreator.getClientId()));

        //TODO: check whether this is anywhere efficient
        if (repository.count(query) > 0) {
            throw new KapuaDuplicateNameException(deviceCreator.getClientId());
        }

        final Device device = entityFactory.newEntity(deviceCreator.getScopeId());

        device.setGroupId(deviceCreator.getGroupId());
        device.setClientId(deviceCreator.getClientId());
        device.setStatus(deviceCreator.getStatus());
        device.setDisplayName(deviceCreator.getDisplayName());
        device.setSerialNumber(deviceCreator.getSerialNumber());
        device.setModelId(deviceCreator.getModelId());
        device.setModelName(deviceCreator.getModelName());
        device.setImei(deviceCreator.getImei());
        device.setImsi(deviceCreator.getImsi());
        device.setIccid(deviceCreator.getIccid());
        device.setBiosVersion(deviceCreator.getBiosVersion());
        device.setFirmwareVersion(deviceCreator.getFirmwareVersion());
        device.setOsVersion(deviceCreator.getOsVersion());
        device.setJvmVersion(deviceCreator.getJvmVersion());
        device.setOsgiFrameworkVersion(deviceCreator.getOsgiFrameworkVersion());
        device.setApplicationFrameworkVersion(deviceCreator.getApplicationFrameworkVersion());
        device.setConnectionInterface(deviceCreator.getConnectionInterface());
        device.setConnectionIp(deviceCreator.getConnectionIp());
        device.setApplicationIdentifiers(deviceCreator.getApplicationIdentifiers());
        device.setAcceptEncoding(deviceCreator.getAcceptEncoding());
        device.setCustomAttribute1(deviceCreator.getCustomAttribute1());
        device.setCustomAttribute2(deviceCreator.getCustomAttribute2());
        device.setCustomAttribute3(deviceCreator.getCustomAttribute3());
        device.setCustomAttribute4(deviceCreator.getCustomAttribute4());
        device.setCustomAttribute5(deviceCreator.getCustomAttribute5());
        device.setExtendedProperties(deviceCreator.getExtendedProperties());

        device.setConnectionId(deviceCreator.getConnectionId());
        device.setLastEventId(deviceCreator.getLastEventId());

        //
        // Do create
        return repository.create(device);
    }

    @Override
    public Device update(Device device)
            throws KapuaException {
        DeviceValidation.validateUpdatePreconditions(device);

        //
        // Do update
        final Device currentDevice = repository.find(device.getScopeId(), device.getId());
        if (currentDevice == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, device.getId());
        }
        // Update
        return repository.update(device);
    }

    @Override
    public Device find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        DeviceValidation.validateFindPreconditions(scopeId, entityId);

        //
        // Do find
        return repository.find(scopeId, entityId);
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        DeviceValidation.validateFindByClientIdPreconditions(scopeId, clientId);

        //
        // Check cache and/or do find
        return repository.findByClientId(scopeId, clientId);
    }

    @Override
    public DeviceListResult query(KapuaQuery query)
            throws KapuaException {
        DeviceValidation.validateQueryPreconditions(query);

        //
        // Do query
        return repository.query(query);
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        DeviceValidation.validateCountPreconditions(query);

        // Do count
        return repository.count(query);
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceId) throws KapuaException {
        DeviceValidation.validateDeletePreconditions(scopeId, deviceId);

        //
        // Do delete
        repository.delete(scopeId, deviceId);
    }

    //@ListenServiceEvent(fromAddress="account")
    //@ListenServiceEvent(fromAddress="authorization")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("DeviceRegistryService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("group".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteDeviceByGroupId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        } else if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteDeviceByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    //
    // Private methods
    //

    private void deleteDeviceByGroupId(KapuaId scopeId, KapuaId groupId) throws KapuaException {
        DeviceQuery query = entityFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(DeviceAttributes.GROUP_ID, groupId));

        DeviceListResult devicesToDelete = repository.query(query);

        for (Device d : devicesToDelete.getItems()) {
            d.setGroupId(null);
            repository.update(d);
        }
    }

    private void deleteDeviceByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

        DeviceQuery query = deviceFactory.newQuery(accountId);

        DeviceListResult devicesToDelete = repository.query(query);

        for (Device d : devicesToDelete.getItems()) {
            repository.delete(d.getScopeId(), d.getId());
        }
    }
}
