/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRepository;
import org.eclipse.kapua.service.device.registry.DeviceRepository;

import javax.inject.Singleton;

public class DeviceManagementRegistryOperationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceManagementOperationFactory.class).to(DeviceManagementOperationFactoryImpl.class);
    }

    @Provides
    @Singleton
    DeviceManagementOperationRegistryService deviceManagementOperationRegistryService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceRepository deviceRepository,
            DeviceManagementOperationRepository repository,
            DeviceManagementOperationFactory entityFactory) {
        return new DeviceManagementOperationRegistryServiceImpl(
                authorizationService,
                permissionFactory,
                deviceRepository,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device_management_operation_registry")),
                repository,
                entityFactory);
    }

    @Provides
    @Singleton
    DeviceManagementOperationRepository deviceManagementOperationRepository() {
        return new DeviceManagementOperationRepositoryImplJpaRepository();
    }
}
