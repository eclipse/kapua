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
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationService;

@KapuaProvider
public class ManagementOperationNotificationServiceImpl extends AbstractKapuaService implements ManagementOperationNotificationService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);

    protected ManagementOperationNotificationServiceImpl() {
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
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.write, null));

        //
        // Check operation existence
        if (KapuaSecurityUtils.doPrivileged(() -> DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.find(creator.getScopeId(), creator.getOperationId()) == null)) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, creator.getOperationId());
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> ManagementOperationNotificationDAO.create(em, creator));
    }

    @Override
    public ManagementOperationNotification find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "managementOperationNotificationId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> ManagementOperationNotificationDAO.find(em, scopeId, entityId));
    }

    @Override
    public ManagementOperationNotificationListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> ManagementOperationNotificationDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(em -> ManagementOperationNotificationDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "managementOperationNotificationId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementRegistryDomains.DEVICE_MANAGEMENT_REGISTRY_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> ManagementOperationNotificationDAO.delete(em, scopeId, entityId));
    }
}
