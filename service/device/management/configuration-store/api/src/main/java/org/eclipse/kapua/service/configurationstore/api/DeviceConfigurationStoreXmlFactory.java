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
package org.eclipse.kapua.service.configurationstore.api;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.configurationstore.config.api.DeviceConfigurationStoreConfiguration;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class DeviceConfigurationStoreXmlFactory {

    private final DeviceConfigurationStoreFactory factory = KapuaLocator.getInstance().getFactory(DeviceConfigurationStoreFactory.class);

    public DeviceConfigurationStoreConfiguration newDeviceConfigurationStoreConfiguration() {
        return factory.newDeviceConfigurationStoreConfiguration();
    }
}
