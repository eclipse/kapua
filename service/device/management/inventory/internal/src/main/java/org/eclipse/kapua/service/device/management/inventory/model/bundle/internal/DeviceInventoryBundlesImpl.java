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
package org.eclipse.kapua.service.device.management.inventory.model.bundle.internal;

import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundles;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceInventoryBundles} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventoryBundlesImpl implements DeviceInventoryBundles {

    private static final long serialVersionUID = 5229149965375932561L;

    private List<DeviceInventoryBundle> inventoryBundles;

    @Override
    public List<DeviceInventoryBundle> getInventoryBundles() {
        if (inventoryBundles == null) {
            inventoryBundles = new ArrayList<>();
        }

        return inventoryBundles;
    }

    @Override
    public void addInventoryBundle(DeviceInventoryBundle inventoryBundle) {
        getInventoryBundles().add(inventoryBundle);
    }

    @Override
    public void setInventoryBundles(List<DeviceInventoryBundle> inventoryBundles) {
        this.inventoryBundles = inventoryBundles;
    }
}
