/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.util;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.commons.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionStatus;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel.GwtDeviceAssetChannelMode;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import java.util.ArrayList;
import java.util.List;

public class GwtKapuaDeviceModelConverter {

    private GwtKapuaDeviceModelConverter() { }

    public static DeviceConnectionQuery convertConnectionQuery(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);
        DeviceConnectionQuery query = factory.newQuery(GwtKapuaModelConverter.convertKapuaId(gwtDeviceConnectionQuery.getScopeId()));
        AndPredicate predicate = new AndPredicate();

        if (gwtDeviceConnectionQuery.getClientId() != null && !gwtDeviceConnectionQuery.getClientId().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("clientId", gwtDeviceConnectionQuery.getClientId(), Operator.LIKE));
        }

        if (gwtDeviceConnectionQuery.getConnectionStatus() != null && !gwtDeviceConnectionQuery.getConnectionStatus().equals(GwtDeviceConnectionStatus.ANY.toString())) {
            predicate.and(new AttributePredicate<DeviceConnectionStatus>("status", convertConnectionStatus(gwtDeviceConnectionQuery.getConnectionStatus()), Operator.EQUAL));
        }

        query.setPredicate(predicate);

        return query;
    }

    public static DeviceConnectionStatus convertConnectionStatus(String connectionStatus) {
        return DeviceConnectionStatus.valueOf(connectionStatus);
    }

    public static DeviceAssets convertDeviceAssets(GwtDeviceAssets deviceAssets) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAssets assets = assetFactory.newAssetListResult();
        List<DeviceAsset> assetList = new ArrayList<DeviceAsset>();
        for (GwtDeviceAsset gwtDeviceAsset : deviceAssets.getAssets()) {
            assetList.add(convertDeviceAsset(gwtDeviceAsset));
        }
        assets.setAssets(assetList);
        return assets;
    }

    public static DeviceAsset convertDeviceAsset(GwtDeviceAsset gwtDeviceAsset) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAsset deviceAsset = assetFactory.newDeviceAsset();
        deviceAsset.setName(gwtDeviceAsset.getName());
        for (GwtDeviceAssetChannel gwtDeviceAssetChannel : gwtDeviceAsset.getChannels()) {
            deviceAsset.getChannels().add(convertDeviceAssetChannel(gwtDeviceAssetChannel));
        }
        return deviceAsset;
    }

    public static DeviceAssetChannel convertDeviceAssetChannel(GwtDeviceAssetChannel gwtDeviceAssetChannel) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceAssetFactory assetFactory = locator.getFactory(DeviceAssetFactory.class);
        DeviceAssetChannel channel = assetFactory.newDeviceAssetChannel();
        channel.setName(gwtDeviceAssetChannel.getName());
        try {
            channel.setType(ObjectTypeConverter.fromString(gwtDeviceAssetChannel.getType()));
            channel.setValue(ObjectValueConverter.fromString(gwtDeviceAssetChannel.getValue(), channel.getType()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        channel.setTimestamp(gwtDeviceAssetChannel.getTimestamp());
        channel.setMode(convert(gwtDeviceAssetChannel.getModeEnum()));
        channel.setError(gwtDeviceAssetChannel.getError());
        return channel;

    }

    public static DeviceAssetChannelMode convert(GwtDeviceAssetChannelMode gwtMode) {
        return DeviceAssetChannelMode.valueOf(gwtMode.toString());
    }
}
