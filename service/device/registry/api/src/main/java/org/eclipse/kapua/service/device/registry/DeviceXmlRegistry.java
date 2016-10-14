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
package org.eclipse.kapua.service.device.registry;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class DeviceXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceFactory factory = locator.getFactory(DeviceFactory.class);
    
    public Device newDevice()
    {
        return factory.newDevice();
    }

    public DeviceCreator newDeviceCreator()
    {
        return factory.newCreator(null, null);
    }

    public DeviceListResult newDeviceListResult()
    {
        return factory.newDeviceListResult();
    }
}
