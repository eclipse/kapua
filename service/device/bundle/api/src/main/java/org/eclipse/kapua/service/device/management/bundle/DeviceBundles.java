/*******************************************************************************
 * Copyright (c) 2011, 20176 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Device bundles list entity definition.
 * 
 * @since 1.0
 * 
 */
@XmlType(propOrder = { "bundles" },
        factoryClass = DeviceBundleXmlRegistry.class,
        factoryMethod = "newBundleListResult")
@XmlRootElement(name = "bundles")
public interface DeviceBundles
{

    /**
     * Get the device bundles list
     * 
     * @return
     */
    @XmlElement(name = "bundle", namespace = "http://eurotech.com/esf/2.0")
    public List<DeviceBundle> getBundles();
}
