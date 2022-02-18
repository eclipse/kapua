/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.model.system;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceInventorySystemPackages} definition.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceInventorySystemPackages")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceInventorySystemPackagesXmlRegistry.class, factoryMethod = "newDeviceInventorySystemPackages")
public interface DeviceInventorySystemPackages extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceInventorySystemPackage}s
     *
     * @return The {@link List} of {@link DeviceInventorySystemPackage}s
     * @since 1.5.0
     */
    @XmlElement(name = "systemPackages")
    List<DeviceInventorySystemPackage> getSystemPackages();

    /**
     * Adds a {@link DeviceInventorySystemPackage} to the {@link List}
     *
     * @param inventorySystemPackage The {@link DeviceInventorySystemPackage} to add.
     * @since 1.5.0
     */
    void addSystemPackage(DeviceInventorySystemPackage inventorySystemPackage);

    /**
     * Sets the {@link List} of {@link DeviceInventorySystemPackage}s
     *
     * @param inventorySystemPackages The {@link List} of {@link DeviceInventorySystemPackage}s
     * @since 1.5.0
     */
    void setSystemPackages(List<DeviceInventorySystemPackage> inventorySystemPackages);
}
