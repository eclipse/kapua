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
package org.eclipse.kapua.service.device.management.configuration;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceConfiguration} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConfigurationXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceConfigurationFactory DEVICE_CONFIGURATION_FACTORY = LOCATOR.getFactory(DeviceConfigurationFactory.class);

    /**
     * Creates a new device configuration
     *
     * @return
     */
    public DeviceConfiguration newConfiguration() {
        return DEVICE_CONFIGURATION_FACTORY.newConfigurationInstance();
    }

    /**
     * Creates a new device component configuration
     *
     * @return
     */
    public DeviceComponentConfiguration newComponentConfiguration() {
        return DEVICE_CONFIGURATION_FACTORY.newComponentConfigurationInstance(null);
    }
}
