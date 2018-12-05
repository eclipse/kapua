/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;

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
