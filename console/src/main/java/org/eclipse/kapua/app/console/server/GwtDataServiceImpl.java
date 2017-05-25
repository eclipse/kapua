/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.shared.model.GwtHeader;
import org.eclipse.kapua.app.console.shared.model.GwtKapuaChartResult;
import org.eclipse.kapua.app.console.shared.model.GwtMessage;
import org.eclipse.kapua.app.console.shared.model.GwtTopic;
import org.eclipse.kapua.app.console.shared.model.KapuaBasePagingCursor;
import org.eclipse.kapua.app.console.shared.model.data.GwtDataChannelInfoQuery;
import org.eclipse.kapua.app.console.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
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
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtDataServiceImpl extends KapuaRemoteServiceServlet implements GwtDataService {

    private static KapuaLocator locator = KapuaLocator.getInstance();
    private static final long serialVersionUID = -5518740923786017558L;

    @Override
    public List<GwtTopic> findTopicsTree(String scopeId) throws GwtKapuaException {
        List<GwtTopic> channelInfoList = new ArrayList<GwtTopic>();
        HashMap<String, GwtTopic> topicMap = new HashMap<String, GwtTopic>();
        ChannelInfoRegistryService channelInfoService = locator.getService(ChannelInfoRegistryService.class);
        ChannelInfoQuery query = new ChannelInfoQueryImpl(GwtKapuaModelConverter.convert(scopeId));
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
                if (channel.getLastMessageOn() != null && t.getTimestamp().before(channel.getLastMessageOn())) {
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

        ChannelInfoRegistryService channelInfoService = locator.getService(ChannelInfoRegistryService.class);

        ChannelInfoQuery channelInfoQuery = GwtKapuaModelConverter.convertChannelInfoQuery(query, config);

        try {
            ChannelInfoListResult channelInfoListResult = channelInfoService.query(channelInfoQuery);
            if (channelInfoListResult != null && !channelInfoListResult.isEmpty()) {
                for (ChannelInfo channelInfo : channelInfoListResult.getItems()) {
                    channelInfoList.add(KapuaGwtModelConverter.convertToTopic(channelInfo));
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
        ClientInfoRegistryService clientInfoService = locator.getService(ClientInfoRegistryService.class);
        List<GwtDatastoreDevice> devices = new ArrayList<GwtDatastoreDevice>();
        KapuaId convertedScopeId = GwtKapuaModelConverter.convert(scopeId);
        ClientInfoQuery query = new ClientInfoQueryImpl(convertedScopeId);
        try {
            ClientInfoListResult result = clientInfoService.query(query);
            if (result != null && !result.isEmpty()) {
                for (ClientInfo client : result.getItems()) {
                    devices.add(KapuaGwtModelConverter.convertToDatastoreDevice(client));
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtDatastoreDevice>(devices);
    }

    @Override
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtTopic topic) throws GwtKapuaException {
        ChannelMatchPredicateImpl predicate = new ChannelMatchPredicateImpl(topic.getSemanticTopic().replaceFirst("/#$", ""));
        return findHeaders(config, scopeId, predicate);
    }

    @Override
    public ListLoadResult<GwtHeader> findNumberHeaders(LoadConfig config, String accountName, GwtTopic topic) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, GwtDatastoreDevice device) throws GwtKapuaException {
        TermPredicateImpl predicate = new TermPredicateImpl(MetricInfoField.CLIENT_ID, device.getDevice());
        return findHeaders(config, scopeId, predicate);
    }

    @Override
    public PagingLoadResult<GwtMessage> findMessagesByTopic(PagingLoadConfig loadConfig, String scopeId, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException {
        ChannelMatchPredicateImpl predicate = new ChannelMatchPredicateImpl(topic.getSemanticTopic());
        return findMessages(loadConfig, scopeId, headers, startDate, endDate, predicate);
    }

    @Override
    public List<GwtMessage> findLastMessageByTopic(String accountName, int limit) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GwtKapuaChartResult findMessagesByTopic(String accountName, GwtTopic topic, List<GwtHeader> metrics, Date startDate, Date endDate) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GwtKapuaChartResult findMessagesByTopic(String accountName, GwtTopic topic, List<GwtHeader> headers, Date startDate, Date endDate, Stack<KapuaBasePagingCursor> cursors, int limit,
            int lastOffset, Integer indexOffset) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PagingLoadResult<GwtMessage> findMessagesByDevice(PagingLoadConfig loadConfig, String scopeId, GwtDatastoreDevice device, List<GwtHeader> headers, Date startDate, Date endDate)
            throws GwtKapuaException {
        TermPredicate predicate = new TermPredicateImpl(MessageField.CLIENT_ID, device.getDevice());
        return findMessages(loadConfig, scopeId, headers, startDate, endDate, predicate);
    }

    @Override
    public GwtKapuaChartResult findMessagesByDevice(String accountName, GwtDatastoreDevice device, List<GwtHeader> headers, Date startDate, Date endDate, Stack<KapuaBasePagingCursor> cursors,
            int limit,
            int lastOffset, Integer indexOffset) throws GwtKapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    private ListLoadResult<GwtHeader> findHeaders(LoadConfig config, String scopeId, StorablePredicate predicate) throws GwtKapuaException {
        MetricInfoRegistryService metricService = locator.getService(MetricInfoRegistryService.class);
        MetricInfoQueryImpl query = new MetricInfoQueryImpl(GwtKapuaModelConverter.convert(scopeId));
        if (predicate != null) {
            query.setPredicate(predicate);
        }
        List<GwtHeader> metrics = new ArrayList<GwtHeader>();

        try {
            MetricInfoListResult result = metricService.query(query);
            if (result != null && !result.isEmpty()) {
                for (MetricInfo metric : result.getItems()) {
                    metrics.add(KapuaGwtModelConverter.convertToHeader(metric));
                }
            }

        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtHeader>(metrics);
    }

    private PagingLoadResult<GwtMessage> findMessages(PagingLoadConfig loadConfig, String scopeId, List<GwtHeader> headers, Date startDate, Date endDate, StorablePredicate predicate)
            throws GwtKapuaException {
        MessageStoreService messageService = locator.getService(MessageStoreService.class);
        List<GwtMessage> messages;
        int totalLength = 0;
        MessageQuery query = new MessageQueryImpl(GwtKapuaModelConverter.convert(scopeId));
        query.setLimit(loadConfig.getLimit());
        query.setOffset(loadConfig.getOffset());
        AndPredicate andPredicate = new AndPredicateImpl();
        if (predicate != null) {
            andPredicate.getPredicates().add(predicate);
        }
        RangePredicate dateRangePredicate = new RangePredicateImpl(MessageField.TIMESTAMP, startDate, endDate);
        andPredicate.getPredicates().add(dateRangePredicate);
        query.setPredicate(andPredicate);
        if (!StringUtils.isEmpty(loadConfig.getSortField())) {
            query.setSortFields(Collections.singletonList(SortField.of(SortDirection.valueOf(loadConfig.getSortDir().name()), loadConfig.getSortField())));
        }
        messages = getMessagesList(query, headers);
        try {
            totalLength = Long.valueOf(messageService.count(query)).intValue();
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtMessage>(messages, loadConfig.getOffset(), totalLength);
    }

    private List<GwtMessage> getMessagesList(MessageQuery query, List<GwtHeader> headers) throws GwtKapuaException {
        MessageStoreService messageService = locator.getService(MessageStoreService.class);
        List<GwtMessage> messages = new ArrayList<GwtMessage>();
        try {
            MessageListResult result = messageService.query(query);
            if (result != null && !result.isEmpty()) {
                for (DatastoreMessage message : result.getItems()) {
                    messages.add(KapuaGwtModelConverter.convertToMessage(message, headers));
                }
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return messages;
    }

}
