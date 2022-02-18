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
package org.eclipse.kapua.message;

import org.eclipse.kapua.commons.util.Payloads;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link KapuaPosition} definition.
 * <p>
 * {@link KapuaPosition} is a data structure to capture a geo location.
 * <p>
 * It can be associated to an {@link org.eclipse.kapua.message.KapuaPayload} to geotag an {@link org.eclipse.kapua.message.KapuaMessage} before sending to Kapua.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "position")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = MessageXmlRegistry.class, factoryMethod = "newPosition")
public interface KapuaPosition extends Position, Serializable {

    /**
     * Gets the GPS position longitude
     *
     * @return The GPS position longitude
     * @since 1.0.0
     */
    @XmlElement(name = "longitude")
    Double getLongitude();

    /**
     * Sets the GPS position longitude
     *
     * @param longitude The GPS position longitude
     * @since 1.0.0
     */
    void setLongitude(Double longitude);

    /**
     * Gets the GPS position latitude
     *
     * @return The GPS position latitude
     * @since 1.0.0
     */
    @XmlElement(name = "latitude")
    Double getLatitude();

    /**
     * Sets the GPS position latitude
     *
     * @param latitude The GPS position latitude
     * @since 1.0.0
     */
    void setLatitude(Double latitude);

    /**
     * Gets the GPS position altitude
     *
     * @return The GPS position altitude
     * @since 1.0.0
     */
    @XmlElement(name = "altitude")
    Double getAltitude();

    /**
     * Sets the GPS position altitude
     *
     * @param altitude The GPS position altitude
     * @since 1.0.0
     */
    void setAltitude(Double altitude);

    /**
     * Gets the GPS precision
     *
     * @return The GPS precision
     * @since 1.0.0
     */
    @XmlElement(name = "precision")
    Double getPrecision();

    /**
     * Sets the GPS precision
     *
     * @param precision The GPS precision
     * @since 1.0.0
     */
    void setPrecision(Double precision);

    /**
     * Gets the GPS heading
     *
     * @return The GPS heading
     * @since 1.0.0
     */
    @XmlElement(name = "heading")
    Double getHeading();

    /**
     * Sets the GPS heading
     *
     * @param heading The GPS heading
     * @since 1.0.0
     */
    void setHeading(Double heading);

    /**
     * Gets the GPS speed
     *
     * @return The GPS speed.
     * @since 1.0.0
     */
    @XmlElement(name = "speed")
    Double getSpeed();

    /**
     * Sets the GPS speed
     *
     * @param speed The GPS speed
     * @since 1.0.0
     */
    void setSpeed(Double speed);

    /**
     * Gets the timestamp
     *
     * @return The timestamp
     * @since 1.0.0
     */
    @XmlElement(name = "timestamp")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getTimestamp();

    /**
     * Sets the timestamp
     *
     * @param timestamp The timestamp
     * @since 1.0.0
     */
    void setTimestamp(Date timestamp);

    /**
     * Gets the satellites count
     *
     * @return The satellites count
     * @since 1.0.0
     */
    @XmlElement(name = "satellites")
    Integer getSatellites();

    /**
     * Sets the satellites count
     *
     * @param satellites The satellites count.
     * @since 1.0.0
     */
    void setSatellites(Integer satellites);

    /**
     * Gets the GPS status
     *
     * @return The GPS status
     * @since 1.0.0
     */
    @XmlElement(name = "status")
    Integer getStatus();

    /**
     * Sets the GPS status
     *
     * @param status The GPS status
     * @since 1.0.0
     */
    void setStatus(Integer status);

    /**
     * Converts the {@link KapuaPosition} attributes to a displayable {@link String}
     *
     * @return The displayable {@link String}
     * @since 1.0.0
     */
    default String toDisplayString() {

        Map<String, Object> properties = new HashMap<>();

        properties.put("latitude", getLatitude());
        properties.put("longitude", getLongitude());
        properties.put("altitude", getAltitude());
        properties.put("precision", getPrecision());
        properties.put("heading", getHeading());
        properties.put("speed", getSpeed());
        properties.put("timestamp", getTimestamp());
        properties.put("satellites", getSatellites());
        properties.put("status", getStatus());

        String displayString = Payloads.toDisplayString(properties);

        return displayString.isEmpty() ? null : displayString;
    }
}
