/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.common;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;

/**
 * Provides logic used to validate preconditions required to execute the device service operation.
 * 
 * @since 1.0
 * 
 */
public final class DeviceValidation {

    private final PermissionFactory permissionFactory;

    private final AuthorizationService authorizationService;

    /**
     * Constructs
     * 
     * @param permissionFactory
     * @param authorizationService
     */
    public DeviceValidation(PermissionFactory permissionFactory, AuthorizationService authorizationService) {
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
    }

    /**
     * Validates the device creates precondition
     * 
     * @param deviceCreator
     * @return
     * @throws KapuaException
     */
    public DeviceCreator validateCreatePreconditions(DeviceCreator deviceCreator) throws KapuaException {
        ArgumentValidator.notNull(deviceCreator, "deviceCreator");
        ArgumentValidator.notNull(deviceCreator.getScopeId(), "deviceCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(deviceCreator.getClientId(), "deviceCreator.clientId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.write, deviceCreator.getScopeId()));

        return deviceCreator;
    }

    /**
     * Validates the device updates precondition
     * 
     * @param device
     * @return
     * @throws KapuaException
     */
    public Device validateUpdatePreconditions(Device device) throws KapuaException {
        ArgumentValidator.notNull(device, "device");
        ArgumentValidator.notNull(device.getId(), "device.id");
        ArgumentValidator.notNull(device.getScopeId(), "v.scopeId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.write, device.getScopeId()));

        return device;
    }

    /**
     * Validates the find device precondition
     * 
     * @param scopeId
     * @param entityId
     * @throws KapuaException
     */
    public void validateFindPreconditions(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.read, scopeId));
    }

    /**
     * Validates the device query precondition
     * 
     * @param query
     * @throws KapuaException
     */
    public void validateQueryPreconditions(KapuaQuery<Device> query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.read, query.getScopeId()));
    }

    /**
     * Validates the device count precondition
     * 
     * @param query
     * @throws KapuaException
     */
    public void validateCountPreconditions(KapuaQuery<Device> query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.read, query.getScopeId()));
    }

    /**
     * Validates the device delete precondition
     * 
     * @param scopeId
     * @param deviceId
     * @throws KapuaException
     */
    public void validateDeletePreconditions(KapuaId scopeId, KapuaId deviceId) throws KapuaException {
        ArgumentValidator.notNull(deviceId, "device.id");
        ArgumentValidator.notNull(scopeId, "device.scopeId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.delete, scopeId));
    }

    /**
     * Validates the device find by identifier precondition
     * 
     * @param scopeId
     * @param clientId
     * @throws KapuaException
     */
    public void validateFindByClientIdPreconditions(KapuaId scopeId, String clientId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notEmptyOrNull(clientId, "clientId");

        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomain.DEVICE, Actions.read, scopeId));
    }

}
