/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementService;

/**
 * Device bundle service definition.
 *
 * @since 1.0
 */
public interface DeviceBundleManagementService extends DeviceManagementService {

    /**
     * Get the device bundles list for the given device identifier
     *
     * @param scopeId
     * @param deviceId
     * @param timeout  timeout waiting for the device response
     * @return
     * @throws KapuaException
     */
    DeviceBundles get(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    /**
     * Start the device bundle identified by the given device identifier and device bundle identifier
     *
     * @param scopeId
     * @param deviceId
     * @param bundleId
     * @param timeout  timeout waiting for the device response
     * @throws KapuaException
     */
    void start(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout) throws KapuaException;

    /**
     * Stop the device bundle identified by the given device identifier and device bundle identifier
     *
     * @param scopeId
     * @param deviceId
     * @param bundleId
     * @param timeout  timeout waiting for the device response
     * @throws KapuaException
     */
    void stop(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout) throws KapuaException;
}
