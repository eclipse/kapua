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

import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;

import java.util.Date;

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
     * Constructor.
     *
     * @since 1.0.0
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
