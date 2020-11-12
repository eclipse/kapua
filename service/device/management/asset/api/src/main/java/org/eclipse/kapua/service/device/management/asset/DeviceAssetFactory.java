/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link DeviceAsset} entity factory definition.
 *
 * @since 1.0.0
 */
public interface DeviceAssetFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link DeviceAssets} instance.
     *
     * @return The newly instantiated {@link DeviceAssets}.
     * @since 1.0.0
     */
    DeviceAssets newAssetListResult();

    /**
     * Instantiate a new {@link DeviceAsset} instance.
     *
     * @return The newly instantiated {@link DeviceAsset}.
     * @since 1.0.0
     */
    DeviceAsset newDeviceAsset();

    /**
     * Instantiate a new {@link DeviceAssetChannel} instance.
     *
     * @return The newly instantiated {@link DeviceAssetChannel}
     * @since 1.0.0
     */
    DeviceAssetChannel newDeviceAssetChannel();
}
