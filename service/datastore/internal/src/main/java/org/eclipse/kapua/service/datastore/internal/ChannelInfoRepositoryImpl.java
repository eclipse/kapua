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

import org.eclipse.kapua.service.datastore.internal.schema.ChannelInfoSchema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;

import javax.inject.Inject;

public class ChannelInfoRepositoryImpl extends ElasticsearchRepository<ChannelInfo, ChannelInfoQuery> implements ChannelInfoRepository {

    @Inject
    protected ChannelInfoRepositoryImpl(
            ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        super(elasticsearchClientProviderInstance, ChannelInfoSchema.CHANNEL_TYPE_NAME, ChannelInfo.class);
    }

    @Override
    public String upsert(String indexName, ChannelInfo channelInfo) throws ClientException {
        UpdateRequest request = new UpdateRequest(channelInfo.getId().toString(), getDescriptor(indexName), channelInfo);
        return elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request).getId();
    }
}
