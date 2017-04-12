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
package org.eclipse.kapua.message.internal;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Embeddable;

import org.eclipse.kapua.message.KapuaPosition;

/**
 * KapuaPosition is a data structure to capture a geo location.<br>
 * It can be associated to an KapuaPayload to geotag an KapuaMessage before sending to Kapua.<br>
 * Refer to the description of each of the fields for more information on the model of KapuaPosition.
 * 
 * @since 1.0
 * 
 */
@Embeddable
public class KapuaPositionImpl implements KapuaPosition {

    private static final long serialVersionUID = 1L;

    /**
     * Longitude of this position in degrees. This is a mandatory field.
     */
    private Double longitude;

    /**
     * Latitude of this position in degrees. This is a mandatory field.
     */
    private Double latitude;

    /**
     * Altitude of the position in meters.
     */
    private Double altitude;

    /**
     * Dilution of the precision (DOP) of the current GPS fix.
     */
    private Double precision;

    /**
     * Heading (direction) of the position in degrees
     */
    private Double heading;

    /**
     * Speed for this position in meter/sec.
     */
    private Double speed;

    /**
     * Timestamp extracted from the GPS system
     */
    private Date timestamp;

    /**
     * Number of satellites seen by the systems
     */
    private Integer satellites;

    /**
     * Status of GPS system: 1 = no GPS response, 2 = error in response, 4 =
     * valid.
     */
    private Integer status;

    /**
     * Constructor
     */
    public KapuaPositionImpl() {
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getAltitude() {
        return altitude;
    }

    @Override
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public Double getPrecision() {
        return precision;
    }

    @Override
    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    @Override
    public Double getHeading() {
        return heading;
    }

    @Override
    public void setHeading(Double heading) {
        this.heading = heading;
    }

    @Override
    public Double getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Integer getSatellites() {
        return satellites;
    }

    @Override
    public void setSatellites(Integer satellites) {
        this.satellites = satellites;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
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
    @Override
    public String toDisplayString() {
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
