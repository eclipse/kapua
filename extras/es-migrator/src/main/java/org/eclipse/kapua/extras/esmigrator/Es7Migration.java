/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Es7Migration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Es7Migration.class.getName());

    private static final String DOC = "_doc";

    private final EsClientWrapper client;
    private final EsClusterDescriptor esClusterDescriptor;
    private final Map<String, String> migrationReport;

    private static final String CHANNEL_SUFFIX = "-channel";
    private static final String CLIENT_SUFFIX = "-client";
    private static final String METRIC_SUFFIX = "-metric";

    public Es7Migration(EsClientWrapper client, EsClusterDescriptor esClusterDescriptor, Map<String, String> migrationReport) {
        this.client = client;
        this.esClusterDescriptor = esClusterDescriptor;
        this.migrationReport = migrationReport;
    }

    public void migrateAccountIndices(String accountId) throws IOException, ElasticsearchException {
        String indicesPrefix = esClusterDescriptor.getIndicesPrefix();
        LOGGER.debug("Migrating indices for account id {}", accountId);
        Map<String, String> indicesToProcess = populateMessageIndices(accountId, indicesPrefix);

        indicesToProcess.entrySet().forEach((index -> {
            String srcIndex = index.getKey();
            String destIndex = index.getValue();
            try {
                if (client.indicesExists(index.getKey())) {
                    LOGGER.debug("Found index {}, renaming to {}", srcIndex, destIndex);
                    Map<String, Object> mappingMetaData = client.getMappings(srcIndex).mappings().get(srcIndex).getSourceAsMap();
                    client.createIndex(destIndex, mappingMetaData);
                    ReindexTaskResult reindexTaskResult = client.reindexWithTask(srcIndex, DOC, destIndex, DOC);
                    if (!reindexTaskResult.isSuccessful()) {
                        migrationReport.put(srcIndex, String.format("Error while renaming Index %s to %s: %s", srcIndex, destIndex, reindexTaskResult.getMessage()));
                        return;
                    }
                    LOGGER.debug("Reindex complete. Deleting old index");
                    switch(esClusterDescriptor.getAction()) {
                        case DELETE:
                            try {
                                client.deleteIndex(srcIndex);
                                LOGGER.debug("Old index {} deleted", srcIndex);
                                migrationReport.put(srcIndex, String.format("Index %s successfully renamed to %s", srcIndex, destIndex));
                            } catch (IOException | ElasticsearchException exception) {
                                String message = MigratorUtils.getExceptionMessageOrName(exception);
                                LOGGER.warn("Unable to delete old index {}: {}", srcIndex, message);
                                migrationReport.put(srcIndex, String.format("Index %s successfully renamed to %s, but old index could not be deleted", srcIndex, destIndex));
                            }
                            break;
                        case CLOSE:
                            try {
                                client.closeIndex(srcIndex);
                                LOGGER.debug("Old index {} closed", srcIndex);
                                migrationReport.put(srcIndex, String.format("Index %s successfully renamed to %s", srcIndex, destIndex));
                            } catch (IOException | ElasticsearchException exception) {
                                String message = MigratorUtils.getExceptionMessageOrName(exception);
                                LOGGER.warn("Unable to close old index {}: {}", srcIndex, message);
                                migrationReport.put(srcIndex, String.format("Index %s successfully renamed to %s, but old index could not be closed", srcIndex, destIndex));
                            }
                            break;
                        default:
                            LOGGER.debug("No action on index {} after completing migration", index);
                            break;
                    }
                } else {
                    migrationReport.put(srcIndex, String.format("Index %s not existing. Skipping...", srcIndex));
                    LOGGER.warn("Index {} not existing. Skipping...", srcIndex);
                }
            } catch (IOException | ElasticsearchException exception) {
                String message = MigratorUtils.getExceptionMessageOrName(exception);
                LOGGER.error("Error migrating index {}: {}", srcIndex, message);
                migrationReport.put(srcIndex, String.format("Error while renaming Index %s to %s: %s", srcIndex, destIndex, message));
            }
        }));
    }

    private Map<String, String> populateMessageIndices(String accountId, String indicesPrefix) throws IOException, ElasticsearchException {
        Map<String, String> indicesToProcess = new HashMap<>();
        String oldDataIndexRadix = (StringUtils.isNotBlank(indicesPrefix) ? indicesPrefix + "-" : "") + accountId;
        JsonNode indices = client.getIndices(oldDataIndexRadix + "-*");
        Iterator<String> indicesIterator = indices.fieldNames();
        while (indicesIterator.hasNext()) {
            String indexName = indicesIterator.next();
            if (isOldMessageIndexNaming(accountId, indexName, indicesPrefix)) {
                indicesToProcess.put(indexName, getNewDataMessageIndexName(indexName, oldDataIndexRadix));
            }
        }
        String oldRegistryIndexRadix = (StringUtils.isNotBlank(indicesPrefix) ? indicesPrefix + "-" : "") + "." + accountId;
        String newRegistryIndexRadix = (StringUtils.isNotBlank(indicesPrefix) ? indicesPrefix + "-" : "") + accountId + "-data";
        indicesToProcess.put(oldRegistryIndexRadix + CHANNEL_SUFFIX, getNewDataRegistryIndexName(newRegistryIndexRadix, CHANNEL_SUFFIX));
        indicesToProcess.put(oldRegistryIndexRadix + CLIENT_SUFFIX, getNewDataRegistryIndexName(newRegistryIndexRadix, CLIENT_SUFFIX));
        indicesToProcess.put(oldRegistryIndexRadix + METRIC_SUFFIX, getNewDataRegistryIndexName(newRegistryIndexRadix, METRIC_SUFFIX));
        return indicesToProcess;
    }

    boolean isOldMessageIndexNaming(String accountId, String indexName, String prefix) {
        StringBuilder regexBuilder = new StringBuilder("^");
        if (StringUtils.isNotBlank(prefix)) {
            regexBuilder.append(prefix).append("-");
        }
        regexBuilder.append(accountId).append("-\\d{4}-\\d{2}(-\\d{2}){0,2}");
        Pattern pattern = Pattern.compile(regexBuilder.toString());
        return pattern.matcher(indexName).matches();
    }

    String getNewDataMessageIndexName(String indexName, String oldDataIndexRadix) {
        return indexName.replaceFirst(oldDataIndexRadix, oldDataIndexRadix + "-data-message");
    }

    String getNewDataRegistryIndexName(String newRegistryIndexRadix, String suffix) {
        return newRegistryIndexRadix + suffix;
    }

}
