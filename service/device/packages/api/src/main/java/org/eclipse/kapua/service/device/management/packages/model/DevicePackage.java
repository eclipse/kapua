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

import org.eclipse.kapua.KapuaSerializable;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "devicePackage")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name",
        "version",
        "bundleInfos",
        "installDate" }, factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackage")
public interface DevicePackage {

    @XmlElement(name = "name")
    public String getName();

    public void setName(String dpName);

    @XmlElement(name = "version")
    public String getVersion();

    public void setVersion(String dpVersion);

    @XmlElement(name = "bundleInfos")
    public <B extends DevicePackageBundleInfos> B getBundleInfos();

    @XmlElement(name = "installDate")
    public Date getInstallDate();

    public void setInstallDate(Date installDate);
}
