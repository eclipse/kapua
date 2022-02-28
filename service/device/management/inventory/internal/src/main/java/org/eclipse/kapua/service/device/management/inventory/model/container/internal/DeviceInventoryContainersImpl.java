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
package org.eclipse.kapua.service.device.management.inventory.model.container.internal;

import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainer;
import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainers;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceInventoryContainers} implementation.
 *
 * @since 2.0.0
 */
public class DeviceInventoryContainersImpl implements DeviceInventoryContainers {

    private static final long serialVersionUID = 5229149965375932561L;

    private List<DeviceInventoryContainer> inventoryContainers;

    @Override
    public List<DeviceInventoryContainer> getInventoryContainers() {
        if (inventoryContainers == null) {
            inventoryContainers = new ArrayList<>();
        }

        return inventoryContainers;
    }

    @Override
    public void addInventoryContainer(DeviceInventoryContainer inventoryContainer) {
        getInventoryContainers().add(inventoryContainer);
    }

    @Override
    public void setInventoryContainers(List<DeviceInventoryContainer> inventoryContainers) {
        this.inventoryContainers = inventoryContainers;
    }
}
