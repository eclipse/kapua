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
package org.eclipse.kapua.service.device.management.configuration.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreService;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;

import javax.inject.Inject;

public class DeviceManagementConfigurationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceConfigurationFactory.class).to(DeviceConfigurationFactoryImpl.class);
    }

    @Provides
    @Inject
    DeviceConfigurationManagementService deviceConfigurationManagementService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceEventRepository deviceEventRepository,
            DeviceEventFactory deviceEventFactory,
            DeviceRepository deviceRepository,
            DeviceConfigurationFactory deviceConfigurationFactory,
            DeviceConfigurationStoreService deviceConfigurationStoreService,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new DeviceConfigurationManagementServiceImpl(
                jpaTxManagerFactory.create("kapua-device_management_operation_registry"),
                authorizationService,
                permissionFactory,
                deviceEventRepository,
                deviceEventFactory,
                deviceRepository,
                deviceConfigurationFactory,
                deviceConfigurationStoreService
        );
    }
}
