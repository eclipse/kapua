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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.device.management.app.settings.ByDeviceAppManagementSettingsService;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.asset.store.settings.DeviceAssetStoreSettings;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link DeviceAsset} store {@link KapuaService} definition.
 *
 * @since 2.0.0
 */
public interface DeviceAssetStoreService extends KapuaService, KapuaConfigurableService, ByDeviceAppManagementSettingsService<DeviceAssetStoreSettings> {

    /**
     * Stores the {@link DeviceAssets} values from a {@link KapuaDataMessage}.
     *
     * @param message The {@link KapuaDataMessage} from which extract data.
     * @throws KapuaException
     * @since 2.0.0
     */
    void storeAssetValues(KapuaDataMessage message) throws KapuaException;

    /**
     * Stores the {@link DeviceAsset} values.
     *
     * @param scopeId      The {@link Device#getScopeId()}.
     * @param deviceId     The {@link Device#getId()}
     * @param deviceAssets The {@link DeviceAssets} to store.
     * @throws KapuaException
     * @since 2.0.0
     */
    void storeAssetsValues(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets) throws KapuaException;

    /**
     * Gets the last {@link DeviceAssets} values stored.
     *
     * @param scopeId      The {@link Device#getScopeId()}.
     * @param deviceId     The {@link Device#getId()}
     * @param deviceAssets The {@link DeviceAssets} to filter results.
     * @return The last stored {@link DeviceAssets}.
     * @throws KapuaException
     * @since 2.0.0
     */
    DeviceAssets getAssetsValues(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets) throws KapuaException;

    /**
     * Stores the {@link DeviceAssets} definition.
     *
     * @param scopeId      The {@link Device#getScopeId()}.
     * @param deviceId     The {@link Device#getId()}.
     * @param deviceAssets The {@link DeviceAssets} to store.
     * @throws KapuaException
     * @since 2.0.0
     */
    void storeAssets(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets) throws KapuaException;

    /**
     * Gets the last stored {@link DeviceAssets} definition.
     *
     * @param scopeId      The {@link Device#getScopeId()}.
     * @param deviceId     The {@link Device#getId()}
     * @param deviceAssets The {@link DeviceAssets} to filter results.
     * @return The last stored {@link DeviceAssets}.
     * @throws KapuaException
     * @since 2.0.0
     */
    DeviceAssets getAssets(KapuaId scopeId, KapuaId deviceId, DeviceAssets deviceAssets) throws KapuaException;

}