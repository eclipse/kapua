/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.esmigrator.migrations;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.eclipse.kapua.extras.esmigrator.EsClientWrapper;
import org.eclipse.kapua.extras.esmigrator.EsClusterDescriptor;
import org.eclipse.kapua.extras.esmigrator.IndexType;
import org.eclipse.kapua.extras.esmigrator.MappingType;
import org.eclipse.kapua.extras.esmigrator.MigratorUtils;
import org.eclipse.kapua.extras.esmigrator.ReindexTaskResult;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Es6Migration implements EsMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Es6Migration.class.getName());

    private static final String MAPPINGS = "mappings";
    private static final String FORMAT = "format";
    private static final String DOC = "_doc";
    private static final String MESSAGE = "message";
    private static final String LOGS = "logs";
    private static final String INTROSPECTION_LOG_MESSAGE = "Index {} identified as {} after introspection";

    private final EsClientWrapper client;
    private final EsClusterDescriptor esClusterDescriptor;
    private final Map<String, String> migrationReport;

    public Es6Migration(EsClientWrapper client, EsClusterDescriptor esClusterDescriptor, Map<String, String> migrationReport) {
        this.client = client;
        this.esClusterDescriptor = esClusterDescriptor;
        this.migrationReport = migrationReport;
    }

    @Override
    public void migrateAccountIndices(String accountId) throws IOException, ElasticsearchException {
        String indexesPrefix = esClusterDescriptor.getIndicesPrefix();
        LOGGER.debug("Migrating indices for account id {}", accountId);
        // Check and Migrate Data Registry Index
        String dataRegistryIndexName = (StringUtils.isNotBlank(indexesPrefix) ? indexesPrefix + "-" : "") + "." + accountId;
        if (client.indicesExists(dataRegistryIndexName)) {
            JsonNode dataRegistryIndexNode = client.getIndices(dataRegistryIndexName).get(dataRegistryIndexName);
            if (dataRegistryIndexNode != null) {
                LOGGER.debug("Found data registry index {}, start migration", dataRegistryIndexName);
                migrateIndex(dataRegistryIndexName, dataRegistryIndexNode.get(MAPPINGS));
            }
        }
        // Find and Migrate Data Indices
        String indexesQuery = (StringUtils.isNotBlank(indexesPrefix) ? indexesPrefix + "-" : "") + accountId + "-*";
        JsonNode dataIndicesNode = client.getIndices(indexesQuery);
        Iterator<Entry<String, JsonNode>> dataIndicesNodeIterator = dataIndicesNode.fields();
        while (dataIndicesNodeIterator.hasNext()) {
            Entry<String, JsonNode> dataIndexEntry = dataIndicesNodeIterator.next();
            String indexName = dataIndexEntry.getKey();
            if (client.isIndexOpen(indexName)) {
                LOGGER.debug("Found open data index {}, start migration", indexName);
                migrateIndex(indexName, dataIndexEntry.getValue().get(MAPPINGS));
            }
        }
    }

    private void migrateIndex(String index, JsonNode mappings) {
        LOGGER.info("Starting migration for index {}", index);
        if (mappings.size() == 3 && mappings.has("metric") && mappings.has("channel") && mappings.has("client")) {
            LOGGER.debug(INTROSPECTION_LOG_MESSAGE, index, IndexType.DATA_REGISTRY);
            migrateRegistryIndex(index, IndexType.DATA_REGISTRY);
        } else if (mappings.size() == 1 && (mappings.has(MESSAGE))) {
            Iterator<Entry<String, JsonNode>> mappingsIterator = mappings.fields();
            Entry<String, JsonNode> mappingEntry = mappingsIterator.next();
            String typeName = mappingEntry.getKey();
            MappingMetaData mappingMetaData;
            try {
                mappingMetaData = new MappingMetaData(typeName, MigratorUtils.getObjectMapper().convertValue(mappingEntry.getValue(), new TypeReference<Map<String, Object>>() {

                }));
                LOGGER.debug(INTROSPECTION_LOG_MESSAGE, index, IndexType.DATA_MESSAGE);
            } catch (IOException ioException) {
                String message = String.format("Unable to convert mapping for type %s, index %s", typeName, index);
                migrationReport.put(index, message);
                LOGGER.warn(message, ioException);
                return;
            }
            updateMappingDates(index, mappingMetaData);
        } else if (mappings.size() == 1 && mappings.has(DOC)) {
            String message = String.format("Index %s only contains a \"_doc\" type mapping, so it should be already migrated. Skipping migration", index);
            LOGGER.warn(message);
            migrationReport.put(index, message);
        } else {
            StringJoiner mappingTypesJoiner = new StringJoiner(", ");
            Iterator<String> mappingTypesIterator = mappings.fieldNames();
            while (mappingTypesIterator.hasNext()) {
                mappingTypesJoiner.add(mappingTypesIterator.next());
            }
            String mappingTypes = mappingTypesJoiner.toString();
            String message = String.format("Unknown mapping types for index %s: %s. Skipping...", index, mappingTypes);
            LOGGER.warn(message);
            migrationReport.put(index, message);
        }
        LOGGER.info("Migration for index {} complete", index);
    }

    private void updateMappingDates(String index, MappingMetaData mappingMetaData) {
        LOGGER.debug("Updating dates mapping for index {}", index);
        String tmpIndex = index + "_tmp";

        // First look for an existing temp index left from a previous execution, and delete if present
        boolean tmpIndexExists;
        try {
            tmpIndexExists = client.indicesExists(tmpIndex);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(index, String.format("Error detecting tmp index %s: %s", index, message));
            return;
        }
        if (tmpIndexExists) {
            LOGGER.debug("temp index already found: {}, deleting it", tmpIndex);
            try {
                client.deleteIndex(tmpIndex);
            } catch (IOException | ElasticsearchException exception) {
                String message = MigratorUtils.getExceptionMessageOrName(exception);
                migrationReport.put(index, String.format("Error deleting tmp index %s: %s", index, message));
                return;
            }
        }

        Map<String, Object> sourceMap = getUpdatedMessageMapping(mappingMetaData);

        try {
            client.createIndex(tmpIndex, sourceMap);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(index, String.format("Error creating tmp index %s: %s", tmpIndex, message));
            return;
        }

        long originalDocsCount;
        try {
            originalDocsCount = client.count(index);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(index, String.format("Error counting documents on index %s: %s", index, message));
            return;
        }

        LOGGER.debug("Found {} documents on {} before reindexing", originalDocsCount, index);

        ReindexTaskResult firstReindexTaskResult = client.reindexWithTask(index, mappingMetaData.type(), tmpIndex, DOC);
        if (!firstReindexTaskResult.isSuccessful()) {
            migrationReport.put(index, firstReindexTaskResult.getMessage());
            return;
        }

        long tempDocsCount;

        try {
            tempDocsCount = client.count(tmpIndex);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(tmpIndex, String.format("Error counting documents on temp index %s, not safe to continue: %s", tmpIndex, message));
            return;
        }

        LOGGER.debug("Found {} documents on {} after first reindex", originalDocsCount, tmpIndex);

        if (tempDocsCount != originalDocsCount) {
            String message = String.format("Documents count different after reindexing %s on %s: before - %d, after - %d, not safe to continue", index, tmpIndex, originalDocsCount, tempDocsCount);
            LOGGER.warn(message);
            migrationReport.put(index, message);
            return;
        }

        try {
            client.deleteIndex(index);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(index, String.format("Error deleting original index %s, duplicate indexes may be present, stopping migration to avoid conflicts: %s", index, message));
            return;
        }

        LOGGER.debug("Creating new index {}, same mappings as {}", index, tmpIndex);

        try {
            client.createIndex(index, sourceMap);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(index, String.format("Error creating index %s before second reindex: %s", index, message));
            return;
        }

        ReindexTaskResult secondReindexTaskResult = client.reindexWithTask(tmpIndex, DOC, index, DOC);
        if (!secondReindexTaskResult.isSuccessful()) {
            migrationReport.put(index, secondReindexTaskResult.getMessage());
            return;
        }

        long newDocsCount;
        try {
            newDocsCount = client.count(index);
        } catch (IOException ioException) {
            String message = MigratorUtils.getExceptionMessageOrName(ioException);
            migrationReport.put(index, String.format("Error counting documents on index %s after second reindex: %s", index, message));
            return;
        }

        LOGGER.debug("Found {} documents on {} after second reindex", newDocsCount, index);

        if (tempDocsCount != newDocsCount) {
            String message = String.format("Documents count different after reindexing %s on %s: before - %d, after - %d. Final action won't be performed",
                                           tmpIndex,
                                           index,
                                           tempDocsCount,
                                           newDocsCount);
            LOGGER.warn(message);
            migrationReport.put(index, message);
            return;
        } else {
            try {
                client.deleteIndex(tmpIndex);
            } catch (IOException | ElasticsearchException exception) {
                LOGGER.warn("Temporary index {} was to be deleted, but the deletion failed: {}", index, MigratorUtils.getExceptionMessageOrName(exception));
            }
        }

        migrationReport.put(index, "OK");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUpdatedMessageMapping(MappingMetaData mappingMetaData) {
        Map<String, Object> sourceMap = mappingMetaData.getSourceAsMap();
        sourceMap.remove("_all");
        Map<String, Object> properties = (Map<String, Object>) sourceMap.get(SchemaKeys.FIELD_NAME_PROPERTIES);
        Map<String, Object> capturedOn = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_CAPTURED_ON);
        Map<String, Object> receivedOn = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_RECEIVED_ON);
        Map<String, Object> sentOn = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_SENT_ON);
        Map<String, Object> timestamp = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_TIMESTAMP);
        Map<String, Object> position = (Map<String, Object>) properties.get(MessageSchema.MESSAGE_POSITION);
        if (position != null) {
            Map<String, Object> positionProperties = (Map<String, Object>) position.get(SchemaKeys.FIELD_NAME_PROPERTIES);
            Map<String, Object> positionTimestamp = (Map<String, Object>) positionProperties.get(MessageSchema.MESSAGE_POS_TIMESTAMP);
            if (positionTimestamp != null) {
                positionTimestamp.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
            }
        }
        capturedOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        receivedOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        sentOn.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        timestamp.put(FORMAT, DatastoreUtils.DATASTORE_DATE_FORMAT);
        return sourceMap;
    }

    private void migrateRegistryIndex(String index, IndexType indexType) {
        LOGGER.debug("Migrating index {}, index type: {}", index, indexType);
        try {
            String channelRegistryIndexName = indexType.deriveNewRegistryIndexName(index, MappingType.CHANNEL);
            String clientRegistryIndexName = indexType.deriveNewRegistryIndexName(index, MappingType.CLIENT);
            String metricRegistryIndexName = indexType.deriveNewRegistryIndexName(index, MappingType.METRIC);
            if (client.indicesExists(channelRegistryIndexName, clientRegistryIndexName, metricRegistryIndexName)) {
                String message = String.format("Already migrated indexes found for index %s. Skipping migration", index);
                LOGGER.warn(message);
                migrationReport.put(index, message);
                return;
            }
        } catch (IOException | ElasticsearchException exception) {
            String message = String.format("Unable to check already migrated indices for %s: %s", index, MigratorUtils.getExceptionMessageOrName(exception));
            LOGGER.warn(message);
            migrationReport.put(index, message);
            return;
        }
        try {
            createRegistryIndex(index, MappingType.CHANNEL);
            createRegistryIndex(index, MappingType.CLIENT);
            createRegistryIndex(index, MappingType.METRIC);

            registryReindex(index, MappingType.CHANNEL);
            registryReindex(index, MappingType.CLIENT);
            registryReindex(index, MappingType.METRIC);
        } catch (IOException | ElasticsearchException exception) {
            String message = MigratorUtils.getExceptionMessageOrName(exception);
            migrationReport.put(index, String.format("Error creating registry index for %s: %s", index, message));
            try {
                client.deleteIndex(indexType.deriveNewRegistryIndexName(index, MappingType.CHANNEL));
                client.deleteIndex(indexType.deriveNewRegistryIndexName(index, MappingType.CLIENT));
                client.deleteIndex(indexType.deriveNewRegistryIndexName(index, MappingType.METRIC));
            } catch (IOException | ElasticsearchException otherException) {
                String otherMessage = MigratorUtils.getExceptionMessageOrName(otherException);
                LOGGER.warn("Unable to delete new registry index: {}", otherMessage);
            }
            return;
        }
        migrationReport.put(index, "OK");
        switch (esClusterDescriptor.getAction()) {
            case CLOSE:
                try {
                    LOGGER.debug("Closing registry index {}", index);
                    client.closeIndex(index);
                } catch (IOException | ElasticsearchException exception) {
                    String message = MigratorUtils.getExceptionMessageOrName(exception);
                    LOGGER.warn("Error closing index {}: {}", index, message);
                }
                break;
            case DELETE:
                try {
                    LOGGER.debug("Deleting registry index {}", index);
                    client.deleteIndex(index);
                } catch (IOException | ElasticsearchException exception) {
                    String message = MigratorUtils.getExceptionMessageOrName(exception);
                    LOGGER.warn("Error deleting index {}: {}", index, message);
                }
                break;
            case NONE:
            default:
                LOGGER.debug("No action on index {} after completing migration on registry index", index);
                break;
        }
    }

    private void createRegistryIndex(String index, MappingType mappingType) throws IOException, ElasticsearchException {
        String mapping = mappingType.getMapping();
        String indexName = mappingType.getIndexType().deriveNewRegistryIndexName(index, mappingType);
        LOGGER.debug("Creating Index {}, type {}, mapping type {}", indexName, mappingType.getIndexType(), mappingType);
        client.createIndex(indexName, mapping);
        LOGGER.debug("Index {} created", indexName);
    }

    private void registryReindex(String index, MappingType mappingType) throws IOException, ElasticsearchException {
        String destIndex = mappingType.getIndexType().deriveNewRegistryIndexName(index, mappingType);
        String sourceDocType = mappingType.getMappingName();

        Script reindexScript = new Script(ScriptType.INLINE, "painless", "ctx._id = ctx._id.replace('/', '_').replace('+','-').replace('=', '')", Collections.emptyMap());
        client.submitReindexTask(index, sourceDocType, destIndex, DOC, reindexScript);
        LOGGER.debug("Reindex complete for index {}", destIndex);
    }

}
