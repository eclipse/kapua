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

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceFactory DEVICE_FACTORY = LOCATOR.getFactory(DeviceFactory.class);

    /**
     * Creates a new {@link Device}
     *
     * @return
     */
    public Device newDevice() {
        return DEVICE_FACTORY.newEntity(null);
    }

    /**
     * Creates a new device creator
     *
     * @return
     */
    public DeviceCreator newDeviceCreator() {
        return DEVICE_FACTORY.newCreator(null, null);
    }

    /**
     * Creates a new device list result
     *
     * @return
     */
    public DeviceListResult newDeviceListResult() {
        return DEVICE_FACTORY.newListResult();
    }

    public DeviceQuery newQuery() {
        return DEVICE_FACTORY.newQuery(null);
    }

    /**
     * Instantiates a new {@link DeviceExtendedProperty}.
     *
     * @return The newly instantiated {@link DeviceExtendedProperty}.
     * @since 1.5.0
     */
    public DeviceExtendedProperty newDeviceExtendedProperty() {
        return DEVICE_FACTORY.newExtendedProperty(null, null, null);
    }
}
