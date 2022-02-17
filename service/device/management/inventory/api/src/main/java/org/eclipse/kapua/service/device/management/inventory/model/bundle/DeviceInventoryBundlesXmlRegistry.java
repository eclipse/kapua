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
package org.eclipse.kapua.service.device.management.inventory.model.bundle;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;

/**
 * {@link DeviceInventoryBundles} XmlFactory definition.
 *
 * @since 1.5.0
 */
public class DeviceInventoryBundlesXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceInventoryManagementFactory factory = locator.getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Instantiates a new {@link DeviceInventoryBundles}.
     *
     * @return The newly instantiated {@link DeviceInventoryBundles}
     * @since 1.5.0
     */
    public DeviceInventoryBundles newDeviceInventoryBundles() {
        return factory.newDeviceInventoryBundles();
    }

    /**
     * Instantiates a new {@link DeviceInventoryBundle}.
     *
     * @return The newly instantiated {@link DeviceInventoryBundle}
     * @since 1.5.0
     */
    public DeviceInventoryBundle newDeviceInventoryBundle() {
        return factory.newDeviceInventoryBundle();
    }

}