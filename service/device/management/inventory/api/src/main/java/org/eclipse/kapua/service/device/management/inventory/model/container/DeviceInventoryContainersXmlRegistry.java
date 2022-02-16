/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.model.container;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;

/**
 * {@link DeviceInventoryContainers} XmlFactory definition.
 *
 * @since 2.0.0
 */
public class DeviceInventoryContainersXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceInventoryManagementFactory factory = locator.getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Instantiates a new {@link DeviceInventoryContainers}.
     *
     * @return The newly instantiated {@link DeviceInventoryContainers}
     * @since 2.0.0
     */
    public DeviceInventoryContainers newDeviceInventoryContainers() {
        return factory.newDeviceInventoryContainers();
    }

    /**
     * Instantiates a new {@link DeviceInventoryContainer}.
     *
     * @return The newly instantiated {@link DeviceInventoryContainer}
     * @since 2.0.0
     */
    public DeviceInventoryContainer newDeviceInventoryContainer() {
        return factory.newDeviceInventoryContainer();
    }

}