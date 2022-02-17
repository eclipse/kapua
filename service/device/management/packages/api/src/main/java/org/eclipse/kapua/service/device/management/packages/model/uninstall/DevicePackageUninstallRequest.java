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
package org.eclipse.kapua.service.device.management.packages.model.uninstall;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Device package uninstall request definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "uninstallRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "name",
        "version",
        "reboot",
        "rebootDelay" },
        factoryClass = DevicePackageXmlRegistry.class,
        factoryMethod = "newDevicePackageUninstallRequest")
public interface DevicePackageUninstallRequest {

    /**
     * Get package name
     *
     * @return
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Set package name
     *
     * @param name
     */
    void setName(String name);

    /**
     * Get package version
     *
     * @return
     */
    @XmlElement(name = "version")
    String getVersion();

    /**
     * Set package version
     *
     * @param version
     */
    void setVersion(String version);

    /**
     * Get the device reboot flag
     *
     * @return
     */
    @XmlElement(name = "reboot")
    Boolean isReboot();

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
