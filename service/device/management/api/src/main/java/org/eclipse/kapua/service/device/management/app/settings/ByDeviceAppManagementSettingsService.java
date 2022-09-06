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
package org.eclipse.kapua.service.device.management.app.settings;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link ByDeviceAppManagementSettingsService} definition.
 * <p>
 * To be used when a Device Management Application has by-device settings.
 *
 * @param <C> The {@link ByDeviceAppManagementSettings} type.
 * @since 2.0.0
 */
public interface ByDeviceAppManagementSettingsService<C extends ByDeviceAppManagementSettings> {

    /**
     * Gets the {@link ByDeviceAppManagementSettings} for the given {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}
     * @return The {@link ByDeviceAppManagementSettings} of the {@link Device}.
     * @throws KapuaException
     * @since 2.0.0
     */
    C getApplicationSettings(KapuaId scopeId, KapuaId deviceId) throws KapuaException;

    /**
     * Sets the {@link ByDeviceAppManagementSettings} for the given {@link Device}.
     *
     * @param scopeId                   The {@link Device#getScopeId()}.
     * @param deviceId                  The {@link Device#getId()}
     * @param deviceApplicationSettings The {@link ByDeviceAppManagementSettings} of the {@link Device}.
     * @throws KapuaException
     * @since 2.0.0
     */
    void setApplicationSettings(KapuaId scopeId, KapuaId deviceId, C deviceApplicationSettings) throws KapuaException;
}
