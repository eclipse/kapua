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
package org.eclipse.kapua.message.internal;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.message.KapuaPosition;

/**
 * KapuaPosition is a data structure to capture a geo location. It can be
 * associated to an KapuaPayload to geotag an KapuaMessage before sending to the
 * Everyware Cloud. Refer to the description of each of the fields for more
 * information on the model of KapuaPosition.
 */
@XmlRootElement(name = "position")
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class KapuaPositionImpl implements KapuaPosition
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

    public KapuaPositionImpl()
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

    @Override
    public String toString()
    {
        // a-la JSON
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"longitude\":");
        sb.append(longitude);
        sb.append(", ");
        sb.append("\"latitude\":");
        sb.append(latitude);
        sb.append(", ");
        sb.append("\"altitude\":");
        sb.append(altitude);
        sb.append(", ");
        sb.append("\"precision\":");
        sb.append(precision);
        sb.append(", ");
        sb.append("\"heading\":");
        sb.append(heading);
        sb.append(", ");
        sb.append("\"speed\":");
        sb.append(speed);
        sb.append(", ");
        sb.append("\"timestamp\":");
        sb.append("\"");
        sb.append(timestamp);
        sb.append("\"");
        sb.append(", ");
        sb.append("\"satellites\":");
        sb.append(satellites);
        sb.append(", ");
        sb.append("\"status\":");
        sb.append(status);
        sb.append("}");

        return sb.toString();
    }

    /**
     * FIXME: Please make me pretty!!! :'(
     */
    public String toDisplayString()
    {
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        if (latitude != null) {
            list.add(new StringBuilder("latitude").append("=").append(latitude)
                                                  .toString());
        }
        if (longitude != null) {
            list.add(new StringBuilder("longitude").append("=")
                                                   .append(longitude).toString());
        }
        if (altitude != null) {
            list.add(new StringBuilder("altitude").append("=").append(altitude)
                                                  .toString());
        }
        if (precision != null) {
            list.add(new StringBuilder("precision").append("=")
                                                   .append(precision).toString());
        }
        if (heading != null) {
            list.add(new StringBuilder("heading").append("=").append(heading)
                                                 .toString());
        }
        if (speed != null) {
            list.add(new StringBuilder("speed").append("=").append(speed)
                                               .toString());
        }
        if (timestamp != null) {
            list.add(new StringBuilder("timestamp").append("=")
                                                   .append(timestamp).toString());
        }
        if (satellites != null) {
            list.add(new StringBuilder("satellites").append("=")
                                                    .append(satellites).toString());
        }
        if (status != null) {
            list.add(new StringBuilder("status").append("=").append(status)
                                                .toString());
        }

        if (list.size() == 0) {
            return null;
        }

        sb.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append("~~").append(list.get(i));
        }

        return sb.toString();
    }
}
