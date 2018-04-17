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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
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
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.apache.commons.lang3.StringUtils;

import static org.eclipse.kapua.service.datastore.model.query.SortField.descending;

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
                if (!entry.getKey().contains("/")) {
                    channelInfoList.add(entry.getValue());
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return channelInfoList;
    }

    @Override
    public List<GwtTopic> updateTimestamps(String gwtScopeId, List<ModelData> topics) throws GwtKapuaException {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        List<GwtTopic> updatedTopics = new ArrayList<GwtTopic>();
        MessageStoreService messageStoreService = LOCATOR.getService(MessageStoreService.class);
        try {
            for (ModelData md : topics) {
                GwtTopic topic = (GwtTopic) md;

                List<SortField> sort = new ArrayList<SortField>();
                sort.add(descending(MessageSchema.MESSAGE_TIMESTAMP));

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
                if (messageList.getSize() == 1) {
                    topic.setTimestamp(messageList.getFirstItem().getTimestamp());
                }
                updatedTopics.add(topic);
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return updatedTopics;
    }

    private void addToMap(HashMap<String, GwtTopic> topicMap, ChannelInfo channel) {
        String[] topicParts = channel.getName().split("/");
        GwtTopic previous = null;
        String topicName = topicParts[0];
        String baseTopic = topicParts[0];
        String semanticTopic = baseTopic;
        if (topicParts.length > 1) {
            semanticTopic += "/#";
        }
        int i = 0;
        do {
            GwtTopic t = topicMap.get(baseTopic);
            if (t == null) {
                t = new GwtTopic(topicName, baseTopic, semanticTopic, channel.getLastMessageOn());
                topicMap.put(baseTopic, t);
                if (previous != null) {
                    previous.add(t);
                }
            } else {
                if (channel.getLastMessageOn() != null && t.getTimestamp() != null && t.getTimestamp().before(channel.getLastMessageOn())) {
                    t.setTimestamp(channel.getLastMessageOn());
                }
            }
            previous = t;
            i++;
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
    public ListLoadResult<GwtDatastoreDevice> findDevices(LoadConfig config, String scopeId) throws GwtKapuaException {
        ClientInfoRegistryService clientInfoService = LOCATOR.getService(ClientInfoRegistryService.class);
        DeviceRegistryService deviceRegistryService = LOCATOR.getService(DeviceRegistryService.class);
        DeviceFactory deviceFactory = LOCATOR.getFactory(DeviceFactory.class);
        List<GwtDatastoreDevice> devices = new ArrayList<GwtDatastoreDevice>();
        KapuaId convertedScopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeId);
        ClientInfoQuery query = DATASTORE_FACTORY.newClientInfoQuery(convertedScopeId);
        query.setLimit(10000);
        try {
            ClientInfoListResult result = clientInfoService.query(query);
            if (result != null && !result.isEmpty()) {
                List<String> clientIds = new ArrayList<String>();
                for (ClientInfo client : result.getItems()) {
                    clientIds.add(client.getClientId());
                }
                DeviceQuery deviceQuery = deviceFactory.newQuery(convertedScopeId);
                deviceQuery.setPredicate(new AttributePredicateImpl<String[]>(DevicePredicates.CLIENT_ID, clientIds.toArray(new String[0])));
                DeviceListResult deviceListResult = deviceRegistryService.query(deviceQuery);
                Map<String, String> clientIdsMap = new HashMap<String, String>();
                for (Device device : deviceListResult.getItems()) {
                    clientIdsMap.put(device.getClientId(), device.getDisplayName());
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
        return new BaseListLoadResult<GwtDatastoreDevice>(devices);
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
        ChannelMatchPredicate predicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(topic.getSemanticTopic().replaceFirst("/#$", ""));
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
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String accountName,
                                                 GwtDatastoreAsset gwtDatastoreAsset) throws GwtKapuaException {
        ChannelMatchPredicate predicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(gwtDatastoreAsset.getTopick());
        return findHeaders(accountName, predicate);
    }

    @Override
    public PagingLoadResult<GwtMessage> findMessagesByTopic(PagingLoadConfig loadConfig, String scopeId, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException {

        StorablePredicate channelPredicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(topic.getSemanticTopic().replaceFirst("/#$", ""));

        return findMessages(loadConfig, scopeId, headers, startDate, endDate, channelPredicate);
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
