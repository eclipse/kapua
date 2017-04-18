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
package org.eclipse.kapua.service.device.call.message.kura;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.service.device.call.message.DevicePosition;

/**
 * Kura device position implementation.
 * 
 * @since 1.0
 *
 */
public class KuraPosition implements DevicePosition
{
    /**
     * Longitude of this position in degrees. This is a mandatory field.
     */
    @XmlElement(name = "longitude")
    private Double  longitude;

    /**
     * Latitude of this position in degrees. This is a mandatory field.
     */
    @XmlElement(name = "latitude")
    private Double  latitude;

    /**
     * Altitude of the position in meters.
     */
    @XmlElement(name = "altitude")
    private Double  altitude;

    /**
     * Dilution of the precision (DOP) of the current GPS fix.
     */
    @XmlElement(name = "precision")
    private Double  precision;

    /**
     * Heading (direction) of the position in degrees
     */
    @XmlElement(name = "heading")
    private Double  heading;

    /**
     * Speed for this position in meter/sec.
     */
    @XmlElement(name = "speed")
    private Double  speed;

    /**
     * Timestamp extracted from the GPS system
     */
    @XmlElement(name = "timestamp")
    private Date    timestamp;

    /**
     * Number of satellites seen by the systems
     */
    @XmlElement(name = "satellites")
    private Integer satellites;

    /**
     * Status of GPS system: 1 = no GPS response, 2 = error in response, 4 =
     * valid.
     */
    @XmlElement(name = "status")
    private Integer status;

    /**
     * Constructor
     */
    public KuraPosition()
    {
    }

    @Override
    public Double getLongitude()
    {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude()
    {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public Double getAltitude()
    {
        return altitude;
    }

    @Override
    public void setAltitude(Double altitude)
    {
        this.altitude = altitude;
    }

    @Override
    public Double getPrecision()
    {
        return precision;
    }

    @Override
    public void setPrecision(Double precision)
    {
        this.precision = precision;
    }

    @Override
    public Double getHeading()
    {
        return heading;
    }

    @Override
    public void setHeading(Double heading)
    {
        this.heading = heading;
    }

    @Override
    public Double getSpeed()
    {
        return speed;
    }

    @Override
    public void setSpeed(Double speed)
    {
        this.speed = speed;
    }

    @Override
    public Date getTimestamp()
    {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public Integer getSatellites()
    {
        return satellites;
    }

    @Override
    public void setSatellites(Integer satellites)
    {
        this.satellites = satellites;
    }

    @Override
    public Integer getStatus()
    {
        return status;
    }

    @Override
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
}
