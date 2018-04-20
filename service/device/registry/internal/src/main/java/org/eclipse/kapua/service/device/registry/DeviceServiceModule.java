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
package org.eclipse.kapua.service.device.registry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;

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
                DeviceEntityManagerFactory.instance(),
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }

}