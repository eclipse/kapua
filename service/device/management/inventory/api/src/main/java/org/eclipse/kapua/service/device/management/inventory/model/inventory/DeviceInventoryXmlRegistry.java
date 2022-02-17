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
package org.eclipse.kapua.service.device.management.inventory.model.inventory;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;

/**
 * {@link DeviceInventory} XmlFactory definition.
 *
 * @since 1.5.0
 */
public class DeviceInventoryXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceInventoryManagementFactory factory = locator.getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Instantiates a new {@link DeviceInventory}.
     *
     * @return The newly instantiated {@link DeviceInventory}
     * @since 1.5.0
     */
    public DeviceInventory newDeviceInventory() {
        return factory.newDeviceInventory();
    }

    /**
     * Instantiates a new {@link DeviceInventoryItem}.
     *
     * @return The newly instantiated {@link DeviceInventoryItem}
     * @since 1.5.0
     */
    public DeviceInventoryItem newDeviceInventoryItem() {
        return factory.newDeviceInventoryItem();
    }
}
