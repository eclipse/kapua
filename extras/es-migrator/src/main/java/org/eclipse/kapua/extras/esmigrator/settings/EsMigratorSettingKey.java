/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
    ELASTICSEARCH_CLUSTER_ADDRESS("elasticsearch.cluster.address"),
    /**
     * Port of the Elasticsearch Cluster
     */
    ELASTICSEARCH_CLUSTER_PORT("elasticsearch.cluster.port"),
    /**
     * Scheme of the Elasticsearch Cluster. Allowed values: http, https
     */
    ELASTICSEARCH_CLUSTER_SCHEME("elasticsearch.cluster.scheme"),

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
    DATASTORE_INDEX_PREFIX("datastore.index.prefix");

    private String key;

    private EsMigratorSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

}
