/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationTransactedRepository;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementRegistryDomains;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceTransactedRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DeviceManagementOperationRegistryServiceImpl
        implements DeviceManagementOperationRegistryService {

    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final DeviceTransactedRepository deviceRepository;
    private final DeviceManagementOperationTransactedRepository repository;
    private final DeviceManagementOperationFactory entityFactory;

    @Inject
    public DeviceManagementOperationRegistryServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceTransactedRepository deviceRepository,
            DeviceManagementOperationTransactedRepository repository,
            DeviceManagementOperationFactory entityFactory) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.deviceRepository = deviceRepository;
        this.repository = repository;
        this.entityFactory = entityFactory;
    }

    @Override
    public DeviceManagementOperation create(DeviceManagementOperationCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "creator");
        ArgumentValidator.notNull(creator.getScopeId(), "creator.scopeId");
        ArgumentValidator.notNull(creator.getStartedOn(), "creator.startedOn");
        ArgumentValidator.notNull(creator.getDeviceId(), "creator.deviceId");
        ArgumentValidator.notNull(creator.getOperationId(), "creator.operationId");
        ArgumentValidator.notNull(creator.getStatus(), "creator.status");
        ArgumentValidator.notNull(creator.getAppId(), "creator.appId");
        ArgumentValidator.notNull(creator.getAction(), "creator.action");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Check device existence
        if (deviceRepository.find(creator.getScopeId(), creator.getDeviceId()) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, creator.getDeviceId());
        }

        //
        // Create DeviceManagementOperationNotification
        DeviceManagementOperation newEntity = entityFactory.newEntity(creator.getScopeId());
        newEntity.setStartedOn(creator.getStartedOn());
        newEntity.setDeviceId(creator.getDeviceId());
        newEntity.setOperationId(creator.getOperationId());
        newEntity.setAppId(creator.getAppId());
        newEntity.setAction(creator.getAction());
        newEntity.setResource(creator.getResource());
        newEntity.setStatus(creator.getStatus());
        newEntity.setStatus(creator.getStatus());
        newEntity.setInputProperties(creator.getInputProperties());

        //
        // Do create
        return repository.create(newEntity);
    }

    @Override
    public DeviceManagementOperation update(DeviceManagementOperation entity) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(entity, "deviceManagementOperation");
        ArgumentValidator.notNull(entity.getScopeId(), "deviceManagementOperation.scopeId");
        ArgumentValidator.notNull(entity.getId(), "deviceManagementOperation.id");
        ArgumentValidator.notNull(entity.getStartedOn(), "deviceManagementOperation.startedOn");
        ArgumentValidator.notNull(entity.getDeviceId(), "deviceManagementOperation.deviceId");
        ArgumentValidator.notNull(entity.getOperationId(), "deviceManagementOperation.operationId");
        ArgumentValidator.notNull(entity.getStatus(), "deviceManagementOperation.status");
        ArgumentValidator.notNull(entity.getAppId(), "deviceManagementOperation.appId");
        ArgumentValidator.notNull(entity.getAction(), "deviceManagementOperation.action");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Check device existence
        if (deviceRepository.find(entity.getScopeId(), entity.getDeviceId()) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, entity.getDeviceId());
        }

        //
        // Check existence
        if (find(entity.getScopeId(), entity.getId()) == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, entity.getId());
        }

        //
        // Do update
        return repository.update(entity);
    }

    @Override
    public DeviceManagementOperation find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "deviceManagementOperationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return repository.find(scopeId, entityId);
    }

    @Override
    public DeviceManagementOperation findByOperationId(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(operationId, "operationId");

        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return repository.findByOperationId(scopeId, operationId);
    }

    @Override
    public DeviceManagementOperationListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return repository.query(query);
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return repository.count(query);
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "deviceManagementOperationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, entityId) == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, entityId);
        }

        //
        // Do delete
        repository.delete(scopeId, entityId);
    }
}
