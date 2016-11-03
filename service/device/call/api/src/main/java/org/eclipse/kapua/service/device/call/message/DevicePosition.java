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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message;

import java.util.Date;

import org.eclipse.kapua.message.Position;

/**
 * Device position entity definition.
 * 
 * @since 1.0
 *
 */
public interface DevicePosition extends Position
{

    /**
     * Get the device position longitude
     * 
     * @return
     */
    public Double getLongitude();

    /**
     * Set the device position longitude
     * 
     * @param longitude
     */
    public void setLongitude(Double longitude);

    /**
     * Get the device position latitude
     * 
     * @return
     */
    public Double getLatitude();

    /**
     * Set the device position latitude
     * 
     * @param latitude
     */
    public void setLatitude(Double latitude);

    /**
     * Get the device position altitude
     * 
     * @return
     */
    public Double getAltitude();

    /**
     * Set the device position altitude
     * 
     * @param altitude
     */
    public void setAltitude(Double altitude);

    /**
     * Get the device precision
     * 
     * @return
     */
    public Double getPrecision();

    /**
     * Set the device precision
     * 
     * @param precision
     */
    public void setPrecision(Double precision);

    /**
     * Get the device heading
     * 
     * @return
     */
    public Double getHeading();

    /**
     * Set the device heading
     * 
     * @param heading
     */
    public void setHeading(Double heading);

    /**
     * Get the device speed
     * 
     * @return
     */
    public Double getSpeed();

    /**
     * Set the device speed
     * 
     * @param speed
     */
    public void setSpeed(Double speed);

    /**
     * Get the timestamp
     * 
     * @return
     */
    public Date getTimestamp();

    /**
     * Set the timestamp
     * 
     * @param timestamp
     */
    public void setTimestamp(Date timestamp);

    /**
     * Get the satellites count
     * 
     * @return
     */
    public Integer getSatellites();

    /**
     * Set the satellites count
     * 
     * @param satellites
     */
    public void setSatellites(Integer satellites);

    /**
     * Get the device status
     * 
     * @return
     */
    public Integer getStatus();

    /**
     * Set the device status
     * 
     * @param status
     */
    public void setStatus(Integer status);

}
