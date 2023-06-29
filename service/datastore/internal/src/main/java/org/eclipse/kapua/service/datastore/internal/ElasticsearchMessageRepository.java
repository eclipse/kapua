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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.InsertRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.predicate.IdsPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class ElasticsearchMessageRepository extends ElasticsearchRepository<DatastoreMessage, MessageQuery> implements MessageRepository {
    private final StorablePredicateFactory storablePredicateFactory;

    @Inject
    public ElasticsearchMessageRepository(
            ElasticsearchClientProvider elasticsearchClientProviderInstance,
            StorablePredicateFactory storablePredicateFactory) {
        super(elasticsearchClientProviderInstance,
                MessageSchema.MESSAGE_TYPE_NAME,
                DatastoreMessage.class);
        this.storablePredicateFactory = storablePredicateFactory;
    }

    @Override
    protected String indexResolver(KapuaId scopeId) {
        return DatastoreUtils.getDataIndexName(scopeId);
    }

    protected String indexResolver(KapuaId scopeId, Long time) {
        final String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingsKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
        return DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);
    }

    /**
     * Store a message
     *
     * @throws ClientException
     */
    @Override
    public String store(DatastoreMessage messageToStore) throws ClientException {
        final TypeDescriptor typeDescriptor;
        final Long messageTime = Optional.ofNullable(messageToStore.getTimestamp())
                .map(date -> date.getTime())
                .orElse(null);
        typeDescriptor = getDescriptor(indexResolver(messageToStore.getScopeId(), messageTime));
        InsertRequest insertRequest = new InsertRequest(messageToStore.getDatastoreId().toString(), typeDescriptor, messageToStore);
        return elasticsearchClientProviderInstance.getElasticsearchClient().insert(insertRequest).getId();
    }

    /**
     * Find message by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws QueryMappingException
     * @throws ClientException
     */
    @Override
    public DatastoreMessage find(KapuaId scopeId, String indexName, StorableId id)
            throws KapuaIllegalArgumentException, ClientException {
        return doFind(scopeId, indexName, id);
    }

    @Override
    public void refreshAllIndexes() throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().refreshAllIndexes();
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().deleteAllIndexes();
    }

    @Override
    public void deleteIndexes(String indexExp) throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().deleteIndexes(indexExp);
    }

    @Override
    public void upsertMappings(KapuaId scopeId, Map<String, Metric> esMetrics) throws ClientException, MappingException {
        final ObjectNode metricsMapping = getNewMessageMappingsBuilder(esMetrics);
        logger.trace("Sending dynamic message mappings: {}", metricsMapping);
        elasticsearchClientProviderInstance.getElasticsearchClient().putMapping(new TypeDescriptor(indexResolver(scopeId), MessageSchema.MESSAGE_TYPE_NAME), metricsMapping);
    }

    @Override
    public void upsertIndex(String dataIndexName) throws ClientException, MappingException {
        super.upsertIndex(dataIndexName);
    }

    @Override
    public void delete(KapuaId scopeId, String id, long time) throws ClientException {
        super.doDelete(id, indexResolver(scopeId, time));
    }

    @Override
    public void upsertIndex(KapuaId scopeId) throws ClientException, MappingException {
        final ElasticsearchClient elasticsearchClient = elasticsearchClientProviderInstance.getElasticsearchClient();
        // Check existence of the kapua internal indexes
        final String indexName = indexResolver(scopeId);
        IndexResponse indexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(indexName));
        if (!indexExistsResponse.isIndexExists()) {
            elasticsearchClient.createIndex(indexName, getMappingSchema(indexName));
            logger.info("Index created: {}", indexExistsResponse);
            elasticsearchClient.putMapping(getDescriptor(indexName), getIndexSchema());
        }
    }

    @Override
    JsonNode getIndexSchema() throws MappingException {
        return MessageSchema.getMessageTypeSchema();
    }

    /**
     * @param esMetrics
     * @return
     * @throws DatamodelMappingException
     * @throws KapuaException
     * @since 1.0.0
     */
    private ObjectNode getNewMessageMappingsBuilder(Map<String, Metric> esMetrics) throws MappingException {
        if (esMetrics == null) {
            return null;
        }
        // metrics mapping container (to be added to message mapping)
        ObjectNode typeNode = MappingUtils.newObjectNode(); // root
        ObjectNode typePropertiesNode = MappingUtils.newObjectNode(); // properties
        ObjectNode metricsNode = MappingUtils.newObjectNode(); // metrics
        ObjectNode metricsPropertiesNode = MappingUtils.newObjectNode(); // properties (metric properties)
        typeNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, typePropertiesNode);
        typePropertiesNode.set(SchemaKeys.FIELD_NAME_METRICS, metricsNode);
        metricsNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, metricsPropertiesNode);

        // metrics mapping
        ObjectNode metricMapping;
        for (Map.Entry<String, Metric> esMetric : esMetrics.entrySet()) {
            Metric metric = esMetric.getValue();
            metricMapping = MappingUtils.newObjectNode(new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_DYNAMIC, SchemaKeys.VALUE_TRUE)});

            ObjectNode metricMappingPropertiesNode = MappingUtils.newObjectNode(); // properties (inside metric name)
            ObjectNode valueMappingNode;

            switch (metric.getType()) {
                case SchemaKeys.TYPE_STRING:
                    valueMappingNode = MappingUtils.newObjectNode(new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE)});
                    break;
                case SchemaKeys.TYPE_DATE:
                    valueMappingNode = MappingUtils.newObjectNode(
                            new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT)});
                    break;
                default:
                    valueMappingNode = MappingUtils.newObjectNode(new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, metric.getType())});
                    break;
            }

            metricMappingPropertiesNode.set(DatastoreUtils.getClientMetricFromAcronym(metric.getType()), valueMappingNode);
            metricMapping.set(SchemaKeys.FIELD_NAME_PROPERTIES, metricMappingPropertiesNode);
            metricsPropertiesNode.set(metric.getName(), metricMapping);
        }
        return typeNode;
    }

    public DatastoreMessage doFind(KapuaId scopeId, String indexName, StorableId id) throws ClientException {
        MessageQueryImpl idsQuery = new MessageQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicate idsPredicate = storablePredicateFactory.newIdsPredicate(MessageSchema.MESSAGE_TYPE_NAME);
        idsPredicate.addId(id);
        idsQuery.setPredicate(idsPredicate);

        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, MessageSchema.MESSAGE_TYPE_NAME);
        final DatastoreMessage res = (DatastoreMessage) elasticsearchClientProviderInstance.getElasticsearchClient().<DatastoreMessage>find(typeDescriptor, idsQuery, DatastoreMessage.class);
        return res;
    }
}
