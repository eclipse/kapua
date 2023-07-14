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
package org.eclipse.kapua.service.elasticsearch.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.Response;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateResponse;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.StorableListResult;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;
import org.eclipse.kapua.service.storable.model.query.predicate.IdsPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;
import org.eclipse.kapua.service.storable.repository.StorableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ElasticsearchRepository<
        T extends Storable,
        L extends StorableListResult<T>,
        Q extends StorableQuery> implements StorableRepository<T, L, Q> {
    protected final ElasticsearchClientProvider elasticsearchClientProviderInstance;
    protected final String type;
    private final Class<T> clazz;
    protected final StorablePredicateFactory storablePredicateFactory;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract String indexResolver(KapuaId scopeId);

    protected abstract L buildList(ResultList<T> fromItems);

    protected abstract JsonNode getIndexSchema() throws MappingException;

    protected abstract ObjectNode getMappingSchema(String idxName) throws MappingException;

    protected abstract StorableId idExtractor(T storable);

    protected abstract Q createQuery(KapuaId scopeId);

    protected ElasticsearchRepository(
            ElasticsearchClientProvider elasticsearchClientProviderInstance,
            String type,
            Class<T> clazz,
            StorablePredicateFactory storablePredicateFactory) {
        this.elasticsearchClientProviderInstance = elasticsearchClientProviderInstance;
        this.type = type;
        this.storablePredicateFactory = storablePredicateFactory;
        this.clazz = clazz;
    }

    @Override
    public T find(KapuaId scopeId, StorableId id) {
        try {
            final Q idsQuery = createQuery(scopeId);
            idsQuery.setLimit(1);

            final IdsPredicate idsPredicate = storablePredicateFactory.newIdsPredicate(type);
            idsPredicate.addId(id);
            idsQuery.setPredicate(idsPredicate);

            final String indexName = indexResolver(scopeId);
            TypeDescriptor typeDescriptor = getDescriptor(indexName);
            final T res;
            res = (T) elasticsearchClientProviderInstance.getElasticsearchClient().<T>find(typeDescriptor, idsQuery, clazz);
            return res;
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public L query(Q query) {
        final ResultList<T> partialResult;
        try {
            partialResult = elasticsearchClientProviderInstance.getElasticsearchClient().query(getDescriptor(indexResolver(query.getScopeId())), query, clazz);
            final L res = buildList(partialResult);
            setLimitExceed(query, partialResult.getTotalHitsExceedsCount(), res);
            return res;
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Storable> void setLimitExceed(StorableQuery query, boolean hitsExceedsTotalCount, StorableListResult<T> list) {
        int offset = query.getOffset() != null ? query.getOffset() : 0;
        if (query.getLimit() != null) {
            if (hitsExceedsTotalCount || //pre-condition: there are more than 10k documents in ES && query limit is <= 10k
                    list.getTotalCount() > offset + query.getLimit()) {
                list.setLimitExceeded(true);
            }
        }
    }

    @Override
    public long count(Q query) {
        try {
            return elasticsearchClientProviderInstance.getElasticsearchClient().count(getDescriptor(indexResolver(query.getScopeId())), query);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id) {
        final String indexName = indexResolver(scopeId);
        doDelete(indexName, id);
    }

    protected void doDelete(String indexName, StorableId id) {
        try {
            elasticsearchClientProviderInstance.getElasticsearchClient().delete(getDescriptor(indexName), id.toString());
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Q query) {
        try {
            elasticsearchClientProviderInstance.getElasticsearchClient().deleteByQuery(getDescriptor(indexResolver(query.getScopeId())), query);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upsertIndex(KapuaId scopeId) {
        final String indexName = indexResolver(scopeId);
        doUpsertIndex(indexName);
    }

    @Override
    public String upsert(String itemId, T item) {
        try {
            final String indexName = indexResolver(item.getScopeId());
            UpdateRequest request = new UpdateRequest(itemId.toString(), getDescriptor(indexName), item);
            final UpdateResponse upsertResponse;
            upsertResponse = elasticsearchClientProviderInstance.getElasticsearchClient().upsert(request);
            final String responseId = upsertResponse.getId();
            logger.debug("Upsert  successfully executed [{}.{}, {} - {}]", indexName, type, itemId, responseId);
            return responseId;
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> upsert(List<T> items) {
        try {
            final BulkUpdateRequest bulkUpdateRequest = new BulkUpdateRequest();
            items.stream()
                    .map(storableItem -> new UpdateRequest(
                            idExtractor(storableItem).toString(),
                            getDescriptor(indexResolver(storableItem.getScopeId())),
                            storableItem))
                    .forEach(bulkUpdateRequest::add);
            final BulkUpdateResponse updateResponse = elasticsearchClientProviderInstance.getElasticsearchClient().upsert(bulkUpdateRequest);
            return updateResponse.getResponse()
                    .stream()
                    .peek(response -> {
                        String index = response.getTypeDescriptor().getIndex();
                        String type = response.getTypeDescriptor().getType();
                        String id = response.getId();
                        logger.debug("Upsert successfully executed [{}.{}, {}]", index, type, id);
                    }).map(Response::getId)
                    .collect(Collectors.toSet());
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }


    protected void doUpsertIndex(String indexName) {
        final ElasticsearchClient elasticsearchClient;
        try {
            elasticsearchClient = elasticsearchClientProviderInstance.getElasticsearchClient();
            // Check existence of the kapua internal indexes
            IndexResponse indexExistsResponse = elasticsearchClient.isIndexExists(new IndexRequest(indexName));
            if (!indexExistsResponse.isIndexExists()) {
                elasticsearchClient.createIndex(indexName, getMappingSchema(indexName));
                logger.info("Index created: {}", indexExistsResponse);
                elasticsearchClient.putMapping(getDescriptor(indexName), getIndexSchema());
            }
        } catch (ClientException | MappingException e) {
            throw new RuntimeException(e);
        }
    }


    public TypeDescriptor getDescriptor(String indexName) {
        return new TypeDescriptor(indexName, type);
    }
}
