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
package org.eclipse.kapua.service.datastore.internal.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.SchemaKeys;
import org.eclipse.kapua.service.datastore.client.model.IndexRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexResponse;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Datastore schema creation/update
 * 
 * @since 1.0
 *
 */
public class Schema {

    private static final Logger logger = LoggerFactory.getLogger(Schema.class);

    /**
     * Construct the Elasticsearch schema
     * 
     */
    public Schema() {
    }

    /**
     * Synchronize metadata
     * 
     * @param scopeId
     * @param time
     * @return
     * @throws ClientException
     */
    public Metadata synch(KapuaId scopeId, long time)
            throws ClientException {
        String dataIndexName = DatastoreUtils.getDataIndexName(scopeId, time);
        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(dataIndexName);
        if (currentMetadata != null) {
            return currentMetadata;
        }

        logger.debug("Before entering updating metadata");
        synchronized (Schema.class) {
            logger.debug("Entered updating metadata");
            DatastoreClient datastoreClient = DatastoreClientFactory.getInstance();
            // Check existence of the data index
            IndexResponse dataIndexExistsResponse = datastoreClient.isIndexExists(new IndexRequest(dataIndexName));
            if (!dataIndexExistsResponse.isIndexExists()) {
                datastoreClient.createIndex(dataIndexName, getMappingSchema(dataIndexName));
                logger.info("Data index created: " + dataIndexName);
            }

            boolean enableAllField = false;
            boolean enableSourceField = true;

            datastoreClient.putMapping(new TypeDescriptor(dataIndexName, MessageSchema.MESSAGE_TYPE_NAME), MessageSchema.getMesageTypeSchema(enableAllField, enableSourceField));
            // Check existence of the kapua internal index
            String registryIndexName = DatastoreUtils.getRegistryIndexName(scopeId);
            IndexResponse registryIndexExistsResponse = datastoreClient.isIndexExists(new IndexRequest(registryIndexName));
            if (!registryIndexExistsResponse.isIndexExists()) {
                datastoreClient.createIndex(registryIndexName, getMappingSchema(registryIndexName));
                logger.info("Metadata index created: " + registryIndexExistsResponse);

                datastoreClient.putMapping(new TypeDescriptor(registryIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME), ChannelInfoSchema.getChannelTypeSchema(enableAllField, enableSourceField));
                datastoreClient.putMapping(new TypeDescriptor(registryIndexName, MetricInfoSchema.METRIC_TYPE_NAME), MetricInfoSchema.getMetricTypeSchema(enableAllField, enableSourceField));
                datastoreClient.putMapping(new TypeDescriptor(registryIndexName, ClientInfoSchema.CLIENT_TYPE_NAME), ClientInfoSchema.getClientTypeSchema(enableAllField, enableSourceField));
            }

            currentMetadata = new Metadata(dataIndexName, registryIndexName);
            logger.debug("Leaving updating metadata");
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
     */
    public void updateMessageMappings(KapuaId scopeId, long time, Map<String, Metric> metrics)
            throws ClientException {
        if (metrics == null || metrics.size() == 0) {
            return;
        }
        String newIndex = DatastoreUtils.getDataIndexName(scopeId, time);
        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(newIndex);

        ObjectNode metricsMapping = null;
        Map<String, Metric> diffs = null;

        synchronized (Schema.class) {
            // Update mappings only if a metric is new (not in cache)
            diffs = getMessageMappingDiffs(currentMetadata, metrics);
            if (diffs == null || diffs.size() == 0) {
                return;
            }
            metricsMapping = getNewMessageMappingsBuilder(diffs);
        }

        logger.trace("Sending dynamic message mappings: " + metricsMapping);
        DatastoreClientFactory.getInstance().putMapping(new TypeDescriptor(currentMetadata.getDataIndexName(), MessageSchema.MESSAGE_TYPE_NAME), metricsMapping);
    }

    private ObjectNode getNewMessageMappingsBuilder(Map<String, Metric> esMetrics) throws DatamodelMappingException {
        if (esMetrics == null) {
            return null;
        }
        // metrics mapping container (to be added to message mapping)
        ObjectNode typeNode = SchemaUtil.getObjectNode(); // root
        ObjectNode messageNode = SchemaUtil.getObjectNode(); // message
        ObjectNode typePropertiesNode = SchemaUtil.getObjectNode(); // properties
        ObjectNode metricsNode = SchemaUtil.getObjectNode(); // metrics
        ObjectNode metricsPropertiesNode = SchemaUtil.getObjectNode(); // properties (metric properties)
        typeNode.set(SchemaKeys.FIELD_NAME_MESSAGE, messageNode);
        messageNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, typePropertiesNode);
        typePropertiesNode.set(SchemaKeys.FIELD_NAME_METRICS, metricsNode);
        metricsNode.set(SchemaKeys.FIELD_NAME_PROPERTIES, metricsPropertiesNode);

        // metrics mapping
        Set<String> keys = esMetrics.keySet();
        ObjectNode metricMapping;
        for (String key : keys) {
            Metric metric = esMetrics.get(key);
            metricMapping = SchemaUtil.getField(new KeyValueEntry[] {
                    new KeyValueEntry(SchemaKeys.KEY_DYNAMIC, SchemaKeys.VALUE_TRUE) });
            ObjectNode matricMappingPropertiesNode = SchemaUtil.getObjectNode(); // properties (inside metric name)
            ObjectNode valueMappingNode;
            if (metric.getType().equals(SchemaKeys.TYPE_STRING)) {
                valueMappingNode = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
            } else if (metric.getType().equals(SchemaKeys.TYPE_DATE)) {
                valueMappingNode = SchemaUtil.getField(
                        new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
            } else {
                valueMappingNode = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, metric.getType()) });
            }
            matricMappingPropertiesNode.set(DatastoreUtils.getClientMetricFromAcronym(metric.getType()), valueMappingNode);
            metricMapping.set(SchemaKeys.FIELD_NAME_PROPERTIES, matricMappingPropertiesNode);
            metricsPropertiesNode.set(metric.getName(), metricMapping);
        }
        return typeNode;
    }

    private Map<String, Metric> getMessageMappingDiffs(Metadata currentMetadata, Map<String, Metric> esMetrics) {
        if (esMetrics == null || esMetrics.size() == 0) {
            return null;
        }

        Entry<String, Metric> el;
        Map<String, Metric> diffs = null;
        Iterator<Entry<String, Metric>> iter = esMetrics.entrySet().iterator();
        while (iter.hasNext()) {
            el = iter.next();
            if (!currentMetadata.getMessageMappingsCache().containsKey(el.getKey())) {
                if (diffs == null) {
                    diffs = new HashMap<String, Metric>(100);
                }
                currentMetadata.getMessageMappingsCache().put(el.getKey(), el.getValue());
                diffs.put(el.getKey(), el.getValue());
            }
        }
        return diffs;
    }

    private ObjectNode getMappingSchema(String idxName) throws DatamodelMappingException {
        String idxRefreshInterval = String.format("%ss", DatastoreSettings.getInstance().getLong(DatastoreSettingKey.INDEX_REFRESH_INTERVAL));
        Integer idxShardNumber = DatastoreSettings.getInstance().getInt(DatastoreSettingKey.INDEX_SHARD_NUMBER, 1);
        Integer idxReplicaNumber = DatastoreSettings.getInstance().getInt(DatastoreSettingKey.INDEX_REPLICA_NUMBER, 0);
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode refreshIntervaleNode = SchemaUtil.getField(new KeyValueEntry[] {
                new KeyValueEntry(SchemaKeys.KEY_REFRESH_INTERVAL, idxRefreshInterval),
                new KeyValueEntry(SchemaKeys.KEY_SHARD_NUMBER, idxShardNumber),
                new KeyValueEntry(SchemaKeys.KEY_REPLICA_NUMBER, idxReplicaNumber) });
        rootNode.set(SchemaKeys.KEY_INDEX, refreshIntervaleNode);
        logger.info("Creating index for '{}' - refresh: '{}' - shards: '{}' replicas: '{}': ", new Object[] { idxName, idxRefreshInterval, idxShardNumber, idxReplicaNumber });
        return rootNode;
    }

}
