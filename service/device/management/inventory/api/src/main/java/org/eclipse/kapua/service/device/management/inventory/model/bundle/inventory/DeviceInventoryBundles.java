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
package org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceInventoryBundles} definition.
 *
 * @since 1.5.0
 */
@XmlType(factoryClass = DeviceInventoryBundlesXmlRegistry.class, factoryMethod = "newDeviceInventoryBundles")
@XmlRootElement(name = "deviceInventoryBundles")
public interface DeviceInventoryBundles extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceInventoryBundle}s
     *
     * @return The {@link List} of {@link DeviceInventoryBundle}s
     * @since 1.5.0
     */
    @XmlElement(name = "inventoryBundles")
    List<DeviceInventoryBundle> getInventoryBundles();

    /**
     * Adds a {@link DeviceInventoryBundle} to the {@link List}
     *
     * @param deviceInventoryBundle The {@link DeviceInventoryBundle} to add.
     * @since 1.5.0
     */
    void addInventoryBundle(DeviceInventoryBundle deviceInventoryBundle);

    /**
     * Sets the {@link List} of {@link DeviceInventoryBundle}s
     *
     * @param deviceInventoryBundles The {@link List} of {@link DeviceInventoryBundle}s
     * @since 1.5.0
     */
    void setInventoryBundles(List<DeviceInventoryBundle> deviceInventoryBundles);
}
