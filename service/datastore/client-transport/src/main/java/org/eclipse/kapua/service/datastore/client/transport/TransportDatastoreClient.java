/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import java.util.Collections;
import java.util.Map;

import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.ClientUndefinedException;
import org.eclipse.kapua.service.datastore.client.ModelContext;
import org.eclipse.kapua.service.datastore.client.QueryConverter;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.datastore.client.model.IndexExistsRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexExistsResponse;
import org.eclipse.kapua.service.datastore.client.model.InsertRequest;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.client.model.UpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.UpdateResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_EXCLUDE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_INCLUDE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SOURCE;

/**
 * Client implementation based on Elasticsearch transport client
 *
 * @since 1.0
 */
public class TransportDatastoreClient implements org.eclipse.kapua.service.datastore.client.DatastoreClient {

    private static final Logger logger = LoggerFactory.getLogger(TransportDatastoreClient.class);

    private static final String CLIENT_UNDEFINED_MSG = "Elasticsearch client must be not null";

    private EsTransportClientProvider esTransportClientProvider;
    private ModelContext modelContext;
    private QueryConverter queryConverter;

    /**
     * Default constructor
     * 
     * @throws ClientUnavailableException
     */
    public TransportDatastoreClient() throws ClientUnavailableException {
        esTransportClientProvider = new EsTransportClientProvider();
    }

    /**
     * Constructs the client with the provided model context and query converter
     * 
     * @param modelContext
     * @param queryConverter
     * @throws ClientUnavailableException
     */
    public TransportDatastoreClient(ModelContext modelContext, QueryConverter queryConverter) throws ClientUnavailableException {
        this();
        this.modelContext = modelContext;
        this.queryConverter = queryConverter;
    }

    @Override
    public void setModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
    }

    @Override
    public void setQueryConverter(QueryConverter queryConverter) {
        this.queryConverter = queryConverter;
    }

    @Override
    public InsertResponse insert(InsertRequest insertRequest) throws ClientException {
        checkClient();
        Map<String, Object> storableMap = modelContext.marshal(insertRequest.getStorable());
        logger.debug("Insert - converted object: '{}'", storableMap.toString());
        IndexRequest idxRequest = new IndexRequest(insertRequest.getTypeDescriptor().getIndex(), insertRequest.getTypeDescriptor().getType()).source(storableMap);
        IndexResponse response = esTransportClientProvider.getClient().index(idxRequest).actionGet(getQueryTimeout());
        return new InsertResponse(response.getId(), insertRequest.getTypeDescriptor());
    }

    @Override
    public UpdateResponse upsert(UpdateRequest upsertRequest) throws ClientException {
        checkClient();
        Map<String, Object> storableMap = modelContext.marshal(upsertRequest.getStorable());
        IndexRequest idxRequest = new IndexRequest(upsertRequest.getTypeDescriptor().getIndex(), upsertRequest.getTypeDescriptor().getType(), upsertRequest.getId()).source(storableMap);
        org.elasticsearch.action.update.UpdateRequest updateRequest = new org.elasticsearch.action.update.UpdateRequest(upsertRequest.getTypeDescriptor().getIndex(),
                upsertRequest.getTypeDescriptor().getType(), upsertRequest.getId()).doc(storableMap);
        org.elasticsearch.action.update.UpdateResponse response = esTransportClientProvider.getClient().update(updateRequest.upsert(idxRequest)).actionGet(getQueryTimeout());
        return new UpdateResponse(response.getId(), upsertRequest.getTypeDescriptor());
    }

    @Override
    public BulkUpdateResponse upsert(BulkUpdateRequest bulkUpsertRequest) throws ClientException {
        checkClient();
        BulkRequest bulkRequest = new BulkRequest();
        for (UpdateRequest upsertRequest : bulkUpsertRequest.getRequest()) {
            String type = upsertRequest.getTypeDescriptor().getType();
            String index = upsertRequest.getTypeDescriptor().getIndex();
            String id = upsertRequest.getId();
            Map<String, Object> mappedObject = modelContext.marshal(upsertRequest.getStorable());
            IndexRequest idxRequest = new IndexRequest(index, type, id).source(mappedObject);
            org.elasticsearch.action.update.UpdateRequest updateRequest = new org.elasticsearch.action.update.UpdateRequest(index, type, id).doc(mappedObject);
            updateRequest.upsert(idxRequest);
            bulkRequest.add(updateRequest);
        }
        
        BulkResponse bulkResponse = esTransportClientProvider.getClient().bulk(bulkRequest).actionGet(getQueryTimeout());

        BulkUpdateResponse response = new BulkUpdateResponse();
        BulkItemResponse[] itemResponses = bulkResponse.getItems();
        if (itemResponses != null) {
            for (BulkItemResponse bulkItemResponse : itemResponses) {
                String metricId = ((org.elasticsearch.action.update.UpdateResponse) bulkItemResponse.getResponse()).getId();
                String indexName = bulkItemResponse.getIndex();
                String typeName = bulkItemResponse.getType();
                if (bulkItemResponse.isFailed()) {
                    String failureMessage = bulkItemResponse.getFailureMessage();
                    response.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName), failureMessage));
                    logger.info("Upsert failed [{}, {}, {}]", new Object[] { indexName, typeName, failureMessage });
                    continue;
                }
                response.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName)));
                logger.debug("Upsert on channel metric succesfully executed [{}.{}, {}]", new Object[] { indexName, typeName, metricId });
            }
        }
        return response;
    }

    @Override
    public <T> T find(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException {
        ResultList<T> result = query(typeDescriptor, query, clazz);
        if (result.getTotalCount() == 0) {
            return null;
        } else {
            return result.getResult().get(0);
        }
    }

    @Override
    public <T> ResultList<T> query(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException {
        checkClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        Object queryFetchStyle = queryConverter.getFetchStyle(query);
        logger.debug("Query - converted query: '{}'", queryMap.toString());
        SearchResponse response = null;
        ObjectNode fetchSourceFields = (ObjectNode) queryMap.path(KEY_SOURCE);
        String[] includesFields = toIncludedExcludedFields(fetchSourceFields.path(KEY_INCLUDE));
        String[] excludesFields = toIncludedExcludedFields(fetchSourceFields.path(KEY_EXCLUDE));
        SearchRequestBuilder searchReqBuilder = esTransportClientProvider.getClient().prepareSearch(typeDescriptor.getIndex());
        searchReqBuilder.setTypes(typeDescriptor.getType())
                .setSource(toSearchSourceBuilder(queryMap))
                .setFetchSource(includesFields, excludesFields);
        // unused since sort fields are already included in the search query mapping
        // ArrayNode sortFields = (ArrayNode) queryMap.path(QueryConverter.SORT_KEY);
        response = searchReqBuilder
                .execute()
                .actionGet(getQueryTimeout());
        SearchHit[] searchHits = response.getHits().getHits();
        // TODO verify total count
        long totalCount = response.getHits().getTotalHits();

        if (totalCount > Integer.MAX_VALUE) {
            throw new RuntimeException("Total hits exceeds integer max value");
        }

        ResultList<T> result = new ResultList<T>(totalCount);
        if (searchHits != null) {
            for (SearchHit searchHit : searchHits) {
                Map<String, Object> object = searchHit.getSource();
                object.put(ModelContext.TYPE_DESCRIPTOR_KEY, new TypeDescriptor(searchHit.getIndex(), searchHit.getType()));
                object.put(ModelContext.DATASTORE_ID_KEY, searchHit.getId());
                object.put(QueryConverter.QUERY_FETCH_STYLE_KEY, queryFetchStyle);
                result.add(modelContext.unmarshal(clazz, object));
            }
        }
        return result;
    }

    @Override
    public long count(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        checkClient();
        // TODO check for fetch none
        JsonNode queryMap = queryConverter.convertQuery(query);
        SearchRequestBuilder searchReqBuilder = esTransportClientProvider.getClient().prepareSearch(typeDescriptor.getIndex());
        SearchResponse response = searchReqBuilder.setTypes(typeDescriptor.getType())
                .setSource(toSearchSourceBuilder(queryMap))
                .execute()
                .actionGet(getQueryTimeout());
        SearchHits searchHits = response.getHits();

        if (searchHits == null)
            return 0;

        return searchHits.getTotalHits();
    }

    @Override
    public void delete(TypeDescriptor typeDescriptor, String id) throws ClientException {
        checkClient();
        esTransportClientProvider.getClient().prepareDelete()
                .setIndex(typeDescriptor.getIndex())
                .setType(typeDescriptor.getType())
                .setId(id)
                .get(getQueryTimeout());
    }

    @Override
    public void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        checkClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        TimeValue queryTimeout = getQueryTimeout();
        TimeValue scrollTimeout = getScrollTimeout();

        // delete by query API is deprecated, scroll with bulk delete must be used
        SearchResponse scrollResponse = esTransportClientProvider.getClient().prepareSearch(typeDescriptor.getIndex())
                .setTypes(typeDescriptor.getType())
                .setFetchSource(false)
                .addSort("_doc", SortOrder.ASC)
                .setVersion(true)
                .setScroll(scrollTimeout)
                .setSource(toSearchSourceBuilder(queryMap))
                .setSize(100)
                .get(queryTimeout);

        // Scroll until no hits are returned
        while (true) {

            // Break condition: No hits are returned
            if (scrollResponse.getHits().getHits().length == 0)
                break;

            BulkRequest bulkRequest = new BulkRequest();
            for (SearchHit hit : scrollResponse.getHits().hits()) {
                DeleteRequest delete = new DeleteRequest().index(hit.index())
                        .type(hit.type())
                        .id(hit.id())
                        .version(hit.version());
                bulkRequest.add(delete);
            }

            esTransportClientProvider.getClient().bulk(bulkRequest).actionGet(queryTimeout);

            scrollResponse = esTransportClientProvider.getClient().prepareSearchScroll(scrollResponse.getScrollId())
                    .setScroll(scrollTimeout)
                    .execute()
                    .actionGet(queryTimeout);
        }
    }

    @Override
    public IndexExistsResponse isIndexExists(IndexExistsRequest indexExistsRequest) throws ClientException {
        checkClient();
        IndicesExistsResponse response = esTransportClientProvider.getClient().admin().indices()
                .exists(new IndicesExistsRequest(indexExistsRequest.getIndex()))
                .actionGet();
        return new IndexExistsResponse(response.isExists());
    }

    @Override
    public void createIndex(String indexName, ObjectNode indexSettings) throws ClientException {
        checkClient();
        esTransportClientProvider.getClient().admin()
                .indices()
                .prepareCreate(indexName)
                .setSettings(indexSettings.toString(), XContentType.JSON)
                .execute()
                .actionGet();
    }

    @Override
    public boolean isMappingExists(TypeDescriptor typeDescriptor) throws ClientException {
        checkClient();
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(typeDescriptor.getIndex());
        GetMappingsResponse mappingsResponse = esTransportClientProvider.getClient().admin().indices().getMappings(mappingsRequest).actionGet();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(typeDescriptor.getIndex());
        MappingMetaData metadata = map.get(typeDescriptor.getType());
        return metadata != null;
    }

    @Override
    public void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) throws ClientException {
        checkClient();
        // Check message type mapping
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(typeDescriptor.getIndex());
        GetMappingsResponse mappingsResponse = esTransportClientProvider.getClient().admin().indices().getMappings(mappingsRequest).actionGet();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(typeDescriptor.getIndex());
        MappingMetaData metadata = map.get(typeDescriptor.getType());
        if (metadata == null) {
            logger.debug("Put mapping: '{}'", mapping.toString());
            esTransportClientProvider.getClient().admin().indices().preparePutMapping(typeDescriptor.getIndex()).setType(typeDescriptor.getType()).setSource(mapping.toString(), XContentType.JSON)
                    .execute().actionGet();
            logger.trace("Put mapping - mapping {} created! ", typeDescriptor.getType());
        }
        else {
            logger.trace("Put mapping - mapping {} already exists! ", typeDescriptor.getType());
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
                    .createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), content);
            searchSourceBuilder.parseXContent(new QueryParseContext(parser));
            logger.debug(searchSourceBuilder.toString());
            return searchSourceBuilder;
        } catch (Throwable t) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, t, "Cannot parse query!");
        }
    }

    private void checkClient() throws ClientUndefinedException {
        if (esTransportClientProvider.getClient() == null) {
            throw new ClientUndefinedException(CLIENT_UNDEFINED_MSG);
        }
    }

    /**
     * Get the scroll timeout (default value)
     * 
     * @return
     */
    public TimeValue getScrollTimeout() {
        return TimeValue.timeValueMillis(ClientSettings.getInstance().getLong(ClientSettingsKey.SCROLL_TIMEOUT, 60000));
    }

    /**
     * Get the query timeout
     *
     * @return
     */
    public TimeValue getQueryTimeout() {
        return TimeValue.timeValueMillis(ClientSettings.getInstance().getLong(ClientSettingsKey.QUERY_TIMEOUT, 15000));
    }

}
