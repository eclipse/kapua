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

import java.util.Date;

import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;

/**
 * {@link DeviceAssetChannel} implementation.
 *
 * @since 1.0.0
 */
public class DeviceAssetChannelImpl implements DeviceAssetChannel {

    private String name;
    private Class<?> clazz;
    private DeviceAssetChannelMode mode;
    private Object value;
    private String error;
    private Date timestamp;

    /**
     * Constructor
     */
    public DeviceAssetChannelImpl() {
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
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public void setType(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public DeviceAssetChannelMode getMode() {
        return mode;
    }

    @Override
    public void setMode(DeviceAssetChannelMode mode) {
        this.mode = mode;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;

        if (value != null) {
            setType(value.getClass());
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
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
