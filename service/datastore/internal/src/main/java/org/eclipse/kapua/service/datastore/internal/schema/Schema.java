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
import java.util.regex.Pattern;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.model.IndexExistsRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexExistsResponse;
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

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_KEYWORD;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_INDEX;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_TYPE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.TYPE_STRING;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_REFRESH_INTERVAL;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.FIELD_NAME_PROPERTIES;

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
            IndexExistsResponse dataIndexExistsResponse = DatastoreClientFactory.getInstance().isIndexExists(new IndexExistsRequest(dataIndexName));
            if (!dataIndexExistsResponse.isIndexExists()) {
                datastoreClient.createIndex(dataIndexName, getMappingSchema());
                logger.info("Data index created: " + dataIndexName);
            }

            boolean enableAllField = false;
            boolean enableSourceField = true;

            datastoreClient.putMapping(new TypeDescriptor(dataIndexName, MessageSchema.MESSAGE_TYPE_NAME), MessageSchema.getMesageTypeSchema(enableAllField, enableSourceField));
            // Check existence of the kapua internal index
            String registryIndexName = DatastoreUtils.getRegistryIndexName(scopeId);
            IndexExistsResponse registryIndexExistsResponse = datastoreClient.isIndexExists(new IndexExistsRequest(registryIndexName));
            if (!registryIndexExistsResponse.isIndexExists()) {
                datastoreClient.createIndex(registryIndexName, getMappingSchema());
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
        final int METRIC_TERM = 0;
        // It is assumed the mappings (key values) are all of the type
        // metrics.metric_name.type
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode messageTypeNode = SchemaUtil.getObjectNode();// MESSAGE_TYPE_NAME
        ObjectNode propertiesRootNode = SchemaUtil.getObjectNode();// properties
        ObjectNode metricsNode = SchemaUtil.getObjectNode();// Schema.MESSAGE_METRICS
        ObjectNode propertiesNode = SchemaUtil.getObjectNode();// propertiesNode

        // TODO precondition for the loop: there are no two consecutive mappings for the same field with
        // two different types (field are all different)
        String[] prevKeySplit = new String[] { "", "" };
        Set<String> keys = esMetrics.keySet();
        String metricName = null;
        for (String key : keys) {
            Metric metric = esMetrics.get(key);
            String[] keySplit = key.split(Pattern.quote("."));

            if (!keySplit[METRIC_TERM].equals(prevKeySplit[METRIC_TERM])) {
                if (!prevKeySplit[METRIC_TERM].isEmpty()) {
                    metricsNode.set(FIELD_NAME_PROPERTIES, propertiesNode);
                    propertiesRootNode.set(MessageSchema.MESSAGE_METRICS, metricsNode);
                }
                metricsNode = SchemaUtil.getObjectNode();
                propertiesRootNode = SchemaUtil.getObjectNode();
                metricName = metric.getName();
            }
            if (!keySplit[METRIC_TERM].equals(prevKeySplit[METRIC_TERM])) {
                ObjectNode metricMapping = SchemaUtil.getObjectNode();
                ObjectNode metricMappingContent = null;
                if (metric.getType().equals(TYPE_STRING)) {
                    metricMappingContent = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, KEY_KEYWORD), new KeyValueEntry(KEY_INDEX, true) });
                } else {
                    metricMappingContent = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, metric.getType()) });
                }
                metricMapping.set(DatastoreUtils.getClientMetricFromAcronym(metric.getType()), metricMappingContent);
                propertiesNode.set(FIELD_NAME_PROPERTIES, metricMapping);
            }
            prevKeySplit = keySplit;
        }
        if (keys.size() > 0) {
            if (!prevKeySplit[METRIC_TERM].isEmpty()) {
                metricsNode.set(FIELD_NAME_PROPERTIES, propertiesNode);
                propertiesRootNode.set(metricName, metricsNode);
            }
        }
        metricsNode.set(FIELD_NAME_PROPERTIES, propertiesNode); // Properties
        propertiesRootNode.set(MessageSchema.MESSAGE_METRICS, metricsNode); // Metrics
        messageTypeNode.set(FIELD_NAME_PROPERTIES, propertiesRootNode);// Properties
        rootNode.set(MessageSchema.MESSAGE_TYPE_NAME, messageTypeNode);// Type
        return rootNode;
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

    private ObjectNode getMappingSchema() throws DatamodelMappingException {
        String idxRefreshInterval = String.format("%ss", DatastoreSettings.getInstance().getLong(DatastoreSettingKey.INDEX_REFRESH_INTERVAL));
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode refreshIntervaleNode = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_REFRESH_INTERVAL, idxRefreshInterval) });
        rootNode.set(KEY_INDEX, refreshIntervaleNode);
        return rootNode;
    }

}
