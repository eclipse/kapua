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
package org.eclipse.kapua.service.device.call.message.kura;

import org.eclipse.kapua.service.device.call.message.DevicePosition;

import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * {@link DevicePosition} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraPosition implements DevicePosition {

    /**
     * Longitude of this position in degrees.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "longitude")
    private Double longitude;

    /**
     * Latitude of this position in degrees.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "latitude")
    private Double latitude;

    /**
     * Altitude of the position in meters.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "altitude")
    private Double altitude;

    /**
     * Dilution of the precision (DOP) of the current GPS fix.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "precision")
    private Double precision;

    /**
     * Heading (direction) of the position in degrees.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "heading")
    private Double heading;

    /**
     * Speed for this position in meter/sec.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "speed")
    private Double speed;

    /**
     * Timestamp extracted from the GPS system.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "timestamp")
    private Date timestamp;

    /**
     * Number of satellites seen by the systems
     *
     * @since 1.0.0
     */
    @XmlElement(name = "satellites")
    private Integer satellites;

    /**
     * Status of GPS system: 1 = no GPS response, 2 = error in response, 4 = valid.
     *
     * @since 1.0.0
     */
    @XmlElement(name = "status")
    private Integer status;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraPosition() {
        super();
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

}
