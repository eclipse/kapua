/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device event xml factory class.
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class DeviceEventXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceEventFactory factory = locator.getFactory(DeviceEventFactory.class);

    /**
     * Creates a new device event
     * 
     * @return
     */
    public DeviceEvent newDeviceEvent() {
        return factory.newDeviceEvent();
    }

    /**
     * Creates a new device event list result
     * 
     * @return
     */
    public DeviceEventListResult newDeviceEventListResult() {
        return factory.newDeviceEventListResult();
    }

    public DeviceEventQuery newQuery() {
        return factory.newQuery(null);
    }
}
