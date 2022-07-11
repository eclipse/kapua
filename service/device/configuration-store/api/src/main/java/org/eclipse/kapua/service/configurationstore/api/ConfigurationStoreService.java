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
package org.eclipse.kapua.service.configurationstore.api;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;

public interface ConfigurationStoreService extends KapuaService {

    /**
     * Store the device configuration
     *
     * @param scopeId
     * @param deviceId
     * @param deviceConfiguration
     * @throws KapuaException
     */
    void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfiguration) throws KapuaException;

    /**
     * Store the device configuration
     *
     * @param scopeId
     * @param deviceId
     * @param componentConfiguration
     * @throws KapuaException
     */
    void storeConfigurations(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration componentConfiguration) throws KapuaException;

    /**
     * Get the device configuration
     *
     * @param scopeId
     * @param deviceId
     * @return
     * @throws KapuaException
     */
    DeviceConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId) throws KapuaException;

    /**
     * Get the component configuration
     *
     * @param scopeId
     * @param deviceId
     * @param configurationComponentPid
     * @return
     * @throws KapuaException
     */
    DeviceComponentConfiguration getConfigurations(KapuaId scopeId, KapuaId deviceId, String configurationComponentPid) throws KapuaException;

}