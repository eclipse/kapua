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
import org.eclipse.kapua.service.datastore.internal.schema.ChannelInfoSchema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;
import org.eclipse.kapua.service.storable.exception.MappingException;

import javax.inject.Inject;

public class ChannelInfoRepositoryImpl extends ElasticsearchRepository<ChannelInfo, ChannelInfoQuery> implements ChannelInfoRepository {

    @Inject
    protected ChannelInfoRepositoryImpl(
            ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        super(elasticsearchClientProviderInstance,
                ChannelInfoSchema.CHANNEL_TYPE_NAME,
                ChannelInfo.class);
    }

    @Override
    protected String indexResolver(KapuaId scopeId) {
        return DatastoreUtils.getChannelIndexName(scopeId);
    }

    @Override
    public String upsert(String channelInfoId, ChannelInfo channelInfo) throws ClientException {
        final String registryIndexName = indexResolver(channelInfo.getScopeId());
        UpdateRequest request = new UpdateRequest(channelInfo.getId().toString(), getDescriptor(registryIndexName), channelInfo);
        final String responseId = elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request).getId();
        logger.debug("Upsert on channel successfully executed [{}.{}, {} - {}]", registryIndexName, type, channelInfoId, responseId);
        return responseId;
    }

    @Override
    JsonNode getIndexSchema() throws MappingException {
        return ChannelInfoSchema.getChannelTypeSchema();
    }
}
