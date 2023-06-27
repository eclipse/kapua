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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.ChannelInfoRepository;
import org.eclipse.kapua.service.datastore.internal.ClientInfoRepository;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.MessageRepository;
import org.eclipse.kapua.service.datastore.internal.MetricInfoRepository;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.Metric;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
    private final MessageRepository messageRepository;
    private final ClientInfoRepository clientInfoRepository;
    private final ChannelInfoRepository channelInfoRepository;
    private final MetricInfoRepository metricInfoRepository;

    @Inject
    public Schema(MessageRepository messageRepository,
                  ClientInfoRepository clientInfoRepository,
                  ChannelInfoRepository channelInfoRepository,
                  MetricInfoRepository metricInfoRepository) {
        this.messageRepository = messageRepository;
        this.clientInfoRepository = clientInfoRepository;
        this.channelInfoRepository = channelInfoRepository;
        this.metricInfoRepository = metricInfoRepository;
    }

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
        String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingsKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
        dataIndexName = DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);

        Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(dataIndexName);
        if (currentMetadata != null) {
            return currentMetadata;
        }

        LOG.debug("Before entering updating metadata");
        synchronized (Schema.class) {
            messageRepository.upsertIndex(dataIndexName);
            channelInfoRepository.upsertIndex(scopeId);
            clientInfoRepository.upsertIndex(scopeId);
            metricInfoRepository.upsertIndex(scopeId);
            currentMetadata = new Metadata();
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
        String indexingWindowOption = DatastoreSettings.getInstance().getString(DatastoreSettingsKey.INDEXING_WINDOW_OPTION, DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
        newIndex = DatastoreUtils.getDataIndexName(scopeId, time, indexingWindowOption);
        final Metadata currentMetadata = DatastoreCacheManager.getInstance().getMetadataCache().get(newIndex);

        ObjectNode metricsMapping = null;
        Map<String, Metric> diffs = null;

        synchronized (Schema.class) {
            // Update mappings only if a metric is new (not in cache)
            diffs = getMessageMappingDiffs(currentMetadata, metrics);
            if (diffs == null || diffs.isEmpty()) {
                return;
            }
        }

        messageRepository.upsertMappings(scopeId, diffs);
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


}
