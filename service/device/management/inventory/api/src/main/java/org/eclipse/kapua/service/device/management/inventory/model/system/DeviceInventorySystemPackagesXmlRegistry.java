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
package org.eclipse.kapua.service.device.management.inventory.model.system;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;

/**
 * {@link DeviceInventorySystemPackages} XmlFactory definition.
 *
 * @since 1.5.0
 */
public class DeviceInventorySystemPackagesXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceInventoryManagementFactory factory = locator.getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Instantiates a new {@link DeviceInventorySystemPackages}.
     *
     * @return The newly instantiated {@link DeviceInventorySystemPackages}
     * @since 1.5.0
     */
    public DeviceInventorySystemPackages newDeviceInventorySystemPackages() {
        return factory.newDeviceInventorySystemPackages();
    }

    /**
     * Instantiates a new {@link DeviceInventorySystemPackage}.
     *
     * @return The newly instantiated {@link DeviceInventorySystemPackage}
     * @since 1.5.0
     */
    public DeviceInventorySystemPackage newDeviceInventorySystemPackage() {
        return factory.newDeviceInventorySystemPackage();
    }
}
