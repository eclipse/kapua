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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.channel.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.management.channel.DeviceChannel;
import org.eclipse.kapua.service.device.management.channel.DeviceChannels;

/**
 * Device channels list entity implementation.
 *
 * @since 1.0
 *
 */
public class DeviceChannelsImpl implements DeviceChannels {

    public List<DeviceChannel<?>> channels;

    @Override
    public List<DeviceChannel<?>> getChannels() {
        if (channels == null) {
            channels = new ArrayList<>();
        }

        return channels;
    }
}
