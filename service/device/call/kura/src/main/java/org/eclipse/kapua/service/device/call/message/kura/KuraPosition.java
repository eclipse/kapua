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
package org.eclipse.kapua.service.device.call.message.kura;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.service.device.call.message.DevicePosition;

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

    public KuraPosition()
    {
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getAltitude()
    {
        return altitude;
    }

    public void setAltitude(Double altitude)
    {
        this.altitude = altitude;
    }

    public Double getPrecision()
    {
        return precision;
    }

    public void setPrecision(Double precision)
    {
        this.precision = precision;
    }

    public Double getHeading()
    {
        return heading;
    }

    public void setHeading(Double heading)
    {
        this.heading = heading;
    }

    public Double getSpeed()
    {
        return speed;
    }

    public void setSpeed(Double speed)
    {
        this.speed = speed;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public Integer getSatellites()
    {
        return satellites;
    }

    public void setSatellites(Integer satellites)
    {
        this.satellites = satellites;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }
}
