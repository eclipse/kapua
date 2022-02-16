/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.model.container;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceInventoryContainers} definition.
 *
 * @since 2.0.0
 */
@XmlRootElement(name = "deviceInventoryContainers")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceInventoryContainersXmlRegistry.class, factoryMethod = "newDeviceInventoryContainers")
public interface DeviceInventoryContainers extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceInventoryContainer}s
     *
     * @return The {@link List} of {@link DeviceInventoryContainer}s
     * @since 2.0.0
     */
    @XmlElement(name = "inventoryContainers")
    List<DeviceInventoryContainer> getInventoryContainers();

    /**
     * Adds a {@link DeviceInventoryContainer} to the {@link List}
     *
     * @param inventoryContainer The {@link DeviceInventoryContainer} to add.
     * @since 2.0.0
     */
    @XmlTransient
    void addInventoryContainer(DeviceInventoryContainer inventoryContainer);

    /**
     * Sets the {@link List} of {@link DeviceInventoryContainer}s
     *
     * @param inventoryContainers The {@link List} of {@link DeviceInventoryContainer}s
     * @since 2.0.0
     */
    void setInventoryContainers(List<DeviceInventoryContainer> inventoryContainers);
}
