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
package org.eclipse.kapua.service.device.management.channel;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Device channel entity service factory definition.
 *
 * @since 1.0
 *
 */
public interface DeviceChannelFactory extends KapuaObjectFactory {

    /**
     * Creates a new device channel list
     *
     * @return
     */
    public DeviceChannels newChannelListResult();

    /**
     * Create a new {@link DeviceChannel}
     *
     * @return
     */
    public <T> DeviceChannel<T> newDeviceChannel();
}
