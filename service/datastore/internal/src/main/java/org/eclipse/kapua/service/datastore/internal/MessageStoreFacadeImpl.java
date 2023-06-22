/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.exception.DatastoreDisabledException;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreChannel;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageInfo;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.DatastoreMessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.MessageUniquenessCheck;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.predicate.ChannelMatchPredicateImpl;
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
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;
import org.eclipse.kapua.service.elasticsearch.client.model.InsertRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.InsertResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;
import org.eclipse.kapua.service.storable.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.storable.model.query.predicate.IdsPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Message store facade
 *
 * @since 1.0.0
 */
public final class MessageStoreFacadeImpl extends AbstractRegistryFacade implements MessageStoreFacade {

    private static final Logger LOG = LoggerFactory.getLogger(MessageStoreFacadeImpl.class);

    private final StorableIdFactory storableIdFactory;
    private final StorablePredicateFactory storablePredicateFactory;
    private final ClientInfoRegistryFacade clientInfoRegistryFacade;
    private final ChannelInfoRegistryFacade channelInfoStoreFacade;
    private final MetricInfoRegistryFacade metricInfoStoreFacade;
    private final MessageStoreMediator mediator;
    private final MessageRepository messageRepository;
    private final MetricsDatastore metrics;

    private static final String QUERY = "query";
    private static final String QUERY_SCOPE_ID = "query.scopeId";
    private static final String SCOPE_ID = "scopeId";

    public MessageStoreFacadeImpl(
            ConfigurationProvider configProvider,
            StorableIdFactory storableIdFactory,
            StorablePredicateFactory storablePredicateFactory,
            ClientInfoRegistryFacade clientInfoRegistryFacade,
            ChannelInfoRegistryFacade channelInfoStoreFacade,
            MetricInfoRegistryFacade metricInfoStoreFacade,
            MessageStoreMediator mediator, MessageRepository messageRepository) {
        super(configProvider);
        this.storableIdFactory = storableIdFactory;
        this.storablePredicateFactory = storablePredicateFactory;
        this.clientInfoRegistryFacade = clientInfoRegistryFacade;
        this.channelInfoStoreFacade = channelInfoStoreFacade;
        this.metricInfoStoreFacade = metricInfoStoreFacade;
        this.messageRepository = messageRepository;
        this.mediator = mediator;
        this.metrics = MetricsDatastore.getInstance();
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
    @Override
    public StorableId store(KapuaMessage<?, ?> message, String messageId, boolean newInsert)
            throws KapuaIllegalArgumentException,
            DatastoreDisabledException,
            ConfigurationException,
            ClientException, MappingException {
        ArgumentValidator.notNull(message, "message");
        ArgumentValidator.notNull(message.getScopeId(), SCOPE_ID);
        ArgumentValidator.notNull(message.getReceivedOn(), "receivedOn");
        ArgumentValidator.notNull(messageId, "messageId");

        // Define data TTL
        if (!isDatastoreServiceEnabled(message.getScopeId())) {
            throw new DatastoreDisabledException(message.getScopeId());
        }

        Date capturedOn = message.getCapturedOn();
        // Overwrite timestamp if necessary
        // Use the account service plan to determine whether we will give
        // precede to the device time
        MessageStoreConfiguration accountServicePlan = getConfigProvider().getConfiguration(message.getScopeId());
        long indexedOn = KapuaDateUtils.getKapuaSysDate().toEpochMilli();
        if (DataIndexBy.DEVICE_TIMESTAMP.equals(accountServicePlan.getDataIndexBy())) {
            if (capturedOn != null) {
                indexedOn = capturedOn.getTime();
            } else {
                LOG.debug("The account is set to use, as date indexing, the device timestamp but the device timestamp is null! Current system date will be used to indexing the message by date!");
            }
        }
        // Extract schema metadata
        Metadata schemaMetadata = mediator.getMetadata(message.getScopeId(), indexedOn);

        Date indexedOnDate = new Date(indexedOn);
        String indexName = schemaMetadata.getDataIndexName();
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);

        if (!newInsert && !MessageUniquenessCheck.NONE.equals(accountServicePlan.getMessageUniquenessCheck())) {
            DatastoreMessage datastoreMessage = MessageUniquenessCheck.FULL.equals(accountServicePlan.getMessageUniquenessCheck()) ?
                    find(message.getScopeId(), storableIdFactory.newStorableId(messageId), StorableFetchStyle.SOURCE_SELECT) :
                    find(message.getScopeId(), indexName, storableIdFactory.newStorableId(messageId), StorableFetchStyle.SOURCE_SELECT);
            if (datastoreMessage != null) {
                LOG.debug("Message with datatstore id '{}' already found", messageId);
                metrics.getAlreadyInTheDatastore().inc();
                return storableIdFactory.newStorableId(messageId);
            }
        }

        // Save message (the big one)
        DatastoreMessage messageToStore = convertTo(message, messageId);
        messageToStore.setTimestamp(indexedOnDate);
        InsertRequest insertRequest = new InsertRequest(messageToStore.getDatastoreId().toString(), typeDescriptor, messageToStore);
        // Possibly update the schema with new metric mappings
        Map<String, Metric> metrics = new HashMap<>();
        if (message.getPayload() != null && message.getPayload().getMetrics() != null && !message.getPayload().getMetrics().isEmpty()) {

            Map<String, Object> messageMetrics = message.getPayload().getMetrics();
            for (Map.Entry<String, Object> messageMetric : messageMetrics.entrySet()) {
                String metricName = DatastoreUtils.normalizeMetricName(messageMetric.getKey());
                String clientMetricType = DatastoreUtils.getClientMetricFromType(messageMetric.getValue().getClass());
                Metric metric = new Metric(metricName, clientMetricType);

                // each metric is potentially a dynamic field so report it a new mapping
                String mappedName = DatastoreUtils.getMetricValueQualifier(metricName, clientMetricType);
                metrics.put(mappedName, metric);
            }
        }
        try {
            mediator.onUpdatedMappings(message.getScopeId(), indexedOn, metrics);
        } catch (KapuaException e) {
            LOG.warn("Update mappings error", e);
        }

        InsertResponse insertResponse = getElasticsearchClient().insert(insertRequest);
        messageToStore.setDatastoreId(storableIdFactory.newStorableId(insertResponse.getId()));

        MessageInfo messageInfo = getConfigProvider().getInfo(message.getScopeId());
        this.onAfterMessageStore(messageInfo, messageToStore);

        return storableIdFactory.newStorableId(insertResponse.getId());
    }

    @Override
    public void onAfterMessageStore(MessageInfo messageInfo, DatastoreMessage message)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            MappingException,
            ClientException {

        // convert semantic channel to String
        String semanticChannel = message.getChannel() != null ? message.getChannel().toString() : "";

        ClientInfoImpl clientInfo = new ClientInfoImpl(message.getScopeId());
        clientInfo.setClientId(message.getClientId());
        clientInfo.setId(storableIdFactory.newStorableId(ClientInfoField.getOrDeriveId(null, message.getScopeId(), message.getClientId())));
        clientInfo.setFirstMessageId(message.getDatastoreId());
        clientInfo.setFirstMessageOn(message.getTimestamp());
        clientInfoRegistryFacade.upstore(clientInfo);

        ChannelInfoImpl channelInfo = new ChannelInfoImpl(message.getScopeId());
        channelInfo.setClientId(message.getClientId());
        channelInfo.setName(semanticChannel);
        channelInfo.setFirstMessageId(message.getDatastoreId());
        channelInfo.setFirstMessageOn(message.getTimestamp());
        channelInfo.setId(storableIdFactory.newStorableId(ChannelInfoField.getOrDeriveId(null, channelInfo)));
        channelInfoStoreFacade.upstore(channelInfo);

        KapuaPayload payload = message.getPayload();
        if (payload == null) {
            return;
        }

        Map<String, Object> metrics = payload.getMetrics();
        if (metrics == null) {
            return;
        }

        int i = 0;
        MetricInfoImpl[] messageMetrics = new MetricInfoImpl[metrics.size()];
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            MetricInfoImpl metricInfo = new MetricInfoImpl(message.getScopeId());
            metricInfo.setClientId(message.getClientId());
            metricInfo.setChannel(semanticChannel);
            metricInfo.setName(entry.getKey());
            metricInfo.setMetricType(entry.getValue().getClass());
            metricInfo.setId(storableIdFactory.newStorableId(MetricInfoField.getOrDeriveId(null, metricInfo)));
            metricInfo.setFirstMessageId(message.getDatastoreId());
            metricInfo.setFirstMessageOn(message.getTimestamp());
            messageMetrics[i++] = metricInfo;
        }

        metricInfoStoreFacade.upstore(messageMetrics);
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
    @Override
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(id, "id");

        if (!isDatastoreServiceEnabled(scopeId)) {
            LOG.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        // get the index by finding the object by id
        DatastoreMessage messageToBeDeleted = find(scopeId, id, StorableFetchStyle.FIELDS);
        if (messageToBeDeleted != null) {
            Metadata schemaMetadata = null;
            try {
                schemaMetadata = mediator.getMetadata(scopeId, messageToBeDeleted.getTimestamp().getTime());
            } catch (KapuaException e) {
                LOG.warn("Retrieving metadata error", e);
            }
            String indexName = schemaMetadata.getDataIndexName();
            TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
            getElasticsearchClient().delete(typeDescriptor, id.toString());
        } else {
            LOG.warn("Cannot find the message to be deleted. scopeId: '{}' - id: '{}'", scopeId, id);
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
     * @throws QueryMappingException
     * @throws ClientException
     */
    @Override
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle) throws KapuaIllegalArgumentException, ClientException {
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        return find(scopeId, SchemaUtil.getDataIndexName(scopeId), id, fetchStyle);
    }

    /**
     * Find message by identifier
     *
     * @param scopeId
     * @param id
     * @param fetchStyle
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public DatastoreMessage find(KapuaId scopeId, String indexName, StorableId id, StorableFetchStyle fetchStyle)
            throws KapuaIllegalArgumentException, ClientException {
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(id, "id");
        ArgumentValidator.notNull(fetchStyle, "fetchStyle");

        MessageQueryImpl idsQuery = new MessageQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicate idsPredicate = storablePredicateFactory.newIdsPredicate(MessageSchema.MESSAGE_TYPE_NAME);
        idsPredicate.addId(id);
        idsQuery.setPredicate(idsPredicate);

        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        return getElasticsearchClient().find(typeDescriptor, idsQuery, DatastoreMessage.class);
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
    @Override
    public MessageListResult query(MessageQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        return messageRepository.query(query);
    }

    /**
     * Get messages count matching the given query
     *
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    @Override
    public long count(MessageQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return 0;
        }

        String indexName = SchemaUtil.getDataIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        return getElasticsearchClient().count(typeDescriptor, query);
    }

    /**
     * Delete messages count matching the given query.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the messages that matching the query without checking the consistency of the registries.</b>
     *
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    @Override
    public void delete(MessageQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug("Storage not enabled for account {}, skipping delete", query.getScopeId());
            return;
        }

        String indexName = SchemaUtil.getDataIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        getElasticsearchClient().deleteByQuery(typeDescriptor, query);
    }

    // TODO cache will not be reset from the client code it should be automatically reset
    // after some time.
    private void resetCache(KapuaId scopeId, KapuaId deviceId, String channel, String clientId) throws Exception {

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

        ChannelMatchPredicateImpl channelPredicate = new ChannelMatchPredicateImpl(MessageField.CHANNEL, channel);
        metricQuery.setPredicate(channelPredicate);

        // Remove metrics
        while (totalHits > 0) {
            TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, MetricInfoSchema.METRIC_TYPE_NAME);
            ResultList<MetricInfo> metrics = getElasticsearchClient().query(typeDescriptor, metricQuery, MetricInfo.class);

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
        LOG.debug("Removed cached channel metrics for: {}", channel);
        TypeDescriptor typeMetricDescriptor = new TypeDescriptor(dataIndexName, MetricInfoSchema.METRIC_TYPE_NAME);
        getElasticsearchClient().deleteByQuery(typeMetricDescriptor, metricQuery);
        LOG.debug("Removed channel metrics for: {}", channel);
        ChannelInfoQueryImpl channelQuery = new ChannelInfoQueryImpl(scopeId);
        channelQuery.setLimit(pageSize + 1);
        channelQuery.setOffset(offset);

        channelPredicate = new ChannelMatchPredicateImpl(MessageField.CHANNEL, channel);
        channelQuery.setPredicate(channelPredicate);

        // Remove channel
        offset = 0;
        totalHits = 1;
        while (totalHits > 0) {
            TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
            ResultList<ChannelInfo> channels = getElasticsearchClient().query(typeDescriptor, channelQuery, ChannelInfo.class);

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

        LOG.debug("Removed cached channels for: {}", channel);
        TypeDescriptor typeChannelDescriptor = new TypeDescriptor(dataIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
        getElasticsearchClient().deleteByQuery(typeChannelDescriptor, channelQuery);

        LOG.debug("Removed channels for: {}", channel);
        // Remove client
        if (isClientToDelete) {
            ClientInfoQueryImpl clientInfoQuery = new ClientInfoQueryImpl(scopeId);
            clientInfoQuery.setLimit(pageSize + 1);
            clientInfoQuery.setOffset(offset);

            channelPredicate = new ChannelMatchPredicateImpl(MessageField.CHANNEL, channel);
            clientInfoQuery.setPredicate(channelPredicate);
            offset = 0;
            totalHits = 1;
            while (totalHits > 0) {
                TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, ClientInfoSchema.CLIENT_TYPE_NAME);
                ResultList<ClientInfo> clients = getElasticsearchClient().query(typeDescriptor, clientInfoQuery, ClientInfo.class);

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

            LOG.debug("Removed cached clients for: {}", channel);
            TypeDescriptor typeClientDescriptor = new TypeDescriptor(dataIndexName, ClientInfoSchema.CLIENT_TYPE_NAME);
            getElasticsearchClient().deleteByQuery(typeClientDescriptor, clientInfoQuery);

            LOG.debug("Removed clients for: {}", channel);
        }
    }

    // Utility methods

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
        KapuaDataChannel datastoreChannel = new KapuaDataChannelImpl();
        datastoreChannel.setSemanticParts(message.getChannel().getSemanticParts());

        DatastoreMessage datastoreMessage = new DatastoreMessageImpl();
        datastoreMessage.setCapturedOn(message.getCapturedOn());
        datastoreMessage.setChannel(datastoreChannel);
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
        datastoreMessage.setDatastoreId(storableIdFactory.newStorableId(messageId));
        return datastoreMessage;
    }

    @Override
    public void refreshAllIndexes() throws ClientException {
        getElasticsearchClient().refreshAllIndexes();
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        getElasticsearchClient().deleteAllIndexes();
        clearCache();
    }


    @Override
    public void deleteIndexes(String indexExp) throws ClientException {
        getElasticsearchClient().deleteIndexes(indexExp);
        clearCache();
    }

    public void clearCache() {
        DatastoreCacheManager.getInstance().getChannelsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getClientsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetricsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetadataCache().invalidateAll();
    }
}
