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
package org.eclipse.kapua.service.datastore.internal.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientErrorCodes;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Datastore schema creation/update
 *
 * @since 1.0.0
 */
public class Schema {

    private static final Logger LOG = LoggerFactory.getLogger(Schema.class);

    /**
     * Synchronize metadata
     *
     * @param scopeId
     * @param time
     * @return
     * @throws ClientException
     * @since 1.0.0
     */
    public Metadata synch(KapuaId scopeId, long time) throws ClientException, MappingException {
        String dataIndexName;
        try {
            String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingsKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
            dataIndexName = DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);
        } catch (KapuaException kaex) {
            throw new ClientException(ClientErrorCodes.INTERNAL_ERROR, kaex, "Error while generating index name");
        }

        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(dataIndexName);
        if (currentMetadata != null) {
            return currentMetadata;
        }

        LOG.debug("Before entering updating metadata");
        synchronized (Schema.class) {
            LOG.debug("Entered updating metadata");
            ElasticsearchClient<?> elasticsearchClient = DatastoreClientFactory.getInstance().getElasticsearchClient();
            // Check existence of the data index
            IndexResponse dataIndexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(dataIndexName));
            if (!dataIndexExistsResponse.isIndexExists()) {
                elasticsearchClient.createIndex(dataIndexName, getMappingSchema(dataIndexName));
                LOG.info("Data index created: {}", dataIndexName);
            }

            boolean enableSourceField = true;

            elasticsearchClient.putMapping(new TypeDescriptor(dataIndexName, MessageSchema.MESSAGE_TYPE_NAME), MessageSchema.getMesageTypeSchema(enableSourceField));

            // Check existence of the kapua internal indexes
            String channelRegistryIndexName = DatastoreUtils.getChannelIndexName(scopeId);
            IndexResponse channelRegistryIndexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(channelRegistryIndexName));
            if (!channelRegistryIndexExistsResponse.isIndexExists()) {
                elasticsearchClient.createIndex(channelRegistryIndexName, getMappingSchema(channelRegistryIndexName));
                LOG.info("Channel Metadata index created: {}", channelRegistryIndexExistsResponse);

                elasticsearchClient.putMapping(new TypeDescriptor(channelRegistryIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME), ChannelInfoSchema.getChannelTypeSchema(enableSourceField));
            }

            String clientRegistryIndexName = DatastoreUtils.getClientIndexName(scopeId);
            IndexResponse clientRegistryIndexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(clientRegistryIndexName));
            if (!clientRegistryIndexExistsResponse.isIndexExists()) {
                elasticsearchClient.createIndex(clientRegistryIndexName, getMappingSchema(clientRegistryIndexName));
                LOG.info("Client Metadata index created: {}", clientRegistryIndexExistsResponse);

                elasticsearchClient.putMapping(new TypeDescriptor(clientRegistryIndexName, ClientInfoSchema.CLIENT_TYPE_NAME), ClientInfoSchema.getClientTypeSchema(enableSourceField));
            }

            String metricRegistryIndexName = DatastoreUtils.getMetricIndexName(scopeId);
            IndexResponse metricRegistryIndexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(metricRegistryIndexName));
            if (!metricRegistryIndexExistsResponse.isIndexExists()) {
                elasticsearchClient.createIndex(metricRegistryIndexName, getMappingSchema(metricRegistryIndexName));
                LOG.info("Metric Metadata index created: {}", metricRegistryIndexExistsResponse);

                elasticsearchClient.putMapping(new TypeDescriptor(metricRegistryIndexName, MetricInfoSchema.METRIC_TYPE_NAME), MetricInfoSchema.getMetricTypeSchema(enableSourceField));
            }

            currentMetadata = new Metadata(dataIndexName, channelRegistryIndexName, clientRegistryIndexName, metricRegistryIndexName);
            LOG.debug("Leaving updating metadata");
        }

        // Current metadata can only increase the custom mappings
        // other fields does not change within the same account id
        // and custom mappings are not and must not be exposed to
        // outside this class to preserve thread safetyness
        DatastoreCacheManager.getInstance().getMetadataCache().put(dataIndexName, currentMetadata);

        return currentMetadata;
    }

    /**
     * Update metric mappings
     *
     * @param scopeId
     * @param time
     * @param metrics
     * @throws ClientException
     * @since 1.0.0
     */
    public void updateMessageMappings(KapuaId scopeId, long time, Map<String, Metric> metrics)
            throws ClientException, MappingException {
        if (metrics == null || metrics.size() == 0) {
            return;
        }
        String newIndex;
        try {
            String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingsKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
            newIndex = DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);
        } catch (KapuaException kaex) {
            throw new ClientException(ClientErrorCodes.INTERNAL_ERROR, kaex, "Error while generating index name");
        }
        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(newIndex);

        ObjectNode metricsMapping = null;
        Map<String, Metric> diffs = null;

        synchronized (Schema.class) {
            // Update mappings only if a metric is new (not in cache)
            diffs = getMessageMappingDiffs(currentMetadata, metrics);
            if (diffs == null || diffs.isEmpty()) {
                return;
            }
            metricsMapping = getNewMessageMappingsBuilder(diffs);
        }

        LOG.trace("Sending dynamic message mappings: {}", metricsMapping);
        DatastoreClientFactory.getInstance().getElasticsearchClient().putMapping(new TypeDescriptor(currentMetadata.getDataIndexName(), MessageSchema.MESSAGE_TYPE_NAME), metricsMapping);
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
        for (Entry<String, Metric> esMetric : esMetrics.entrySet()) {
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

    /**
     * @param currentMetadata
     * @param esMetrics
     * @return
     * @since 1.0.0
     */
    private Map<String, Metric> getMessageMappingDiffs(Metadata currentMetadata, Map<String, Metric> esMetrics) {
        if (esMetrics == null || esMetrics.isEmpty()) {
            return null;
        }

        Map<String, Metric> diffs = null;
        for (Entry<String, Metric> esMetric : esMetrics.entrySet()) {
            if (!currentMetadata.getMessageMappingsCache().containsKey(esMetric.getKey())) {
                if (diffs == null) {
                    diffs = new HashMap<>(100);
                }
                currentMetadata.getMessageMappingsCache().put(esMetric.getKey(), esMetric.getValue());
                diffs.put(esMetric.getKey(), esMetric.getValue());
            }
        }

        return diffs;
    }

    /**
     * @param idxName
     * @return
     * @throws MappingException
     * @since 1.0.0
     */
    private ObjectNode getMappingSchema(String idxName) throws MappingException {
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
        LOG.info("Creating index for '{}' - refresh: '{}' - shards: '{}' replicas: '{}': ", idxName, idxRefreshInterval, idxShardNumber, idxReplicaNumber);
        return rootNode;
    }

}
