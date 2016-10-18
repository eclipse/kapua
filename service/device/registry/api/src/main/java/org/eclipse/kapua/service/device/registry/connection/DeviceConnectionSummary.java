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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "connectionSummary")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "connected",
                       "disconnected",
                       "missing",
                       "enabled",
                       "disabled" }, factoryClass = DeviceConnectionXmlRegistry.class, factoryMethod = "newConnectionSummary")
public interface DeviceConnectionSummary extends KapuaSerializable
{

    @XmlElement(name = "connected")
    public long getConnected();

    public void setConnected(long connected);

    @XmlElement(name = "disconnected")
    public long getDisconnected();

    public void setDisconnected(long disconnected);

    @XmlElement(name = "missing")
    public long getMissing();

    public void setMissing(long missing);

    @XmlElement(name = "enabled")
    public long getEnabled();

    public void setEnabled(long enabled);

    @XmlElement(name = "disabled")
    public long getDisabled();

    public void setDisabled(long disabled);
}
