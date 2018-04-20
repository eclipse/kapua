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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientErrorMessages;
import org.eclipse.kapua.service.datastore.client.QueryMappingException;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.client.model.UpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.UpdateResponse;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
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
    private DatastoreClient client;

    /**
     * Constructs the metric info registry facade
     *
     * @param configProvider
     * @param mediator
     * @throws ClientUnavailableException
     * 
     * @since 1.0.0
     */
    public MetricInfoRegistryFacade(ConfigurationProvider configProvider, MetricInfoRegistryMediator mediator) throws ClientUnavailableException {
        this.configProvider = configProvider;
        this.mediator = mediator;
        client = DatastoreClientFactory.getInstance();
    }

    /**
     * Update the metric information after a message store operation (for a single metric)
     * 
     * @param metricInfo
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public StorableId upstore(MetricInfo metricInfo)
            throws KapuaIllegalArgumentException,
            ConfigurationException, ClientException {
        ArgumentValidator.notNull(metricInfo, "metricInfo");
        ArgumentValidator.notNull(metricInfo.getScopeId(), "metricInfo.scopeId");
        ArgumentValidator.notNull(metricInfo.getFirstMessageId(), "metricInfoCreator.firstPublishedMessageId");
        ArgumentValidator.notNull(metricInfo.getFirstMessageOn(), "metricInfoCreator.firstPublishedMessageTimestamp");

        String metricInfoId = MetricInfoField.getOrDeriveId(metricInfo.getId(), metricInfo);
        StorableId storableId = new StorableIdImpl(metricInfoId);

        UpdateResponse response = null;
        // Store channel. Look up channel in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getMetricsCache().get(metricInfoId)) {
            // fix #REPLACE_ISSUE_NUMBER
            MetricInfo storedField = find(metricInfo.getScopeId(), storableId);
            if (storedField == null) {
                Metadata metadata = mediator.getMetadata(metricInfo.getScopeId(), metricInfo.getFirstMessageOn().getTime());
                String kapuaIndexName = metadata.getRegistryIndexName();

                UpdateRequest request = new UpdateRequest(metricInfo.getId().toString(), new TypeDescriptor(metadata.getRegistryIndexName(), MetricInfoSchema.METRIC_TYPE_NAME), metricInfo);
                response = client.upsert(request);

                if (!metricInfoId.equals(response.getId())) {
                    // this condition shouldn't happens
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, String.format(ClientErrorMessages.CRUD_INTERNAL_ERROR, "MetricInfoRegistry - upstore"));
                }
                logger.debug(String.format("Upsert on metric succesfully executed [%s.%s, %s]",
                        kapuaIndexName, MetricInfoSchema.METRIC_TYPE_NAME, metricInfoId));
                // Update cache if channel update is completed successfully
            }
            DatastoreCacheManager.getInstance().getMetricsCache().put(metricInfoId, true);
        }
        return storableId;
    }

    /**
     * Update the metrics informations after a message store operation (for few metrics)
     * 
     * @param metricInfos
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public BulkUpdateResponse upstore(MetricInfo[] metricInfos)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        ArgumentValidator.notNull(metricInfos, "metricInfos");

        BulkUpdateRequest bulkRequest = new BulkUpdateRequest();
        boolean performUpdate = false;
        // Create a bulk request
        for (MetricInfo metricInfo : metricInfos) {
            String metricInfoId = MetricInfoField.getOrDeriveId(metricInfo.getId(), metricInfo);
            // fix #REPLACE_ISSUE_NUMBER
            if (!DatastoreCacheManager.getInstance().getMetricsCache().get(metricInfoId)) {
                StorableId storableId = new StorableIdImpl(metricInfoId);
                MetricInfo storedField = find(metricInfo.getScopeId(), storableId);
                if (storedField != null) {
                    DatastoreCacheManager.getInstance().getMetricsCache().put(metricInfoId, true);
                    continue;
                }
                performUpdate = true;
                Metadata metadata = mediator.getMetadata(metricInfo.getScopeId(), metricInfo.getFirstMessageOn().getTime());
                bulkRequest.add(
                        new UpdateRequest(metricInfo.getId().toString(), new TypeDescriptor(metadata.getRegistryIndexName(), MetricInfoSchema.METRIC_TYPE_NAME), metricInfo));
            }
        }

        BulkUpdateResponse upsertResponse = null;
        if (performUpdate) {
            // execute the upstore
            try {
                upsertResponse = client.upsert(bulkRequest);
            } catch (ClientException e) {
                logger.trace(String.format("Upsert failed [%s]", e.getMessage()));
                throw e;
            }

            if (upsertResponse != null) {
                if (upsertResponse.getResponse().size() <= 0) {
                    return upsertResponse;
                }
                for (UpdateResponse response : upsertResponse.getResponse()) {
                    String index = response.getTypeDescriptor().getIndex();
                    String type = response.getTypeDescriptor().getType();
                    String id = response.getId();
                    logger.debug(String.format("Upsert on channel metric succesfully executed [%s.%s, %s]",
                            index, type, id));

                    if (id == null || DatastoreCacheManager.getInstance().getMetricsCache().get(id)) {
                        continue;
                    }

                    // Update cache if channel metric update is completed
                    // successfully
                    DatastoreCacheManager.getInstance().getMetricsCache().put(id, true);
                }
            }
        }
        return upsertResponse;
    }

    /**
     * Delete metric information by identifier.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the metric info registry entry by id without checking the consistency of the others registries or the message store.</b>
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

        String indexName = SchemaUtil.getKapuaIndexName(scopeId);
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MetricInfoSchema.METRIC_TYPE_NAME);
        client.delete(typeDescriptor, id.toString());
    }

    /**
     * Find metric information by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public MetricInfo find(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        MetricInfoQueryImpl idsQuery = new MetricInfoQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicateImpl idsPredicate = new IdsPredicateImpl(MetricInfoSchema.METRIC_TYPE_NAME);
        idsPredicate.addValue(id);
        idsQuery.setPredicate(idsPredicate);

        MetricInfoListResult result = query(idsQuery);
        return result.getFirstItem();
    }

    /**
     * Find metrics informations matching the given query
     * 
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public MetricInfoListResult query(MetricInfoQuery query)
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
            return new MetricInfoListResultImpl();
        }

        String indexNme = SchemaUtil.getKapuaIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexNme, MetricInfoSchema.METRIC_TYPE_NAME);
        ResultList<MetricInfo> result = client.query(typeDescriptor, query, MetricInfo.class);
        return new MetricInfoListResultImpl(result);
    }

    /**
     * Get metrics informations count matching the given query
     * 
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public long count(MetricInfoQuery query)
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

        String indexName = SchemaUtil.getKapuaIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MetricInfoSchema.METRIC_TYPE_NAME);
        return client.count(typeDescriptor, query);
    }

    /**
     * Delete metrics informations count matching the given query.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the metric info registry entries that matching the query without checking the consistency of the others registries or the message store.</b>
     * 
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public void delete(MetricInfoQuery query)
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
        }

        String indexName = SchemaUtil.getKapuaIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MetricInfoSchema.METRIC_TYPE_NAME);
        client.deleteByQuery(typeDescriptor, query);
    }
}
