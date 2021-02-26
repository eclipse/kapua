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
package org.eclipse.kapua.service.device.management.inventory;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventoryPackage;

/**
 * {@link DeviceInventoryPackage} {@link KapuaObjectFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaObjectFactory
 * @since 1.5.0
 */
public interface DeviceInventoryManagementFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link DeviceInventory}.
     *
     * @return The newly instantiated {@link DeviceInventory}
     * @since 1.5.0
     */
    DeviceInventory newDeviceInventory();

    /**
     * Instantiates a new {@link DeviceInventoryPackage}.
     *
     * @return The newly instantiated {@link DeviceInventoryPackage}
     * @since 1.5.0
     */
    DeviceInventoryPackage newDeviceInventoryPackage();
}
