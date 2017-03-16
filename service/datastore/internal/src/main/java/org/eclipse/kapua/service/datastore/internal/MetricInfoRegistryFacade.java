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

import java.util.ArrayList;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsConfigurationException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema.Metadata;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsMetricInfoDAO;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Metric information registry facade
 * 
 * @since 1.0.0
 */
public class MetricInfoRegistryFacade {

    private static final Logger logger = LoggerFactory.getLogger(MetricInfoRegistryFacade.class);

    private final MetricInfoRegistryMediator mediator;
    private final ConfigurationProvider configProvider;
    private final Object metadataUpdateSync;

    /**
     * Constructs the metric info registry facade
     * 
     * @param configProvider
     * @param mediator
     * 
     * @since 1.0.0
     */
    public MetricInfoRegistryFacade(ConfigurationProvider configProvider, MetricInfoRegistryMediator mediator) {
        this.configProvider = configProvider;
        this.mediator = mediator;
        this.metadataUpdateSync = new Object();
    }

    /**
     * Update the metric information after a message store operation (for a single metric)
     * 
     * @param scopeId
     * @param metricInfo
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsDocumentBuilderException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     * 
     * @since 1.0.0
     */
    public StorableId upstore(MetricInfo metricInfo)
            throws KapuaIllegalArgumentException,
            EsDocumentBuilderException,
            EsClientUnavailableException,
            EsConfigurationException {
        //
        // Argument Validation
        ArgumentValidator.notNull(metricInfo, "metricInfoCreator");
        ArgumentValidator.notNull(metricInfo.getScopeId(), "metricInfo.scopeId");
        ArgumentValidator.notNull(metricInfo.getFirstMessageId(), "metricInfoCreator.firstPublishedMessageId");
        ArgumentValidator.notNull(metricInfo.getFirstMessageOn(), "metricInfoCreator.firstPublishedMessageTimestamp");

        String metricInfoId = MetricInfoXContentBuilder.getOrDeriveId(metricInfo.getId(), metricInfo);

        // Store channel. Look up channel in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getMetricsCache().get(metricInfoId)) {

            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its
            // update then the others of the same type will find the cache
            // updated and skip the update.
            synchronized (this.metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getChannelsCache().get(metricInfoId)) {
                    UpdateResponse response = null;
                    try {
                        Metadata metadata = this.mediator.getMetadata(metricInfo.getScopeId(), metricInfo.getFirstMessageOn().getTime());
                        String kapuaIndexName = metadata.getKapuaIndexName();

                        response = EsMetricInfoDAO.getInstance()
                                .index(metadata.getKapuaIndexName())
                                .upsert(metricInfo);

                        metricInfoId = response.getId();

                        logger.debug(String.format("Upsert on metric succesfully executed [%s.%s, %s]",
                                kapuaIndexName, EsSchema.METRIC_TYPE_NAME, metricInfoId));

                    } catch (DocumentAlreadyExistsException exc) {
                        logger.trace(String.format("Upsert failed because metric already exists [%s, %s]",
                                metricInfoId, exc.getMessage()));
                    }
                    // Update cache if channel update is completed successfully
                    DatastoreCacheManager.getInstance().getChannelsCache().put(metricInfoId, true);
                }
            }
        }
        return new StorableIdImpl(metricInfoId);
    }

    /**
     * Update the metrics informations after a message store operation (for few metrics)
     * 
     * @param scopeId
     * @param metricInfos
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsDocumentBuilderException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     * 
     * @since 1.0.0
     */
    public StorableId[] upstore(MetricInfo[] metricInfos)
            throws KapuaIllegalArgumentException,
            EsDocumentBuilderException,
            EsClientUnavailableException,
            EsConfigurationException {
        //
        // Argument Validation
        ArgumentValidator.notNull(metricInfos, "metricInfoCreator");

        // Create a bulk request
        BulkRequest bulkRequest = new BulkRequest();
        for (MetricInfo metricInfo : metricInfos) {
            String metricInfoId = MetricInfoXContentBuilder.getOrDeriveId(metricInfo.getId(), metricInfo);

            if (DatastoreCacheManager.getInstance().getMetricsCache().get(metricInfoId))
                continue;

            Metadata metadata = mediator.getMetadata(metricInfo.getScopeId(), metricInfo.getFirstMessageOn().getTime());
            String kapuaIndexName = metadata.getKapuaIndexName();

            EsMetricInfoDAO.getInstance().index(kapuaIndexName);

            bulkRequest.add(EsMetricInfoDAO.getInstance()
                    .index(kapuaIndexName)
                    .getUpsertRequest(metricInfo));
        }

        StorableId[] idResults = null;

        if (bulkRequest.numberOfActions() <= 0)
            return idResults;

        // The code is safe even without the synchronized block
        // Synchronize in order to let the first thread complete its update
        // then the others of the same type will find the cache updated and
        // skip the update.
        synchronized (this.metadataUpdateSync) {
            BulkResponse response = EsMetricInfoDAO.getInstance().bulk(bulkRequest);
            BulkItemResponse[] itemResponses = response.getItems();
            idResults = new StorableId[itemResponses.length];

            if (itemResponses != null) {
                for (BulkItemResponse bulkItemResponse : itemResponses) {
                    if (bulkItemResponse.isFailed()) {
                        MetricInfo failedMetricInfoCreator = metricInfos[bulkItemResponse.getItemId()];
                        String failureMessage = bulkItemResponse.getFailureMessage();
                        logger.trace(String.format("Upsert failed [%s, %s, %s]",
                                failedMetricInfoCreator.getChannel(), failedMetricInfoCreator.getName(), failureMessage));
                        continue;
                    }

                    String channelMetricId = ((UpdateResponse) bulkItemResponse.getResponse()).getId();
                    idResults[bulkItemResponse.getItemId()] = new StorableIdImpl(channelMetricId);

                    String kapuaIndexName = bulkItemResponse.getIndex();
                    String channelTypeName = bulkItemResponse.getType();
                    logger.debug(String.format("Upsert on channel metric succesfully executed [%s.%s, %s]",
                            kapuaIndexName, channelTypeName, channelMetricId));

                    if (DatastoreCacheManager.getInstance().getMetricsCache().get(channelMetricId))
                        continue;

                    // Update cache if channel metric update is completed
                    // successfully
                    DatastoreCacheManager.getInstance().getMetricsCache().put(channelMetricId, true);
                }
            }
        }
        return idResults;
    }

    /**
     * Delete metric information by identifier
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

        String indexName = EsSchema.getKapuaIndexName(scopeId);
        EsMetricInfoDAO.getInstance()
                .index(indexName)
                .deleteById(id.toString());
    }

    /**
     * Find metric information by identifier
     * 
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsObjectBuilderException
     * 
     * @since 1.0.0
     */
    public MetricInfo find(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsQueryConversionException,
            EsClientUnavailableException,
            EsObjectBuilderException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        MetricInfoQueryImpl query = new MetricInfoQueryImpl(scopeId);
        query.setLimit(1);

        ArrayList<StorableId> ids = new ArrayList<>();
        ids.add(id);

        AndPredicateImpl allPredicates = new AndPredicateImpl();
        allPredicates.addPredicate(new IdsPredicateImpl(EsSchema.MESSAGE_TYPE_NAME, ids));

        MetricInfoListResult result = query(query);
        return result.getFirstItem();
    }

    /**
     * Find metrics informations matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsObjectBuilderException
     * 
     * @since 1.0.0
     */
    public MetricInfoListResult query(MetricInfoQuery query)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsQueryConversionException,
            EsClientUnavailableException,
            EsObjectBuilderException {
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
            return new MetricInfoListResultImpl();
        }

        String indexNme = EsSchema.getKapuaIndexName(query.getScopeId());
        return EsMetricInfoDAO.getInstance()
                .index(indexNme)
                .query(query);
    }

    /**
     * Get metrics informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * 
     * @since 1.0.0
     */
    public long count(MetricInfoQuery query)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsClientUnavailableException,
            EsQueryConversionException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return 0;
        }

        String indexName = EsSchema.getKapuaIndexName(query.getScopeId());
        return EsMetricInfoDAO.getInstance()
                .index(indexName)
                .count(query);
    }

    /**
     * Delete metrics informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * 
     * @since 1.0.0
     */
    public void delete(MetricInfoQuery query)
            throws KapuaIllegalArgumentException,
            EsConfigurationException,
            EsClientUnavailableException,
            EsQueryConversionException {
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
        }

        String indexName = EsSchema.getKapuaIndexName(query.getScopeId());
        EsMetricInfoDAO.getInstance()
                .index(indexName)
                .deleteByQuery(query);
    }
}
