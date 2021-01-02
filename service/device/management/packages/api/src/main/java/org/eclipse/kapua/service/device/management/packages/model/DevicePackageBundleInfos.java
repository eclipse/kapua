/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Package bundle informations list container definition.
 *
 * @since 1.0
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
    List<DevicePackageBundleInfo> getBundlesInfos();
}
