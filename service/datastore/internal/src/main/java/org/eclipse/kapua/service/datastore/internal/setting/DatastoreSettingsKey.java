/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Datastore setting keys.
 *
 * @since 1.0
 */
public enum DatastoreSettingsKey implements SettingKey {

    /**
     * Local cache expire time
     */
    CONFIG_CACHE_LOCAL_EXPIRE_AFTER("datastore.cache.local.expire.after"),
    /**
     * Local cache maximum size
     */
    CONFIG_CACHE_LOCAL_SIZE_MAXIMUM("datastore.cache.local.size.maximum"),
    /**
     * Metadata cache maximum size
     */
    CONFIG_CACHE_METADATA_LOCAL_SIZE_MAXIMUM("datastore.cache.metadata.local.size.maximum"),
    /**
     * Enable datastore timing profile
     */
    CONFIG_DATA_STORAGE_ENABLE_TIMING_PROFILE("datastore.enableTimingProfile"),
    /**
     * Datastore timing profile threshold
     */
    CONFIG_DATA_STORAGE_TIMING_PROFILE_THRESHOLD("datastore.timingProfileThreshold"),
    /**
     * Elasticsearch index refresh interval (the data is available for a search operation only if it is indexed)
     */
    INDEX_REFRESH_INTERVAL("datastore.index.refresh_interval"),
    /**
     * Shards number
     */
    INDEX_SHARD_NUMBER("datastore.index.number_of_shards"),
    /**
     * Replicas count
     */
    INDEX_REPLICA_NUMBER("datastore.index.number_of_replicas"),
    /**
     * Elasticsearch index refresh interval (the data is available for a search operation only if it is indexed)
     */
    CONFIG_MAX_ENTRIES_ON_DELETE("datastore.delete.max_entries_on_delete"),
    /**
     * Elasticsearch index name system-wide prefix
     */
    INDEX_PREFIX("datastore.index.prefix"),
    /**
     * Elasticsearch index width. Allowed values: "week", "day", "hour"
     */
    INDEXING_WINDOW_OPTION("datastore.index.window"),
    /**
     * Disables the entire Datastore feature
     */
    DISABLE_DATASTORE("datastore.disable");

    private String key;

    private DatastoreSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
