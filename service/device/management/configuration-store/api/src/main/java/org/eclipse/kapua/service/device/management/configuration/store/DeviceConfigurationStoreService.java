/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.configuration.store;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.device.management.app.settings.ByDeviceAppManagementSettingsService;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.store.settings.DeviceConfigurationStoreSettings;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link DeviceConfiguration} store {@link KapuaService} definition.
 *
 * @since 2.0.0
 */
public interface DeviceConfigurationStoreService extends KapuaService, KapuaConfigurableService, ByDeviceAppManagementSettingsService<DeviceConfigurationStoreSettings> {

    /**
     * Stores the {@link DeviceConfiguration}.
     *
     * @param scopeId             The {@link Device#getScopeId()}.
     * @param deviceId            The {@link Device#getId()}.
     * @param deviceConfiguration The {@link DeviceConfiguration} to store.
     * @throws KapuaException
     * @since 2.0.0
     */
    void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfiguration) throws KapuaException;

    /**
     * Stores the {@link DeviceComponentConfiguration}
     *
     * @param scopeId                      The {@link Device#getScopeId()}.
     * @param deviceId                     The {@link Device#getId()}.
     * @param deviceComponentConfiguration The {@link DeviceComponentConfiguration} to store.
     * @throws KapuaException
     * @since 2.0.0
     */
    void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration deviceComponentConfiguration) throws KapuaException;

    /**
     * Gets the last stored {@link DeviceConfiguration}
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @return The last stored {@link DeviceConfiguration}.
     * @throws KapuaException
     * @since 2.0.0
     */
    DeviceConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId) throws KapuaException;

    /**
     * Gets the last stored {@link DeviceComponentConfiguration}.
     *
     * @param scopeId                   The {@link Device#getScopeId()}.
     * @param deviceId                  The {@link Device#getId()}.
     * @param configurationComponentPid The {@link DeviceComponentConfiguration#getId()}.
     * @return The last stored {@link DeviceComponentConfiguration}.
     * @throws KapuaException
     * @since 2.0.0
     */
    DeviceComponentConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId, String configurationComponentPid) throws KapuaException;

}