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

import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;

public abstract class ElasticsearchRepository<T extends Storable, Q extends StorableQuery> implements DatastoreRepository<T, Q> {
    protected final ElasticsearchClientProvider elasticsearchClientProviderInstance;
    private final String type;
    private final Class<T> clazz;

    protected ElasticsearchRepository(
            ElasticsearchClientProvider elasticsearchClientProviderInstance,
            String type, Class<T> clazz) {
        this.elasticsearchClientProviderInstance = elasticsearchClientProviderInstance;
        this.type = type;
        this.clazz = clazz;
    }

    @Override
    public ResultList<T> query(String dataIndexName, Q query) throws ClientException {
        final ResultList<T> res = elasticsearchClientProviderInstance.getElasticsearchClient().query(getDescriptor(dataIndexName), query, clazz);
        return res;
    }

    @Override
    public long count(String dataIndexName, Q query) throws ClientException {
        return elasticsearchClientProviderInstance.getElasticsearchClient().count(getDescriptor(dataIndexName), query);
    }

    @Override
    public void delete(String indexName, String id) throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().delete(getDescriptor(indexName), id);
    }

    @Override
    public void delete(String indexName, Q query) throws ClientException {
        elasticsearchClientProviderInstance.getElasticsearchClient().deleteByQuery(getDescriptor(indexName), query);
    }

    public TypeDescriptor getDescriptor(String indexName) {
        return new TypeDescriptor(indexName, type);
    }
}
