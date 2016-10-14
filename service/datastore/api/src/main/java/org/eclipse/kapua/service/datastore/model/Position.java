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
package org.eclipse.kapua.service.datastore.model;

import java.util.Date;

public interface Position
{
    public Double getLongitude();

    public void setLongitude(double longitude);

    public Double getLatitude();

    public void setLatitude(double latitude);

    public Double getAltitude();

    public void setAltitude(double altitude);

    public Double getPrecision();

    public void setPrecision(double precision);

    public Double getHeading();

    public void setHeading(double heading);

    public Double getSpeed();

    public void setSpeed(double speed);

    public Date getTimestamp();

    public void setTimestamp(Date timestamp);

    public Integer getSatellites();

    public void setSatellites(int satellites);

    public Integer getStatus();

    public void setStatus(int status);
}
