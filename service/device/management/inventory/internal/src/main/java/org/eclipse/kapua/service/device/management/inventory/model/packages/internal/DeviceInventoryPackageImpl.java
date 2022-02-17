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
package org.eclipse.kapua.service.device.management.inventory.model.packages.internal;

import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryItem;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceInventoryItem} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventoryPackageImpl implements DeviceInventoryPackage {

    private String name;
    private String version;
    private List<DeviceInventoryBundle> packageBundles;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceInventoryPackageImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public List<DeviceInventoryBundle> getPackageBundles() {
        if (packageBundles == null) {
            packageBundles = new ArrayList<>();
        }

        return packageBundles;
    }

    @Override
    public void addPackageBundle(DeviceInventoryBundle inventoryBundle) {
        getPackageBundles().add(inventoryBundle);
    }

    @Override
    public void setPackageBundles(List<DeviceInventoryBundle> packageBundles) {
        this.packageBundles = packageBundles;
    }
}
