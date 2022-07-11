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
package org.eclipse.kapua.service.assetstore.api;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;

public interface AssetStoreService extends KapuaService {

    /**
     * Store the asset values (with all channels related to the provided asset)
     *
     * @param message
     * @throws KapuaException
     */
    void storeAssetValues(KapuaDataMessage message) throws KapuaException;

    /**
     * Store the asset values (with all channels related to the provided asset)
     *
     * @param scopeId
     * @param deviceId
     * @param assets
     * @throws KapuaException
     */
    void storeAssetsValues(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException;

    /**
     * Get the asset values (with all channels related to the provided asset)
     *
     * @param scopeId
     * @param deviceId
     * @param assets
     * @return
     * @throws KapuaException
     */
    DeviceAssets getAssetsValues(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException;

    /**
     * Store the asset definition (with all channels related to the provided asset)
     *
     * @param scopeId
     * @param deviceId
     * @param assets
     * @throws KapuaException
     */
    void storeAssets(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException;

    /**
     * Get the asset definition (with all channels related to the provided asset)
     *
     * @param scopeId
     * @param deviceId
     * @param assets
     * @return
     * @throws KapuaException
     */
    DeviceAssets getAssets(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException;

}