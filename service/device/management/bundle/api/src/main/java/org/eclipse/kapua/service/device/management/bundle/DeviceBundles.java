/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Device bundles list entity definition.
 *
 * @since 1.0
 */
@XmlType(propOrder = { "bundles" }, factoryClass = DeviceBundleXmlRegistry.class, factoryMethod = "newBundleListResult")
@XmlRootElement(name = "bundles")
public interface DeviceBundles extends KapuaSerializable {

    /**
     * Get the device bundles list
     *
     * @return
     */
    @XmlElement(name = "bundle")
    List<DeviceBundle> getBundles();
}
