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
 * {@link DeviceChannel} implementation.
 *
 * @since 1.0.0
 */
public class DeviceChannelImpl<T> implements DeviceChannel<T> {

    private String name;
    private Class<T> clazz;
    private ChannelMode mode;
    private T value;
    private String error;
    private Long timestamp;

    /**
     * Constructor
     */
    public DeviceChannelImpl() {
        // Required by JAXB
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
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException(cce);
        }
    }

    @Override
    public ChannelMode getMode() {
        return mode;
    }

    @Override
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

    @Override
    public String getError() {
        return error;
    }

    @Override
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Long timestamp) {
        if (timestamp != null && timestamp > 0) {
            this.timestamp = timestamp;
        }
    }
}
