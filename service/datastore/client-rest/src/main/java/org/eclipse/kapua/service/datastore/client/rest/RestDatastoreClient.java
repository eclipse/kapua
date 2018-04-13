/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientProvider;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.ClientUndefinedException;
import org.eclipse.kapua.service.datastore.client.ClientCommunicationException;
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
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Client implementation based on Elasticsearch rest client.<br>
 * The Elasticsearch client provider is instantiated as singleton.
 *
 * @since 1.0
 */
public class RestDatastoreClient implements org.eclipse.kapua.service.datastore.client.DatastoreClient {

    private static final Logger logger = LoggerFactory.getLogger(RestDatastoreClient.class);

    private final static String GET_ACTION = "GET";
    private final static String DELETE_ACTION = "DELETE";
    private final static String POST_ACTION = "POST";
    private final static String PUT_ACTION = "PUT";
    private final static String HEAD_ACTION = "HEAD";

    private final static String INDEX_ALL = "ALL";

    private static final String KEY_DOC = "doc";
    private static final String KEY_DOC_AS_UPSERT = "doc_as_upsert";
    private static final String KEY_DOC_ID = "_id";
    private static final String KEY_DOC_INDEX = "_index";
    private static final String KEY_DOC_TYPE = "_type";

    private static final String KEY_ITEMS = "items";
    private static final String KEY_RESULT = "result";
    private static final String KEY_STATUS = "status";
    private static final String KEY_UPDATE = "update";

    private static final String KEY_HITS = "hits";
    private static final String KEY_TOTAL = "total";

    private static final ObjectMapper MAPPER;

    private static final String MSG_EMPTY_ERROR = "Empty error message";

    private static final String CLIENT_CANNOT_PARSE_INDEX_RESPONSE_ERROR_MSG = "Cannot convert the indexes list";
    private static final String CLIENT_HITS_MAX_VALUE_EXCEDEED = "Total hits exceeds integer max value";
    private static final String CLIENT_UNDEFINED_MSG = "Elasticsearch client must be not null";
    private static final String CLIENT_CLEANUP_CANNOT_CLOSE_CLIENT_MSG = "Client instance is null. Cannot close the client!";
    private static final String CLIENT_CLEANUP_ERROR_MSG = "Cannot cleanup rest datastore driver. Cannot close Elasticsearch client instance";
    private static final String CLIENT_COMMUNICATION_TIMEOUT_MSG = "Elasticsearch client timeout";
    private static final String CLIENT_GENERIC_ERROR_MSG = "Generic client error";

    private final static Random RANDOM = new Random();
    private final static int MAX_RETRY_ATTEMPT = ClientSettings.getInstance().getInt(ClientSettingsKey.ELASTICSEARCH_REST_TIMEOUT_MAX_RETRY, 3);
    private final static long MAX_RETRY_WAIT_TIME = ClientSettings.getInstance().getLong(ClientSettingsKey.ELASTICSEARCH_REST_TIMEOUT_MAX_WAIT, 2500);

    private static RestDatastoreClient instance;

    private ClientProvider<RestClient> esClientProvider;

    private MetricsService metricService = MetricServiceFactory.getInstance();

    private Counter restCallRuntimeExecCount;
    private Counter timeoutRetryCount;
    private Counter timeoutRetryLimitReachedCount;

    private ModelContext modelContext;
    private QueryConverter queryConverter;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Get the singleton {@link RestDatastoreClient} instance
     *
     * @return
     * @throws ClientUnavailableException
     */
    public static RestDatastoreClient getInstance() throws ClientUnavailableException {
        if (instance == null) {
            synchronized (RestDatastoreClient.class) {
                if (instance == null) {
                    instance = new RestDatastoreClient();
                }
            }
        }
        else if (instance.getClientProvider() == null) {
            synchronized (RestDatastoreClient.class) {
                if (instance.getClientProvider() == null) {
                    instance.initClientProvider();
                }
            }
        }
        return instance;
    }

    /**
     * Default constructor
     * Initialize the client provider ({@link ClientProvider}) as singleton.
     *
     * @throws ClientUnavailableException
     */
    private RestDatastoreClient() throws ClientUnavailableException {
        // lazy synchronization
        if (instance == null) {
            synchronized (RestDatastoreClient.class) {
                if (instance == null) {
                    if (esClientProvider != null) {
                        cleanupClient(true);
                    }
                    initClientProvider();
                    restCallRuntimeExecCount = metricService.getCounter("datastore-rest-client", "rest-client", new String[]{"runtime_exc", "count"});
                    timeoutRetryCount = metricService.getCounter("datastore-rest-client", "rest-client", new String[]{"timeout_retry", "count"});
                    timeoutRetryLimitReachedCount = metricService.getCounter("datastore-rest-client", "rest-client", new String[] { "timeout_retry_limit_reached", "count" });
                }
            }
        }
    }

    private void initClientProvider() throws ClientUnavailableException {
        logger.info("Starting Elasticsearch rest client...");
        esClientProvider = EsRestClientProvider.init();
        logger.info("Starting Elasticsearch rest client... DONE");
    }

    private ClientProvider<RestClient> getClientProvider() {
        return esClientProvider;
    }

    @Override
    public void close() throws ClientUnavailableException {
        synchronized (RestDatastoreClient.class) {
            if (instance != null) {
                if (esClientProvider != null) {
                    logger.info("Stopping Elasticsearch rest client...");
                    // all fine... try to cleanup the client
                    cleanupClient(false);
                    logger.info("Stopping Elasticsearch rest client... DONE");
                } else {
                    logger.warn("Close method called for a not initialized client!");
                }
            } else if (esClientProvider == null) {
                // something wrong happened in the client lifecycle... try to cleanup and raise exception
                cleanupClient(true);
            }
        }
    }

    private void cleanupClient(boolean raiseException) throws ClientUnavailableException {
        Throwable cause = null;
        try {
            if (esClientProvider != null && esClientProvider.getClient() != null) {
                esClientProvider.getClient().close();
            } else {
                logger.warn(CLIENT_CLEANUP_CANNOT_CLOSE_CLIENT_MSG);
            }
            esClientProvider = null;
        } catch (Throwable e) {
            cause = e;
            logger.error(CLIENT_CLEANUP_ERROR_MSG, e);
        }
        if (cause != null) {
            throw new ClientUnavailableException(CLIENT_CLEANUP_ERROR_MSG, cause);
        } else if (raiseException) {
            throw new ClientUnavailableException(CLIENT_CLEANUP_ERROR_MSG);
        }
    }

    @Override
    public InsertResponse insert(InsertRequest insertRequest) throws ClientException {
        checkClient();
        Map<String, Object> storableMap = modelContext.marshal(insertRequest.getStorable());
        logger.debug("Insert - converted object: '{}'", storableMap);
        String json = null;
        try {
            json = MAPPER.writeValueAsString(storableMap);
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
        }
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Response insertResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        POST_ACTION,
                        getInsertTypePath(insertRequest),
                        Collections.<String, String>emptyMap(),
                        entity);
            }
        }, insertRequest.getTypeDescriptor().getIndex(), "INSERT");

        if (isRequestSuccessful(insertResponse)) {
            JsonNode responseNode = null;
            try {
                responseNode = MAPPER.readTree(EntityUtils.toString(insertResponse.getEntity()));
            } catch (IOException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
            }
            String id = responseNode.get(KEY_DOC_ID).asText();
            String index = responseNode.get(KEY_DOC_INDEX).asText();
            String type = responseNode.get(KEY_DOC_TYPE).asText();
            return new InsertResponse(id, new TypeDescriptor(index, type));
        } else {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (insertResponse != null && insertResponse.getStatusLine() != null) ? insertResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public UpdateResponse upsert(UpdateRequest updateRequest) throws ClientException {
        checkClient();
        Map<String, Object> storableMap = modelContext.marshal(updateRequest.getStorable());
        Map<String, Object> updateRequestMap = new HashMap<>();
        updateRequestMap.put(KEY_DOC, storableMap);
        updateRequestMap.put(KEY_DOC_AS_UPSERT, true);
        logger.debug("Upsert - converted object: '{}'", updateRequestMap);
        String json = null;
        try {
            json = MAPPER.writeValueAsString(updateRequestMap);
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
        }
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        Response updateResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        POST_ACTION,
                        getUpsertPath(updateRequest.getTypeDescriptor(), updateRequest.getId()),
                        Collections.<String, String>emptyMap(),
                        entity);
            }

        }, updateRequest.getTypeDescriptor().getIndex(), "UPSERT");
        if (isRequestSuccessful(updateResponse)) {
            JsonNode responseNode = null;
            try {
                responseNode = MAPPER.readTree(EntityUtils.toString(updateResponse.getEntity()));
            } catch (IOException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
            }
            String id = responseNode.get(KEY_DOC_ID).asText();
            String index = responseNode.get(KEY_DOC_INDEX).asText();
            String type = responseNode.get(KEY_DOC_TYPE).asText();
            return new UpdateResponse(id, new TypeDescriptor(index, type));
        } else {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (updateResponse != null && updateResponse.getStatusLine() != null) ? updateResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public BulkUpdateResponse upsert(BulkUpdateRequest bulkUpdateRequest) throws ClientException {
        checkClient();
        StringBuilder bulkOperation = new StringBuilder();
        for (UpdateRequest upsertRequest : bulkUpdateRequest.getRequest()) {
            Map<String, Object> storableMap = modelContext.marshal(upsertRequest.getStorable());
            bulkOperation.append("{ \"update\": {\"_id\": \"")
                    .append(upsertRequest.getId())
                    .append("\", \"_type\": \"")
                    .append(upsertRequest.getTypeDescriptor().getType())
                    .append("\", \"_index\": \"")
                    .append(upsertRequest.getTypeDescriptor().getIndex())
                    .append("\"}\n");

            bulkOperation.append("{ \"doc\": ");
            try {

                bulkOperation.append(MAPPER.writeValueAsString(storableMap));
            } catch (IOException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
            }
            bulkOperation.append(", \"doc_as_upsert\": true }\n");
        }
        Response updateResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        POST_ACTION,
                        getBulkPath(),
                        Collections.<String, String>emptyMap(),
                        EntityBuilder.create().setText(bulkOperation.toString()).build(),
                        new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            }

        }, "multi-index", "UPSERT BULK");
        if (isRequestSuccessful(updateResponse)) {
            BulkUpdateResponse bulkResponse = new BulkUpdateResponse();
            JsonNode responseNode = null;
            try {
                responseNode = MAPPER.readTree(EntityUtils.toString(updateResponse.getEntity()));
            } catch (IOException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
            }
            ArrayNode items = (ArrayNode) responseNode.get(KEY_ITEMS);
            for (JsonNode item : items) {
                JsonNode jsonNode = item.get(KEY_UPDATE);
                if (jsonNode != null) {
                    JsonNode idNode = jsonNode.get(KEY_DOC_ID);
                    String metricId = null;
                    if (idNode != null) {
                        metricId = idNode.asText();
                    }
                    String indexName = jsonNode.get(KEY_DOC_INDEX).asText();
                    String typeName = jsonNode.get(KEY_DOC_TYPE).asText();
                    int responseCode = jsonNode.get(KEY_STATUS).asInt();
                    if (!isRequestSuccessful(responseCode)) {
                        JsonNode failureNode = jsonNode.get(KEY_RESULT);
                        String failureMessage = MSG_EMPTY_ERROR;
                        if (failureNode != null) {
                            failureMessage = failureNode.asText();
                        }
                        bulkResponse.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName), failureMessage));
                        logger.info("Upsert failed [{}, {}, {}]", new Object[] { indexName, typeName, failureMessage });
                        continue;
                    }
                    bulkResponse.add(new UpdateResponse(metricId, new TypeDescriptor(indexName, typeName)));
                    logger.debug("Upsert on channel metric succesfully executed [{}.{}, {}]", new Object[] { indexName, typeName, metricId });
                } else {
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, "Unexpected action response");
                }
            }
            return bulkResponse;
        } else {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (updateResponse != null && updateResponse.getStatusLine() != null) ? updateResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public <T> T find(TypeDescriptor typeDescriptor, Object query, Class<T> clazz) throws ClientException {
        checkClient();
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
        logger.debug("Query - converted query: '{}'", queryMap);
        long totalCount = 0;
        ArrayNode resultsNode = null;
        Response queryResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        GET_ACTION,
                        getSearchPath(typeDescriptor),
                        Collections.<String, String>emptyMap(),
                        EntityBuilder.create().setText(MAPPER.writeValueAsString(queryMap)).build(),
                        new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            }

        }, typeDescriptor.getIndex(), "QUERY");
        if (isRequestSuccessful(queryResponse)) {
            JsonNode responseNode;
            try {
                responseNode = MAPPER.readTree(EntityUtils.toString(queryResponse.getEntity()));
            } catch (IOException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
            }
            JsonNode hitsNode = responseNode.get(KEY_HITS);
            totalCount = hitsNode.get(KEY_TOTAL).asInt();
            if (totalCount > Integer.MAX_VALUE) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, "Total hits exceeds integer max value");
            }
            resultsNode = ((ArrayNode) hitsNode.get(KEY_HITS));
        } else if (queryResponse != null) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (queryResponse != null && queryResponse.getStatusLine() != null) ? queryResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
        ResultList<T> resultList = new ResultList<>(totalCount);
        if (resultsNode != null && resultsNode.size() > 0) {
            for (JsonNode result : resultsNode) {
                @SuppressWarnings("unchecked")
                Map<String, Object> object = MAPPER.convertValue(result.get(SchemaKeys.KEY_SOURCE), Map.class);
                String id = result.get(KEY_DOC_ID).asText();
                String index = result.get(KEY_DOC_INDEX).asText();
                String type = result.get(KEY_DOC_TYPE).asText();
                object.put(ModelContext.TYPE_DESCRIPTOR_KEY, new TypeDescriptor(index, type));
                object.put(ModelContext.DATASTORE_ID_KEY, id);
                object.put(QueryConverter.QUERY_FETCH_STYLE_KEY, queryFetchStyle);
                resultList.add(modelContext.unmarshal(clazz, object));
            }
        }
        return resultList;
    }

    @Override
    public long count(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        checkClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        logger.debug("Query - converted query: '{}'", queryMap);
        long totalCount = 0;
        Response queryResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        GET_ACTION,
                        getSearchPath(typeDescriptor),
                        Collections.<String, String>emptyMap(),
                        EntityBuilder.create().setText(MAPPER.writeValueAsString(queryMap)).build(),
                        new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            }

        }, typeDescriptor.getIndex(), "COUNT");
        if (isRequestSuccessful(queryResponse)) {
            JsonNode responseNode = null;
            try {
                responseNode = MAPPER.readTree(EntityUtils.toString(queryResponse.getEntity()));
            } catch (IOException e) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
            }
            JsonNode hitsNode = responseNode.get(KEY_HITS);
            totalCount = hitsNode.get(KEY_TOTAL).asInt();
            if (totalCount > Integer.MAX_VALUE) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_HITS_MAX_VALUE_EXCEDEED);
            }
        } else if (queryResponse != null) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (queryResponse != null && queryResponse.getStatusLine() != null) ? queryResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
        return totalCount;
    }

    @Override
    public void delete(TypeDescriptor typeDescriptor, String id) throws ClientException {
        logger.debug("Delete - id: '{}'", id);
        checkClient();
        Response deleteResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        DELETE_ACTION,
                        getIdPath(typeDescriptor, id),
                        Collections.<String, String>emptyMap());
            }
        }, typeDescriptor.getIndex(), "DELETE");
        if (deleteResponse != null && !isRequestSuccessful(deleteResponse)) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (deleteResponse != null && deleteResponse.getStatusLine() != null) ? deleteResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        checkClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        JsonNode deleteRequestNode = queryMap.get(SchemaKeys.KEY_QUERY);
        logger.debug("Query - converted query: '{}'", queryMap);
        Response deleteResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        POST_ACTION,
                        getDeleteByQueryPath(typeDescriptor),
                        Collections.<String, String>emptyMap(),
                        EntityBuilder.create().setText(MAPPER.writeValueAsString(deleteRequestNode)).build(),
                        new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            }
        }, typeDescriptor.getIndex(), "DELETE BY QUERY");
        if (deleteResponse != null && !isRequestSuccessful(deleteResponse)) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (deleteResponse != null && deleteResponse.getStatusLine() != null) ? deleteResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public IndexResponse isIndexExists(IndexRequest indexRequest) throws ClientException {
        logger.debug("Index exists - index name: '{}'", indexRequest.getIndex());
        checkClient();
        Response isIndexExistsResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        HEAD_ACTION,
                        getIndexPath(indexRequest.getIndex()),
                        Collections.<String, String>emptyMap());
            }
        }, indexRequest.getIndex(), "INDEX EXIST");
        if (isIndexExistsResponse != null && isIndexExistsResponse.getStatusLine() != null) {
            if (isIndexExistsResponse.getStatusLine().getStatusCode() == 200) {
                return new IndexResponse(true);
            } else if (isIndexExistsResponse.getStatusLine().getStatusCode() == 404) {
                return new IndexResponse(false);
            }
        }
        throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                (isIndexExistsResponse != null && isIndexExistsResponse.getStatusLine() != null) ? isIndexExistsResponse.getStatusLine().getReasonPhrase() : "");
    }

    @Override
    public IndexResponse findIndexes(IndexRequest indexRequest) throws ClientException {
        logger.debug("Find indexes - index prefix: '{}'", indexRequest.getIndex());
        Response isIndexExistsResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        GET_ACTION,
                        getFindIndexPath(indexRequest.getIndex()),
                        Collections.singletonMap("pretty", "true"));
            }
        }, "*", "INDEX EXIST");
        if (isIndexExistsResponse != null && isIndexExistsResponse.getStatusLine() != null) {
            if (isIndexExistsResponse.getStatusLine().getStatusCode() == 200) {
                try {
                    return new IndexResponse(EntityUtils.toString(isIndexExistsResponse.getEntity()).split("\n"));
                } catch (ParseException | IOException e) {
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_CANNOT_PARSE_INDEX_RESPONSE_ERROR_MSG, e);
                }
            } else if (isIndexExistsResponse.getStatusLine().getStatusCode() == 404) {
                return new IndexResponse(null);
            }
        }
        throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                (isIndexExistsResponse != null && isIndexExistsResponse.getStatusLine() != null) ? isIndexExistsResponse.getStatusLine().getReasonPhrase() : "");
    }

    @Override
    public void createIndex(String indexName, ObjectNode indexSettings) throws ClientException {
        logger.debug("Create index - object: '{}'", indexSettings);
        checkClient();
        Response createIndexResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        PUT_ACTION,
                        getIndexPath(indexName),
                        Collections.<String, String>emptyMap(),
                        EntityBuilder.create().setText(MAPPER.writeValueAsString(indexSettings)).build(),
                        new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            }

        }, indexName, "CREATE INDEX");
        if (!isRequestSuccessful(createIndexResponse)) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (createIndexResponse != null && createIndexResponse.getStatusLine() != null) ? createIndexResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public boolean isMappingExists(TypeDescriptor typeDescriptor) throws ClientException {
        logger.debug("Mapping exists - mapping name: '{} - {}'", typeDescriptor.getIndex(), typeDescriptor.getType());
        checkClient();
        Response isMappingExistsResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        GET_ACTION,
                        getMappingPath(typeDescriptor),
                        Collections.<String, String>emptyMap());
            }
        }, typeDescriptor.getIndex(), "MAPPING EXIST");
        if (isMappingExistsResponse !=null && isMappingExistsResponse.getStatusLine()!=null) {
            if (isMappingExistsResponse.getStatusLine().getStatusCode() == 200) {
                return true;
            } else if (isMappingExistsResponse.getStatusLine().getStatusCode() == 404) {
                return false;
            }
        }
        throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                (isMappingExistsResponse != null && isMappingExistsResponse.getStatusLine() != null) ? isMappingExistsResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
    }

    @Override
    public void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) throws ClientException {
        logger.debug("Create mapping - object: '{}, index: {}, type: {}'", mapping, typeDescriptor.getIndex(), typeDescriptor.getType());
        checkClient();
        Response createMappingResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        PUT_ACTION,
                        getMappingPath(typeDescriptor),
                        Collections.<String, String>emptyMap(),
                        EntityBuilder.create().setText(MAPPER.writeValueAsString(mapping)).build(),
                        new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            }
        }, typeDescriptor.getIndex(), "PUT MAPPING");
        if (!isRequestSuccessful(createMappingResponse)) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (createMappingResponse != null && createMappingResponse.getStatusLine() != null) ? createMappingResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public void refreshAllIndexes() throws ClientException {
        logger.debug("Refresh all indexes");
        checkClient();
        Response refreshIndexResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        POST_ACTION,
                        getRefreshAllIndexesPath(),
                        Collections.<String, String>emptyMap());
            }
        }, INDEX_ALL, "REFRESH INDEX");
        if (!isRequestSuccessful(refreshIndexResponse)) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (refreshIndexResponse != null && refreshIndexResponse.getStatusLine() != null) ? refreshIndexResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        logger.debug("Delete all indexes");
        checkClient();
        Response deleteIndexResponse = restCallTimeoutHandler(new Callable<Response>() {

            @Override
            public Response call() throws Exception {
                return esClientProvider.getClient().performRequest(
                        DELETE_ACTION,
                        getIndexPath("_all"),
                        Collections.<String, String>emptyMap());
            }
        }, INDEX_ALL, "DELETE INDEX");
        if (!isRequestSuccessful(deleteIndexResponse)) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                    (deleteIndexResponse != null && deleteIndexResponse.getStatusLine() != null) ? deleteIndexResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
        }
    }

    @Override
    public void deleteIndexes(String... indexes) throws ClientException {
        logger.debug("Delete indexes");
        checkClient();
        for (String index : indexes) {
            logger.debug("Delete index {}", index);
            Response deleteIndexResponse = restCallTimeoutHandler(new Callable<Response>() {

                @Override
                public Response call() throws Exception {
                    logger.debug("Deleting index {}", index);
                    return esClientProvider.getClient().performRequest(
                            DELETE_ACTION,
                            getIndexPath(index),
                            Collections.<String, String>emptyMap());
                }
            }, index, "DELETE INDEX");

            // for that call the deleteIndexResponse=null case could be considered as good response since if an index doesn't exist (404) the delete could be considered successful.
            // the deleteIndexResponse is null also if the error is due to a bad index request (400) but this error, except if there is an application bug, shouldn't never happen.
            if (deleteIndexResponse == null) {
                logger.debug("Deleting index {} : index does not exist", index);               
            } else if (!isRequestSuccessful(deleteIndexResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR,
                        (deleteIndexResponse != null && deleteIndexResponse.getStatusLine() != null) ? deleteIndexResponse.getStatusLine().getReasonPhrase() : CLIENT_GENERIC_ERROR_MSG);
            }
            logger.debug("Deleting index {} DONE", index);
        }
    }

    @Override
    public void setModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
    }

    @Override
    public void setQueryConverter(QueryConverter queryConverter) {
        this.queryConverter = queryConverter;
    }

    private <T> T restCallTimeoutHandler(Callable<T> restAction, String index, String operationName) throws ClientException {
        int retryCount = 0;
        try {
            do {
                try {
                    return restAction.call();
                } catch (RuntimeException e) {
                    restCallRuntimeExecCount.inc();
                    if (e.getCause() instanceof TimeoutException) {
                        timeoutRetryCount.inc();
                        if (retryCount < MAX_RETRY_ATTEMPT - 1) {
                            // try again
                            try {
                                Thread.sleep((long) (MAX_RETRY_WAIT_TIME * (0.5 + RANDOM.nextFloat() / 2)));
                            } catch (InterruptedException e1) {
                                // DO NOTHING
                            }
                        }
                    } else {
                        throw e;
                    }
                }
            } while (++retryCount <= MAX_RETRY_ATTEMPT);
        } catch (ResponseException re) {
            if (re.getResponse().getStatusLine().getStatusCode() == 404) {
                logger.warn("Resource for index '{}' not found on action '{}'! {}", index, operationName, re.getLocalizedMessage());
                return (T) null;
            } else if (re.getResponse().getStatusLine().getStatusCode() == 400) {
                logger.warn("Bad request for index '{}' on action '{}'! {}", index, operationName, re.getLocalizedMessage());
                return (T) null;
            } else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, re);
            }
        } catch (JsonProcessingException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e);
        } catch (Exception e) {
            throw new ClientException(KapuaErrorCodes.SEVERE_INTERNAL_ERROR);
        }
        timeoutRetryLimitReachedCount.inc();
        throw new ClientCommunicationException(CLIENT_COMMUNICATION_TIMEOUT_MSG, null);
    }

    private boolean isRequestSuccessful(Response response) {
        if (response != null && response.getStatusLine() != null) {
            return isRequestSuccessful(response.getStatusLine().getStatusCode());
        } else {
            return false;
        }
    }

    private boolean isRequestSuccessful(int responseCode) {
        return (200 <= responseCode && responseCode <= 299);
    }

    private String getRefreshAllIndexesPath() {
        return "/_all/_refresh";
    }

    private String getIndexPath(String index) {
        return String.format("/%s", index);
    }

    private String getFindIndexPath(String index) {
        return String.format("/_cat/indices?h=index&index=%s", index);
    }

    private String getBulkPath() {
        return String.format("/_bulk");
    }

    private String getInsertTypePath(InsertRequest request) {
        if (request.getId() != null) {
            return String.format("%s?id=%s&version=1&version_type=external", getTypePath(request.getTypeDescriptor()), request.getId());
        } else {
            return getTypePath(request.getTypeDescriptor());
        }
    }

    private String getTypePath(TypeDescriptor typeDescriptor) {
        return String.format("/%s/%s", typeDescriptor.getIndex(), typeDescriptor.getType());
    }

    private String getDeleteByQueryPath(TypeDescriptor typeDescriptor) {
        return String.format("/%s/%s/_delete_by_query", typeDescriptor.getIndex(), typeDescriptor.getType());
    }

    private String getMappingPath(TypeDescriptor typeDescriptor) {
        return String.format("/%s/_mapping/%s", typeDescriptor.getIndex(), typeDescriptor.getType());
    }

    private String getIdPath(TypeDescriptor typeDescriptor, String id) throws UnsupportedEncodingException {
        return String.format("/%s/%s/%s", typeDescriptor.getIndex(), typeDescriptor.getType(), URLEncoder.encode(id, "UTF-8"));
    }

    private String getUpsertPath(TypeDescriptor typeDescriptor, String id) throws UnsupportedEncodingException {
        return String.format("/%s/%s/%s/_update", typeDescriptor.getIndex(), typeDescriptor.getType(), URLEncoder.encode(id, "UTF-8"));
    }

    private String getSearchPath(TypeDescriptor typeDescriptor) {
        return String.format("/%s/%s/_search", typeDescriptor.getIndex(), typeDescriptor.getType());
    }

    private void checkClient() throws ClientUndefinedException {
        if (esClientProvider == null || esClientProvider.getClient() == null) {
            throw new ClientUndefinedException(CLIENT_UNDEFINED_MSG);
        }
    }

}
