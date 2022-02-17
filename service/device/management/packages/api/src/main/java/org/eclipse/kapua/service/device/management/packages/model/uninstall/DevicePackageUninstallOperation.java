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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DevicePackageUninstallOperation} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "devicePackageUninstallOperation")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageUninstallOperation")
public interface DevicePackageUninstallOperation {

    /**
     * Get the package identifier
     *
     * @return
     */
    @XmlElement(name = "id")
    KapuaId getId();

    /**
     * Set the package identifier
     *
     * @param id
     */
    void setId(KapuaId id);

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
     * @param packageName
     */
    void setName(String packageName);

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
     * @param version
     */
    void setVersion(String version);

    /**
     * Get the package uninstall status
     *
     * @return
     */
    @XmlElement(name = "status")
    DevicePackageUninstallStatus getStatus();

    /**
     * Set the package uninstall status
     *
     * @param status
     */
    void setStatus(DevicePackageUninstallStatus status);
}
