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
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.assetstore.api.AssetStoreService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;

import java.util.Map;

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

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return null;
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {

    }
}