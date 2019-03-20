/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.message.KapuaPosition;

import javax.persistence.Embeddable;
import java.util.Date;

/**
 * {@link KapuaPosition} implementation.
 *
 * @since 1.0.0
 */
@Embeddable
public class KapuaPositionImpl implements KapuaPosition {

    private static final long serialVersionUID = 1L;

    private Double longitude;
    private Double latitude;
    private Double altitude;
    private Double precision;
    private Double heading;
    private Double speed;
    private Date timestamp;
    private Integer satellites;
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

}
