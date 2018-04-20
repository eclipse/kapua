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
package org.eclipse.kapua.service.device.registry.connection.option;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device connection options XML factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConnectionOptionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConnectionOptionFactory factory = locator.getFactory(DeviceConnectionOptionFactory.class);

    /**
     * Creates a new {@link DeviceConnectionOption}
     * 
     * @return
     */
    public DeviceConnectionOption newDeviceConnectionOption() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new device connection options list result
     * 
     * @return
     */
    public DeviceConnectionOptionListResult newDeviceConnectionOptionListResult() {
        return factory.newListResult();
    }

    public DeviceConnectionOptionQuery newQuery() {
        return factory.newQuery(null);
    }
}
