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
package org.eclipse.kapua.service.device.management.bundle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Device bundle entity definition.<br>
 * This entity is used to get information about bundles installed in the device.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "bundle")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryClass = DeviceBundleXmlRegistry.class, factoryMethod = "newDeviceBundle")
public interface DeviceBundle {

    /**
     * Get the bundle identifier
     *
     * @return
     */
    @XmlElement(name = "id")
    public long getId();

    /**
     * Set the bundle identifier
     *
     * @param id
     */
    public void setId(long id);

    /**
     * Get the bundle name
     *
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the bundle name
     *
     * @param name
     */
    public void setName(String name);

    /**
     * Get the bundle state
     *
     * @return
     */
    @XmlElement(name = "state")
    public String getState();

    /**
     * Set the bundle state
     *
     * @param state
     */
    public void setState(String state);

    /**
     * Get the bundle version
     *
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion();

    /**
     * Set the bundle version
     *
     * @param version
     */
    public void setVersion(String version);

}
