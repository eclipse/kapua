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

import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceAssets} implementation.
 *
 * @since 1.0.0
 */
public class DeviceAssetsImpl implements DeviceAssets {

    private static final long serialVersionUID = -6657213220333406876L;

    private List<DeviceAsset> assets;

    @Override
    public List<DeviceAsset> getAssets() {
        if (assets == null) {
            assets = new ArrayList<>();
        }

        return assets;
    }

    @Override
    public void setAssets(List<DeviceAsset> assets) {
        this.assets = assets;
    }
}
