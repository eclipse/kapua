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
package org.eclipse.kapua.service.device.management.asset.store;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.asset.store.settings.DeviceAssetStoreSettings;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link DeviceAssetStoreService} {@link XmlRegistry}.
 *
 * @since 2.0.0
 */
@XmlRegistry
public class DeviceAssetStoreXmlFactory {

    private final DeviceAssetStoreFactory factory = KapuaLocator.getInstance().getFactory(DeviceAssetStoreFactory.class);

    /**
     * Instantiates a new {@link DeviceAssetStoreSettings}.
     *
     * @return The newly instantiated {@link DeviceAssetStoreSettings}.
     * @since 2.0.0
     */
    public DeviceAssetStoreSettings newDeviceAssetStoreSettings() {
        return factory.newDeviceAssetStoreSettings();
    }
}
