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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.DeviceManagementOperationNotificationRegistryFactory;

@KapuaProvider
public class DeviceManagementOperationNotificationRegistryFactoryImpl implements DeviceManagementOperationNotificationRegistryFactory {

    @Override
    public DeviceManagementOperationNotification newEntity(KapuaId scopeId) {
        return new DeviceManagementOperationNotificationImpl(scopeId);
    }

    @Override
    public DeviceManagementOperationNotificationCreator newCreator(KapuaId scopeId) {
        return new DeviceManagementOperationNotificationCreatorImpl(scopeId);
    }

    @Override
    public DeviceManagementOperationNotificationQuery newQuery(KapuaId scopeId) {
        return new DeviceManagementOperationNotificationQueryImpl(scopeId);
    }

    @Override
    public DeviceManagementOperationNotificationListResult newListResult() {
        return new DeviceManagementOperationNotificationListResultImpl();
    }

}
