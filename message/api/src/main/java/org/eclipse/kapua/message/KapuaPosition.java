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
package org.eclipse.kapua.message;

import java.util.Date;

public interface KapuaPosition extends Position
{
    public Double getLongitude();

    public void setLongitude(Double longitude);

    public Double getLatitude();

    public void setLatitude(Double latitude);

    public Double getAltitude();

    public void setAltitude(Double altitude);

    public Double getPrecision();

    public void setPrecision(Double precision);

    public Double getHeading();

    public void setHeading(Double heading);

    public Double getSpeed();

    public void setSpeed(Double speed);

    public Date getTimestamp();

    public void setTimestamp(Date timestamp);

    public Integer getSatellites();

    public void setSatellites(Integer satellites);

    public Integer getStatus();

    public void setStatus(Integer status);

    public String toDisplayString();
}
