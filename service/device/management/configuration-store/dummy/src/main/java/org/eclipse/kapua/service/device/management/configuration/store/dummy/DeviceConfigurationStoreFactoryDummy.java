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
package org.eclipse.kapua.service.device.management.configuration.store.dummy;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreFactory;
import org.eclipse.kapua.service.device.management.configuration.store.settings.DeviceConfigurationStoreSettings;

/**
 * {@link DeviceConfigurationStoreFactory} dummy implementation.
 *
 * @since 2.0.0
 */
@KapuaProvider
public class DeviceConfigurationStoreFactoryDummy implements DeviceConfigurationStoreFactory {

    @Override
    public DeviceConfigurationStoreSettings newDeviceConfigurationStoreSettings() {
        return null;
    }
}
