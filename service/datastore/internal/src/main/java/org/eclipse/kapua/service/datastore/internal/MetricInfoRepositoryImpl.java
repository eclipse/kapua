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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;
import org.eclipse.kapua.service.storable.exception.MappingException;

import javax.inject.Inject;
import java.util.List;

public class MetricInfoRepositoryImpl extends ElasticsearchRepository<MetricInfo, MetricInfoQuery> implements MetricInfoRepository {

    @Inject
    protected MetricInfoRepositoryImpl(
            ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        super(elasticsearchClientProviderInstance,
                MetricInfoSchema.METRIC_TYPE_NAME,
                MetricInfo.class);
    }

    @Override
    protected String indexResolver(KapuaId scopeId) {
        return DatastoreUtils.getMetricIndexName(scopeId);
    }

    @Override
    public String upsert(String metricInfoId, MetricInfo metricInfo) throws ClientException {
        UpdateRequest request = new UpdateRequest(metricInfo.getId().toString(), getDescriptor(indexResolver(metricInfo.getScopeId())), metricInfo);
        final String responseId = elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request).getId();
        logger.debug("Upsert on metric successfully executed [{}.{}, {} - {}]", DatastoreUtils.getMetricIndexName(metricInfo.getScopeId()), MetricInfoSchema.METRIC_TYPE_NAME, metricInfoId, responseId);
        return responseId;
    }

    @Override
    public BulkUpdateResponse upsert(List<MetricInfo> metricInfos) throws ClientException {
        final BulkUpdateRequest bulkUpdateRequest = new BulkUpdateRequest();
        metricInfos.stream()
                .map(metricInfo -> {
                    return new UpdateRequest(
                            metricInfo.getId().toString(),
                            new TypeDescriptor(indexResolver(metricInfo.getScopeId()),
                                    MetricInfoSchema.METRIC_TYPE_NAME),
                            metricInfo);
                })
                .forEach(bulkUpdateRequest::add);
        return elasticsearchClientProviderInstance.getElasticsearchClient().upsert(bulkUpdateRequest);
    }

    @Override
    JsonNode getIndexSchema() throws MappingException {
        return MetricInfoSchema.getMetricTypeSchema();
    }
}
