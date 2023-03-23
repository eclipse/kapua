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
package org.eclipse.kapua.service.device.management.asset.internal;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.asset.store.DeviceAssetStoreService;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRepository;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;

import javax.inject.Named;

public class DeviceManagementAssetModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceAssetFactory.class).to(DeviceAssetFactoryImpl.class);
    }

    @Provides
    @Singleton
    DeviceAssetManagementService deviceAssetManagementService(AuthorizationService authorizationService,
                                                              PermissionFactory permissionFactory,
                                                              DeviceEventRepository deviceEventRepository,
                                                              DeviceEventFactory deviceEventFactory,
                                                              DeviceRepository deviceRepository,
                                                              DeviceManagementOperationRepository deviceManagementOperationRepository,
                                                              DeviceManagementOperationFactory deviceManagementOperationFactory,
                                                              DeviceAssetStoreService deviceAssetStoreService,
                                                              @Named("maxInsertAttempts") Integer maxInsertAttempts) {
        return new DeviceAssetManagementServiceImpl(
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-device_management_operation_registry"), maxInsertAttempts),
                authorizationService,
                permissionFactory,
                deviceEventRepository,
                deviceEventFactory,
                deviceRepository,
                deviceManagementOperationRepository,
                deviceManagementOperationFactory,
                deviceAssetStoreService
        );
    }
}
