/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link Device} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceXmlRegistry {

    private final DeviceFactory deviceFactory = KapuaLocator.getInstance().getFactory(DeviceFactory.class);

    /**
     * Creates a new {@link Device}
     *
     * @return
     * @since 1.0.0
     */
    public Device newDevice() {
        return deviceFactory.newEntity(null);
    }

    /**
     * Creates a new device creator
     *
     * @return
     * @since 1.0.0
     */
    public DeviceCreator newDeviceCreator() {
        return deviceFactory.newCreator(null, null);
    }

    /**
     * Creates a new device list result
     *
     * @return
     * @since 1.0.0
     */
    public DeviceListResult newDeviceListResult() {
        return deviceFactory.newListResult();
    }

    public DeviceQuery newQuery() {
        return deviceFactory.newQuery(null);
    }

    /**
     * Instantiates a new {@link DeviceExtendedProperty}.
     *
     * @return The newly instantiated {@link DeviceExtendedProperty}.
     * @since 1.5.0
     */
    public DeviceExtendedProperty newDeviceExtendedProperty() {
        return deviceFactory.newExtendedProperty(null, null, null);
    }
}
