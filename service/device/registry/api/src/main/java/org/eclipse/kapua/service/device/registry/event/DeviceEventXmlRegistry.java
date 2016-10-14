/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class DeviceEventXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceEventFactory factory = locator.getFactory(DeviceEventFactory.class);
    
    public DeviceEvent newDeviceEvent() {
        return factory.newDeviceEvent();
    }

    public DeviceEventListResult newDeviceListResult() { return factory.newDeviceListResult(); }
}
