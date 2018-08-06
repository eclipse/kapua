/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;

@KapuaProvider
public class DeviceManagementOperationRegistryServiceImpl extends AbstractKapuaService implements DeviceManagementOperationRegistryService {


    private static KapuaLocator locator = KapuaLocator.getInstance();

    private static AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
    private static PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

    private static DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);

    protected DeviceManagementOperationRegistryServiceImpl() {
        super(DeviceManagementOperationEntityManagerFactory.getInstance());
    }

    @Override
    public DeviceManagementOperation create(DeviceManagementOperationCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "deviceManagementOperationCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "deviceManagementOperationCreator.scopeId");
        ArgumentValidator.notNull(creator.getStartedOn(), "deviceManagementOperationCreator.startedOn");
        ArgumentValidator.notNull(creator.getDeviceId(), "deviceManagementOperationCreator.deviceId");
        ArgumentValidator.notNull(creator.getOperationId(), "deviceManagementOperationCreator.operationId");
        ArgumentValidator.notNull(creator.getStatus(), "deviceManagementOperationCreator.status");
        ArgumentValidator.notNull(creator.getAppId(), "deviceManagementOperationCreator.appId");
        ArgumentValidator.notNull(creator.getAction(), "deviceManagementOperationCreator.action");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Check device existence
        if (KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.find(creator.getScopeId(), creator.getDeviceId()) == null)) {
            throw new KapuaEntityNotFoundException(Device.TYPE, creator.getDeviceId());
        }

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> DeviceManagementOperationDAO.create(em, creator));
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
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Check device existence
        if (KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.find(entity.getScopeId(), entity.getDeviceId()) == null)) {
            throw new KapuaEntityNotFoundException(Device.TYPE, entity.getDeviceId());
        }

        //
        // Check existence
        if (find(entity.getScopeId(), entity.getId()) == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, entity.getId());
        }

        //
        // Do update
        return entityManagerSession.onTransactedInsert(em -> DeviceManagementOperationDAO.update(em, entity));
    }

    @Override
    public DeviceManagementOperation find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "deviceManagementOperationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> DeviceManagementOperationDAO.find(em, entityId));
    }

    @Override
    public DeviceManagementOperationListResult query(KapuaQuery<DeviceManagementOperation> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> DeviceManagementOperationDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<DeviceManagementOperation> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> DeviceManagementOperationDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "deviceManagementOperationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, entityId) == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, entityId);
        }

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> DeviceManagementOperationDAO.delete(em, entityId));
    }
}