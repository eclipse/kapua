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

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateResponse;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;
import org.eclipse.kapua.service.storable.model.query.predicate.IdsPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Metric information registry facade
 *
 * @since 1.0.0
 */
public class MetricInfoRegistryFacadeImpl extends AbstractRegistryFacade implements MetricInfoRegistryFacade {

    private static final Logger LOG = LoggerFactory.getLogger(MetricInfoRegistryFacadeImpl.class);

    private final StorableIdFactory storableIdFactory;
    private final StorablePredicateFactory storablePredicateFactory;
    private final MetricInfoRegistryMediator mediator;
    private final MetricInfoRepository repository;

    private static final String QUERY = "query";
    private static final String QUERY_SCOPE_ID = "query.scopeId";
    private static final String STORAGE_NOT_ENABLED = "Storage not enabled for account {}, returning empty result";

    /**
     * Constructs the metric info registry facade
     *
     * @param configProvider
     * @param mediator
     * @param metricInfoRepository
     * @since 1.0.0
     */
    public MetricInfoRegistryFacadeImpl(ConfigurationProvider configProvider,
                                        StorableIdFactory storableIdFactory,
                                        StorablePredicateFactory storablePredicateFactory,
                                        MetricInfoRegistryMediator mediator,
                                        MetricInfoRepository metricInfoRepository) {
        super(configProvider);
        this.storableIdFactory = storableIdFactory;
        this.storablePredicateFactory = storablePredicateFactory;
        this.mediator = mediator;
        this.repository = metricInfoRepository;
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
    @Override
    public StorableId upstore(MetricInfo metricInfo) throws KapuaIllegalArgumentException, ConfigurationException, ClientException, MappingException {
        ArgumentValidator.notNull(metricInfo, "metricInfo");
        ArgumentValidator.notNull(metricInfo.getScopeId(), "metricInfo.scopeId");
        ArgumentValidator.notNull(metricInfo.getFirstMessageId(), "metricInfoCreator.firstPublishedMessageId");
        ArgumentValidator.notNull(metricInfo.getFirstMessageOn(), "metricInfoCreator.firstPublishedMessageTimestamp");

        String metricInfoId = MetricInfoField.getOrDeriveId(metricInfo.getId(), metricInfo);
        StorableId storableId = storableIdFactory.newStorableId(metricInfoId);

        // Store channel. Look up channel in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getMetricsCache().get(metricInfoId)) {
            // fix #REPLACE_ISSUE_NUMBER
            MetricInfo storedField = find(metricInfo.getScopeId(), storableId);
            if (storedField == null) {
                Metadata metadata = mediator.getMetadata(metricInfo.getScopeId(), metricInfo.getFirstMessageOn().getTime());
                String kapuaIndexName = metadata.getMetricRegistryIndexName();
                final String responseId = repository.upsert(kapuaIndexName, metricInfo);
                LOG.debug("Upsert on metric successfully executed [{}.{}, {} - {}]", kapuaIndexName, MetricInfoSchema.METRIC_TYPE_NAME, metricInfoId, responseId);
            }
            // Update cache if metric update is completed successfully
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
    @Override
    public BulkUpdateResponse upstore(MetricInfo[] metricInfos)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException,
            MappingException {
        ArgumentValidator.notNull(metricInfos, "metricInfos");

        // Create a bulk request
        final List<Pair<String, MetricInfo>> toUpsert = new ArrayList<>();
        for (MetricInfo metricInfo : metricInfos) {
            String metricInfoId = MetricInfoField.getOrDeriveId(metricInfo.getId(), metricInfo);
            // fix #REPLACE_ISSUE_NUMBER
            if (!DatastoreCacheManager.getInstance().getMetricsCache().get(metricInfoId)) {
                StorableId storableId = storableIdFactory.newStorableId(metricInfoId);
                MetricInfo storedField = find(metricInfo.getScopeId(), storableId);
                if (storedField != null) {
                    DatastoreCacheManager.getInstance().getMetricsCache().put(metricInfoId, true);
                    continue;
                }
                Metadata metadata = mediator.getMetadata(metricInfo.getScopeId(), metricInfo.getFirstMessageOn().getTime());
                toUpsert.add(Pair.of(metadata.getMetricRegistryIndexName(), metricInfo));
            }
        }

        BulkUpdateResponse upsertResponse = null;
        if (!toUpsert.isEmpty()) {
            // execute the upstore
            try {
                upsertResponse = repository.upsert(toUpsert);
            } catch (ClientException e) {
                LOG.trace("Upsert failed {}", e.getMessage());
                throw e;
            }

            if (upsertResponse != null) {
                if (upsertResponse.getResponse().isEmpty()) {
                    return upsertResponse;
                }

                for (UpdateResponse response : upsertResponse.getResponse()) {
                    String index = response.getTypeDescriptor().getIndex();
                    String type = response.getTypeDescriptor().getType();
                    String id = response.getId();
                    LOG.debug("Upsert on channel metric successfully executed [{}.{}, {}]", index, type, id);

                    if (id == null || DatastoreCacheManager.getInstance().getMetricsCache().get(id)) {
                        continue;
                    }

                    // Update cache if channel metric update is completed successfully
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
    @Override
    public void delete(KapuaId scopeId, StorableId id) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        if (!isDatastoreServiceEnabled(scopeId)) {
            LOG.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        String indexName = SchemaUtil.getMetricIndexName(scopeId);
        repository.delete(indexName, id.toString());
    }

    /**
     * Find metric information by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    @Override
    public MetricInfo find(KapuaId scopeId, StorableId id) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        MetricInfoQueryImpl idsQuery = new MetricInfoQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicate idsPredicate = storablePredicateFactory.newIdsPredicate(MetricInfoSchema.METRIC_TYPE_NAME);
        idsPredicate.addId(id);
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
     * @throws ClientException
     */
    @Override
    public MetricInfoListResult query(MetricInfoQuery query) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug(STORAGE_NOT_ENABLED, query.getScopeId());
            return new MetricInfoListResultImpl();
        }

        String indexName = SchemaUtil.getMetricIndexName(query.getScopeId());
        final ResultList<MetricInfo> queried = repository.query(indexName, query);
        MetricInfoListResult result = new MetricInfoListResultImpl(queried);
        setLimitExceed(query, queried.getTotalHitsExceedsCount(), result);
        return result;
    }

    /**
     * Get metrics informations count matching the given query
     *
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    @Override
    public long count(MetricInfoQuery query) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug(STORAGE_NOT_ENABLED, query.getScopeId());
            return 0;
        }

        String indexName = SchemaUtil.getMetricIndexName(query.getScopeId());
        return repository.count(indexName, query);
    }

    /**
     * Delete metrics informations count matching the given query.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the metric info registry entries that matching the query without checking the consistency of the others registries or the message store.</b>
     *
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    @Override
    public void delete(MetricInfoQuery query) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug(STORAGE_NOT_ENABLED, query.getScopeId());
            return;
        }

        String indexName = SchemaUtil.getMetricIndexName(query.getScopeId());
        repository.delete(indexName, query);
    }
}
