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
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionStatus;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel.GwtDeviceAssetChannelMode;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.device.management.asset.DeviceAsset;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import java.util.ArrayList;
import java.util.List;

public class GwtKapuaDeviceModelConverter {

    private GwtKapuaDeviceModelConverter() {
    }

    public static DeviceConnectionQuery convertConnectionQuery(PagingLoadConfig loadConfig, GwtDeviceConnectionQuery gwtDeviceConnectionQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);
        DeviceConnectionQuery query = factory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtDeviceConnectionQuery.getScopeId()));
        AndPredicate predicate = new AndPredicate();

        if (gwtDeviceConnectionQuery.getClientId() != null && !gwtDeviceConnectionQuery.getClientId().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("clientId", gwtDeviceConnectionQuery.getClientId(), Operator.LIKE));
        }
        if (gwtDeviceConnectionQuery.getConnectionStatus() != null && !gwtDeviceConnectionQuery.getConnectionStatus().equals(GwtDeviceConnectionStatus.ANY.toString())) {
            predicate.and(new AttributePredicate<DeviceConnectionStatus>("status", convertConnectionStatus(gwtDeviceConnectionQuery.getConnectionStatus()), Operator.EQUAL));
        }
        if (gwtDeviceConnectionQuery.getClientIP() != null && !gwtDeviceConnectionQuery.getClientIP().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("clientIp", gwtDeviceConnectionQuery.getClientIP(), Operator.LIKE));
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
        channel.setMode(convertDeviceAssetChannel(gwtDeviceAssetChannel.getModeEnum()));
        channel.setError(gwtDeviceAssetChannel.getError());
        return channel;

    }

    public static DeviceAssetChannelMode convertDeviceAssetChannel(GwtDeviceAssetChannelMode gwtMode) {
        return DeviceAssetChannelMode.valueOf(gwtMode.toString());
    }

    public static DeviceQuery convertDeviceQuery(PagingLoadConfig loadConfig, GwtDeviceQuery gwtDeviceQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

        DeviceQuery deviceQuery = deviceFactory.newQuery(KapuaEid.parseCompactId(gwtDeviceQuery.getScopeId()));
        if (loadConfig != null) {
            deviceQuery.setLimit(loadConfig.getLimit() + 1);
            deviceQuery.setOffset(loadConfig.getOffset());
        }

        GwtDeviceQueryPredicates predicates = gwtDeviceQuery.getPredicates();
        AndPredicate andPred = new AndPredicate();

        if (predicates.getClientId() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.CLIENT_ID, predicates.getUnescapedClientId(), Operator.STARTS_WITH));
        }
        if (predicates.getDisplayName() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.DISPLAY_NAME, predicates.getUnescapedDisplayName(), Operator.STARTS_WITH));
        }
        if (predicates.getSerialNumber() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.SERIAL_NUMBER, predicates.getUnescapedSerialNumber()));
        }
        if (predicates.getDeviceStatus() != null) {
            andPred = andPred.and(new AttributePredicate<DeviceStatus>(DevicePredicates.STATUS, DeviceStatus.valueOf(predicates.getDeviceStatus())));
        }
        if (predicates.getIotFrameworkVersion() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.APPLICATION_FRAMEWORK_VERSION, predicates.getIotFrameworkVersion()));
        }
        if (predicates.getApplicationIdentifiers() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.APPLICATION_IDENTIFIERS, predicates.getApplicationIdentifiers(), Operator.LIKE));
        }
        if (predicates.getCustomAttribute1() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.CUSTOM_ATTRIBUTE_1, predicates.getCustomAttribute1()));
        }
        if (predicates.getCustomAttribute2() != null) {
            andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.CUSTOM_ATTRIBUTE_2, predicates.getCustomAttribute2()));
        }
        if (predicates.getDeviceConnectionStatus() != null) {
            andPred = andPred.and(new AttributePredicate<DeviceConnectionStatus>(DevicePredicates.CONNECTION_STATUS, DeviceConnectionStatus.valueOf(predicates.getDeviceConnectionStatus())));
        }
        if (predicates.getGroupId() != null) {
            andPred = andPred.and(new AttributePredicate<KapuaId>(DevicePredicates.GROUP_ID, KapuaEid.parseCompactId(predicates.getGroupId())));
        }

        if (predicates.getSortAttribute() != null) {
            SortOrder sortOrder = SortOrder.ASCENDING;
            if (predicates.getSortOrder().equals(SortOrder.DESCENDING.name())) {
                sortOrder = SortOrder.DESCENDING;
            }

            if (predicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.CLIENT_ID.name())) {
                deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.CLIENT_ID, sortOrder));
            } else if (predicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.DISPLAY_NAME.name())) {
                deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.DISPLAY_NAME, sortOrder));
            } else if (predicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.LAST_EVENT_ON.name())) {
                deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.LAST_EVENT_ON, sortOrder));
            }
        } else {
            deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.CLIENT_ID, SortOrder.ASCENDING));
        }

        deviceQuery.setPredicate(andPred);

        return deviceQuery;
    }
}
