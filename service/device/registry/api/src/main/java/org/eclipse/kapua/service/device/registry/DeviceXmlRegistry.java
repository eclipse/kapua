/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
}
