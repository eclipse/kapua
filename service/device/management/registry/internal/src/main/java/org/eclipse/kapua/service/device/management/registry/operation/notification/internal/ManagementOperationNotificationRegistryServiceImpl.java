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
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementRegistryDomains;
import org.eclipse.kapua.service.device.management.registry.operation.internal.DeviceManagementOperationEntityManagerFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationRegistryService;

@KapuaProvider
public class ManagementOperationNotificationRegistryServiceImpl extends AbstractKapuaService implements ManagementOperationNotificationRegistryService {

    private static KapuaLocator locator = KapuaLocator.getInstance();

    private static AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
    private static PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

    private static DeviceManagementOperationRegistryService deviceManagementOperationRegistryService = locator.getService(DeviceManagementOperationRegistryService.class);

    protected ManagementOperationNotificationRegistryServiceImpl() {
        super(DeviceManagementOperationEntityManagerFactory.getInstance());
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
        if (KapuaSecurityUtils.doPrivileged(() -> deviceManagementOperationRegistryService.find(creator.getScopeId(), creator.getOperationId()) == null)) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, creator.getOperationId());
        }

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> ManagementOperationNotificationDAO.create(em, creator));
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
        return entityManagerSession.onResult(em -> ManagementOperationNotificationDAO.find(em, entityId));
    }

    @Override
    public ManagementOperationNotificationListResult query(KapuaQuery<ManagementOperationNotification> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> ManagementOperationNotificationDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<ManagementOperationNotification> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> ManagementOperationNotificationDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "managementOperationNotificationId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, scopeId));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> ManagementOperationNotificationDAO.delete(em, entityId));
    }
}