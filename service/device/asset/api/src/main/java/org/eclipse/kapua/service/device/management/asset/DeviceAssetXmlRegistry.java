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

/**
 * Device asset xml factory class
 *
 * @since 1.0
 *
 */
public class DeviceAssetXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();

    private final DeviceAssetFactory factory = locator.getFactory(DeviceAssetFactory.class);

    /**
     * Creates a new device assets list
     *
     * @return
     */
    public DeviceAssets newAssetListResult() {
        return factory.newAssetListResult();
    }

    /**
     * Creates a new device asset
     *
     * @return
     */
    public DeviceAsset newDeviceAsset() {
        return factory.newDeviceAsset();
    }
}
