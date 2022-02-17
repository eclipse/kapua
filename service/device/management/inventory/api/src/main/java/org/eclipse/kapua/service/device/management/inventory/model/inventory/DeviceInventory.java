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
package org.eclipse.kapua.service.device.management.inventory.model.inventory;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceInventory} definition.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceInventory")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceInventoryXmlRegistry.class, factoryMethod = "newDeviceInventory")
public interface DeviceInventory extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceInventoryItem}s
     *
     * @return The {@link List} of {@link DeviceInventoryItem}s
     * @since 1.5.0
     */
    @XmlElement(name = "inventoryItems")
    List<DeviceInventoryItem> getInventoryItems();

    /**
     * Adds a {@link DeviceInventoryItem} to the {@link List}
     *
     * @param inventoryItem The {@link DeviceInventoryItem} to add.
     * @since 1.5.0
     */
    void addInventoryItem(DeviceInventoryItem inventoryItem);

    /**
     * Sets the {@link List} of {@link DeviceInventoryItem}s
     *
     * @param inventoryItems The {@link List} of {@link DeviceInventoryItem}s
     * @since 1.5.0
     */
    void setInventoryItems(List<DeviceInventoryItem> inventoryItems);
}
