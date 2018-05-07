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
package org.eclipse.kapua.service.device.registry.event;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceEvent} xml factory class.
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceEventXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceEventFactory DEVICE_EVENT_FACTORY = LOCATOR.getFactory(DeviceEventFactory.class);

    /**
     * Creates a new device event
     *
     * @return
     */
    public DeviceEvent newDeviceEvent() {
        return DEVICE_EVENT_FACTORY.newEntity(null);
    }

    /**
     * Creates a new device event list result
     *
     * @return
     */
    public DeviceEventListResult newDeviceEventListResult() {
        return DEVICE_EVENT_FACTORY.newListResult();
    }

    public DeviceEventQuery newQuery() {
        return DEVICE_EVENT_FACTORY.newQuery(null);
    }
}
