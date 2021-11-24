/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//@KapuaProvider
public class DeviceServiceModule extends ServiceEventModule {

    @Inject
    private DeviceConnectionService deviceConnectionService;
    @Inject
    private DeviceRegistryService deviceRegistryService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaDeviceRegistrySettings kds = KapuaDeviceRegistrySettings.getInstance();
        List<ServiceEventClientConfiguration> selc = new ArrayList<>();
        selc.addAll(ServiceInspector.getEventBusClients(deviceRegistryService, DeviceRegistryService.class));
        selc.addAll(ServiceInspector.getEventBusClients(deviceConnectionService, DeviceConnectionService.class));
        return new ServiceEventModuleConfiguration(
                kds.getString(KapuaDeviceRegistrySettingKeys.DEVICE_EVENT_ADDRESS),
                DeviceEntityManagerFactory.getInstance(),
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }

}
