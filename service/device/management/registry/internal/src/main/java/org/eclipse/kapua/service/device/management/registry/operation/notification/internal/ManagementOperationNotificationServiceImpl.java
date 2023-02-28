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
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationTransactedRepository;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementRegistryDomains;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationTransactedRepository;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ManagementOperationNotificationServiceImpl implements ManagementOperationNotificationService {

    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final ManagementOperationNotificationFactory entityFactory;
    private final ManagementOperationNotificationTransactedRepository repository;
    private final DeviceManagementOperationTransactedRepository deviceManagementOperationRepository;

    @Inject
    public ManagementOperationNotificationServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            ManagementOperationNotificationFactory entityFactory,
            ManagementOperationNotificationTransactedRepository repository,
            DeviceManagementOperationTransactedRepository deviceManagementOperationRepository) {
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.entityFactory = entityFactory;
        this.repository = repository;
        this.deviceManagementOperationRepository = deviceManagementOperationRepository;
    }

    @Override
    public ManagementOperationNotification create(ManagementOperationNotificationCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "managementOperationNotificationCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "managementOperationNotificationCreator.scopeId");
        ArgumentValidator.notNull(creator.getOperationId(), "managementOperationNotificationCreator.operationId");
        ArgumentValidator.notNull(creator.getSentOn(), "managementOperationNotificationCreator.sentOn");
        ArgumentValidator.notNull(creator.getStatus(), "managementOperationNotificationCreator.status");
        ArgumentValidator.notNull(creator.getProgress(), "managementOperationNotificationCreator.progress");
        ArgumentValidator.notNegative(creator.getProgress(), "managementOperationNotificationCreator.progress");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Check operation existence
        if (deviceManagementOperationRepository.find(creator.getScopeId(), creator.getOperationId()) == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, creator.getOperationId());
        }
        //
        // Create DeviceManagementOperationNotification
        final ManagementOperationNotification newEntity = entityFactory.newEntity(creator.getScopeId());
        newEntity.setOperationId(creator.getOperationId());
        newEntity.setSentOn(creator.getSentOn());
        newEntity.setStatus(creator.getStatus());
        newEntity.setResource(creator.getResource());
        newEntity.setProgress(creator.getProgress());
        newEntity.setMessage(creator.getMessage());
        //
        // Do create
        return repository.create(newEntity);
    }

    @Override
    public ManagementOperationNotification find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "managementOperationNotificationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return repository.find(scopeId, entityId);
    }

    @Override
    public ManagementOperationNotificationListResult query(KapuaQuery query) throws KapuaException {
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
        ArgumentValidator.notNull(entityId, "managementOperationNotificationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, null));

        //
        // Do delete
        repository.delete(scopeId, entityId);
    }
}
