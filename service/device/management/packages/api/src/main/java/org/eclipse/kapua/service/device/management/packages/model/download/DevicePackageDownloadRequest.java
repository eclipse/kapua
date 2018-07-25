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

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 * Device package download request definition.
 *
 * @since 1.0
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
    URI getUri();

    /**
     * Set the download URI
     *
     * @param uri
     */
    void setUri(URI uri);

    /**
     * Get the package name
     *
     * @return
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Set the package name
     *
     * @param name
     */
    void setName(String name);

    /**
     * Get the package version
     *
     * @return
     */
    @XmlElement(name = "version")
    String getVersion();

    /**
     * Set the package version
     *
     * @param version
     */
    void setVersion(String version);

    /**
     * Get the package installed flag
     *
     * @return
     */
    @XmlElement(name = "install")
    Boolean getInstall();

    /**
     * Set the package installed flag
     *
     * @param install
     */
    void setInstall(Boolean install);

    /**
     * Get the device reboot flag
     *
     * @return
     */
    @XmlElement(name = "reboot")
    Boolean getReboot();

    /**
     * Set the device reboot flag
     *
     * @param reboot
     */
    void setReboot(Boolean reboot);

    /**
     * Get the reboot delay
     *
     * @return
     */
    @XmlElement(name = "rebootDelay")
    Integer getRebootDelay();

    /**
     * Set the reboot delay
     *
     * @param rebootDelay
     */
    void setRebootDelay(Integer rebootDelay);
}
