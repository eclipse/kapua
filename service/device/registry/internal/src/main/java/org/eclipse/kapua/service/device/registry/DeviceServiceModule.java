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

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventListenerConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;

@KapuaProvider
public class DeviceServiceModule extends ServiceEventModule {

    @Inject
    private DeviceConnectionService deviceConnectionService;
    @Inject
    private DeviceRegistryService deviceRegistryService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaDeviceRegistrySettings kds = KapuaDeviceRegistrySettings.getInstance();
        ServiceEventListenerConfiguration[] selc = new ServiceEventListenerConfiguration[3];
        selc[0] = new ServiceEventListenerConfiguration(
                kds.getString(KapuaDeviceRegistrySettingKeys.ACCOUNT_EVENT_ADDRESS),
                kds.getString(KapuaDeviceRegistrySettingKeys.DEVICE_REGISTRY_SUBSCRIPTION_NAME),
                (serviceEvent) -> deviceRegistryService.onKapuaEvent(serviceEvent));
        selc[1] = new ServiceEventListenerConfiguration(
                kds.getString(KapuaDeviceRegistrySettingKeys.ACCOUNT_EVENT_ADDRESS),
                kds.getString(KapuaDeviceRegistrySettingKeys.DEVICE_CONNECTION_SUBSCRIPTION_NAME),
                (serviceEvent) -> deviceConnectionService.onKapuaEvent(serviceEvent));
        selc[2] = new ServiceEventListenerConfiguration(
                kds.getString(KapuaDeviceRegistrySettingKeys.AUTHORIZATION_EVENT_ADDRESS),
                kds.getString(KapuaDeviceRegistrySettingKeys.DEVICE_REGISTRY_SUBSCRIPTION_NAME),
                (serviceEvent) -> deviceRegistryService.onKapuaEvent(serviceEvent));
        return new ServiceEventModuleConfiguration(
                kds.getString(KapuaDeviceRegistrySettingKeys.DEVICE_INTERNAL_EVENT_ADDRESS),
                kds.getList(String.class, KapuaDeviceRegistrySettingKeys.DEVICE_SERVICES_NAMES),
                DeviceEntityManagerFactory.instance(),
                selc);
    }

}