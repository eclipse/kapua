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
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

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
import org.eclipse.kapua.service.device.management.registry.operation.internal.DeviceManagementOperationEntityManagerFactory;
import org.eclipse.kapua.service.device.management.registry.operation.internal.DeviceManagementRegistryDomain;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationRegistryService;

import javax.inject.Inject;

@KapuaProvider
public class DeviceManagementOperationNotificationRegistryServiceImpl extends AbstractKapuaService implements DeviceManagementOperationNotificationRegistryService {

    private static final Domain DEVICE_MANAGEMENT_REGISTRY_DOMAIN = new DeviceManagementRegistryDomain();

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    protected DeviceManagementOperationNotificationRegistryServiceImpl() {
        super(DeviceManagementOperationEntityManagerFactory.getInstance());
    }

    @Override
    public DeviceManagementOperationNotification create(DeviceManagementOperationNotificationCreator creator) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(creator, "devicePackageDownloadOperationCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "devicePackageDownloadOperationCreator.scopeId");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> DeviceManagementOperationNotificationDAO.create(em, creator));
    }

    @Override
    public DeviceManagementOperationNotification find(KapuaId scopeId, KapuaId devicePackageDownloadOperationNotificationId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(devicePackageDownloadOperationNotificationId, "devicePackageDownloadOperationNotificationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> DeviceManagementOperationNotificationDAO.find(em, devicePackageDownloadOperationNotificationId));
    }

    @Override
    public DeviceManagementOperationNotificationListResult query(KapuaQuery<DeviceManagementOperationNotification> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> DeviceManagementOperationNotificationDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<DeviceManagementOperationNotification> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> DeviceManagementOperationNotificationDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId devicePackageDownloadOperationNotificationId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(devicePackageDownloadOperationNotificationId, "devicePackageDownloadOperationNotificationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onResult(em -> DeviceManagementOperationNotificationDAO.find(em, devicePackageDownloadOperationNotificationId));
    }
}