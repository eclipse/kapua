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

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;

import javax.inject.Inject;
import java.util.List;

public class MetricInfoRepositoryImpl extends ElasticsearchRepository<MetricInfo, MetricInfoQuery> implements MetricInfoRepository {

    @Inject
    protected MetricInfoRepositoryImpl(
            ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        super(elasticsearchClientProviderInstance, MetricInfoSchema.METRIC_TYPE_NAME, MetricInfo.class);
    }

    @Override
    public String upsert(String indexName, MetricInfo metricInfo) throws ClientException {
        UpdateRequest request = new UpdateRequest(metricInfo.getId().toString(), getDescriptor(indexName), metricInfo);
        return elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request).getId();
    }

    @Override
    public BulkUpdateResponse upsert(List<Pair<String, MetricInfo>> metricInfos) throws ClientException {
        final BulkUpdateRequest bulkUpdateRequest = new BulkUpdateRequest();
        metricInfos.stream().map(p -> {
                    return new UpdateRequest(
                            p.getRight().getId().toString(),
                            new TypeDescriptor(p.getLeft(),
                                    MetricInfoSchema.METRIC_TYPE_NAME),
                            p.getRight());
                })
                .forEach(bulkUpdateRequest::add);
        return elasticsearchClientProviderInstance.getElasticsearchClient().upsert(bulkUpdateRequest);
    }
}
