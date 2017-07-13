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
package org.eclipse.kapua.service.device.management.packages.model.download;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

/**
 * Device package download request definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "downloadRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "uri",
        "name",
        "version",
        "install",
        "reboot",
        "rebootDelay"
}, //
        factoryClass = DevicePackageXmlRegistry.class, //
        factoryMethod = "newDevicePackageDownloadRequest")
public interface DevicePackageDownloadRequest {

    /**
     * Get the download URI
     * 
     * @return
     */
    @XmlElement(name = "uri")
    public URI getUri();

    /**
     * Set the download URI
     * 
     * @param uri
     */
    public void setUri(URI uri);

    /**
     * Get the package name
     * 
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the package name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get the package version
     * 
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion();

    /**
     * Set the package version
     * 
     * @param version
     */
    public void setVersion(String version);

    /**
     * Get the package installed flag
     * 
     * @return
     */
    @XmlElement(name = "install")
    public Boolean getInstall();

    /**
     * Set the package installed flag
     * 
     * @param install
     */
    public void setInstall(Boolean install);

    /**
     * Get the device reboot flag
     * 
     * @return
     */
    @XmlElement(name = "reboot")
    public Boolean getReboot();

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
