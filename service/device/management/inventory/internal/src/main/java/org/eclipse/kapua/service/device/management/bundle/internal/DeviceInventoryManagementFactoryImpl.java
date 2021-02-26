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
import org.eclipse.kapua.service.device.management.bundle.model.internal.DeviceInventoryImpl;
import org.eclipse.kapua.service.device.management.bundle.model.internal.DeviceInventoryPackageImpl;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventoryPackage;

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
}
