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
package org.eclipse.kapua.service.device.management.asset;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Device asset service definition.
 *
 * @since 1.0
 *
 */
public interface DeviceAssetManagementService extends KapuaService {

    /**
     * Get the device assets list for the given device.
     *
     * @param scopeId
     *            The target scope id
     * @param deviceId
     *            The target device id
     * @param deviceAssets
     *            The {@link DeviceAssets} to filter read channel meta-meta
     * @param timeout
     *            timeout waiting for the device response
     * @return The {@link DeviceAssets} meta-data read
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    public DeviceAssets get(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout)
            throws KapuaException;

    /**
     * Reads current values from the device channels
     *
     * @param scopeId
     *            The target scope id
     * @param deviceId
     *            The target device id
     * @param deviceAssets
     *            The {@link DeviceAssets} to filter read channel values
     * @param timeout
     *            timeout waiting for the device response
     * @return The {@link DeviceAssets} read from the device.
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    public DeviceAssets read(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout)
            throws KapuaException;

    /**
     * Writes values to the device channels
     *
     * @param scopeId
     *            The target scope id
     * @param deviceId
     *            The target device id
     * @param deviceAssets
     *            The {@link DeviceAssets} to write
     * @param timeout
     *            timeout waiting for the device response
     * @return The {@link DeviceAssets} read after the write.
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    public DeviceAssets write(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets, Long timeout)
            throws KapuaException;

}
