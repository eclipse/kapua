/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset;

import java.util.Date;

/**
 * Device channel entity definition.<br>
 * This entity is used to get information about channels installed in the device.
 *
 * @since 1.0.0
 */
public interface DeviceAssetChannel {

    /**
     * Gets the name of the channel.
     * 
     * @return The name of the channel.
     * 
     * @since 1.0.0
     */
    public String getName();

    /**
     * Gets the name of the channel.
     * 
     * @return The name of the channel.
     * 
     * @since 1.0.0
     */
    public void setName(String name);

    /**
     * Gets the channel type.
     * This is the type returned by {@link #getValue()}.
     * 
     * @return The channel value type.
     * 
     * @since 1.0.0
     */
    public Class<?> getType();

    /**
     * Sets the channel type.
     * This type must be coherent with the value given to {@link #setValue(Object)}.
     * If not errors will occur during the interaction with the device.
     * 
     * @param type
     *            The channel type.
     * 
     * @since 1.0.0
     */
    public void setType(Class<?> type);

    /**
     * Gets the channel mode.
     * 
     * @return The channel mode.
     * 
     * @since 1.0.0
     */
    public DeviceAssetChannelMode getMode();

    /**
     * Sets the channel mode.
     * A {@link DeviceAssetChannel} can have modes available from {@link DeviceAssetChannelMode}.
     * 
     * @param deviceAssetChannelMode
     *            The channel mode to set.
     * 
     * @since 1.0.0
     */
    public void setMode(DeviceAssetChannelMode deviceAssetChannelMode);

    /**
     * Gets the value of the channel.
     * Depending on the {@link DeviceAssetChannelMode} this can be a value {@link DeviceAssetChannelMode#READ} from the channel or
     * to {@link DeviceAssetChannelMode#WRITE} into the channel.
     * This is mutually exclusive with {@link #getError()}
     * 
     * @return The value of the channel.
     * 
     * @since 1.0.0
     */
    public Object getValue();

    /**
     * Sets the value of the channel.
     * Depending on the {@link DeviceAssetChannelMode} this can be a value {@link DeviceAssetChannelMode#READ} from the channel or
     * to {@link DeviceAssetChannelMode#WRITE} into the channel.
     * 
     * @param value
     *            The value of the channel to set.
     * 
     * @since 1.0.0
     */
    public void setValue(Object value);

    /**
     * Gets the error message for this channel
     * When reading from or writing to a channel, if any error occurs it will be reported here.
     * This is mutually exclusive with {@link #getValue()}
     * 
     * @return The error message, if error has occurred.
     * 
     * @since 1.0.0
     */
    public String getError();

    /**
     * Sets the error message for this channel.
     * This must be set if error has occurred during reading from/wrtiting to
     * 
     * @param error
     *            The error message.
     */
    public void setError(String error);

    /**
     * Gets the timestamp in millisecond of the time when the value was read from the channel.
     * 
     * @return The timestamp in millisecond of the time when the value was read from the channel.
     * 
     * @since 1.0.0
     */
    public Date getTimestamp();

    /**
     * Sets timestamp in millisecond of the time when the value was read from the channel.
     * 
     * @param timestamp
     *            The timestamp in millisecond of the time when the value was read from the channel.
     * 
     * @since 1.0.0
     */
    public void setTimestamp(Date timestamp);
}
