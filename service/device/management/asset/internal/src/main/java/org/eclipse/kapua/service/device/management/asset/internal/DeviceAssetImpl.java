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
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceAsset} implementation.
 *
 * @since 1.0.0
 */
public class DeviceAssetImpl implements DeviceAsset {

    private String name;
    private List<DeviceAssetChannel> channels;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public DeviceAssetImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<DeviceAssetChannel> getChannels() {
        if (channels == null) {
            channels = new ArrayList<>();
        }

        return channels;
    }

    @Override
    public void setChannels(List<DeviceAssetChannel> channels) {
        this.channels = channels;
    }
}
