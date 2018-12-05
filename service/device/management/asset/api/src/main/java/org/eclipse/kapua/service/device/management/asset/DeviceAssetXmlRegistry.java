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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceAsset} XML factory class
 *
 * @since 1.0.0
 */
@XmlRegistry
public class DeviceAssetXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceAssetFactory DEVICE_ASSET_FACTORY = LOCATOR.getFactory(DeviceAssetFactory.class);

    /**
     * Instantiate a new {@link DeviceAssets}.
     *
     * @return The newly instantiate {@link DeviceAssets}.
     * @since 1.0.0
     */
    public DeviceAssets newAssetListResult() {
        return DEVICE_ASSET_FACTORY.newAssetListResult();
    }

    /**
     * Instantiate a new {@link DeviceAsset}.
     *
     * @return The newly instantiated {@link DeviceAsset}.
     * @since 1.0.0
     */
    public DeviceAsset newDeviceAsset() {
        return DEVICE_ASSET_FACTORY.newDeviceAsset();
    }

    /**
     * Instantiate a new {@link DeviceAssetChannel}.
     *
     * @return The newly instantiated {@link DeviceAssetChannel}.
     * @since 1.0.0
     */
    public DeviceAssetChannel newDeviceAssetChannel() {
        return DEVICE_ASSET_FACTORY.newDeviceAssetChannel();
    }
}
