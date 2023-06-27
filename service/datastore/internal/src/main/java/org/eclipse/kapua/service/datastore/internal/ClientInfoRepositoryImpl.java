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
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;
import org.eclipse.kapua.service.storable.exception.MappingException;

import javax.inject.Inject;

public class ClientInfoRepositoryImpl extends ElasticsearchRepository<ClientInfo, ClientInfoQuery> implements ClientInfoRepository {

    @Inject
    protected ClientInfoRepositoryImpl(
            ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        super(elasticsearchClientProviderInstance,
                ClientInfoSchema.CLIENT_TYPE_NAME,
                ClientInfo.class);
    }

    @Override
    protected String indexResolver(KapuaId scopeId) {
        return DatastoreUtils.getClientIndexName(scopeId);
    }

    @Override
    public String upsert(ClientInfo clientInfo) throws ClientException {
        final String clientIndexName = DatastoreUtils.getClientIndexName(clientInfo.getScopeId());
        UpdateRequest request = new UpdateRequest(clientInfo.getId().toString(), getDescriptor(clientIndexName), clientInfo);
        final String responseId = elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request).getId();
        logger.debug("Upsert on asset successfully executed [{}.{}, {} - {}]", clientIndexName, ClientInfoSchema.CLIENT_TYPE_NAME, responseId, responseId);
        return responseId;
    }

    @Override
    JsonNode getIndexSchema() throws MappingException {
        return ClientInfoSchema.getClientTypeSchema();
    }
}
