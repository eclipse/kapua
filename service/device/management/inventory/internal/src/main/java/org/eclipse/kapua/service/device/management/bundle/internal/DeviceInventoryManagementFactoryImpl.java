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
package org.eclipse.kapua.service.device.management.bundle.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.bundle.model.bundle.internal.internal.DeviceInventoryBundleImpl;
import org.eclipse.kapua.service.device.management.bundle.model.bundle.internal.internal.DeviceInventoryBundlesImpl;
import org.eclipse.kapua.service.device.management.bundle.model.inventory.internal.DeviceInventoryImpl;
import org.eclipse.kapua.service.device.management.bundle.model.inventory.internal.DeviceInventoryPackageImpl;
import org.eclipse.kapua.service.device.management.bundle.model.inventory.system.internal.DeviceInventorySystemPackageImpl;
import org.eclipse.kapua.service.device.management.bundle.model.inventory.system.internal.DeviceInventorySystemPackagesImpl;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackages;

/**
 * {@link DeviceInventoryManagementFactory} implementation.
 *
 * @since 1.5.0
 */
@KapuaProvider
public class DeviceInventoryManagementFactoryImpl implements DeviceInventoryManagementFactory {

    @Override
    public DeviceInventory newDeviceInventory() {
        return new DeviceInventoryImpl();
    }

    @Override
    public DeviceInventoryPackage newDeviceInventoryPackage() {
        return new DeviceInventoryPackageImpl();
    }

    @Override
    public DeviceInventoryBundles newDeviceInventoryBundles() {
        return new DeviceInventoryBundlesImpl();
    }

    @Override
    public DeviceInventoryBundle newDeviceInventoryBundle() {
        return new DeviceInventoryBundleImpl();
    }

    @Override
    public DeviceInventorySystemPackages newDeviceInventorySystemPackages() {
        return new DeviceInventorySystemPackagesImpl();
    }

    @Override
    public DeviceInventorySystemPackage newDeviceInventorySystemPackage() {
        return new DeviceInventorySystemPackageImpl();
    }
}
