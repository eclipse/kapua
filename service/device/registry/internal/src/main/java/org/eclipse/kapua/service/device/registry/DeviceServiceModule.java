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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModuleTransactionalConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventTransactionalModule;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

public class DeviceServiceModule extends ServiceEventTransactionalModule {

    @Inject
    private DeviceConnectionService deviceConnectionService;
    @Inject
    private DeviceRegistryService deviceRegistryService;
    @Inject
    private KapuaJpaRepositoryConfiguration jpaRepoConfig;
    @Inject
    @Named("maxInsertAttempts")
    private Integer maxInsertAttempts;

    @Override
    protected ServiceEventModuleTransactionalConfiguration initializeConfiguration() {
        KapuaDeviceRegistrySettings kapuaDeviceRegistrySettings = KapuaDeviceRegistrySettings.getInstance();

        List<ServiceEventClientConfiguration> serviceEventListenerConfigurations = new ArrayList<>();
        serviceEventListenerConfigurations.addAll(ServiceInspector.getEventBusClients(deviceRegistryService, DeviceRegistryService.class));
        serviceEventListenerConfigurations.addAll(ServiceInspector.getEventBusClients(deviceConnectionService, DeviceConnectionService.class));

        return new ServiceEventModuleTransactionalConfiguration(
                kapuaDeviceRegistrySettings.getString(KapuaDeviceRegistrySettingKeys.DEVICE_EVENT_ADDRESS),
                new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-device"),
                serviceEventListenerConfigurations.toArray(new ServiceEventClientConfiguration[0]),
                jpaRepoConfig
        );
    }

}
