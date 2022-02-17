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
package org.eclipse.kapua.service.device.registry.connection.option;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceConnectionOptionService} XML factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConnectionOptionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceConnectionOptionFactory DEVICE_CONNECTION_OPTION_FACTORY = LOCATOR.getFactory(DeviceConnectionOptionFactory.class);

    /**
     * Creates a new {@link DeviceConnectionOption}
     *
     * @return
     */
    public DeviceConnectionOption newDeviceConnectionOption() {
        return DEVICE_CONNECTION_OPTION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new device connection options list result
     *
     * @return
     */
    public DeviceConnectionOptionListResult newDeviceConnectionOptionListResult() {
        return DEVICE_CONNECTION_OPTION_FACTORY.newListResult();
    }

    public DeviceConnectionOptionQuery newQuery() {
        return DEVICE_CONNECTION_OPTION_FACTORY.newQuery(null);
    }
}
