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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.QueryMappingException;
import org.eclipse.kapua.service.datastore.client.model.InsertRequest;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreChannel;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageInfo;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.DatastoreMessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.ChannelInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Message store facade
 *
 * @since 1.0.0
 *
 */
public final class MessageStoreFacade {

    private static final Logger logger = LoggerFactory.getLogger(MessageStoreFacade.class);

    private final Counter metricMessagesAlreadyInTheDatastoreCount;

    private static final String METRIC_COMPONENT_NAME = "datastore";

    private final MessageStoreMediator mediator;
    private final ConfigurationProvider configProvider;
    private DatastoreClient client;

    /**
     * Constructs the message store facade
     *
     * @param confProvider
     * @param mediator
     * @throws ClientUnavailableException
     * 
     * @since 1.0.0
     */
    public MessageStoreFacade(ConfigurationProvider confProvider, MessageStoreMediator mediator) throws ClientUnavailableException {
        configProvider = confProvider;
        this.mediator = mediator;
        client = DatastoreClientFactory.getInstance();
        MetricsService metricService = MetricServiceFactory.getInstance();
        metricMessagesAlreadyInTheDatastoreCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "already_in_the_datastore", "count");
    }

    /**
     * Store a message
     *
     * @param message
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public StorableId store(KapuaMessage<?, ?> message, String messageId, boolean newInsert)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(message, "message");
        ArgumentValidator.notNull(message.getScopeId(), "scopeId");
        ArgumentValidator.notNull(message.getReceivedOn(), "receivedOn");
        ArgumentValidator.notNull(messageId, "messageId");

        // Collect context data
        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(message.getScopeId());
        MessageInfo messageInfo = configProvider.getInfo(message.getScopeId());

        // Define data TTL
        long ttlSecs = accountServicePlan.getDataTimeToLiveMilliseconds();
        if (!accountServicePlan.getDataStorageEnabled() || ttlSecs == MessageStoreConfiguration.DISABLED) {
            String msg = String.format("Message Store not enabled for account %s", messageInfo.getAccount().getName());
            logger.debug(msg);
            throw new ConfigurationException(msg);
        }

        Date capturedOn = message.getCapturedOn();
        long currentDate = KapuaDateUtils.getKapuaSysDate().toEpochMilli();

        // Overwrite timestamp if necessary
        // Use the account service plan to determine whether we will give
        // precede to the device time
        long indexedOn = currentDate;
        if (DataIndexBy.DEVICE_TIMESTAMP.equals(accountServicePlan.getDataIndexBy())) {
            if (capturedOn != null) {
                indexedOn = capturedOn.getTime();
            } else {
                logger.debug("The account is set to use, as date indexing, the device timestamp but the device timestamp is null! Current system date will be used to indexing the message by date!");
            }
        }
        // Extract schema metadata
        Metadata schemaMetadata = mediator.getMetadata(message.getScopeId(), indexedOn);
        Date indexedOnDate = new Date(indexedOn);
        String indexName = schemaMetadata.getDataIndexName();
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);

        if (!newInsert) {
            DatastoreMessage datastoreMessage = find(message.getScopeId(), new StorableIdImpl(messageId), StorableFetchStyle.SOURCE_SELECT);
            if (datastoreMessage != null) {
                logger.debug("Message with datatstore id '{}' already found", messageId);
                metricMessagesAlreadyInTheDatastoreCount.inc();
                return new StorableIdImpl(messageId);
            }
        }

        // Save message (the big one)
        DatastoreMessage messageToStore = convertTo(message, messageId);
        messageToStore.setTimestamp(indexedOnDate);
        InsertRequest insertRequest = new InsertRequest(messageToStore.getDatastoreId().toString(), typeDescriptor, messageToStore);
        // Possibly update the schema with new metric mappings
        Map<String, Metric> metrics = new HashMap<>();
        if (message.getPayload() != null && message.getPayload().getMetrics() != null && message.getPayload().getMetrics().size() > 0) {
            Map<String, Object> messageMetrics = message.getPayload().getMetrics();
            Iterator<String> metricsIterator = messageMetrics.keySet().iterator();
            while (metricsIterator.hasNext()) {
                String kapuaMetricName = metricsIterator.next();
                String metricName = DatastoreUtils.normalizeMetricName(kapuaMetricName);
                Object metricValue = messageMetrics.get(kapuaMetricName);
                String clientMetricType = DatastoreUtils.getClientMetricFromType(metricValue.getClass());
                Metric metric = new Metric(metricName, clientMetricType);
                // each metric is potentially a dynamic field so report it a new mapping
                String mappedName = DatastoreUtils.getMetricValueQualifier(metricName, clientMetricType);
                metrics.put(mappedName, metric);
            }
        }
        mediator.onUpdatedMappings(message.getScopeId(), indexedOn, metrics);

        InsertResponse insertResponse = client.insert(insertRequest);
        messageToStore.setDatastoreId(new StorableIdImpl(insertResponse.getId()));

        mediator.onAfterMessageStore(messageInfo, messageToStore);
        return new StorableIdImpl(insertResponse.getId());
    }

    /**
     * Delete message by identifier.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the message by id without checking the consistency of the registries.</b>
     *
     * @param scopeId
     * @param id
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        // get the index by finding the object by id
        DatastoreMessage messageToBeDeleted = find(scopeId, id, StorableFetchStyle.FIELDS);
        if (messageToBeDeleted != null) {
            Metadata schemaMetadata = mediator.getMetadata(scopeId, messageToBeDeleted.getTimestamp().getTime());
            String indexName = schemaMetadata.getDataIndexName();
            TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
            client.delete(typeDescriptor, id.toString());
        } else {
            logger.warn("Cannot find the message to be deleted. scopeId: '{}' - id: '{}'", scopeId, id);
        }
        // otherwise no message to be deleted found
    }

    /**
     * Find message by identifier
     *
     * @param scopeId
     * @param id
     * @param fetchStyle
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        ArgumentValidator.notNull(fetchStyle, "fetchStyle");

        MessageQueryImpl idsQuery = new MessageQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicateImpl idsPredicate = new IdsPredicateImpl(MessageSchema.MESSAGE_TYPE_NAME);
        idsPredicate.addValue(id);
        idsQuery.setPredicate(idsPredicate);

        String indexName = SchemaUtil.getDataIndexName(scopeId);
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        return client.find(typeDescriptor, idsQuery, DatastoreMessage.class);
    }

    /**
     * Find messages matching the given query
     * 
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public MessageListResult query(MessageQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return new MessageListResultImpl();
        }

        String dataIndexName = SchemaUtil.getDataIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, MessageSchema.MESSAGE_TYPE_NAME);
        return new MessageListResultImpl(client.query(typeDescriptor, query, DatastoreMessage.class));
    }

    /**
     * Get messages count matching the given query
     * 
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public long count(MessageQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return 0;
        }

        String indexName = SchemaUtil.getDataIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        return client.count(typeDescriptor, query);
    }

    /**
     * Delete messages count matching the given query.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the messages that matching the query without checking the consistency of the registries.</b>
     * 
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public void delete(MessageQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, skipping delete", query.getScopeId());
            return;
        }

        String indexName = SchemaUtil.getDataIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        client.deleteByQuery(typeDescriptor, query);
    }

    // TODO cache will not be reset from the client code it should be automatically reset
    // after some time.
    @SuppressWarnings("unused")
    private void resetCache(KapuaId scopeId, KapuaId deviceId, String channel, String clientId)
            throws Exception {

        boolean isAnyClientId;
        boolean isClientToDelete = false;
        String semTopic;

        if (channel != null) {

            // determine if we should delete an client if topic = account/clientId/#
            isAnyClientId = isAnyClientId(channel);
            semTopic = channel;

            if (semTopic.isEmpty() && !isAnyClientId) {
                isClientToDelete = true;
            }
        } else {
            isClientToDelete = true;
        }

        // Find all topics
        String dataIndexName = SchemaUtil.getDataIndexName(scopeId);

        int pageSize = 1000;
        int offset = 0;
        long totalHits = 1;

        MetricInfoQueryImpl metricQuery = new MetricInfoQueryImpl(scopeId);
        metricQuery.setLimit(pageSize + 1);
        metricQuery.setOffset(offset);

        ChannelMatchPredicateImpl channelPredicate = new ChannelMatchPredicateImpl(MessageField.CHANNEL.field(), channel);
        metricQuery.setPredicate(channelPredicate);

        // Remove metrics
        while (totalHits > 0) {
            TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, MetricInfoSchema.METRIC_TYPE_NAME);
            ResultList<MetricInfo> metrics = client.query(typeDescriptor, metricQuery, MetricInfo.class);

            totalHits = metrics.getTotalCount();
            LocalCache<String, Boolean> metricsCache = DatastoreCacheManager.getInstance().getMetricsCache();
            long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

            for (int i = 0; i < toBeProcessed; i++) {
                String id = metrics.getResult().get(i).getId().toString();
                if (metricsCache.get(id)) {
                    metricsCache.remove(id);
                }
            }

            if (totalHits > pageSize) {
                offset += pageSize + 1;
            }
        }
        logger.debug(String.format("Removed cached channel metrics for [%s]", channel));
        TypeDescriptor typeMetricDescriptor = new TypeDescriptor(dataIndexName, MetricInfoSchema.METRIC_TYPE_NAME);
        client.deleteByQuery(typeMetricDescriptor, metricQuery);
        logger.debug(String.format("Removed channel metrics for [%s]", channel));
        ChannelInfoQueryImpl channelQuery = new ChannelInfoQueryImpl(scopeId);
        channelQuery.setLimit(pageSize + 1);
        channelQuery.setOffset(offset);

        channelPredicate = new ChannelMatchPredicateImpl(MessageField.CHANNEL.field(), channel);
        channelQuery.setPredicate(channelPredicate);

        // Remove channel
        offset = 0;
        totalHits = 1;
        while (totalHits > 0) {
            TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
            ResultList<ChannelInfo> channels = client.query(typeDescriptor, channelQuery, ChannelInfo.class);

            totalHits = channels.getTotalCount();
            LocalCache<String, Boolean> channelsCache = DatastoreCacheManager.getInstance().getChannelsCache();
            long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

            for (int i = 0; i < toBeProcessed; i++) {
                String id = channels.getResult().get(0).getId().toString();
                if (channelsCache.get(id)) {
                    channelsCache.remove(id);
                }
            }
            if (totalHits > pageSize) {
                offset += pageSize + 1;
            }
        }

        logger.debug(String.format("Removed cached channels for [%s]", channel));
        TypeDescriptor typeChannelDescriptor = new TypeDescriptor(dataIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
        client.deleteByQuery(typeChannelDescriptor, channelQuery);

        logger.debug(String.format("Removed channels for [%s]", channel));
        // Remove client
        if (isClientToDelete) {
            ClientInfoQueryImpl clientInfoQuery = new ClientInfoQueryImpl(scopeId);
            clientInfoQuery.setLimit(pageSize + 1);
            clientInfoQuery.setOffset(offset);

            channelPredicate = new ChannelMatchPredicateImpl(MessageField.CHANNEL.field(), channel);
            clientInfoQuery.setPredicate(channelPredicate);
            offset = 0;
            totalHits = 1;
            while (totalHits > 0) {
                TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, ClientInfoSchema.CLIENT_TYPE_NAME);
                ResultList<ClientInfo> clients = client.query(typeDescriptor, clientInfoQuery, ClientInfo.class);

                totalHits = clients.getTotalCount();
                LocalCache<String, Boolean> clientsCache = DatastoreCacheManager.getInstance().getClientsCache();
                long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

                for (int i = 0; i < toBeProcessed; i++) {
                    String id = clients.getResult().get(i).getId().toString();
                    if (clientsCache.get(id)) {
                        clientsCache.remove(id);
                    }
                }
                if (totalHits > pageSize) {
                    offset += pageSize + 1;
                }
            }

            logger.debug(String.format("Removed cached clients for [%s]", channel));
            TypeDescriptor typeClientDescriptor = new TypeDescriptor(dataIndexName, ClientInfoSchema.CLIENT_TYPE_NAME);
            client.deleteByQuery(typeClientDescriptor, clientInfoQuery);

            logger.debug(String.format("Removed clients for [%s]", channel));
        }
    }

    // Utility methods

    /**
     * Check if the full channel admit any account (so if the channel starts with a specific wildcard).<br>
     * In the MQTT word this method return true if the topic starts with '+/'.
     *
     * @return
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    private boolean isAnyAccount(String accountPart) {
        return DatastoreChannel.SINGLE_LEVEL_WCARD.equals(accountPart);
    }

    /**
     * Check if the channel admit any client identifier (so if the channel has a specific wildcard in the second topic level).<br>
     * In the MQTT word this method return true if the topic starts with 'account/+/'.
     *
     * @param clientId
     * @return
     * @since 1.0.0
     */
    private boolean isAnyClientId(String clientId) {
        return DatastoreChannel.SINGLE_LEVEL_WCARD.equals(clientId);
    }

    /**
     * This constructor should be used for wrapping Kapua message into datastore message for insert purpose
     * 
     * @param message
     */
    private DatastoreMessage convertTo(KapuaMessage<?, ?> message, String messageId) {
        DatastoreMessage datastoreMessage = new DatastoreMessageImpl();
        datastoreMessage.setCapturedOn(message.getCapturedOn());
        datastoreMessage.setChannel(message.getChannel());
        datastoreMessage.setClientId(message.getClientId());
        datastoreMessage.setDeviceId(message.getDeviceId());
        datastoreMessage.setId(message.getId());
        datastoreMessage.setPayload(message.getPayload());
        datastoreMessage.setPosition(message.getPosition());
        datastoreMessage.setReceivedOn(message.getReceivedOn());
        datastoreMessage.setScopeId(message.getScopeId());
        datastoreMessage.setSentOn(message.getSentOn());

        // generate uuid
        datastoreMessage.setId(message.getId());
        datastoreMessage.setDatastoreId(new StorableIdImpl(messageId));
        return datastoreMessage;
    }

    public void refreshAllIndexes() throws ClientException {
        client.refreshAllIndexes();
    }

    public void deleteAllIndexes() throws ClientException {
        client.deleteAllIndexes();
    }
}
