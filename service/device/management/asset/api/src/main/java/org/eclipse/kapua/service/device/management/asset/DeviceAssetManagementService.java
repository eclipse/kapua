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
package org.eclipse.kapua.service.device.management.asset;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.DeviceManagementService;

/**
 * {@link DeviceAsset} {@link KapuaService} definition.
 *
 * @see org.eclipse.kapua.service.KapuaService
 * @since 1.0.0
 */
public interface DeviceAssetManagementService extends DeviceManagementService {

    /**
     * Gets the {@link DeviceAssets} for the given Device.
     *
     * @param scopeId      The target scope id
     * @param deviceId     The target {@link org.eclipse.kapua.service.device.registry.Device} id
     * @param deviceAssets The {@link DeviceAssets} to filter read channel meta-meta
     * @param timeout      The timeout waiting for the Device response
     * @return The {@link DeviceAssets} meta-data read
     * @throws KapuaException
     * @since 1.0.0
     */
    DeviceAssets get(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout) throws KapuaException;

    /**
     * Reads current values from the device channels
     *
     * @param scopeId      The target scope id
     * @param deviceId     The target device id
     * @param deviceAssets The {@link DeviceAssets} to filter read channel values
     * @param timeout      The timeout waiting for the Device response.
     * @return The {@link DeviceAssets} read from the device.
     * @throws KapuaException
     * @since 1.0.0
     */
    DeviceAssets read(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout) throws KapuaException;

    /**
     * Writes values to the device channels
     *
     * @param scopeId      The target scope id
     * @param deviceId     The target device id
     * @param deviceAssets The {@link DeviceAssets} to write
     * @param timeout      The timeout waiting for the Device response.
     * @return The {@link DeviceAssets} read after the write.
     * @throws KapuaException
     * @since 1.0.0
     */
    DeviceAssets write(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout) throws KapuaException;
}
