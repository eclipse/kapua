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
package org.eclipse.kapua.service.configurationstore.dummy;

import org.eclipse.kapua.service.configurationstore.api.DeviceConfigurationStoreFactory;
import org.eclipse.kapua.service.configurationstore.config.api.DeviceConfigurationStoreConfiguration;

import javax.inject.Singleton;

@Singleton
public class DeviceConfigurationStoreFactoryDummy implements DeviceConfigurationStoreFactory {

    @Override
    public DeviceConfigurationStoreConfiguration newDeviceConfigurationStoreConfiguration() {
        return null;
    }
}
