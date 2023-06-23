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

import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;

import javax.inject.Inject;

public class ClientInfoRepositoryImpl extends ElasticsearchRepository<ClientInfo, ClientInfoQuery> implements ClientInfoRepository {

    @Inject
    protected ClientInfoRepositoryImpl(
            ElasticsearchClientProvider elasticsearchClientProviderInstance) {
        super(elasticsearchClientProviderInstance, ClientInfoSchema.CLIENT_TYPE_NAME, ClientInfo.class);
    }

    @Override
    public String upsert(String indexName, ClientInfo clientInfo) throws ClientException {
        UpdateRequest request = new UpdateRequest(clientInfo.getId().toString(), getDescriptor(indexName), clientInfo);
        return elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request).getId();
    }
}
