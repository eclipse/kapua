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
package org.eclipse.kapua.service.datastore.internal.model;

import java.util.Date;

import org.eclipse.kapua.service.datastore.model.Position;

public class PositionImpl implements Position
{
    private double longitude;
    private double latitude;
    private double altitude;
    private double precision;
    private double heading;
    private double speed;
    private Date   timestamp;
    private int    satellites;
    private int    status;

    public PositionImpl()
    {
    }

    @Override
    public Double getLongitude()
    {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude()
    {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public Double getAltitude()
    {
        return altitude;
    }

    @Override
    public void setAltitude(double altitude)
    {
        this.altitude = altitude;
    }

    @Override
    public Double getPrecision()
    {
        return precision;
    }

    @Override
    public void setPrecision(double precision)
    {
        this.precision = precision;
    }

    @Override
    public Double getHeading()
    {
        return heading;
    }

    @Override
    public void setHeading(double heading)
    {
        this.heading = heading;
    }

    @Override
    public Double getSpeed()
    {
        return speed;
    }

    @Override
    public void setSpeed(double speed)
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
    public void setSatellites(int satellites)
    {
        this.satellites = satellites;
    }

    @Override
    public Integer getStatus()
    {
        return status;
    }

    @Override
    public void setStatus(int status)
    {
        this.status = status;
    }
}
