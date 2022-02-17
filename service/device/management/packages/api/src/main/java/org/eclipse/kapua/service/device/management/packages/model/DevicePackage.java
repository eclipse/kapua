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
package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * {@link DevicePackage} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "devicePackage")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackage")
public interface DevicePackage {

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the version.
     *
     * @return The version.
     * @since 1.0.0
     */
    @XmlElement(name = "version")
    String getVersion();

    /**
     * Sets the version.
     *
     * @param version The version.
     * @since 1.0.0
     */
    void setVersion(String version);

    /**
     * Gets the {@link DevicePackageBundleInfos}.
     *
     * @return The {@link DevicePackageBundleInfos}.
     * @since 1.0.0
     */
    @XmlElement(name = "bundleInfos")
    DevicePackageBundleInfos getBundleInfos();

    /**
     * Sets the {@link DevicePackageBundleInfos}.
     *
     * @param bundleInfos The {@link DevicePackageBundleInfos}.
     * @since 1.0.0
     */
    void setBundleInfos(DevicePackageBundleInfos bundleInfos);

    /**
     * Gets the installation date.
     *
     * @return The installation date.
     * @since 1.0.0
     */
    @XmlElement(name = "installDate")
    Date getInstallDate();

    /**
     * Sets the installation date.
     *
     * @param installDate The installation date.
     * @since 1.0.0
     */
    void setInstallDate(Date installDate);
}
