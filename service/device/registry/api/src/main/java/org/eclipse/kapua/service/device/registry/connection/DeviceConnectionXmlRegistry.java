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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceConnection} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConnectionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceConnectionFactory DEVICE_CONNECTION_FACTORY = LOCATOR.getFactory(DeviceConnectionFactory.class);

    /**
     * Creates a new {@link DeviceConnection}
     *
     * @return
     */
    public DeviceConnection newDeviceConnection() {
        return DEVICE_CONNECTION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new device list result
     *
     * @return
     */
    public DeviceConnectionListResult newDeviceConnectionListResult() {
        return DEVICE_CONNECTION_FACTORY.newListResult();
    }

    public DeviceConnectionQuery newQuery() {
        return DEVICE_CONNECTION_FACTORY.newQuery(null);
    }
}
