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
import java.util.Map;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.DatastoreChannel;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsConfigurationException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsMetric;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageInfo;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageStoreMediator;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsChannelInfoDAO;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsClientInfoDAO;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsMessageDAO;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsMetricInfoDAO;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message store facade
 * 
 * @since 1.0.0
 *
 */
public final class MessageStoreFacade {

    private static final Logger logger = LoggerFactory.getLogger(MessageStoreFacade.class);

    private final MessageStoreMediator mediator;
    private final ConfigurationProvider configProvider;

    /**
     * Constructs the message store facade
     * 
     * @param confProvider
     * @param mediator
     * 
     * @since 1.0.0
     */
    public MessageStoreFacade(ConfigurationProvider confProvider, MessageStoreMediator mediator) {
        this.configProvider = confProvider;
        this.mediator = mediator;
    }

    /**
     * Store a message
     * 
     * @param message
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsDocumentBuilderException
     * 
     * @since 1.0.0
     */
    public StorableId store(KapuaMessage<?, ?> message)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsClientUnavailableException,
            EsDocumentBuilderException {
        //
        // Argument Validation
        ArgumentValidator.notNull(message, "message");
        ArgumentValidator.notNull(message.getScopeId(), "scopeId");
        ArgumentValidator.notNull(message.getReceivedOn(), "receivedOn");

        // Collect context data
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(message.getScopeId());
        MessageInfo accountInfo = this.configProvider.getInfo(message.getScopeId());

        // Define data TTL
        long ttlSecs = accountServicePlan.getDataTimeToLiveMilliseconds();
        if (!accountServicePlan.getDataStorageEnabled() || ttlSecs == MessageStoreConfiguration.DISABLED) {
            String msg = String.format("Message Store not enabled for account %s", accountInfo.getAccount().getName());
            logger.debug(msg);
            throw new EsConfigurationException(msg);
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
                logger.warn("The account is set to use, as date indexing, the device timestamp but the device timestamp is null! Current system date will be used to indexing the message by date!");
            }
        }

        // Extract schema metadata
        EsSchema.Metadata schemaMetadata = mediator.getMetadata(message.getScopeId(), indexedOn);

        Date indexedOnDt = new Date(indexedOn);

        // Parse document
        MessageInfo messageInfo = configProvider.getInfo(message.getScopeId());
        MessageXContentBuilder docBuilder = new MessageXContentBuilder();
        docBuilder.build(messageInfo.getAccount().getId(), message, indexedOnDt, message.getReceivedOn());

        // Possibly update the schema with new metric mappings
        Map<String, EsMetric> esMetrics = docBuilder.getMetricMappings();
        mediator.onUpdatedMappings(message.getScopeId(), indexedOn, esMetrics);

        String indexName = schemaMetadata.getDataIndexName();

        // Save message (the big one)
        // TODO check response
        EsMessageDAO.getInstance()
                .index(indexName)
                .upsert(docBuilder.getMessageId().toString(), docBuilder.getBuilder());

        mediator.onAfterMessageStore(docBuilder, message);

        return docBuilder.getMessageId();
    }

    /**
     * Delete message by identifier
     * 
     * @param scopeId
     * @param id
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsClientUnavailableException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        String dataIndexName = EsSchema.getDataIndexName(scopeId);
        EsMessageDAO.getInstance()
                .index(dataIndexName)
                .deleteById(id.toString());
    }

    /**
     * Find message by identifier
     * 
     * @param scopeId
     * @param id
     * @param fetchStyle
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     * 
     * @since 1.0.0
     */
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsClientUnavailableException,
            EsQueryConversionException, EsObjectBuilderException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        ArgumentValidator.notNull(fetchStyle, "fetchStyle");

        // Query by Id
        IdsPredicateImpl idsPredicate = new IdsPredicateImpl(EsSchema.MESSAGE_TYPE_NAME);
        idsPredicate.addValue(id);
        MessageQueryImpl idsQuery = new MessageQueryImpl(scopeId);
        idsQuery.setPredicate(idsPredicate);

        String dataIndexName = EsSchema.getDataIndexName(scopeId);
        MessageListResult result = EsMessageDAO.getInstance()
                .index(dataIndexName)
                .query(idsQuery);

        return result.getFirstItem();
    }

    /**
     * Find messages matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     * 
     * @since 1.0.0
     */
    public MessageListResult query(MessageQuery query)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsClientUnavailableException,
            EsQueryConversionException, EsObjectBuilderException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return new MessageListResultImpl();
        }

        String dataIndexName = EsSchema.getDataIndexName(query.getScopeId());
        return EsMessageDAO.getInstance()
                .index(dataIndexName)
                .query(query);
    }

    /**
     * Get messages count matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public long count(MessageQuery query)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsQueryConversionException,
            EsClientUnavailableException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return 0;
        }

        String dataIndexName = EsSchema.getDataIndexName(query.getScopeId());
        return EsMessageDAO.getInstance()
                .index(dataIndexName)
                .count(query);
    }

    /**
     * Delete messages count matching the given query
     * 
     * @param scopeId
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * 
     * @since 1.0.0
     */
    public void delete(MessageQuery query)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsQueryConversionException,
            EsClientUnavailableException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, skipping delete", query.getScopeId());
            return;
        }

        String dataIndexName = EsSchema.getDataIndexName(query.getScopeId());
        EsMessageDAO.getInstance()
                .index(dataIndexName)
                .deleteByQuery(query);
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
            isAnyClientId = DatastoreChannel.isAnyClientId(clientId);
            semTopic = channel;

            if (semTopic.isEmpty() && !isAnyClientId)
                isClientToDelete = true;
        } else {
            isClientToDelete = true;
        }

        // Find all topics
        String dataIndexName = EsSchema.getDataIndexName(scopeId);

        int pageSize = 1000;
        int offset = 0;
        long totalHits = 1;

        MetricInfoQueryImpl metricQuery = new MetricInfoQueryImpl(scopeId);
        metricQuery.setLimit(pageSize + 1);
        metricQuery.setOffset(offset);

        ChannelMatchPredicateImpl channelPredicate = new ChannelMatchPredicateImpl();
        channelPredicate.setExpression(channel);
        metricQuery.setPredicate(channelPredicate);

        // Remove metrics
        while (totalHits > 0) {
            MetricInfoListResult metrics = EsMetricInfoDAO.getInstance()
                    .index(dataIndexName).query(metricQuery);

            totalHits = metrics.getSize();
            LocalCache<String, Boolean> metricsCache = DatastoreCacheManager.getInstance().getMetricsCache();
            long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

            for (int i = 0; i < toBeProcessed; i++) {
                String id = metrics.getItem(i).getId().toString();
                if (metricsCache.get(id))
                    metricsCache.remove(id);
            }

            if (totalHits > pageSize)
                offset += (pageSize + 1);
        }

        logger.debug(String.format("Removed cached channel metrics for [%s]", channel));

        EsMetricInfoDAO.getInstance().index(dataIndexName)
                .deleteByQuery(metricQuery);

        logger.debug(String.format("Removed channel metrics for [%s]", channel));
        //

        ChannelInfoQueryImpl channelQuery = new ChannelInfoQueryImpl(scopeId);
        channelQuery.setLimit(pageSize + 1);
        channelQuery.setOffset(offset);

        channelPredicate = new ChannelMatchPredicateImpl();
        channelPredicate.setExpression(channel);
        channelQuery.setPredicate(channelPredicate);

        // Remove channel
        offset = 0;
        totalHits = 1;
        while (totalHits > 0) {
            ChannelInfoListResult channels = EsChannelInfoDAO.getInstance()
                    .index(dataIndexName).query(channelQuery);

            totalHits = channels.getSize();
            LocalCache<String, Boolean> channelsCache = DatastoreCacheManager.getInstance().getChannelsCache();
            long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

            for (int i = 0; i < toBeProcessed; i++) {
                String id = channels.getItem(0).getId().toString();
                if (channelsCache.get(id))
                    channelsCache.remove(id);
            }
            if (totalHits > pageSize)
                offset += (pageSize + 1);
        }

        logger.debug(String.format("Removed cached channels for [%s]", channel));

        EsChannelInfoDAO.getInstance().index(dataIndexName)
                .deleteByQuery(channelQuery);

        logger.debug(String.format("Removed channels for [%s]", channel));
        //

        // Remove client
        if (isClientToDelete) {

            ClientInfoQueryImpl clientInfoQuery = new ClientInfoQueryImpl(scopeId);
            clientInfoQuery.setLimit(pageSize + 1);
            clientInfoQuery.setOffset(offset);

            channelPredicate = new ChannelMatchPredicateImpl();
            channelPredicate.setExpression(channel);
            clientInfoQuery.setPredicate(channelPredicate);

            offset = 0;
            totalHits = 1;
            while (totalHits > 0) {
                ClientInfoListResult clients = EsClientInfoDAO.getInstance()
                        .index(dataIndexName)
                        .query(clientInfoQuery);

                totalHits = clients.getSize();
                LocalCache<String, Boolean> clientsCache = DatastoreCacheManager.getInstance().getClientsCache();
                long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

                for (int i = 0; i < toBeProcessed; i++) {
                    String id = clients.getItem(i).getId().toString();
                    if (clientsCache.get(id))
                        clientsCache.remove(id);
                }
                if (totalHits > pageSize)
                    offset += (pageSize + 1);
            }

            logger.debug(String.format("Removed cached clients for [%s]", channel));

            EsClientInfoDAO.getInstance()
                    .index(dataIndexName)
                    .deleteByQuery(clientInfoQuery);

            logger.debug(String.format("Removed clients for [%s]", channel));
        }
    }
}
