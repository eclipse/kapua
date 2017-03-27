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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device connection xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConnectionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);

    /**
     * Creates a new connection summary
     *
     * @return
     */
    public DeviceConnectionSummary newConnectionSummary() {
        return factory.newConnectionSummary();
    }

    /**
     * Creates a new {@link DeviceConnection}
     * 
     * @return
     */
    public DeviceConnection newDeviceConnection() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new device list result
     * 
     * @return
     */
    public DeviceConnectionListResult newDeviceConnectionListResult() {
        return factory.newListResult();
    }

    public DeviceConnectionQuery newQuery() {
        return factory.newQuery(null);
    }
}
