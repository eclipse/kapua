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
package org.eclipse.kapua.service.device.call.message;

import org.eclipse.kapua.message.Position;

import java.util.Date;

/**
 * Device {@link Position} definition.
 *
 * @since 1.0
 */
public interface DevicePosition extends Position {

    /**
     * Get the device position longitude
     *
     * @return
     */
    Double getLongitude();

    /**
     * Set the device position longitude
     *
     * @param longitude
     */
    void setLongitude(Double longitude);

    /**
     * Get the device position latitude
     *
     * @return
     */
    Double getLatitude();

    /**
     * Set the device position latitude
     *
     * @param latitude
     */
    void setLatitude(Double latitude);

    /**
     * Get the device position altitude
     *
     * @return
     */
    Double getAltitude();

    /**
     * Set the device position altitude
     *
     * @param altitude
     */
    void setAltitude(Double altitude);

    /**
     * Get the device precision
     *
     * @return
     */
    Double getPrecision();

    /**
     * Set the device precision
     *
     * @param precision
     */
    void setPrecision(Double precision);

    /**
     * Get the device heading
     *
     * @return
     */
    Double getHeading();

    /**
     * Set the device heading
     *
     * @param heading
     */
    void setHeading(Double heading);

    /**
     * Get the device speed
     *
     * @return
     */
    Double getSpeed();

    /**
     * Set the device speed
     *
     * @param speed
     */
    void setSpeed(Double speed);

    /**
     * Get the timestamp
     *
     * @return
     */
    Date getTimestamp();

    /**
     * Set the timestamp
     *
     * @param timestamp
     */
    void setTimestamp(Date timestamp);

    /**
     * Get the satellites count
     *
     * @return
     */
    Integer getSatellites();

    /**
     * Set the satellites count
     *
     * @param satellites
     */
    void setSatellites(Integer satellites);

    /**
     * Get the device status
     *
     * @return
     */
    Integer getStatus();

    /**
     * Set the device status
     *
     * @param status
     */
    void setStatus(Integer status);

}
