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
package org.eclipse.kapua.service.assetstore.dummy;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.assetstore.api.AssetStoreService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;

public class AssetStoreServiceDummy implements AssetStoreService {

    @Override
    public DeviceAssets getAssets(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException {
        //nothing to do
        return null;
    }

    @Override
    public DeviceAssets getAssetsValues(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException {
        //nothing to do
        return null;
    }

    @Override
    public void storeAssets(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException {
        //nothing to do
    }

    @Override
    public void storeAssetsValues(KapuaId scopeId, KapuaId deviceId, DeviceAssets assets) throws KapuaException {
        //nothing to do
    }

    @Override
    public void storeAssetValues(KapuaDataMessage message) throws KapuaException {
        //nothing to do
    }

}