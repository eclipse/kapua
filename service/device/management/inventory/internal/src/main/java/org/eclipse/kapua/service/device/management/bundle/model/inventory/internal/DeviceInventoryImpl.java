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
package org.eclipse.kapua.service.device.management.bundle.model.inventory.internal;

import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceInventory} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventoryImpl implements DeviceInventory {

    private static final long serialVersionUID = 734716753080998855L;

    private List<DeviceInventoryPackage> inventoryPackages;

    @Override
    public List<DeviceInventoryPackage> getInventoryPackages() {
        if (inventoryPackages == null) {
            inventoryPackages = new ArrayList<>();
        }

        return inventoryPackages;
    }

    @Override
    public void addInventoryPackage(DeviceInventoryPackage inventoryPackage) {
        getInventoryPackages().add(inventoryPackage);
    }

    @Override
    public void setInventoryPackages(List<DeviceInventoryPackage> inventoryPackages) {
        this.inventoryPackages = inventoryPackages;
    }
}
