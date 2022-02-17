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
package org.eclipse.kapua.extras.esmigrator.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum EsMigratorSettingKey implements SettingKey {

    /**
     * Address of the Elasticsearch Cluster
     */
    ELASTICSEARCH_CLUSTER_NODES("elasticsearch.cluster.nodes"),
    /**
     * Use HTTPS to interact with Elasticsearch
     */
    ELASTICSEARCH_CLUSTER_SSL("elasticsearch.cluster.ssl"),
    /**
     * Skip SSL Certificate Validation when interacting with Elasticsearch
     */
    ELASTICSEARCH_CLUSTER_SSL_IGNORE_CERTIFICATE("elasticsearch.cluster.ssl.ignore-certificate"),
    /**
     * Elasticsearch User
     */
    ELASTICSEARCH_USERNAME("elasticsearch.username"),
    /**
     * Elasticsearch Password
     */
    ELASTICSEARCH_PASSWORD("elasticsearch.password"),
    /**
     * Elasticsearch Socket Timeout
     */
    ELASTICSEARCH_SOCKET_TIMEOUT("elasticsearch.socket-timeout"),
    /**
     * Elasticsearch Batch Size
     */
    ELASTICSEARCH_BATCH_SIZE("elasticsearch.batch-size"),
    /**
     * Elasticsearch Task Pollint Interval
     */
    ELASTICSEARCH_TASK_POLLING_INTERVAL("elasticsearch.task-polling-interval"),

    /**
     * The refresh interval for new indexes
     */
    DATASTORE_INDEX_REFRESH_INTERVAL("datastore.index.refresh_interval"),
    /**
     * The number of shards for new indexes
     */
    DATASTORE_INDEX_NUMBER_OF_SHARDS("datastore.index.number_of_shards"),
    /**
     * The number of replicas for new indexes
     */
    DATASTORE_INDEX_NUMBER_OF_REPLICAS("datastore.index.number_of_replicas"),
    /**
     * The prefix of all index names to operate on
     */
    DATASTORE_INDEX_PREFIX("datastore.index.prefix"),
    /**
     * Action performed on a migrated index (ES5 to ES6 only). Allowed values: delete, close, none
     */
    DATASTORE_INDEX_MIGRATION_COMPLETE_ACTION("datastore.index.migration-complete.action"),

    /**
     * Report results to file
     */
    MIGRATOR_REPORT_TO_FILE("migrator.report-to-file"),
    /**
     * JDBC Connection String
     */
    MIGRATOR_JDBC_CONNECTION_STRING("migrator.jdbc.connection-string"),
    /**
     * JDBC Username
     */
    MIGRATOR_JDBC_USERNAME("migrator.jdbc.username"),
    /**
     * JDBC Password
     */
    MIGRATOR_JDBC_PASSWORD("migrator.jdbc.password");


    private final String key;

    private EsMigratorSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

}
