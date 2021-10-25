/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.device.management.registry.manager.DeviceManagementRegistryManagerService;
import org.eclipse.kapua.service.device.management.registry.manager.internal.DeviceManagementRegistryManagerServiceImpl;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.internal.DeviceManagementOperationFactoryImpl;
import org.eclipse.kapua.service.device.management.registry.operation.internal.DeviceManagementOperationRegistryServiceImpl;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationService;
import org.eclipse.kapua.service.device.management.registry.operation.notification.internal.ManagementOperationNotificationFactoryImpl;
import org.eclipse.kapua.service.device.management.registry.operation.notification.internal.ManagementOperationNotificationServiceImpl;

public class DeviceManagementRegistryModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceManagementRegistryManagerService.class).to(DeviceManagementRegistryManagerServiceImpl.class);

        bind(DeviceManagementOperationFactory.class).to(DeviceManagementOperationFactoryImpl.class);
        bind(DeviceManagementOperationRegistryService.class).to(DeviceManagementOperationRegistryServiceImpl.class);

        bind(ManagementOperationNotificationFactory.class).to(ManagementOperationNotificationFactoryImpl.class);
        bind(ManagementOperationNotificationService.class).to(ManagementOperationNotificationServiceImpl.class);
    }
}
