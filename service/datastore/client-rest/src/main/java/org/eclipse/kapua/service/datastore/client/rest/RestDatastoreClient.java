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

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SOURCE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientProvider;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.ClientUndefinedException;
import org.eclipse.kapua.service.datastore.client.ModelContext;
import org.eclipse.kapua.service.datastore.client.QueryConverter;
import org.eclipse.kapua.service.datastore.client.SchemaKeys;
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
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Client implementation based on Elasticsearch rest client.<br>
 * The Elasticsearch client provider is instantiated as singleton by reflection using those provided by {@link ClientSettingsKey#ELASTICSEARCH_CLIENT_PROVIDER}
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

    private static final String CLIENT_HITS_MAX_VALUE_EXCEDEED = "Total hits exceeds integer max value";
    private static final String CLIENT_UNDEFINED_MSG = "Elasticsearch client must be not null";
    private static final String CLIENT_CLEANUP_ERROR_MSG = "Cannot cleanup transport datastore driver. Cannot close Elasticsearch client instance";
    private final static String CLIENT_CANNOT_LOAD_CLIENT_ERROR_MSG = "Cannot load the provided client class name [%s]. Check the configuration.";
    private final static String CLIENT_CLASS_NAME;
    private static Class<ClientProvider<RestClient>> providerInstance;
    private static RestDatastoreClient instance;

    private ClientProvider<RestClient> esClientProvider;

    private ModelContext modelContext;
    private QueryConverter queryConverter;

    static {
        ClientSettings config = ClientSettings.getInstance();
        CLIENT_CLASS_NAME = config.getString(ClientSettingsKey.ELASTICSEARCH_CLIENT_PROVIDER);
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
        return instance;
    }

    /**
     * Default constructor
     * Initialize the client provider ({@link ClientProvider}) as singleton. The implementation is specified by {@link ClientSettingsKey#ELASTICSEARCH_CLIENT_PROVIDER}
     * 
     * @throws ClientUnavailableException
     */
    @SuppressWarnings("unchecked")
    private RestDatastoreClient() throws ClientUnavailableException {
        // lazy synchronization
        if (instance == null) {
            synchronized (RestDatastoreClient.class) {
                if (instance == null) {
                    if (esClientProvider != null) {
                        cleanupClient(true);
                    }
                    logger.info("Starting Elasticsearch transport client...");
                    try {
                        providerInstance = (Class<ClientProvider<RestClient>>) Class.forName(CLIENT_CLASS_NAME);
                    } catch (ClassNotFoundException e) {
                        throw new ClientUnavailableException(String.format(CLIENT_CANNOT_LOAD_CLIENT_ERROR_MSG, CLIENT_CLASS_NAME), e);
                    }
                    try {
                        // esClientProvider = providerInstance.newInstance();
                        Method initMethod = providerInstance.getMethod("init", new Class[0]);
                        initMethod.invoke(null, new Object[0]);
                        Method getInstanceMethod = providerInstance.getMethod("getInstance", new Class[0]);
                        esClientProvider = (ClientProvider<RestClient>) getInstanceMethod.invoke(null, new Object[0]);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        instance = null;
                        throw new ClientUnavailableException(String.format(CLIENT_CANNOT_LOAD_CLIENT_ERROR_MSG, CLIENT_CLASS_NAME), e);
                    }
                    logger.info("Starting Elasticsearch transport client... DONE");
                }
            }
        }
    }

    @Override
    public void close() throws ClientUnavailableException {
        synchronized (RestDatastoreClient.class) {
            if (instance != null) {
                if (esClientProvider != null) {
                    logger.info("Stopping Elasticsearch transport client...");
                    // all fine... try to cleanup the client
                    cleanupClient(false);
                    logger.info("Stopping Elasticsearch transport client... DONE");
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
            esClientProvider.getClient().close();
            esClientProvider = null;
            instance = null;
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
        try {
            Map<String, Object> storableMap = modelContext.marshal(insertRequest.getStorable());
            logger.info("Insert - converted object: '{}'", storableMap.toString());
            String json = MAPPER.writeValueAsString(storableMap);
            HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            Response insertResponse = esClientProvider.getClient().performRequest(
                    POST_ACTION,
                    getTypePath(insertRequest.getTypeDescriptor()),
                    Collections.<String, String> emptyMap(),
                    entity);
            if (isRequestSuccessful(insertResponse)) {
                JsonNode responseNode = MAPPER.readTree(EntityUtils.toString(insertResponse.getEntity()));
                String id = ((TextNode) responseNode.get(KEY_DOC_ID)).asText();
                String index = ((TextNode) responseNode.get(KEY_DOC_INDEX)).asText();
                String type = ((TextNode) responseNode.get(KEY_DOC_TYPE)).asText();
                return new InsertResponse(id, new TypeDescriptor(index, type));
            }
            else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, insertResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public UpdateResponse upsert(UpdateRequest updateRequest) throws ClientException {
        checkClient();
        try {
            Map<String, Object> storableMap = modelContext.marshal(updateRequest.getStorable());
            Map<String, Object> updateRequestMap = new HashMap<>();
            updateRequestMap.put(KEY_DOC, storableMap);
            updateRequestMap.put(KEY_DOC_AS_UPSERT, true);
            logger.info("Upsert - converted object: '{}'", updateRequestMap.toString());
            String json = MAPPER.writeValueAsString(updateRequestMap);
            HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            Response updateResponse = esClientProvider.getClient().performRequest(
                    POST_ACTION,
                    getUpsertPath(updateRequest.getTypeDescriptor(), updateRequest.getId()),
                    Collections.<String, String> emptyMap(),
                    entity);
            if (isRequestSuccessful(updateResponse)) {
                JsonNode responseNode = MAPPER.readTree(EntityUtils.toString(updateResponse.getEntity()));
                String id = ((TextNode) responseNode.get(KEY_DOC_ID)).asText();
                String index = ((TextNode) responseNode.get(KEY_DOC_INDEX)).asText();
                String type = ((TextNode) responseNode.get(KEY_DOC_TYPE)).asText();
                return new UpdateResponse(id, new TypeDescriptor(index, type));
            } else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, updateResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Throwable e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public BulkUpdateResponse upsert(BulkUpdateRequest bulkUpdateRequest) throws ClientException {
        checkClient();
        try {
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

                bulkOperation.append("{ \"doc\": ")
                        .append(MAPPER.writeValueAsString(storableMap))
                        .append(", \"doc_as_upsert\": true }\n");
            }
            Response updateResponse = esClientProvider.getClient().performRequest(
                    POST_ACTION,
                    getBulkPath(),
                    Collections.<String, String> emptyMap(),
                    EntityBuilder.create().setText(bulkOperation.toString()).build(),
                    new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            if (isRequestSuccessful(updateResponse)) {
                BulkUpdateResponse bulkResponse = new BulkUpdateResponse();
                JsonNode responseNode = MAPPER.readTree(EntityUtils.toString(updateResponse.getEntity()));
                ArrayNode items = ((ArrayNode) responseNode.get(KEY_ITEMS));
                for (JsonNode item : items) {
                    JsonNode itemNode = item.get(KEY_UPDATE);
                    if (itemNode != null) {
                        TextNode idNode = ((TextNode) itemNode.get(KEY_DOC_ID));
                        String metricId = null;
                        if (idNode != null) {
                            metricId = idNode.asText();
                        }
                        String indexName = ((TextNode) itemNode.get(KEY_DOC_INDEX)).asText();
                        String typeName = ((TextNode) itemNode.get(KEY_DOC_TYPE)).asText();
                        int responseCode = ((NumericNode) itemNode.get(KEY_STATUS)).asInt();
                        if (!isRequestSuccessful(responseCode)) {
                            String failureMessage = ((TextNode) itemNode.get(KEY_RESULT)).asText();
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
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, updateResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
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
        logger.debug("Query - converted query: '{}'", queryMap.toString());
        long totalCount = 0;
        Response queryResponse = null;
        ArrayNode resultsNode = null;
        try {
            queryResponse = esClientProvider.getClient().performRequest(
                    GET_ACTION,
                    getSearchPath(typeDescriptor),
                    Collections.<String, String> emptyMap(),
                    EntityBuilder.create().setText(MAPPER.writeValueAsString(queryMap)).build(),
                    new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            if (isRequestSuccessful(queryResponse)) {
                JsonNode responseNode = MAPPER.readTree(EntityUtils.toString(queryResponse.getEntity()));
                JsonNode hitsNode = responseNode.get(KEY_HITS);
                totalCount = ((NumericNode) hitsNode.get(KEY_TOTAL)).asInt();
                if (totalCount > Integer.MAX_VALUE) {
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, "Total hits exceeds integer max value");
                }
                resultsNode = ((ArrayNode) hitsNode.get(KEY_HITS));
            } else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, queryResponse.getStatusLine().getReasonPhrase());
            }
        } catch (ResponseException re) {
            handleResponseException(re, typeDescriptor.getIndex(), "QUERY");
        } catch (JsonProcessingException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
        ResultList<T> resultList = new ResultList<T>(totalCount);
        if (resultsNode != null && resultsNode.size() > 0) {
            for (JsonNode result : resultsNode) {
                @SuppressWarnings("unchecked")
                Map<String, Object> object = MAPPER.convertValue(result.get(KEY_SOURCE), Map.class);
                String id = ((TextNode) result.get(KEY_DOC_ID)).asText();
                String index = ((TextNode) result.get(KEY_DOC_INDEX)).asText();
                String type = ((TextNode) result.get(KEY_DOC_TYPE)).asText();
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
        logger.debug("Query - converted query: '{}'", queryMap.toString());
        long totalCount = 0;
        Response queryResponse = null;
        try {
            queryResponse = esClientProvider.getClient().performRequest(
                    GET_ACTION,
                    getSearchPath(typeDescriptor),
                    Collections.<String, String> emptyMap(),
                    EntityBuilder.create().setText(MAPPER.writeValueAsString(queryMap)).build(),
                    new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            if (isRequestSuccessful(queryResponse)) {
                JsonNode responseNode = MAPPER.readTree(EntityUtils.toString(queryResponse.getEntity()));
                JsonNode hitsNode = responseNode.get(KEY_HITS);
                totalCount = ((NumericNode) hitsNode.get(KEY_TOTAL)).asInt();
                if (totalCount > Integer.MAX_VALUE) {
                    throw new ClientException(ClientErrorCodes.ACTION_ERROR, CLIENT_HITS_MAX_VALUE_EXCEDEED);
                }
            } else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, queryResponse.getStatusLine().getReasonPhrase());
            }
        } catch (ResponseException re) {
            handleResponseException(re, typeDescriptor.getIndex(), "COUNT");
        } catch (JsonProcessingException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
        return totalCount;
    }

    @Override
    public void delete(TypeDescriptor typeDescriptor, String id) throws ClientException {
        logger.debug("Delete - id: '{}'", id);
        checkClient();
        Response deleteResponse = null;
        try {
            deleteResponse = esClientProvider.getClient().performRequest(
                    DELETE_ACTION,
                    getIdPath(typeDescriptor, id),
                    Collections.<String, String> emptyMap());
            if (!isRequestSuccessful(deleteResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, deleteResponse.getStatusLine().getReasonPhrase());
            }
        } catch (ResponseException re) {
            handleResponseException(re, typeDescriptor.getIndex(), "DELETE");
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteByQuery(TypeDescriptor typeDescriptor, Object query) throws ClientException {
        checkClient();
        JsonNode queryMap = queryConverter.convertQuery(query);
        JsonNode deleteRequestNode = queryMap.get(SchemaKeys.KEY_QUERY);
        logger.debug("Query - converted query: '{}'", queryMap.toString());
        Response deleteResponse = null;
        try {
            deleteResponse = esClientProvider.getClient().performRequest(
                    POST_ACTION,
                    getDeleteByQueryPath(typeDescriptor),
                    Collections.<String, String> emptyMap(),
                    EntityBuilder.create().setText(MAPPER.writeValueAsString(deleteRequestNode)).build(),
                    new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            if (!isRequestSuccessful(deleteResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, deleteResponse.getStatusLine().getReasonPhrase());
            }
        } catch (ResponseException re) {
            handleResponseException(re, typeDescriptor.getIndex(), "DELETE BY QUERY");
        } catch (JsonProcessingException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public IndexExistsResponse isIndexExists(IndexExistsRequest indexExistsRequest) throws ClientException {
        logger.debug("Index exists - index name: '{}'", indexExistsRequest.getIndex());
        checkClient();
        Response isIndexExistsResponse = null;
        try {
            isIndexExistsResponse = esClientProvider.getClient().performRequest(
                    HEAD_ACTION,
                    getIndexPath(indexExistsRequest.getIndex()),
                    Collections.<String, String> emptyMap());
            if (isIndexExistsResponse.getStatusLine().getStatusCode() == 200) {
                return new IndexExistsResponse(true);
            } else if (isIndexExistsResponse.getStatusLine().getStatusCode() == 404) {
                return new IndexExistsResponse(false);
            } else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, isIndexExistsResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public void createIndex(String indexName, ObjectNode indexSettings) throws ClientException {
        logger.debug("Create index - object: '{}'", indexSettings);
        checkClient();
        Response createIndexResponse = null;
        try {
            createIndexResponse = esClientProvider.getClient().performRequest(
                    PUT_ACTION,
                    getIndexPath(indexName),
                    Collections.<String, String> emptyMap(),
                    EntityBuilder.create().setText(MAPPER.writeValueAsString(indexSettings)).build(),
                    new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            if (!isRequestSuccessful(createIndexResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, createIndexResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public boolean isMappingExists(TypeDescriptor typeDescriptor) throws ClientException {
        logger.debug("Mapping exists - mapping name: '{} - {}'", typeDescriptor.getIndex(), typeDescriptor.getType());
        checkClient();
        Response isMappingExistsResponse = null;
        try {
            isMappingExistsResponse = esClientProvider.getClient().performRequest(
                    GET_ACTION,
                    getMappingPath(typeDescriptor),
                    Collections.<String, String> emptyMap());
            if (isMappingExistsResponse.getStatusLine().getStatusCode() == 200) {
                return true;
            } else if (isMappingExistsResponse.getStatusLine().getStatusCode() == 404) {
                return false;
            } else {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, isMappingExistsResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public void putMapping(TypeDescriptor typeDescriptor, JsonNode mapping) throws ClientException {
        logger.debug("Create mapping - object: '{}, index: {}, type: {}'", mapping, typeDescriptor.getIndex(), typeDescriptor.getType());
        checkClient();
        Response createMappingResponse = null;
        try {
            createMappingResponse = esClientProvider.getClient().performRequest(
                    PUT_ACTION,
                    getMappingPath(typeDescriptor),
                    Collections.<String, String> emptyMap(),
                    EntityBuilder.create().setText(MAPPER.writeValueAsString(mapping)).build(),
                    new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()));
            if (!isRequestSuccessful(createMappingResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, createMappingResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public void refreshAllIndexes() throws ClientException {
        logger.debug("Refresh all indexes");
        checkClient();
        Response refreshIndexResponse = null;
        try {
            refreshIndexResponse = esClientProvider.getClient().performRequest(
                    POST_ACTION,
                    getRefreshAllIndexesPath(),
                    Collections.<String, String> emptyMap());
            if (!isRequestSuccessful(refreshIndexResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, refreshIndexResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteAllIndexes() throws ClientException {
        logger.debug("Delete all indexes");
        checkClient();
        Response deleteIndexResponse = null;
        try {
            deleteIndexResponse = esClientProvider.getClient().performRequest(
                    DELETE_ACTION,
                    getIndexPath("_all"),
                    Collections.<String, String> emptyMap());
            if (!isRequestSuccessful(deleteIndexResponse)) {
                throw new ClientException(ClientErrorCodes.ACTION_ERROR, deleteIndexResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, e, e.getLocalizedMessage());
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

    private void handleResponseException(ResponseException re, String index, String action) throws ClientException {
        if (re.getResponse().getStatusLine().getStatusCode() == 404) {
            logger.warn("Resource for index '{}' not found on action '{}'! {}", index, action, re.getLocalizedMessage());
        } else if (re.getResponse().getStatusLine().getStatusCode() == 400) {
            logger.warn("Bad request for index '{}' on action '{}'! {}", index, action, re.getLocalizedMessage());
        } else {
            throw new ClientException(ClientErrorCodes.ACTION_ERROR, re, re.getLocalizedMessage());
        }
    }

    private boolean isRequestSuccessful(Response response) {
        return isRequestSuccessful(response.getStatusLine().getStatusCode());
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

    private String getBulkPath() {
        return String.format("/_bulk");
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
        if (esClientProvider.getClient() == null) {
            throw new ClientUndefinedException(CLIENT_UNDEFINED_MSG);
        }
    }

}
