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
package org.eclipse.kapua.service.device.registry.event;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;

/**
 * Device event entity definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "deviceEvent")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "deviceId",
        "sentOn",
        "receivedOn",
        "position",
        "resource",
        "action",
        "responseCode",
        "eventMessage" }, factoryClass = DeviceEventXmlRegistry.class, factoryMethod = "newDeviceEvent")
public interface DeviceEvent extends KapuaEntity {

    public static final String TYPE = "dvce-event";

    default public String getType() {
        return TYPE;
    }

    /**
     * Get the device identifier
     * 
     * @return
     */
    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getDeviceId();

    /**
     * Set the device identifier
     * 
     * @param deviceId
     */
    public void setDeviceId(KapuaId deviceId);

    /**
     * Get the sent on date
     * 
     * @return
     */
    @XmlElement(name = "sentOn")
    public Date getSentOn();

    /**
     * Set the sent on date
     * 
     * @param sentOn
     */
    public void setSentOn(Date sentOn);

    /**
     * Get the received on date
     * 
     * @return
     */
    @XmlElement(name = "receivedOn")
    public Date getReceivedOn();

    /**
     * Set the received on date
     * 
     * @param receivedOn
     */
    public void setReceivedOn(Date receivedOn);

    /**
     * Get the device position
     * 
     * @return
     */
    @XmlElement(name = "position")
    public KapuaPosition getPosition();

    /**
     * Set the device position
     * 
     * @param position
     */
    public void setPosition(KapuaPosition position);

    /**
     * Get resource
     * 
     * @return
     */
    @XmlElement(name = "resource")
    public String getResource();

    /**
     * Set resource
     * 
     * @param resource
     */
    public void setResource(String resource);

    /**
     * Get action
     * 
     * @return
     */
    @XmlElement(name = "action")
    public KapuaMethod getAction();

    /**
     * Set action
     * 
     * @param action
     */
    public void setAction(KapuaMethod action);

    /**
     * Get response code
     * 
     * @return
     */
    @XmlElement(name = "responseCode")
    public KapuaResponseCode getResponseCode();

    /**
     * Set the response code
     * 
     * @param responseCode
     */
    public void setResponseCode(KapuaResponseCode responseCode);

    /**
     * Get event message
     * 
     * @return
     */
    @XmlElement(name = "eventMessage")
    public String getEventMessage();

    /**
     * Set the event message
     * 
     * @param eventMessage
     */
    public void setEventMessage(String eventMessage);

}
