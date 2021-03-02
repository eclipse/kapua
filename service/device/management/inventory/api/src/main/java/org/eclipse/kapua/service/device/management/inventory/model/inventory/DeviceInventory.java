/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.model.inventory;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceInventory} definition.
 *
 * @since 1.5.0
 */
@XmlType(factoryClass = DeviceInventoryXmlRegistry.class, factoryMethod = "newDeviceInventory")
@XmlRootElement(name = "deviceInventory")
public interface DeviceInventory extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceInventoryPackage}s
     *
     * @return The {@link List} of {@link DeviceInventoryPackage}s
     * @since 1.5.0
     */
    @XmlElement(name = "inventoryPackages")
    List<DeviceInventoryPackage> getInventoryPackages();

    /**
     * Adds a {@link DeviceInventoryPackage} to the {@link List}
     *
     * @param deviceInventoryPackage The {@link DeviceInventoryPackage} to add.
     * @since 1.5.0
     */
    void addInventoryPackage(DeviceInventoryPackage deviceInventoryPackage);

    /**
     * Sets the {@link List} of {@link DeviceInventoryPackage}s
     *
     * @param deviceInventoryPackages The {@link List} of {@link DeviceInventoryPackage}s
     * @since 1.5.0
     */
    void setInventoryPackages(List<DeviceInventoryPackage> deviceInventoryPackages);
}
