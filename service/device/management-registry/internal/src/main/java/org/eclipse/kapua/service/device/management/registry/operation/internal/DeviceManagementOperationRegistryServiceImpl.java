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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;

import javax.inject.Inject;

@KapuaProvider
public class DeviceManagementOperationRegistryServiceImpl extends AbstractKapuaService implements DeviceManagementOperationRegistryService {

    private static final Domain DEVICE_MANAGEMENT_REGISTRY_DOMAIN = new DeviceManagementRegistryDomain();

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    protected DeviceManagementOperationRegistryServiceImpl() {
        super(DeviceManagementOperationEntityManagerFactory.getInstance());
    }

    @Override
    public DeviceManagementOperation create(DeviceManagementOperationCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "devicePackageDownloadOperationCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "devicePackageDownloadOperationCreator.scopeId");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> DeviceManagementOperationDAO.create(em, creator));
    }

    @Override
    public DeviceManagementOperation update(DeviceManagementOperation entity) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(entity, "devicePackageDownloadOperation");
        ArgumentValidator.notNull(entity.getScopeId(), "devicePackageDownloadOperation.scopeId");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Do update
        return entityManagerSession.onTransactedInsert(em -> DeviceManagementOperationDAO.update(em, entity));
    }

    @Override
    public DeviceManagementOperation find(KapuaId scopeId, KapuaId devicePackageDownloadOperationId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(devicePackageDownloadOperationId, "devicePackageDownloadOperationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> DeviceManagementOperationDAO.find(em, devicePackageDownloadOperationId));
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
    public void delete(KapuaId scopeId, KapuaId devicePackageDownloadOperationId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(devicePackageDownloadOperationId, "devicePackageDownloadOperationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onResult(em -> DeviceManagementOperationDAO.find(em, devicePackageDownloadOperationId));
    }
}