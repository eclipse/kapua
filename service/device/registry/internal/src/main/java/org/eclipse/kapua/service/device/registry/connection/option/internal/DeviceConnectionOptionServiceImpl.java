/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionCreator;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionListResult;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;

/**
 * DeviceConnectionService exposes APIs to retrieve Device connections under a scope.
 * It includes APIs to find, list, and update devices connections associated with a scope.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceConnectionOptionServiceImpl extends AbstractKapuaService implements DeviceConnectionOptionService {

    private static final Domain DEVICE_CONNECTION_DOMAIN = new DeviceConnectionDomain();

    public DeviceConnectionOptionServiceImpl() {
        super(DeviceEntityManagerFactory.instance());
    }

    @Override
    public DeviceConnectionOption create(DeviceConnectionOptionCreator deviceConnectionCreator)
            throws KapuaException {
        throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.SERVICE_OPERATION_NOT_SUPPORTED);
    }

    @Override
    public DeviceConnectionOption update(DeviceConnectionOption deviceConnectionOptions)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionOptions, "deviceConnection");
        ArgumentValidator.notNull(deviceConnectionOptions.getId(), "deviceConnection.id");
        ArgumentValidator.notNull(deviceConnectionOptions.getScopeId(), "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_CONNECTION_DOMAIN, Actions.write, deviceConnectionOptions.getScopeId()));

        return entityManagerSession.onTransactedResult(em -> {
            if (DeviceConnectionOptionDAO.find(em, deviceConnectionOptions.getId()) == null) {
                throw new KapuaEntityNotFoundException(DeviceConnectionOption.TYPE, deviceConnectionOptions.getId());
            }

            return DeviceConnectionOptionDAO.update(em, deviceConnectionOptions);
        });
    }

    @Override
    public DeviceConnectionOption find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_CONNECTION_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> DeviceConnectionOptionDAO.find(em, entityId));
    }

    @Override
    public DeviceConnectionOptionListResult query(KapuaQuery<DeviceConnectionOption> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_CONNECTION_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> DeviceConnectionOptionDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<DeviceConnectionOption> query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DEVICE_CONNECTION_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> DeviceConnectionOptionDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceConnectionOptionsId)
            throws KapuaException {
        throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.SERVICE_OPERATION_NOT_SUPPORTED);
    }
}
