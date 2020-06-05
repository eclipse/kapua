/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.transport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.MoreObjects;
import org.eclipse.kapua.service.elasticsearch.client.AbstractElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ModelContext;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientErrorCodes;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientInitializationException;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.IndexResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.InsertRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.InsertResponse;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshAction;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.LoggingDeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.indices.InvalidIndexNameException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Client implementation based on Elasticsearch transport client.
 *
 * @since 1.0.0
 * @deprecated Since 1.0.0. Elasticsearch transport client will be removed in the next releases. Please use the Rest client instead.
 */
@Deprecated
public class TransportElasticsearchClient extends AbstractElasticsearchClient<Client> {

    private static final Logger LOG = LoggerFactory.getLogger(TransportElasticsearchClient.class);

    private static final String CLIENT_QUERY_PARSING_ERROR_MSG = "Cannot parse query!";
    private static final String CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG = "Cannot delete indexes!";
    private static final String CLIENT_CANNOT_REFRESH_INDEX_ERROR_MSG = "Cannot refresh indexes!";
    private static final String CANNOT_FIND_INDEX = "Cannot find index '{}'";
    private static final String GENERIC_SEARCH_ERROR = "Generic search error {}";

    private static final String INDEXES_ALL = "_all";
    private static final String DOC = "_doc";


    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public TransportElasticsearchClient() {
        super("transport");
    }


    @Override
    public void init() throws ClientInitializationException {
        if (getClientConfiguration() == null) {
            throw new ClientInitializationException("Client configuration not defined");
        }
        if (getModelContext() == null) {
            throw new ClientInitializationException("Missing model context");
        }
        if (getModelConverter() == null) {
            throw new ClientInitializationException("Missing model converter");
        }
    }

    @Override
    public void close() {
        // No resources to close
    }

    @Override
    public InsertResponse insert(InsertRequest insertRequest) throws ClientException {
        Map<String, Object> storableMap = getModelContext().marshal(insertRequest.getStorable());
        LOG.debug("Insert - converted object: '{}'", storableMap);

        org.elasticsearch.action.index.IndexRequest idxRequest = new org.elasticsearch.action.index.IndexRequest(insertRequest.getTypeDescriptor().getIndex(), insertRequest.getTypeDescriptor().getType()).source(storableMap);
        if (insertRequest.getId() != null) {
            idxRequest.id(insertRequest.getId()).version(1).versionType(VersionType.EXTERNAL);
        }
        org.elasticsearch.action.index.IndexResponse response = getClient().index(idxRequest).actionGet(getQueryTimeout());

        return new InsertResponse(response.getId(), insertRequest.getTypeDescriptor());
    }

    @Override
    public UpdateResponse upsert(UpdateRequest upsertRequest) throws ClientException {
        Map<String, Object> storableMap = getModelContext().marshal(upsertRequest.getStorable());
        LOG.debug("Upsert - converted object: '{}'", storableMap);

        org.elasticsearch.action.index.IndexRequest idxRequest = new org.elasticsearch.action.index.IndexRequest(upsertRequest.getTypeDescriptor().getIndex(), upsertRequest.getTypeDescriptor().getType(), upsertRequest.getId()).source(storableMap);
        org.elasticsearch.action.update.UpdateRequest updateRequest =
                new org.elasticsearch.action.update.UpdateRequest(
                        upsertRequest.getTypeDescriptor().getIndex(),
                        upsertRequest.getTypeDescriptor().getType(),
                        upsertRequest.getId()).doc(storableMap);
        org.elasticsearch.action.update.UpdateResponse response = getClient().update(updateRequest.upsert(idxRequest)).actionGet(getQueryTimeout());

        return new UpdateResponse(response.getId(), upsertRequest.getTypeDescriptor());
    }

    @Override
    public BulkUpdateResponse upsert(BulkUpdateRequest bulkUpsertRequest) throws ClientException {
        BulkRequest bulkRequest = new BulkRequest();
        for (UpdateRequest upsertRequest : bulkUpsertRequest.getRequest()) {
            String type = upsertRequest.getTypeDescriptor().getType();
            String index = upsertRequest.getTypeDescriptor().getIndex();
            String id = upsertRequest.getId();

            Map<String, Object> mappedObject = getModelContext().marshal(upsertRequest.getStorable());
            LOG.debug("Upsert - converted object: '{}'", mappedObject);

            org.elasticsearch.action.index.IndexRequest idxRequest = new org.elasticsearch.action.index.IndexRequest(index, type, id).source(mappedObject);
            org.elasticsearch.action.update.UpdateRequest updateRequest = new org.elasticsearch.action.update.UpdateRequest(index, type, id).doc(mappedObject);
            updateRequest.upsert(idxRequest);
            bulkRequest.add(updateRequest);
        }

        BulkResponse bulkResponse = getClient().bulk(bulkRequest).actionGet(getQueryTimeout());

        BulkUpdateResponse response = new BulkUpdateResponse();
        BulkItemResponse[] itemResponses = bulkResponse.getItems();
        if (itemResponses != null) {
            for (BulkItemResponse bulkItemResponse : itemResponses) {
                String metricId = null;
                String indexName = bulkItemResponse.getIndex();
                String typeName = bulkItemResponse.getType();
                if (bulkItemResponse.isFailed()) {
                    String failureMessage = bulkItemResponse.getFailureMessage();
                    response.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName), failureMessage));
                    LOG.info("Upsert failed [{}, {}, {}]", indexName, typeName, failureMessage);
                    continue;
                }
                metricId = ((org.elasticsearch.action.update.UpdateResponse) bulkItemResponse.getResponse()).getId();
                response.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName)));
                LOG.debug("Upsert on channel metric succesfully executed [{}.{}, {}]", indexName, typeName, metricId);
            }
        }

        return response;
    }

    @Override
    public <T> T find(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException {
        ResultList<T> result = query(typeDescriptor, query, clazz);

        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    @Override
    public <T> ResultList<T> query(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException {
        JsonNode queryMap = getModelConverter().convertQuery(query);
        LOG.debug("Query - converted query: '{}'", queryMap);

        ObjectNode fetchSourceFields = (ObjectNode) queryMap.path(SchemaKeys.KEY_SOURCE);
        String[] includesFields = toIncludedExcludedFields(fetchSourceFields.path(SchemaKeys.KEY_INCLUDES));
        String[] excludesFields = toIncludedExcludedFields(fetchSourceFields.path(SchemaKeys.KEY_EXCLUDES));
        SearchRequestBuilder searchReqBuilder = getClient().prepareSearch(typeDescriptor.getIndex());
        searchReqBuilder.setTypes(typeDescriptor.getType())
                .setSource(toSearchSourceBuilder(queryMap))
                .setFetchSource(includesFields, excludesFields);

        SearchHit[] searchHits = null;
        long totalCount = 0;
        SearchResponse response;
        try {
            response = searchReqBuilder
                    .execute()
                    .actionGet(getQueryTimeout());
            searchHits = response.getHits().getHits();
            totalCount = response.getHits().getTotalHits();
            if (totalCount > Integer.MAX_VALUE) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, "Total hits exceeds integer max value");
            }
        } catch (IndexNotFoundException infe) {
            LOG.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex());
        } catch (SearchPhaseExecutionException spee) {
            LOG.warn(GENERIC_SEARCH_ERROR, spee.getMessage(), spee);
        }

        ResultList<T> result = new ResultList<>(totalCount);
        Object queryFetchStyle = getModelConverter().getFetchStyle(query);
        if (searchHits != null) {
            for (SearchHit searchHit : searchHits) {
                Map<String, Object> object = searchHit.getSourceAsMap();
                object.put(ModelContext.TYPE_DESCRIPTOR_KEY, new TypeDescriptor(searchHit.getIndex(), searchHit.getType()));
                object.put(getModelContext().getIdKeyName(), searchHit.getId());
                object.put(QueryConverter.QUERY_FETCH_STYLE_KEY, queryFetchStyle);
                result.add(getModelContext().unmarshal(clazz, object));
            }
        }

        return result;
    }

    @Override
    public long count(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        JsonNode queryMap = getModelConverter().convertQuery(query);

        SearchRequestBuilder searchReqBuilder = getClient().prepareSearch(typeDescriptor.getIndex());
        SearchHits searchHits = null;
        try {
            SearchResponse response = searchReqBuilder.setTypes(typeDescriptor.getType())
                    .setSource(toSearchSourceBuilder(queryMap))
                    .execute()
                    .actionGet(getQueryTimeout());
            searchHits = response.getHits();
        } catch (IndexNotFoundException infe) {
            LOG.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex());
        } catch (SearchPhaseExecutionException spee) {
            LOG.warn(GENERIC_SEARCH_ERROR, spee.getMessage(), spee);
        }

        if (searchHits == null) {
            return 0;
        }

        return searchHits.getTotalHits();
    }

    @Override
    public void delete(TypeDescriptor typeDescriptor, String id) throws ClientException {
        try {
            getClient().prepareDelete()
                    .setIndex(typeDescriptor.getIndex())
                    .setType(typeDescriptor.getType())
                    .setId(id)
                    .get(getQueryTimeout());
        } catch (InvalidIndexNameException iine) {
            LOG.warn("Index '{}' not valid", typeDescriptor.getIndex(), iine);
        } catch (IndexNotFoundException infe) {
            LOG.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex(), infe);
        }
    }

    @Override
    public void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        JsonNode queryMap = getModelConverter().convertQuery(query);

        TimeValue queryTimeout = getQueryTimeout();
        TimeValue scrollTimeout = getScrollTimeout();

        SearchResponse scrollResponse = null;
        try {
            // delete by query API is deprecated, scroll with bulk delete must be used
            scrollResponse = getClient()
                    .prepareSearch(typeDescriptor.getIndex())
                    .setTypes(typeDescriptor.getType())
                    .setFetchSource(false)
                    .addSort(DOC, SortOrder.ASC)
                    .setVersion(true)
                    .setScroll(scrollTimeout)
                    .setSource(toSearchSourceBuilder(queryMap))
                    .get(queryTimeout);
        } catch (IndexNotFoundException infe) {
            LOG.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex());
        } catch (SearchPhaseExecutionException spee) {
            LOG.warn(GENERIC_SEARCH_ERROR, spee.getMessage(), spee);
        }

        if (scrollResponse != null) {
            // Scroll until no hits are returned
            while (true) {

                // Break condition: No hits are returned
                if (scrollResponse.getHits().getHits().length == 0) {
                    break;
                }

                BulkRequest bulkRequest = new BulkRequest();
                for (SearchHit hit : scrollResponse.getHits().getHits()) {
                    DeleteRequest delete = new DeleteRequest().index(hit.getIndex())
                            .type(hit.getType())
                            .id(hit.getId());
                    bulkRequest.add(delete);
                }

                getClient().bulk(bulkRequest).actionGet(queryTimeout);

                scrollResponse = getClient().prepareSearchScroll(scrollResponse.getScrollId())
                        .setScroll(scrollTimeout)
                        .execute()
                        .actionGet(queryTimeout);
            }
        }
    }

    @Override
    public IndexResponse isIndexExists(IndexRequest indexRequest) {
        IndicesExistsResponse response = getClient().admin().indices()
                .exists(new IndicesExistsRequest(indexRequest.getIndex()))
                .actionGet(getQueryTimeout());

        return new IndexResponse(response.isExists());
    }

    @Override
    public IndexResponse findIndexes(IndexRequest indexRequest) {
        try {
            GetSettingsResponse response =
                    getClient()
                            .admin()
                            .indices()
                            .prepareGetSettings(indexRequest.getIndex())
                            .get(getQueryTimeout());
            List<String> list = new ArrayList<>();
            response.getIndexToSettings().keysIt().forEachRemaining(list::add);

            return new IndexResponse(list.toArray(new String[0]));
        } catch (IndexNotFoundException e) {
            return new IndexResponse(new String[0]);
        }
    }

    @Override
    public void createIndex(String indexName, ObjectNode indexSettings) {
        getClient().admin()
                .indices()
                .prepareCreate(indexName)
                .setSettings(indexSettings.toString(), XContentType.JSON)
                .execute()
                .actionGet(getQueryTimeout());
    }

    @Override
    public boolean isMappingExists(TypeDescriptor typeDescriptor) {
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(typeDescriptor.getIndex());
        GetMappingsResponse mappingsResponse = getClient().admin().indices().getMappings(mappingsRequest).actionGet(getQueryTimeout());
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(typeDescriptor.getIndex());
        MappingMetaData metadata = map.get(typeDescriptor.getType());

        return metadata != null;
    }

    @Override
    public void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) {

        if (!isMappingExists(typeDescriptor)) {
            LOG.debug("Put mapping: '{}'", mapping);

            getClient().admin()
                    .indices()
                    .preparePutMapping(typeDescriptor.getIndex())
                    .setType(typeDescriptor.getType())
                    .setSource(mapping.toString(), XContentType.JSON)
                    .execute()
                    .actionGet(getQueryTimeout());
            LOG.trace("Put mapping - mapping {} created! ", typeDescriptor.getType());
        } else {
            LOG.trace("Put mapping - mapping {} already exists! ", typeDescriptor.getType());
        }
    }

    private String[] toIncludedExcludedFields(JsonNode queryMap) throws ClientException {
        if (queryMap instanceof ArrayNode) {
            ArrayNode arrayNode = (ArrayNode) queryMap;
            String[] fields = new String[arrayNode.size()];
            int index = 0;
            for (JsonNode node : arrayNode) {
                fields[index++] = node.asText();
            }
            return fields;
        } else {
            throw new ClientException(ClientErrorCodes.QUERY_MAPPING_EXCEPTION, String.format("Invalid includes/excludes fields type! (%s)", (queryMap != null ? queryMap.getClass() : "null")));
        }
    }

    private SearchSourceBuilder toSearchSourceBuilder(JsonNode queryMap) throws ClientException {
        SearchSourceBuilder searchSourceBuilder = null;
        try {
            String content = queryMap.toString();
            searchSourceBuilder = new SearchSourceBuilder();
            SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
            XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                    .createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), LoggingDeprecationHandler.INSTANCE, content);
            searchSourceBuilder.parseXContent(parser);
            LOG.debug("Search builder: {}", searchSourceBuilder);
            return searchSourceBuilder;
        } catch (Exception e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_QUERY_PARSING_ERROR_MSG);
        }
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        Client client = getClient();
        final DeleteIndexRequest request = DeleteIndexAction.INSTANCE.newRequestBuilder(client).request();
        request.indices(INDEXES_ALL);
        try {
            AcknowledgedResponse deleteResponse = client.admin().indices().delete(request).actionGet(getQueryTimeout());
            if (!deleteResponse.isAcknowledged()) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
            }
        } catch (IllegalStateException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
        }
    }

    @Override
    public void deleteIndexes(String... indexes) throws ClientException {
        DeleteIndexRequest request = DeleteIndexAction.INSTANCE.newRequestBuilder(getClient()).request();
        for (String index : indexes) {
            request.indices(index);
            try {
                LOG.debug("Deleting index {}", index);
                AcknowledgedResponse deleteResponse = getClient().admin().indices().delete(request).actionGet(getQueryTimeout());
                if (!deleteResponse.isAcknowledged()) {
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
                }
                LOG.debug("Deleting index {} DONE", index);
            } catch (IllegalStateException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
            } catch (IndexNotFoundException e) {
                // do nothing it's not an error
                // switch the log level to debug?
                LOG.debug("Deleting index {} : index does not exist!", index);
            }
        }

    }

    @Override
    public void refreshAllIndexes() throws ClientException {

        RefreshRequest request = RefreshAction.INSTANCE.newRequestBuilder(getClient()).request();
        request.indices(INDEXES_ALL);

        try {
            RefreshResponse refreshResponse = getClient().admin().indices().refresh(request).actionGet(getQueryTimeout());
            if (refreshResponse.getFailedShards() > 0) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_REFRESH_INDEX_ERROR_MSG);
            }
        } catch (IllegalStateException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_CANNOT_REFRESH_INDEX_ERROR_MSG);
        }
    }

    /**
     * Gets the query timeout.
     *
     * @return The query timeout
     * @since 1.0.0
     */
    public TimeValue getQueryTimeout() {
        return new TimeValue(MoreObjects.firstNonNull(getClientConfiguration().getRequestConfiguration().getQueryTimeout(), 15000));
    }

    /**
     * Gets the scroll timeout (default value).
     *
     * @return The scroll timeout (default value)
     * @since 1.0.0
     */
    public TimeValue getScrollTimeout() {
        return new TimeValue(MoreObjects.firstNonNull(getClientConfiguration().getRequestConfiguration().getScrollTimeout(), 60000));
    }

}
