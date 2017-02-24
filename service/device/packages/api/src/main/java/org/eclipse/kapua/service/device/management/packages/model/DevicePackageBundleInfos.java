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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model;

import java.util.List;

import javax.xml.bind.annotation.*;

/**
 * Package bundle informations list container definition.
 * 
 * @since 1.0
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageBundleInfos")
public interface DevicePackageBundleInfos {

    /**
     * Get the device package bundle informations
     * 
     * @return
     */
    @XmlElement(name = "bundleInfo")
    public List<DevicePackageBundleInfo> getBundlesInfos();
}
