/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.service.datastore.client.AbstractDatastoreClient;
import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientProvider;
import org.eclipse.kapua.service.datastore.client.ModelContext;
import org.eclipse.kapua.service.datastore.client.QueryConverter;
import org.eclipse.kapua.service.datastore.client.SchemaKeys;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.BulkUpdateResponse;
import org.eclipse.kapua.service.datastore.client.model.IndexRequest;
import org.eclipse.kapua.service.datastore.client.model.IndexResponse;
import org.eclipse.kapua.service.datastore.client.model.InsertRequest;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.client.model.UpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.UpdateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
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
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.indices.InvalidIndexNameException;
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

/**
 * Client implementation based on Elasticsearch transport client.<br>
 * The Elasticsearch client provider is instantiated as singleton by reflection using those provided by {@link ClientSettingsKey#ELASTICSEARCH_CLIENT_PROVIDER}
 *
 * @deprecated Elasticsearch transport client will be removed in the next releases. Please use the Rest client instead.
 *
 * @since 1.0
 */
@Deprecated
public class TransportDatastoreClient extends AbstractDatastoreClient<Client> {

    private static final Logger logger = LoggerFactory.getLogger(TransportDatastoreClient.class);

    private static final String CLIENT_QUERY_PARSING_ERROR_MSG = "Cannot parse query!";
    private static final String CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG = "Cannot delete indexes!";
    private static final String CLIENT_CANNOT_REFRESH_INDEX_ERROR_MSG = "Cannot refresh indexes!";
    private static final String CANNOT_FIND_INDEX = "Cannot find index '{}'";
    private static final String GENERIC_SEARCH_ERROR = "Generic search error {}";

    private static final String INDEXES_ALL = "_all";
    private static final String DOC = "_doc";

    private static TransportDatastoreClient instance;

    private ModelContext modelContext;
    private QueryConverter queryConverter;

    static {
        instance = new TransportDatastoreClient();
    }

    /**
     * Get the singleton {@link TransportDatastoreClient} instance
     *
     * @return
     */
    public static TransportDatastoreClient getInstance() {
        return instance;
    }

    /**
     * Default constructor
     * Initialize the client provider ({@link ClientProvider}) as singleton.
     *
     */
    private TransportDatastoreClient() {
        super("transport");
    }

    @Override
    public ClientProvider<Client> getNewInstance() {
        return EsTransportClientProvider.init();
    }

    @Override
    public InsertResponse insert(InsertRequest insertRequest) throws ClientException {
        Client client = getClient();
        Map<String, Object> storableMap = modelContext.marshal(insertRequest.getStorable());
        logger.debug("Insert - converted object: '{}'", storableMap);
        org.elasticsearch.action.index.IndexRequest idxRequest = new org.elasticsearch.action.index.IndexRequest(insertRequest.getTypeDescriptor().getIndex(), insertRequest.getTypeDescriptor().getType()).source(storableMap);
        if (insertRequest.getId() != null) {
            idxRequest.id(insertRequest.getId()).version(1).versionType(VersionType.EXTERNAL);
        }
        org.elasticsearch.action.index.IndexResponse response = client.index(idxRequest).actionGet(getQueryTimeout());
        return new InsertResponse(response.getId(), insertRequest.getTypeDescriptor());
    }

    @Override
    public UpdateResponse upsert(UpdateRequest upsertRequest) throws ClientException {
        Client client = getClient();
        Map<String, Object> storableMap = modelContext.marshal(upsertRequest.getStorable());
        logger.debug("Upsert - converted object: '{}'", storableMap);
        org.elasticsearch.action.index.IndexRequest idxRequest = new org.elasticsearch.action.index.IndexRequest(upsertRequest.getTypeDescriptor().getIndex(), upsertRequest.getTypeDescriptor().getType(), upsertRequest.getId()).source(storableMap);
        org.elasticsearch.action.update.UpdateRequest updateRequest = new org.elasticsearch.action.update.UpdateRequest(upsertRequest.getTypeDescriptor().getIndex(),
                upsertRequest.getTypeDescriptor().getType(), upsertRequest.getId()).doc(storableMap);
        org.elasticsearch.action.update.UpdateResponse response = client.update(updateRequest.upsert(idxRequest)).actionGet(getQueryTimeout());
        return new UpdateResponse(response.getId(), upsertRequest.getTypeDescriptor());
    }

    @Override
    public BulkUpdateResponse upsert(BulkUpdateRequest bulkUpsertRequest) throws ClientException {
        Client client = getClient();
        BulkRequest bulkRequest = new BulkRequest();
        for (UpdateRequest upsertRequest : bulkUpsertRequest.getRequest()) {
            String type = upsertRequest.getTypeDescriptor().getType();
            String index = upsertRequest.getTypeDescriptor().getIndex();
            String id = upsertRequest.getId();
            Map<String, Object> mappedObject = modelContext.marshal(upsertRequest.getStorable());
            logger.debug("Upsert - converted object: '{}'", mappedObject);
            org.elasticsearch.action.index.IndexRequest idxRequest = new org.elasticsearch.action.index.IndexRequest(index, type, id).source(mappedObject);
            org.elasticsearch.action.update.UpdateRequest updateRequest = new org.elasticsearch.action.update.UpdateRequest(index, type, id).doc(mappedObject);
            updateRequest.upsert(idxRequest);
            bulkRequest.add(updateRequest);
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest).actionGet(getQueryTimeout());

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
                    logger.info("Upsert failed [{}, {}, {}]", indexName, typeName, failureMessage);
                    continue;
                }
                metricId = ((org.elasticsearch.action.update.UpdateResponse) bulkItemResponse.getResponse()).getId();
                response.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName)));
                logger.debug("Upsert on channel metric succesfully executed [{}.{}, {}]", indexName, typeName, metricId);
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
        Client client = getClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        Object queryFetchStyle = queryConverter.getFetchStyle(query);
        logger.debug("Query - converted query: '{}'", queryMap);
        SearchResponse response = null;
        ObjectNode fetchSourceFields = (ObjectNode) queryMap.path(SchemaKeys.KEY_SOURCE);
        String[] includesFields = toIncludedExcludedFields(fetchSourceFields.path(SchemaKeys.KEY_INCLUDES));
        String[] excludesFields = toIncludedExcludedFields(fetchSourceFields.path(SchemaKeys.KEY_EXCLUDES));
        SearchRequestBuilder searchReqBuilder = client.prepareSearch(typeDescriptor.getIndex());
        searchReqBuilder.setTypes(typeDescriptor.getType())
                .setSource(toSearchSourceBuilder(queryMap))
                .setFetchSource(includesFields, excludesFields);
        // unused since sort fields are already included in the search query mapping
        // ArrayNode sortFields = (ArrayNode) queryMap.path(QueryConverter.SORT_KEY);
        SearchHit[] searchHits = null;
        long totalCount = 0;
        try {
            response = searchReqBuilder
                    .execute()
                    .actionGet(getQueryTimeout());
            searchHits = response.getHits().getHits();
            totalCount = response.getHits().getTotalHits();
            if (totalCount > Integer.MAX_VALUE) {
                throw new RuntimeException("Total hits exceeds integer max value");
            }
        } catch (IndexNotFoundException infe) {
            logger.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex());
        } catch (SearchPhaseExecutionException spee) {
            logger.warn(GENERIC_SEARCH_ERROR, spee.getMessage(), spee);
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
        Client client = getClient();
        // TODO check for fetch none
        JsonNode queryMap = queryConverter.convertQuery(query);
        SearchRequestBuilder searchReqBuilder = client.prepareSearch(typeDescriptor.getIndex());
        SearchHits searchHits = null;
        try {
            SearchResponse response = searchReqBuilder.setTypes(typeDescriptor.getType())
                    .setSource(toSearchSourceBuilder(queryMap))
                    .execute()
                    .actionGet(getQueryTimeout());
            searchHits = response.getHits();
        } catch (IndexNotFoundException infe) {
            logger.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex());
        } catch (SearchPhaseExecutionException spee) {
            logger.warn(GENERIC_SEARCH_ERROR, spee.getMessage(), spee);
        }
        if (searchHits == null) {
            return 0;
        }
        return searchHits.getTotalHits();
    }

    @Override
    public void delete(TypeDescriptor typeDescriptor, String id) throws ClientException {
        Client client = getClient();
        try {
            client.prepareDelete()
                    .setIndex(typeDescriptor.getIndex())
                    .setType(typeDescriptor.getType())
                    .setId(id)
                    .get(getQueryTimeout());
        } catch (InvalidIndexNameException iine) {
            logger.warn("Index '{}' not valid", typeDescriptor.getIndex(), iine);
        } catch (IndexNotFoundException infe) {
            logger.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex(), infe);
        }
    }

    @Override
    public void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        Client client = getClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        TimeValue queryTimeout = getQueryTimeout();
        TimeValue scrollTimeout = getScrollTimeout();

        SearchResponse scrollResponse = null;
        try {
            // delete by query API is deprecated, scroll with bulk delete must be used
            scrollResponse = client.prepareSearch(typeDescriptor.getIndex())
                    .setTypes(typeDescriptor.getType())
                    .setFetchSource(false)
                    .addSort(DOC, SortOrder.ASC)
                    .setVersion(true)
                    .setScroll(scrollTimeout)
                    .setSource(toSearchSourceBuilder(queryMap))
                    .get(queryTimeout);
        } catch (IndexNotFoundException infe) {
            logger.warn(CANNOT_FIND_INDEX, typeDescriptor.getIndex());
        } catch (SearchPhaseExecutionException spee) {
            logger.warn(GENERIC_SEARCH_ERROR, spee.getMessage(), spee);
        }

        if (scrollResponse != null) {
            // Scroll until no hits are returned
            while (true) {

                // Break condition: No hits are returned
                if (scrollResponse.getHits().getHits().length == 0) {
                    break;
                }

                BulkRequest bulkRequest = new BulkRequest();
                for (SearchHit hit : scrollResponse.getHits().hits()) {
                    DeleteRequest delete = new DeleteRequest().index(hit.index())
                            .type(hit.type())
                            .id(hit.id());
                    bulkRequest.add(delete);
                }

                client.bulk(bulkRequest).actionGet(queryTimeout);

                scrollResponse = client.prepareSearchScroll(scrollResponse.getScrollId())
                        .setScroll(scrollTimeout)
                        .execute()
                        .actionGet(queryTimeout);
            }
        }
    }

    @Override
    public IndexResponse isIndexExists(IndexRequest indexRequest) throws ClientException {
        Client client = getClient();
        IndicesExistsResponse response = client.admin().indices()
                .exists(new IndicesExistsRequest(indexRequest.getIndex()))
                .actionGet(getQueryTimeout());
        return new IndexResponse(response.isExists());
    }

    @Override
    public IndexResponse findIndexes(IndexRequest indexRequest) throws ClientException {
        Client client = getClient();
        try {
            GetSettingsResponse response = client.admin().indices().prepareGetSettings(indexRequest.getIndex())
                    .get(getQueryTimeout());
            List<String> list = new ArrayList<>();
            response.getIndexToSettings().keysIt().forEachRemaining(list::add);
            return new IndexResponse(list.toArray(new String[list.size()]));
        }
        catch (IndexNotFoundException e) {
            return new IndexResponse(new String[0]);
        }
    }

    @Override
    public void createIndex(String indexName, ObjectNode indexSettings) throws ClientException {
        Client client = getClient();
        client.admin()
                .indices()
                .prepareCreate(indexName)
                .setSettings(indexSettings.toString(), XContentType.JSON)
                .execute()
                .actionGet(getQueryTimeout());
    }

    @Override
    public boolean isMappingExists(TypeDescriptor typeDescriptor) throws ClientException {
        Client client = getClient();
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(typeDescriptor.getIndex());
        GetMappingsResponse mappingsResponse = client.admin().indices().getMappings(mappingsRequest).actionGet(getQueryTimeout());
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(typeDescriptor.getIndex());
        MappingMetaData metadata = map.get(typeDescriptor.getType());
        return metadata != null;
    }

    @Override
    public void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) throws ClientException {
        Client client = getClient();
        // Check message type mapping
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(typeDescriptor.getIndex());
        GetMappingsResponse mappingsResponse = client.admin().indices().getMappings(mappingsRequest).actionGet(getQueryTimeout());
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(typeDescriptor.getIndex());
        MappingMetaData metadata = map.get(typeDescriptor.getType());
        if (metadata == null) {
            logger.debug("Put mapping: '{}'", mapping);
            client.admin().indices().preparePutMapping(typeDescriptor.getIndex()).setType(typeDescriptor.getType()).setSource(mapping.toString(), XContentType.JSON)
                    .execute().actionGet(getQueryTimeout());
            logger.trace("Put mapping - mapping {} created! ", typeDescriptor.getType());
        } else {
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
            logger.debug("Search builder: {}", searchSourceBuilder);
            return searchSourceBuilder;
        } catch (Throwable t) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, t, CLIENT_QUERY_PARSING_ERROR_MSG);
        }
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        Client client = getClient();
        final DeleteIndexRequest request = DeleteIndexAction.INSTANCE.newRequestBuilder(client).request();
        request.indices(INDEXES_ALL);
        try {
            DeleteIndexResponse deleteResponse = client.admin().indices().delete(request).actionGet(getQueryTimeout());
            if (!deleteResponse.isAcknowledged()) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
            }
        } catch (IllegalStateException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
        }
    }

    @Override
    public void deleteIndexes(String... indexes) throws ClientException {
        Client client = getClient();
        final DeleteIndexRequest request = DeleteIndexAction.INSTANCE.newRequestBuilder(client).request();
        for (String index : indexes) {
            request.indices(index);
            try {
                logger.debug("Deleting index {}", index);
                DeleteIndexResponse deleteResponse = client.admin().indices().delete(request).actionGet(getQueryTimeout());
                if (!deleteResponse.isAcknowledged()) {
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
                }
                logger.debug("Deleting index {} DONE", index);
            } catch (IllegalStateException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_CANNOT_DELETE_INDEX_ERROR_MSG);
            } catch (IndexNotFoundException e) {
                // do nothing it's not an error
                // switch the log level to debug?
                logger.debug("Deleting index {} : index does not exist!", index);
            }
        }

    }

    @Override
    public void refreshAllIndexes() throws ClientException {
        Client client = getClient();
        // final RefreshRequest request = new RefreshRequestBuilder(client, action).request();//DeleteIndexAction.INSTANCE.newRequestBuilder(client).request();
        final RefreshRequest request = RefreshAction.INSTANCE.newRequestBuilder(client).request();
        request.indices(INDEXES_ALL);
        try {
            RefreshResponse refreshResponse = client.admin().indices().refresh(request).actionGet(getQueryTimeout());
            if (refreshResponse.getFailedShards() > 0) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_REFRESH_INDEX_ERROR_MSG);
            }
        } catch (IllegalStateException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, CLIENT_CANNOT_REFRESH_INDEX_ERROR_MSG);
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
