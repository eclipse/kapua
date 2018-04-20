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
package org.eclipse.kapua.message;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.message.xml.MessageXmlRegistry;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

/**
 * Kapua position object definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "position")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "longitude", //
        "latitude", //
        "altitude", //
        "precision", //
        "heading", //
        "speed", //
        "timestamp", //
        "satellites", //
        "status", //
}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newPosition")
public interface KapuaPosition extends Position, Serializable {

    /**
     * Get the device position longitude
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "longitude")
    public Double getLongitude();

    /**
     * Set the device position longitude
     *
     * @param longitude
     * @since 1.0.0
     */
    public void setLongitude(Double longitude);

    /**
     * Get the device position latitude
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "latitude")
    public Double getLatitude();

    /**
     * Set the device position latitude
     *
     * @param latitude
     * @since 1.0.0
     */
    public void setLatitude(Double latitude);

    /**
     * Get the device position altitude
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "altitude")
    public Double getAltitude();

    /**
     * Set the device position altitude
     *
     * @param altitude
     * @since 1.0.0
     */
    public void setAltitude(Double altitude);

    /**
     * Get the device precision
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "precision")
    public Double getPrecision();

    /**
     * Set the device precision
     *
     * @param precision
     * @since 1.0.0
     */
    public void setPrecision(Double precision);

    /**
     * Get the device heading
     *
     * @return
     */
    @XmlElement(name = "heading")
    public Double getHeading();

    /**
     * Set the device heading
     *
     * @param heading
     * @since 1.0.0
     */
    public void setHeading(Double heading);

    /**
     * Get the device speed
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "speed")
    public Double getSpeed();

    /**
     * Set the device speed
     *
     * @param speed
     * @since 1.0.0
     */
    public void setSpeed(Double speed);

    /**
     * Get the timestamp
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "timestamp")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    public Date getTimestamp();

    /**
     * Set the timestamp
     *
     * @param timestamp
     * @since 1.0.0
     */
    public void setTimestamp(Date timestamp);

    /**
     * Get the satellites count
     *
     * @return
     */
    @XmlElement(name = "satellites")
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
    @XmlElement(name = "status")
    public Integer getStatus();

    /**
     * Set the device status
     *
     * @param status
     */
    public void setStatus(Integer status);

    /**
     * Convert the position to a displayable String
     *
     * @return
     */
    public String toDisplayString();
}
