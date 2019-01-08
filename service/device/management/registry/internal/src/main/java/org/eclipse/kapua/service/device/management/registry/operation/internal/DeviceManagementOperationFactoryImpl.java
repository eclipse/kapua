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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;

@KapuaProvider
public class DeviceManagementOperationFactoryImpl implements DeviceManagementOperationFactory {

    @Override
    public DeviceManagementOperation newEntity(KapuaId scopeId) {
        return new DeviceManagementOperationImpl(scopeId);
    }

    @Override
    public DeviceManagementOperationCreator newCreator(KapuaId scopeId) {
        return new DeviceManagementOperationCreatorImpl(scopeId);
    }

    @Override
    public DeviceManagementOperationQuery newQuery(KapuaId scopeId) {
        return new DeviceManagementOperationQueryImpl(scopeId);
    }

    @Override
    public DeviceManagementOperationListResult newListResult() {
        return new DeviceManagementOperationListResultImpl();
    }

    @Override
    public DeviceManagementOperationProperty newStepProperty(String name, String propertyType, String propertyValue) {
        return new DeviceManagementOperationPropertyImpl(name, propertyType, propertyValue);
    }
}
