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
package org.eclipse.kapua.service.device.management.configuration.store.dummy;

import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreService;
import org.eclipse.kapua.service.device.management.configuration.store.settings.DeviceConfigurationStoreSettings;

import javax.inject.Singleton;
import java.util.Map;

/**
 * {@link DeviceConfigurationStoreService} dummy implementation.
 *
 * @since 2.0.0
 */
@Singleton
public class DeviceConfigurationStoreServiceDummy implements DeviceConfigurationStoreService {

    @Override
    public DeviceComponentConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId, String configurationComponentPid) {
        return null;
    }

    @Override
    public DeviceConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId) {
        return null;
    }

    @Override
    public void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration deviceComponentConfiguration) {
    }

    @Override
    public void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfiguration) {
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) {
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) {
        return null;
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) {
    }

    @Override
    public DeviceConfigurationStoreSettings getApplicationSettings(KapuaId scopeId, KapuaId deviceId) {
        return null;
    }

    @Override
    public void setApplicationSettings(KapuaId scopeId, KapuaId deviceId, DeviceConfigurationStoreSettings deviceApplicationSettings) {
    }

    @Override
    public boolean isServiceEnabled(KapuaId scopeId) {
        return false;
    }
}