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
package org.eclipse.kapua.service.device.management.asset.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;

/**
 * {@link DeviceAssetFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceAssetFactoryImpl implements DeviceAssetFactory {

    @Override
    public DeviceAssets newAssetListResult() {
        return new DeviceAssetsImpl();
    }

    @Override
    public DeviceAsset newDeviceAsset() {
        return new DeviceAssetImpl();
    }

    @Override
    public DeviceAssetChannel newDeviceAssetChannel() {
        return new DeviceAssetChannelImpl();
    }
}
