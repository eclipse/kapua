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
package org.eclipse.kapua.service.device.registry.event;

import java.util.Date;

import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "deviceId",
        "sentOn",
        "receivedOn",
        "position",
        "resource",
        "action",
        "responseCode",
        "eventMessage" },
        factoryClass = DeviceEventXmlRegistry.class,
        factoryMethod = "newDeviceEvent")
public interface DeviceEvent extends KapuaEntity
{
    public static final String TYPE = "dvce-event";

    default public String getType()
    {
        return TYPE;
    }

    @XmlElement(name = "deviceId")
    public KapuaId getDeviceId();

    public void setDeviceId(KapuaId deviceId);

    @XmlElement(name = "sentOn")
    public Date getSentOn();

    public void setSentOn(Date sentOn);

    @XmlElement(name = "receivedOn")
    public Date getReceivedOn();

    public void setReceivedOn(Date receivedOn);

    @XmlElement(name = "position")
    public KapuaPosition getPosition();

    public void setPosition(KapuaPosition position);

    @XmlElement(name = "resource")
    public String getResource();

    public void setResource(String resource);

    @XmlElement(name = "action")
    public KapuaMethod getAction();

    public void setAction(KapuaMethod action);

    @XmlElement(name = "responseCode")
    public KapuaResponseCode getResponseCode();

    public void setResponseCode(KapuaResponseCode responseCode);

    @XmlElement(name = "eventMessage")
    public String getEventMessage();

    public void setEventMessage(String eventMessage);

}
