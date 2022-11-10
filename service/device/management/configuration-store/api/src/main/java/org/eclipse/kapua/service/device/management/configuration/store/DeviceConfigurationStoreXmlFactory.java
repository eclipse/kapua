/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.configuration.store;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.configuration.store.settings.DeviceConfigurationStoreSettings;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceConfigurationStoreService} {@link XmlRegistry}.
 *
 * @since 2.0.0
 */
@XmlRegistry
public class DeviceConfigurationStoreXmlFactory {

    private final DeviceConfigurationStoreFactory factory = KapuaLocator.getInstance().getFactory(DeviceConfigurationStoreFactory.class);

    /**
     * Instantiates a new {@link DeviceConfigurationStoreSettings}.
     *
     * @return The newly instantiated {@link DeviceConfigurationStoreSettings}.
     */
    public DeviceConfigurationStoreSettings newDeviceConfigurationStoreSettings() {
        return factory.newDeviceConfigurationStoreSettings();
    }
}
