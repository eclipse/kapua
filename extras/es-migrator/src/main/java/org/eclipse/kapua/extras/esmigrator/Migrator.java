/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.extras.esmigrator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSetting;
import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSettingKey;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
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

    private final String scheme;
    private final String address;
    private final int port;

    private static final String DEFAULT_ELASTICSEARCH_ADDRESS = "localhost";
    private static final String DEFAULT_ELASTICSEARCH_SCHEME = "http";
    private static final int DEFAULT_ELASTICSEARCH_PORT = 9200;

    private final String indexRefreshInterval;
    private final int indexShardNumber;
    private final int indexReplicaNumber;

    private static final String DEFAULT_INDEX_REFRESH_INTERVAL = "5s";
    private static final int DEFAULT_INDEX_SHARD_NUMBER = 1;
    private static final int DEFAULT_INDEX_REPLICA_NUMBER = 0;

    private static final String CLIENT = "client";
    private static final String CHANNEL = "channel";
    private static final String METRIC = "metric";
    private static final String FORMAT = "format";

    public Migrator() {
        EsMigratorSetting esMigratorSetting = EsMigratorSetting.getInstance();

        scheme = esMigratorSetting.getString(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_SCHEME, DEFAULT_ELASTICSEARCH_SCHEME);
        address = esMigratorSetting.getString(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_ADDRESS, DEFAULT_ELASTICSEARCH_ADDRESS);
        port = esMigratorSetting.getInt(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_PORT, DEFAULT_ELASTICSEARCH_PORT);

        indexRefreshInterval = esMigratorSetting.getString(EsMigratorSettingKey.DATASTORE_INDEX_REFRESH_INTERVAL, DEFAULT_INDEX_REFRESH_INTERVAL);
        indexShardNumber = esMigratorSetting.getInt(EsMigratorSettingKey.DATASTORE_INDEX_NUMBER_OF_SHARDS, DEFAULT_INDEX_SHARD_NUMBER);
        indexReplicaNumber = esMigratorSetting.getInt(EsMigratorSettingKey.DATASTORE_INDEX_NUMBER_OF_REPLICAS, DEFAULT_INDEX_REPLICA_NUMBER);

        LOGGER.info("Elasticsearch Cluster Address: {}://{}:{}", scheme, address, port);
    }

    public void doMigrate() {
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(address, port, scheme));

        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {
            GetIndexRequest request = new GetIndexRequest("*");
            GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
            List<String> indices = Arrays.stream(response.getIndices())
                    .filter(index -> Stream.of("-channel", "-client", "-metric").noneMatch(index::endsWith))
                    .collect(Collectors.toList());

            LOGGER.debug("Found {} indexes", indices.size());

            for (String index : indices) {
                if (index.startsWith(".")) {
                    migrateIndex(index, client);
                } else {
                    updateMappingDates(index, client);
                }
            }
        } catch (IOException ioException) {
            LOGGER.error("Error migrating Elasticsearch indices: {}", ioException.getMessage());
        }
    }

    @SuppressWarnings({ "unchecked" })
    private void updateMappingDates(String index, RestHighLevelClient client) throws IOException {
        LOGGER.info("Updating dates mapping for index {}", index);

        String tmpIndex = index + "_tmp";

        GetMappingsRequest getMappingsRequest = new GetMappingsRequest().indices(index);
        GetMappingsResponse getMappingsResponse = client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);

        MappingMetaData mappingMetaData = getMappingsResponse.mappings().get(index);
        Map<String, Object> sourceMap = mappingMetaData.getSourceAsMap();
        Map<String, Object> properties = (Map<String, Object>) sourceMap.get("properties");
        Map<String, Object> capturedOn = (Map<String, Object>) properties.get("captured_on");
        Map<String, Object> receivedOn = (Map<String, Object>) properties.get("received_on");
        Map<String, Object> sentOn = (Map<String, Object>) properties.get("sent_on");
        Map<String, Object> timestamp = (Map<String, Object>) properties.get("timestamp");
        Map<String, Object> position = (Map<String, Object>) properties.get("position");
        Map<String, Object> positionProperties = (Map<String, Object>) position.get("properties");
        Map<String, Object> positionTimestamp = (Map<String, Object>) positionProperties.get("timestamp");
        capturedOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        receivedOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        sentOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        timestamp.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        positionTimestamp.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);

        Settings settings = Settings.builder()
                .put("index.number_of_shards", indexShardNumber)
                .put("index.number_of_replicas", indexReplicaNumber)
                .put("index.refresh_interval", indexRefreshInterval)
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
                .setDestDocType("_doc");

        LOGGER.debug("Reindexing {} to {}", index, tmpIndex);

        client.reindex(firstReindexRequest, RequestOptions.DEFAULT);

        DeleteIndexRequest firstDeleteIndexRequest = new DeleteIndexRequest(index);

        LOGGER.debug("Deleting index {}", index);

        client.indices().delete(firstDeleteIndexRequest, RequestOptions.DEFAULT);

        CreateIndexRequest secondCreateIndexRequest = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(sourceMap);

        LOGGER.debug("Creating new index {}, same mappings as {}", index, tmpIndex);

        client.indices().create(secondCreateIndexRequest, RequestOptions.DEFAULT);

        ReindexRequest secondReindexRequest = new ReindexRequest()
                .setSourceIndices(tmpIndex)
                .setSourceDocTypes("_doc")
                .setDestIndex(index)
                .setDestDocType("_doc");

        LOGGER.debug("Reindexing {} to {}", tmpIndex, index);

        client.reindex(secondReindexRequest, RequestOptions.DEFAULT);

        DeleteIndexRequest secondDeleteIndexRequest = new DeleteIndexRequest(tmpIndex);

        LOGGER.debug("Deleting index {}", tmpIndex);

        client.indices().delete(secondDeleteIndexRequest, RequestOptions.DEFAULT);
    }

    private void migrateIndex(String index, RestHighLevelClient client) throws IOException {
        LOGGER.debug("Migrating index {}", index);
        createIndex(index, CHANNEL, client);
        createIndex(index, CLIENT, client);
        createIndex(index, METRIC, client);

        reindex(index, CHANNEL, client);
        reindex(index, CLIENT, client);
        reindex(index, METRIC, client);

        closeIndex(index, client);
    }

    private void createIndex(String index, String indexType, RestHighLevelClient client) throws IOException {
        String mapping;
        switch (indexType) {
            case CHANNEL:
                mapping = IOUtils.toString(ResourceUtils.getResource("mappings/channel.json"));
                break;
            case CLIENT:
                mapping = IOUtils.toString(ResourceUtils.getResource("mappings/client.json"));
                break;
            case METRIC:
                mapping = IOUtils.toString(ResourceUtils.getResource("mappings/metric.json"));
                break;
            default:
                throw new IllegalArgumentException("Unknown Mapping Type");
        }

        String indexName = index + "-" + indexType;
        Settings settings = Settings.builder()
                .put("index.number_of_shards", indexShardNumber)
                .put("index.number_of_replicas", indexReplicaNumber)
                .put("index.refresh_interval", indexRefreshInterval)
                .build();
        CreateIndexRequest request = new CreateIndexRequest(indexName)
                .settings(settings)
                .mapping(mapping, XContentType.JSON);

        LOGGER.info("Creating index for '{}' - refresh: '{}' - shards: '{}' replicas: '{}': ", indexName, indexRefreshInterval, indexShardNumber, indexReplicaNumber);
        LOGGER.debug("Creating index, body: {}", mapping);

        client.indices().create(request, RequestOptions.DEFAULT);
        LOGGER.debug("Index {} created", indexName);
    }

    private void reindex(String index, String indexType, RestHighLevelClient client) throws IOException {
        ReindexRequest request = new ReindexRequest()
                .setSourceIndices(index)
                .setSourceDocTypes(indexType)
                .setScript(new Script(ScriptType.INLINE, "painless", "ctx._id = ctx._id.replace('/', '_').replace('+','-').replace('=', '')", Collections.emptyMap()))
                .setDestIndex(index + "-" + indexType)
                .setDestDocType("_doc");
        client.reindex(request, RequestOptions.DEFAULT);

        LOGGER.debug("Reindex complete for index {}", index);
    }

    private void closeIndex(String index, RestHighLevelClient client) throws IOException {
        CloseIndexRequest request = new CloseIndexRequest(index);
        client.indices().close(request, RequestOptions.DEFAULT);
    }

}
