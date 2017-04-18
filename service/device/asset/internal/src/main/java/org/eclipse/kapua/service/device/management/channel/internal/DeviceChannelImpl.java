/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.channel.internal;

import org.eclipse.kapua.service.device.management.channel.ChannelMode;
import org.eclipse.kapua.service.device.management.channel.DeviceChannel;

/**
 * Device channel entity implementation.
 *
 * @since 1.0
 *
 */
public class DeviceChannelImpl<T> implements DeviceChannel<T> {

    public String name;
    public Class<T> clazz;
    public ChannelMode mode;
    public T value;

    /**
     * Constructor
     */
    public DeviceChannelImpl() {
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
    public Class<T> getType() {
        return clazz;
    }

    @Override
    public void setType(Class<T> clazz) {
        this.clazz = clazz;
        
        try {
            clazz.cast(getValue());
        }
        catch (ClassCastException cce) {
            throw new IllegalArgumentException(cce);
        }
    }

    public ChannelMode getMode() {
        return mode;
    }
    
    public void setMode(ChannelMode mode) {
        this.mode = mode;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(T value) {
        this.value = value;
        
        if (value != null) {
            setType((Class<T>) value.getClass());
        }
    }
}
