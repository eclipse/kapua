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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.List;

/**
 * Device packages list container definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "devicePackages")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackages")
public interface DevicePackages extends KapuaSerializable {

    /**
     * Get the device package list
     * 
     * @return
     */
    @XmlElement(name = "devicePackage")
    public List<DevicePackage> getPackages();
}
