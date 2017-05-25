/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device bundle xml factory class
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class DeviceConfigurationXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConfigurationFactory factory = locator.getFactory(DeviceConfigurationFactory.class);

    /**
     * Creates a new device configuration
     * 
     * @return
     */
    public DeviceConfiguration newConfiguration() {
        return factory.newConfigurationInstance();
    }

    /**
     * Creates a new device component configuration
     * 
     * @return
     */
    public DeviceComponentConfiguration newComponentConfiguration() {
        return factory.newComponentConfigurationInstance(null);
    }
}
