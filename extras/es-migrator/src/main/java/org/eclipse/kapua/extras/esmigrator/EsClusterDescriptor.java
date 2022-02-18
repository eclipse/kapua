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

import java.util.List;

import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSetting;
import org.eclipse.kapua.extras.esmigrator.settings.EsMigratorSettingKey;

public class EsClusterDescriptor {

    private static final boolean DEFAULT_ELASTICSEARCH_CLUSTER_SSL = false;
    private static final String DEFAULT_ELASTICSEARCH_USERNAME = "";
    private static final String DEFAULT_ELASTICSEARCH_PASSWORD = "";
    private static final String DEFAULT_INDEX_REFRESH_INTERVAL = "5s";
    private static final int DEFAULT_INDEX_SHARD_NUMBER = 1;
    private static final int DEFAULT_INDEX_REPLICA_NUMBER = 0;
    private static final String DEFAULT_INDICES_PREFIX = "";
    private static final String DEFAULT_DATASTORE_INDEX_MIGRATION_COMPLETE_ACTION = "delete";

    /**
     * The ES Nodes addresses
     */
    private final List<String> esClusterNodes;

    /**
     * Use SSL to connect to ES Nodes
     */
    private final boolean esClusterSsl;

    /**
     * The refresh interval for new indexes
     */
    private final String indicesRefreshInterval;

    /**
     * The number of shards for new indexes
     */
    private final int indicesShardNumber;

    /**
     * The number of replicas for new indexes
     */
    private final int indicesReplicaNumber;

    /**
     * The prefix of all index names to operate on
     */
    private final String indicesPrefix;

    /**
     * Elasticsearch Cluster Username
     */
    private final String username;

    /**
     * Elasticsearch Cluster Password
     */
    private final String password;

    /**
     * Index action to be performed after a successful migration
     */
    private final IndexMigrationCompleteAction action;

    public EsClusterDescriptor() {
        EsMigratorSetting esMigratorSetting = EsMigratorSetting.getInstance();
        esClusterNodes = esMigratorSetting.getList(String.class, EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_NODES);
        esClusterSsl = esMigratorSetting.getBoolean(EsMigratorSettingKey.ELASTICSEARCH_CLUSTER_SSL, DEFAULT_ELASTICSEARCH_CLUSTER_SSL);
        username = esMigratorSetting.getString(EsMigratorSettingKey.ELASTICSEARCH_USERNAME, DEFAULT_ELASTICSEARCH_USERNAME);
        password = esMigratorSetting.getString(EsMigratorSettingKey.ELASTICSEARCH_PASSWORD, DEFAULT_ELASTICSEARCH_PASSWORD);
        indicesRefreshInterval = esMigratorSetting.getString(EsMigratorSettingKey.DATASTORE_INDEX_REFRESH_INTERVAL, DEFAULT_INDEX_REFRESH_INTERVAL);
        indicesShardNumber = esMigratorSetting.getInt(EsMigratorSettingKey.DATASTORE_INDEX_NUMBER_OF_SHARDS, DEFAULT_INDEX_SHARD_NUMBER);
        indicesReplicaNumber = esMigratorSetting.getInt(EsMigratorSettingKey.DATASTORE_INDEX_NUMBER_OF_REPLICAS, DEFAULT_INDEX_REPLICA_NUMBER);
        indicesPrefix = esMigratorSetting.getString(EsMigratorSettingKey.DATASTORE_INDEX_PREFIX, DEFAULT_INDICES_PREFIX);
        action = IndexMigrationCompleteAction.valueOf(esMigratorSetting.getString(EsMigratorSettingKey.DATASTORE_INDEX_MIGRATION_COMPLETE_ACTION, DEFAULT_DATASTORE_INDEX_MIGRATION_COMPLETE_ACTION).toUpperCase());
    }

    public List<String> getEsClusterNodes() {
        return esClusterNodes;
    }

    public boolean isEsClusterSsl() {
        return esClusterSsl;
    }

    public String getIndicesRefreshInterval() {
        return indicesRefreshInterval;
    }

    public int getIndicesShardNumber() {
        return indicesShardNumber;
    }

    public int getIndicesReplicaNumber() {
        return indicesReplicaNumber;
    }

    public String getIndicesPrefix() {
        return indicesPrefix;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public IndexMigrationCompleteAction getAction() {
        return action;
    }

}
