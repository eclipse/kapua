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
package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Device package definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "devicePackage")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name",
        "version",
        "bundleInfos",
        "installDate" }, factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackage")
public interface DevicePackage {

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
     * @param dpName
     */
    void setName(String dpName);

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
     * @param dpVersion
     */
    void setVersion(String dpVersion);

    /**
     * Get device package bundle informations
     *
     * @return
     */
    @XmlElement(name = "bundleInfos")
    <B extends DevicePackageBundleInfos> B getBundleInfos();

    /**
     * Set device package bundle informations
     */
    <B extends DevicePackageBundleInfos> void setBundleInfos(B bundleInfos);

    /**
     * Get the installation date
     *
     * @return
     */
    @XmlElement(name = "installDate")
    Date getInstallDate();

    /**
     * Set the installation date
     *
     * @param installDate
     */
    void setInstallDate(Date installDate);
}
