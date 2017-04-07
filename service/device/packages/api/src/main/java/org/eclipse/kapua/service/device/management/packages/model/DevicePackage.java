/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
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
package org.eclipse.kapua.service.device.management.packages.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Device package definition.
 * 
 * @since 1.0
 *
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
    public String getName();

    /**
     * Set the package name
     * 
     * @param dpName
     */
    public void setName(String dpName);

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
     * @param dpVersion
     */
    public void setVersion(String dpVersion);

    /**
     * Get device package bundle informations
     * 
     * @return
     */
    @XmlElement(name = "bundleInfos")
    public <B extends DevicePackageBundleInfos> B getBundleInfos();
    
    /**
     * Set device package bundle informations
     * 
     * @return
     */
    public <B extends DevicePackageBundleInfos> void setBundleInfos(B bundleInfos);

    /**
     * Get the installation date
     * 
     * @return
     */
    @XmlElement(name = "installDate")
    public Date getInstallDate();

    /**
     * Set the installation date
     * 
     * @param installDate
     */
    public void setInstallDate(Date installDate);
}
