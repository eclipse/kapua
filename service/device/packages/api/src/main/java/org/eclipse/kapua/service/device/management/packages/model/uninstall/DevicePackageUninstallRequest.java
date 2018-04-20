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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

/**
 * Device package uninstall request definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "uninstallRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "name",
        "version",
        "reboot",
        "rebootDelay"},
        factoryClass = DevicePackageXmlRegistry.class,
        factoryMethod = "newDevicePackageUninstallRequest")
public interface DevicePackageUninstallRequest {

    /**
     * Get package name
     * 
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set package name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get package version
     * 
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion();

    /**
     * Set package version
     * 
     * @param version
     */
    public void setVersion(String version);

    /**
     * Get the device reboot flag
     * 
     * @return
     */
    @XmlElement(name = "reboot")
    public Boolean isReboot();

    /**
     * Set the device reboot flag
     * 
     * @param reboot
     */
    public void setReboot(Boolean reboot);

    /**
     * Get the reboot delay
     * 
     * @return
     */
    @XmlElement(name = "rebootDelay")
    public Integer getRebootDelay();

    /**
     * Set the reboot delay
     * 
     * @param rebootDelay
     */
    public void setRebootDelay(Integer rebootDelay);
}
