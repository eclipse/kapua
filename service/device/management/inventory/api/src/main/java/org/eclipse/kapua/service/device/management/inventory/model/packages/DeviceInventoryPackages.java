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
package org.eclipse.kapua.service.device.management.inventory.model.packages;

import org.eclipse.kapua.KapuaSerializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DeviceInventoryPackages} definition.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceInventoryDeploymentPackages")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceInventoryPackagesXmlRegistry.class, factoryMethod = "newDeviceInventoryPackages")
public interface DeviceInventoryPackages extends KapuaSerializable {

    /**
     * Gets the {@link List} of {@link DeviceInventoryPackage}s.
     *
     * @return The {@link List} of {@link DeviceInventoryPackage}s.
     * @since 1.5.0
     */
    @XmlElement(name = "deploymentPackages")
    List<DeviceInventoryPackage> getPackages();

    /**
     * Adds a {@link DeviceInventoryPackage} to the {@link List}.
     *
     * @param aPackage The {@link DeviceInventoryPackage} to add.
     * @since 1.5.0
     */
    void addPackage(DeviceInventoryPackage aPackage);

    /**
     * Sets the {@link List} of {@link DeviceInventoryPackage}s.
     *
     * @param inventoryPackages The {@link List} of {@link DeviceInventoryPackage}s.
     * @since 1.5.0
     */
    void setPackages(List<DeviceInventoryPackage> inventoryPackages);
}
