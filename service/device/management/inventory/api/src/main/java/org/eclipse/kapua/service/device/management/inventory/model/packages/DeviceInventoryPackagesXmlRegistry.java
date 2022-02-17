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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;

/**
 * {@link DeviceInventoryPackages} XmlFactory definition.
 *
 * @since 1.5.0
 */
public class DeviceInventoryPackagesXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceInventoryManagementFactory factory = locator.getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Instantiates a new {@link DeviceInventoryPackages}.
     *
     * @return The newly instantiated {@link DeviceInventoryPackages}
     * @since 1.5.0
     */
    public DeviceInventoryPackages newDeviceInventoryPackages() {
        return factory.newDeviceInventoryPackages();
    }

    /**
     * Instantiates a new {@link DeviceInventoryPackage}.
     *
     * @return The newly instantiated {@link DeviceInventoryPackage}
     * @since 1.5.0
     */
    public DeviceInventoryPackage newDeviceInventoryPackage() {
        return factory.newDeviceInventoryPackage();
    }
}
