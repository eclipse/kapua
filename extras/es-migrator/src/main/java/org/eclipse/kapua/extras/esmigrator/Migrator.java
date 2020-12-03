/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.esmigrator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSetting;
import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSettingKey;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Migrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Migrator.class.getName());

    private static final String DEFAULT_ELASTICSEARCH_ADDRESS = "localhost";
    private static final String DEFAULT_ELASTICSEARCH_SCHEME = "http";
    private static final int DEFAULT_ELASTICSEARCH_PORT = 9200;

    private static final String DEFAULT_INDEX_REFRESH_INTERVAL = "5s";
    private static final int DEFAULT_INDEX_SHARD_NUMBER = 1;
    private static final int DEFAULT_INDEX_REPLICA_NUMBER = 0;

    private static final String FORMAT = "format";
    private static final String DOC = "_doc";

    private static final String INDEX_NUMBER_OF_SHARDS = "index.number_of_shards";
    private static final String INDEX_NUMBER_OF_REPLICAS = "index.number_of_replicas";
    private static final String INDEX_REFRESH_INTERVAL = "index.refresh_interval";

    private static final String REINDEXING = "Reindexing {} to {}";
    private static final String DELETING_INDEX = "Deleting index {}";

    private final String indexRefreshInterval;
    private final int indexShardNumber;
    private final int indexReplicaNumber;
    private final List<String> indexPrefixes;

    private final ObjectMapper mapper = new ObjectMapper();

    private final RestClientBuilder restClientBuilder;

    public Migrator() {
        EsMigratorSetting esMigratorSetting = EsMigratorSetting.getInstance();

        String scheme = esMigratorSetting.getString(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_SCHEME, DEFAULT_ELASTICSEARCH_SCHEME);
        String address = esMigratorSetting.getString(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_ADDRESS, DEFAULT_ELASTICSEARCH_ADDRESS);
        int port = esMigratorSetting.getInt(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_PORT, DEFAULT_ELASTICSEARCH_PORT);

        indexRefreshInterval = esMigratorSetting.getString(EsMigratorSettingKey.DATASTORE_INDEX_REFRESH_INTERVAL, DEFAULT_INDEX_REFRESH_INTERVAL);
        indexShardNumber = esMigratorSetting.getInt(EsMigratorSettingKey.DATASTORE_INDEX_NUMBER_OF_SHARDS, DEFAULT_INDEX_SHARD_NUMBER);
        indexReplicaNumber = esMigratorSetting.getInt(EsMigratorSettingKey.DATASTORE_INDEX_NUMBER_OF_REPLICAS, DEFAULT_INDEX_REPLICA_NUMBER);
        indexPrefixes = esMigratorSetting.getList(String.class, EsMigratorSettingKey.DATASTORE_INDEX_PREFIX);

        LOGGER.info("Elasticsearch Cluster Address: {}://{}:{}", scheme, address, port);

        restClientBuilder = RestClient.builder(new HttpHost(address, port, scheme));
    }

    void doMigrate() {
        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {
            Request catIndicesRequest = new Request(HttpGet.METHOD_NAME, "_cat/indices?format=json");
            Response catIndicesResponse = client.getLowLevelClient().performRequest(catIndicesRequest);
            JsonNode[] catIndicesObject = mapper.readValue(EntityUtils.toString(catIndicesResponse.getEntity()), JsonNode[].class);
            List<String> openIndexes = Stream.of(catIndicesObject)
                    .filter(index -> index.get("status").asText().equals("open"))
                    .map(index -> index.get("index").asText())
                    .filter(index -> Stream.of("-channel", "-client", "-metric").noneMatch(index::endsWith))
                    .collect(Collectors.toList());
            LOGGER.debug("Found {} open indexes to be migrated", openIndexes.size());
            for (String index : openIndexes) {
                String indexWithoutPrefix = index;
                Optional<String> indexPrefix = indexPrefixes.stream().filter(prefix -> index.startsWith(prefix + "-")).findFirst();
                if (indexPrefix.isPresent()) {
                    LOGGER.debug("The index {} matches one of the prefixes: {}", index, indexPrefix.get());
                    // Trim out the optional index prefix to make sure the evaluation is done on the correct name
                    indexWithoutPrefix = index.replace(indexPrefix.get() + "-", "");
                }
                if (indexWithoutPrefix.startsWith(".")) {
                    migrateRegistryIndex(index, client);
                } else {
                    updateMappingDates(index, client);
                }
            }
        } catch (IOException ioException) {
            LOGGER.error("Error migrating Elasticsearch indexes: {}", ioException.getMessage());
        }
    }

    @SuppressWarnings({ "unchecked" })
    private void updateMappingDates(String index, RestHighLevelClient client) throws IOException {
        LOGGER.info("Updating dates mapping for index {}", index);

        String tmpIndex = index + "_tmp";

        // First look for an existing temp index left from a previous execution, and delete if present
        GetIndexRequest getTempIndexRequest = new GetIndexRequest(tmpIndex);
        boolean tmpIndexExists = client.indices().exists(getTempIndexRequest, RequestOptions.DEFAULT);
        if (tmpIndexExists) {
            LOGGER.info("temp index already found: {}, deleting it", tmpIndex);
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(tmpIndex);
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        }

        GetMappingsRequest getMappingsRequest = new GetMappingsRequest().indices(index);
        GetMappingsResponse getMappingsResponse = client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);

        MappingMetaData mappingMetaData = getMappingsResponse.mappings().get(index);
        Map<String, Object> sourceMap = mappingMetaData.getSourceAsMap();
        sourceMap.remove("_all");
        Map<String, Object> properties = (Map<String, Object>) sourceMap.get(SchemaKeys.FIELD_NAME_PROPERTIES);
        Map<String, Object> capturedOn = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_CAPTURED_ON);
        Map<String, Object> receivedOn = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_RECEIVED_ON);
        Map<String, Object> sentOn = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_SENT_ON);
        Map<String, Object> timestamp = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_TIMESTAMP);
        Map<String, Object> position = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_POSITION);
        Map<String, Object> positionProperties = (Map<String, Object>) position.get(SchemaKeys.FIELD_NAME_PROPERTIES);
        Map<String, Object> positionTimestamp = (Map<String, Object>) positionProperties.get(MessageSchema.MESSAGE_POS_TIMESTAMP);
        capturedOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        receivedOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        sentOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        timestamp.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        positionTimestamp.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);

        Settings settings = Settings.builder()
                .put(INDEX_NUMBER_OF_SHARDS, indexShardNumber)
                .put(INDEX_NUMBER_OF_REPLICAS, indexReplicaNumber)
                .put(INDEX_REFRESH_INTERVAL, indexRefreshInterval)
                .build();

        CreateIndexRequest firstCreateIndexRequest = new CreateIndexRequest(tmpIndex)
                .settings(settings)
                .mapping(sourceMap);

        String firstCreateIndexRequestBody = firstCreateIndexRequest.mappings().utf8ToString();
        LOGGER.debug("Creating temporary index {}, body {}", index, firstCreateIndexRequestBody);

        client.indices().create(firstCreateIndexRequest, RequestOptions.DEFAULT);

        ReindexRequest firstReindexRequest = new ReindexRequest()
                .setSourceIndices(index)
                .setSourceDocTypes("message")
                .setDestIndex(tmpIndex)
                .setDestDocType(DOC)
                .setRefresh(true);

        CountRequest countRequest = new CountRequest(index);
        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long originalDocsCount = countResponse.getCount();

        LOGGER.info("Found {} documents on {} before reindexing", originalDocsCount, index);
        LOGGER.debug(REINDEXING, index, tmpIndex);

        client.reindex(firstReindexRequest, RequestOptions.DEFAULT);
        LOGGER.info("Reindex complete: {}", tmpIndex);

        DeleteIndexRequest firstDeleteIndexRequest = new DeleteIndexRequest(index);

        LOGGER.debug(DELETING_INDEX, index);

        client.indices().delete(firstDeleteIndexRequest, RequestOptions.DEFAULT);

        CreateIndexRequest secondCreateIndexRequest = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(sourceMap);

        LOGGER.debug("Creating new index {}, same mappings as {}", index, tmpIndex);

        client.indices().create(secondCreateIndexRequest, RequestOptions.DEFAULT);

        ReindexRequest secondReindexRequest = new ReindexRequest()
                .setSourceIndices(tmpIndex)
                .setSourceDocTypes(DOC)
                .setDestIndex(index)
                .setDestDocType(DOC)
                .setRefresh(true);

        LOGGER.debug(REINDEXING, tmpIndex, index);

        client.reindex(secondReindexRequest, RequestOptions.DEFAULT);
        LOGGER.info("Reindex complete: {}", tmpIndex);

        countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long newDocsCount = countResponse.getCount();
        LOGGER.info("Found {} documents on {} after reindexing", newDocsCount, index);

        DeleteIndexRequest secondDeleteIndexRequest = new DeleteIndexRequest(tmpIndex);

        LOGGER.debug(DELETING_INDEX, tmpIndex);

        client.indices().delete(secondDeleteIndexRequest, RequestOptions.DEFAULT);
    }

    private void migrateRegistryIndex(String index, RestHighLevelClient client) throws IOException {
        LOGGER.debug("Migrating index {}", index);
        createIndex(index, MappingType.CHANNEL, client);
        createIndex(index, MappingType.CLIENT, client);
        createIndex(index, MappingType.METRIC, client);

        reindex(index, MappingType.CHANNEL, client);
        reindex(index, MappingType.CLIENT, client);
        reindex(index, MappingType.METRIC, client);

        closeIndex(index, client);
    }

    private void createIndex(String index, MappingType mappingType, RestHighLevelClient client) throws IOException {
        String mapping = mappingType.getMapping();

        String indexName = index + "-" + mappingType;
        Settings settings = Settings.builder()
                .put(INDEX_NUMBER_OF_SHARDS, indexShardNumber)
                .put(INDEX_NUMBER_OF_REPLICAS, indexReplicaNumber)
                .put(INDEX_REFRESH_INTERVAL, indexRefreshInterval)
                .build();
        CreateIndexRequest request = new CreateIndexRequest(indexName)
                .settings(settings)
                .mapping(mapping, XContentType.JSON);

        LOGGER.info("Creating index for '{}' - refresh: '{}' - shards: '{}' replicas: '{}': ", indexName, indexRefreshInterval, indexShardNumber, indexReplicaNumber);
        LOGGER.debug("Creating index {}, body: {}", indexName, mapping);

        client.indices().create(request, RequestOptions.DEFAULT);
        LOGGER.debug("Index {} created", indexName);
    }

    private void reindex(String index, MappingType mappingType, RestHighLevelClient client) throws IOException {
        String mappingTypeName = mappingType.name().toLowerCase();
        ReindexRequest request = new ReindexRequest()
                .setSourceIndices(index)
                .setSourceDocTypes(mappingTypeName)
                .setScript(new Script(ScriptType.INLINE, "painless", "ctx._id = ctx._id.replace('/', '_').replace('+','-').replace('=', '')", Collections.emptyMap()))
                .setDestIndex(index + "-" + mappingTypeName)
                .setDestDocType(DOC)
                .setRefresh(true);
        client.reindex(request, RequestOptions.DEFAULT);
        LOGGER.debug("Reindex complete for index {}", index);
    }

    private void closeIndex(String index, RestHighLevelClient client) throws IOException {
        CloseIndexRequest request = new CloseIndexRequest(index);
        client.indices().close(request, RequestOptions.DEFAULT);
    }

}
