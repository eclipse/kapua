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
package org.eclipse.kapua.service.device.management.bundle.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleFactory;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import javax.inject.Singleton;

public class DeviceManagementBundleModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(DeviceBundleFactory.class).to(DeviceBundleFactoryImpl.class);
    }

    @Provides
    @Singleton
    DeviceBundleManagementService deviceBundleManagementService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceEventService deviceEventService,
            DeviceEventFactory deviceEventFactory,
            DeviceRegistryService deviceRegistryService,
            KapuaJpaTxManagerFactory jpaTxManagerFactory) {
        return new DeviceBundleManagementServiceImpl(
                jpaTxManagerFactory.create("kapua-device_management_operation_registry"),
                authorizationService,
                permissionFactory,
                deviceEventService,
                deviceEventFactory,
                deviceRegistryService
        );
    }
}
