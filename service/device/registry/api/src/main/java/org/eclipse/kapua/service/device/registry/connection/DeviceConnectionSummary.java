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

/**
 * Device connection summary definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "connectionSummary")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "connected",
        "disconnected",
        "missing",
        "enabled",
        "disabled" }, factoryClass = DeviceConnectionXmlRegistry.class, factoryMethod = "newConnectionSummary")
public interface DeviceConnectionSummary extends KapuaSerializable {

    /**
     * Get the connected count
     *
     * @return
     */
    @XmlElement(name = "connected")
    public long getConnected();

    /**
     * Set the connected count
     *
     * @param connected
     */
    public void setConnected(long connected);

    /**
     * Get the disconnected count
     *
     * @return
     */
    @XmlElement(name = "disconnected")
    public long getDisconnected();

    /**
     * Set the disconnected count
     *
     * @param disconnected
     */
    public void setDisconnected(long disconnected);

    /**
     * Get the missing count
     *
     * @return
     */
    @XmlElement(name = "missing")
    public long getMissing();

    /**
     * Set the missing count
     *
     * @param missing
     */
    public void setMissing(long missing);

    /**
     * Get the enabled count
     *
     * @return
     */
    @XmlElement(name = "enabled")
    public long getEnabled();

    /**
     * Set the enabled count
     *
     * @param enabled
     */
    public void setEnabled(long enabled);

    /**
     * Get the disabled count
     *
     * @return
     */
    @XmlElement(name = "disabled")
    public long getDisabled();

    /**
     * Set the disabled count
     *
     * @param disabled
     */
    public void setDisabled(long disabled);
}
