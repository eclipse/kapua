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
package org.eclipse.kapua.app.console.module.data.server;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.data.client.GwtTopic;
import org.eclipse.kapua.app.console.module.data.client.util.GwtMessage;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDataChannelInfoQuery;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.util.GwtKapuaDataModelConverter;
import org.eclipse.kapua.app.console.module.data.shared.util.KapuaGwtDataModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GwtDataServiceImpl extends KapuaRemoteServiceServlet implements GwtDataService {

    private static final long serialVersionUID = -5518740923786017558L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DatastoreObjectFactory DATASTORE_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);

    @Override
    public List<GwtTopic> findTopicsTree(String scopeId) throws GwtKapuaException {
        List<GwtTopic> channelInfoList = new ArrayList<GwtTopic>();
        HashMap<String, GwtTopic> topicMap = new HashMap<String, GwtTopic>();
        ChannelInfoRegistryService channelInfoService = LOCATOR.getService(ChannelInfoRegistryService.class);
        ChannelInfoQuery query = DATASTORE_FACTORY.newChannelInfoQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        int offset = 0;
        int limit = 250;
        try {
            query.setOffset(offset);
            query.setLimit(limit);
            ChannelInfoListResult result = channelInfoService.query(query);
            while (result != null && !result.isEmpty()) {
                for (ChannelInfo channel : result.getItems()) {
                    addToMap(topicMap, channel);
                }
                offset += limit;
                query.setOffset(offset);
                result = channelInfoService.query(query);
            }

            for (Map.Entry<String, GwtTopic> entry : topicMap.entrySet()) {
                if (!entry.getKey().contains("/") || (entry.getKey().split("/").length == 2 && entry.getKey().split("/")[1].equals("#"))) {
                    channelInfoList.add(entry.getValue());
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        Collections.sort(channelInfoList, new Comparator<GwtTopic>() {

            @Override
            public int compare(GwtTopic o1, GwtTopic o2) {
                return o1.getSemanticTopic().compareTo(o2.getSemanticTopic());
            }
        });
        return channelInfoList;
    }

    @Override
    public List<GwtTopic> updateTopicTimestamps(String gwtScopeId, List<ModelData> topics) throws GwtKapuaException {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        List<GwtTopic> updatedTopics = new ArrayList<GwtTopic>();
        MessageStoreService messageStoreService = LOCATOR.getService(MessageStoreService.class);
        try {
            for (ModelData md : topics) {
                GwtTopic topic = (GwtTopic) md;

                List<SortField> sort = new ArrayList<SortField>();
                sort.add(SortField.descending(MessageSchema.MESSAGE_TIMESTAMP));

                MessageQuery messageQuery = new MessageQueryImpl(scopeId);
                messageQuery.setAskTotalCount(true);
                messageQuery.setFetchStyle(StorableFetchStyle.FIELDS);
                messageQuery.setLimit(1);
                messageQuery.setOffset(0);
                messageQuery.setSortFields(sort);

                String semanticTopic = topic.getSemanticTopic();
                StorablePredicate predicate;

                if (semanticTopic.endsWith("/#")) {
                    predicate = new ChannelMatchPredicateImpl(semanticTopic.replaceFirst("/#$", "/"));
                } else {
                    predicate = new TermPredicateImpl(MessageField.CHANNEL, semanticTopic);
                }

                messageQuery.setPredicate(predicate);

                MessageListResult messageList = messageStoreService.query(messageQuery);
                if (messageList.getFirstItem() != null) {
                    topic.setTimestamp(messageList.getFirstItem().getTimestamp());
                }
                updatedTopics.add(topic);
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return updatedTopics;
    }

    @Override
    public List<GwtDatastoreDevice> updateDeviceTimestamps(String gwtScopeId, List<GwtDatastoreDevice> devices) throws GwtKapuaException {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        ClientInfoRegistryService clientInfoRegistryService = LOCATOR.getService(ClientInfoRegistryService.class);
        List<GwtDatastoreDevice> updatedDevices = new ArrayList<GwtDatastoreDevice>();
        GwtDatastoreDevice updatedDevice = null;
        try {
            for (GwtDatastoreDevice device : devices) {
                ClientInfoQuery query = new ClientInfoQueryImpl(scopeId);
                query.setLimit(1);
                query.setOffset(0);
                query.setFetchStyle(StorableFetchStyle.FIELDS);
                query.setAskTotalCount(true);
                query.setSortFields(Collections.singletonList(SortField.descending(MessageSchema.MESSAGE_TIMESTAMP)));
                query.setFetchAttributes(Collections.singletonList(ClientInfoField.TIMESTAMP.field()));
                query.setPredicate(new TermPredicateImpl(ClientInfoField.CLIENT_ID, device.getClientId()));
                ClientInfoListResult result = clientInfoRegistryService.query(query);

                if (result.getFirstItem() != null) {
                    updatedDevice = KapuaGwtDataModelConverter.convertToDatastoreDevice(result.getFirstItem());
                    updatedDevices.add(updatedDevice);
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return updatedDevices;
    }

    private void addToMap(HashMap<String, GwtTopic> topicMap, ChannelInfo channel) {
        String[] topicParts = channel.getName().split("/");
        GwtTopic parent = null;
        String topicName = topicParts[0];
        String baseTopic = topicParts[0];
        String semanticTopic = baseTopic;
        if (topicParts.length > 1) {
            semanticTopic += "/#";
        }
        int i = 0;
        do {
            GwtTopic t = null;
            t = topicMap.get(semanticTopic);
            if (t == null) {
                t = new GwtTopic(topicName, baseTopic, semanticTopic, null);
                topicMap.put(semanticTopic, t);
                if (parent != null) {
                    parent.add(t);
                }
            }
            i++;
            parent = t;
            if (i < topicParts.length) {
                topicName = topicParts[i];
                baseTopic += "/" + topicName;
                semanticTopic = baseTopic;
                if (i < (topicParts.length - 1)) {
                    semanticTopic += "/#";
                }
            }
        } while (i < topicParts.length);

    }

    @Override
    public PagingLoadResult<GwtTopic> findTopicsList(PagingLoadConfig config, GwtDataChannelInfoQuery query) throws GwtKapuaException {
        List<GwtTopic> channelInfoList = new ArrayList<GwtTopic>();
        int totalLength = 0;

        ChannelInfoRegistryService channelInfoService = LOCATOR.getService(ChannelInfoRegistryService.class);

        ChannelInfoQuery channelInfoQuery = GwtKapuaDataModelConverter.convertChannelInfoQuery(query, config);

        try {
            ChannelInfoListResult channelInfoListResult = channelInfoService.query(channelInfoQuery);
            if (channelInfoListResult != null && !channelInfoListResult.isEmpty()) {
                for (ChannelInfo channelInfo : channelInfoListResult.getItems()) {
                    channelInfoList.add(KapuaGwtDataModelConverter.convertToTopic(channelInfo));
                }
                channelInfoQuery.setLimit(-1);
                channelInfoQuery.setOffset(0);
                totalLength = Long.valueOf(channelInfoService.count(channelInfoQuery)).intValue();
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtTopic>(channelInfoList, config.getOffset(), totalLength);
    }

    @Override
    public PagingLoadResult<GwtDatastoreDevice> findDevices(PagingLoadConfig config, String scopeId, String filter) throws GwtKapuaException {
        ClientInfoRegistryService clientInfoService = LOCATOR.getService(ClientInfoRegistryService.class);
        DeviceRegistryService deviceRegistryService = LOCATOR.getService(DeviceRegistryService.class);
        DeviceFactory deviceFactory = LOCATOR.getFactory(DeviceFactory.class);
        List<GwtDatastoreDevice> devices = new ArrayList<GwtDatastoreDevice>();
        KapuaId convertedScopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeId);
        ClientInfoQuery clientInfoQuery = DATASTORE_FACTORY.newClientInfoQuery(convertedScopeId);
        if (!Strings.isNullOrEmpty(filter)) {
            StorablePredicate predicate = new ChannelMatchPredicateImpl(ClientInfoField.CLIENT_ID.field(), filter);
            clientInfoQuery.setPredicate(predicate);
        }
        clientInfoQuery.setAskTotalCount(true);
        clientInfoQuery.setLimit(config.getLimit());
        clientInfoQuery.setOffset(config.getOffset());
        SortDirection sortDirection = config.getSortDir() == Style.SortDir.ASC ? SortDirection.ASC : SortDirection.DESC;
        String sortField = config.getSortField().equals("friendlyDevice") ? ClientInfoField.CLIENT_ID.field() : ClientInfoField.TIMESTAMP.field();
        clientInfoQuery.setSortFields(Collections.singletonList(SortField.of(sortDirection, sortField)));

        ClientInfoListResult result = null;
        try {
            result = clientInfoService.query(clientInfoQuery);
            if (result != null && !result.isEmpty()) {
                List<String> clientIds = new ArrayList<String>();
                for (ClientInfo client : result.getItems()) {
                    clientIds.add(client.getClientId());
                }
                DeviceQuery deviceQuery = deviceFactory.newQuery(convertedScopeId);
                DeviceListResult deviceListResult = deviceRegistryService.query(deviceQuery);
                Map<String, String> clientIdsMap = new HashMap<String, String>();
                for (Device device : deviceListResult.getItems()) {
                    if (clientIds.contains(device.getClientId())) {
                        clientIdsMap.put(device.getClientId(), device.getDisplayName());
                    }
                }

                for (ClientInfo client : result.getItems()) {
                    GwtDatastoreDevice gwtDatastoreDevice = KapuaGwtDataModelConverter.convertToDatastoreDevice(client);
                    String clientId = client.getClientId();
                    String displayName = clientIdsMap.get(clientId);
                    if (StringUtils.isNotEmpty(displayName)) {
                        gwtDatastoreDevice.setFriendlyDevice(displayName + " (" + clientId + ")");
                    } else {
                        gwtDatastoreDevice.setFriendlyDevice(clientId);
                    }
                    devices.add(gwtDatastoreDevice);
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtDatastoreDevice>(devices, config.getOffset(), result != null ? result.getTotalCount().intValue() : 0);
    }

    @Override
    public ListLoadResult<GwtDatastoreAsset> findAssets(LoadConfig config, String scopeId, GwtDatastoreDevice selectedDevice) throws GwtKapuaException {
        ChannelInfoRegistryService clientInfoService = LOCATOR.getService(ChannelInfoRegistryService.class);
        List<GwtDatastoreAsset> asset = new ArrayList<GwtDatastoreAsset>();
        KapuaId convertedScopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeId);
        ChannelInfoQuery query = DATASTORE_FACTORY.newChannelInfoQuery(convertedScopeId);
        query.setLimit(10000);
        try {
            ChannelInfoListResult result = clientInfoService.query(query);
            if (result != null && !result.isEmpty()) {
                for (ChannelInfo client : result.getItems()) {
                    if (client.getName().startsWith("W1/A1") && client.getClientId().contentEquals(selectedDevice.getDevice())) {
                        asset.add(KapuaGwtDataModelConverter.convertToAssets(client));
                    }
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtDatastoreAsset>(asset);
    }

    @Override
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtTopic topic) throws GwtKapuaException {
        StorablePredicate predicate;
        if (topic.getSemanticTopic().endsWith("/#")) {
            predicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(topic.getSemanticTopic().replaceFirst("/#$", "/"));
        } else {
            predicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CHANNEL, topic.getSemanticTopic());
        }
        return findHeaders(scopeId, predicate);
    }

    @Override
    public ListLoadResult<GwtHeader> findNumberHeaders(LoadConfig config, String accountName, GwtTopic topic) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtDatastoreDevice device) throws GwtKapuaException {
        TermPredicate predicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MetricInfoField.CLIENT_ID, device.getDevice());
        return findHeaders(scopeId, predicate);
    }

    @Override
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String accountName, GwtDatastoreAsset gwtDatastoreAsset) throws GwtKapuaException {
        ChannelMatchPredicate predicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(gwtDatastoreAsset.getTopick());
        return findHeaders(accountName, predicate);
    }

    @Override
    public PagingLoadResult<GwtMessage> findMessagesByTopic(PagingLoadConfig loadConfig, String scopeId, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException {
        StorablePredicate predicate;
        if (topic.getSemanticTopic().endsWith("/#")) {
            predicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(topic.getSemanticTopic().replaceFirst("/#$", "/"));
        } else {
            predicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CHANNEL, topic.getSemanticTopic());
        }

        return findMessages(loadConfig, scopeId, headers, startDate, endDate, predicate);
    }

    @Override
    public List<GwtMessage> findLastMessageByTopic(String accountName, int limit) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PagingLoadResult<GwtMessage> findMessagesByDevice(PagingLoadConfig loadConfig, String scopeId, GwtDatastoreDevice device, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException {
        TermPredicate predicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CLIENT_ID, device.getDevice());
        return findMessages(loadConfig, scopeId, headers, startDate, endDate, predicate);
    }

    @Override
    public PagingLoadResult<GwtMessage> findMessagesByAssets(PagingLoadConfig loadConfig, String scopeId, GwtDatastoreAsset asset, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException {
        ChannelMatchPredicate predicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(asset.getTopick());
        return findMessages(loadConfig, scopeId, headers, startDate, endDate, predicate);
    }

    private ListLoadResult<GwtHeader> findHeaders(String scopeId, StorablePredicate predicate) throws GwtKapuaException {
        MetricInfoRegistryService metricService = LOCATOR.getService(MetricInfoRegistryService.class);
        MetricInfoQuery query = DATASTORE_FACTORY.newMetricInfoQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        query.setLimit(10000);
        if (predicate != null) {
            query.setPredicate(predicate);
        }
        Map<String, GwtHeader> metrics = new HashMap<String, GwtHeader>();

        try {
            MetricInfoListResult result = metricService.query(query);
            if (result != null && !result.isEmpty()) {
                for (MetricInfo metric : result.getItems()) {
                    metrics.put(metric.getName(), KapuaGwtDataModelConverter.convertToHeader(metric));
                }
            }

        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtHeader>(new ArrayList<GwtHeader>(metrics.values()));
    }

    private PagingLoadResult<GwtMessage> findMessages(PagingLoadConfig loadConfig, String scopeId, List<GwtHeader> headers, Date startDate, Date endDate, StorablePredicate predicate)
            throws GwtKapuaException {
        MessageStoreService messageService = LOCATOR.getService(MessageStoreService.class);
        List<GwtMessage> messages;
        int totalLength = 0;
        MessageQuery query = DATASTORE_FACTORY.newDatastoreMessageQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        query.setLimit(loadConfig.getLimit());
        query.setOffset(loadConfig.getOffset());
        AndPredicate andPredicate = STORABLE_PREDICATE_FACTORY.newAndPredicate();
        if (predicate != null) {
            andPredicate.getPredicates().add(predicate);
        }
        RangePredicate dateRangePredicate = STORABLE_PREDICATE_FACTORY.newRangePredicate(MessageField.TIMESTAMP.field(), startDate, endDate);
        andPredicate.getPredicates().add(dateRangePredicate);
        query.setPredicate(andPredicate);
        if (!StringUtils.isEmpty(loadConfig.getSortField())) {
            String sortField = loadConfig.getSortField();
            if (sortField.equals("timestampFormatted")) {
                sortField = MessageField.TIMESTAMP.field();
            } else if (sortField.equals("clientId")) {
                sortField = MessageField.CLIENT_ID.field();
            }
            query.setSortFields(Collections.singletonList(SortField.of(SortDirection.valueOf(loadConfig.getSortDir().name()), sortField)));
        }
        messages = getMessagesList(query, headers);
        try {
            totalLength = Long.valueOf(messageService.count(query)).intValue();
            if (totalLength > 10000) {
                totalLength = 10000;
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtMessage>(messages, loadConfig.getOffset(), totalLength);
    }

    private List<GwtMessage> getMessagesList(MessageQuery query, List<GwtHeader> headers) throws GwtKapuaException {
        MessageStoreService messageService = LOCATOR.getService(MessageStoreService.class);
        List<GwtMessage> messages = new ArrayList<GwtMessage>();
        try {
            MessageListResult result = messageService.query(query);
            if (result != null && !result.isEmpty()) {
                for (DatastoreMessage message : result.getItems()) {
                    messages.add(KapuaGwtDataModelConverter.convertToMessage(message, headers));
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return messages;
    }

}
