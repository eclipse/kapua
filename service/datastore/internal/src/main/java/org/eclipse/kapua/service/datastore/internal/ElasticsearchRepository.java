/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ElasticsearchRepository<T extends Storable, Q extends StorableQuery> implements DatastoreRepository<T, Q> {
    protected final ElasticsearchClientProvider elasticsearchClientProviderInstance;
    protected final String type;
    private final Class<T> clazz;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ElasticsearchRepository(
            ElasticsearchClientProvider elasticsearchClientProviderInstance,
            String type,
            Class<T> clazz
    ) {
        this.elasticsearchClientProviderInstance = elasticsearchClientProviderInstance;
        this.type = type;
        this.clazz = clazz;
    }

    protected abstract String indexResolver(KapuaId scopeId);

    @Override
    public ResultList<T> query(Q query) throws ClientException {
        final ResultList<T> res = elasticsearchClientProviderInstance.getElasticsearchClient().query(getDescriptor(indexResolver(query.getScopeId())), query, clazz);
        return res;
    }

    @Override
    public long count(Q query) throws ClientException {
        return elasticsearchClientProviderInstance.getElasticsearchClient().count(getDescriptor(indexResolver(query.getScopeId())), query);
    }

    @Override
    public void delete(KapuaId scopeId, String id) throws ClientException {
        final String indexName = indexResolver(scopeId);
        doDelete(id, indexName);
    }

    protected void doDelete(String id, String indexName) throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().delete(getDescriptor(indexName), id);
    }

    @Override
    public void delete(Q query) throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().deleteByQuery(getDescriptor(indexResolver(query.getScopeId())), query);
    }

    @Override
    public void upsertIndex(KapuaId scopeId) throws ClientException, MappingException {
        final String indexName = indexResolver(scopeId);
        upsertIndex(indexName);
    }

    protected void upsertIndex(String indexName) throws ClientException, MappingException {
        final ElasticsearchClient elasticsearchClient = elasticsearchClientProviderInstance.getElasticsearchClient();
        // Check existence of the kapua internal indexes
        IndexResponse indexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(indexName));
        if (!indexExistsResponse.isIndexExists()) {
            elasticsearchClient.createIndex(indexName, getMappingSchema(indexName));
            logger.info("Index created: {}", indexExistsResponse);
            elasticsearchClient.putMapping(getDescriptor(indexName), getIndexSchema());
        }
    }

    abstract JsonNode getIndexSchema() throws MappingException;

    /**
     * @param idxName
     * @return
     * @throws MappingException
     * @since 1.0.0
     */
    protected ObjectNode getMappingSchema(String idxName) throws MappingException {
        String idxRefreshInterval = String.format("%ss", DatastoreSettings.getInstance().getLong(DatastoreSettingsKey.INDEX_REFRESH_INTERVAL));
        Integer idxShardNumber = DatastoreSettings.getInstance().getInt(DatastoreSettingsKey.INDEX_SHARD_NUMBER, 1);
        Integer idxReplicaNumber = DatastoreSettings.getInstance().getInt(DatastoreSettingsKey.INDEX_REPLICA_NUMBER, 0);

        ObjectNode rootNode = MappingUtils.newObjectNode();
        ObjectNode settingsNode = MappingUtils.newObjectNode();
        ObjectNode refreshIntervalNode = MappingUtils.newObjectNode(new KeyValueEntry[]{
                new KeyValueEntry(SchemaKeys.KEY_REFRESH_INTERVAL, idxRefreshInterval),
                new KeyValueEntry(SchemaKeys.KEY_SHARD_NUMBER, idxShardNumber),
                new KeyValueEntry(SchemaKeys.KEY_REPLICA_NUMBER, idxReplicaNumber)});
        settingsNode.set(SchemaKeys.KEY_INDEX, refreshIntervalNode);
        rootNode.set(SchemaKeys.KEY_SETTINGS, settingsNode);
        logger.info("Creating index for '{}' - refresh: '{}' - shards: '{}' replicas: '{}': ", idxName, idxRefreshInterval, idxShardNumber, idxReplicaNumber);
        return rootNode;
    }

    public TypeDescriptor getDescriptor(String indexName) {
        return new TypeDescriptor(indexName, type);
    }
}
