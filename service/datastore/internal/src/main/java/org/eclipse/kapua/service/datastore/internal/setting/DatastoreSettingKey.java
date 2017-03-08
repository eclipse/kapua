/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Datastore setting keys.
 * 
 * @since 1.0
 *
 */
public enum DatastoreSettingKey implements SettingKey
{

    /**
     * Elasticsearch datastore
     */
    ELASTICSEARCH("datastore.elasticsearch"),
    /**
     * Elasticsearch client provider
     */
    ELASTICSEARCH_CLIENT_PROVIDER("datastore.elasticsearch.client.provider"),
    /**
     * Elasticsearch nodes count
     */
    ELASTICSEARCH_NODES("datastore.elasticsearch.node"),
    /**
     * Elasticsearch cluster name
     */
    ELASTICSEARCH_CLUSTER("datastore.elasticsearch.cluster"),
    /**
     * Elasticsearch Maximum topic depth
     */
    ELASTICSEARCH_TOPIC_MAX_DEPTH("datastore.elasticsearch.topic.max.depth"),
    /**
     * Elasticsearch index refresh interval (the data is available for a search operation only if it is indexed)
     */
    ELASTICSEARCH_IDX_REFRESH_INTERVAL("datastore.elasticsearch.index.refresh_interval"),

    /**
     * Local cache expire time
     */
    CONFIG_CACHE_LOCAL_EXPIRE_AFTER("datastore.cache.local.expire.after"),
    /**
     * Local cache maximum size
     */
    CONFIG_CACHE_LOCAL_SIZE_MAXIMUM("datastore.cache.local.size.maximum"),
    /**
     * Enable datastore timing profile
     */
    CONFIG_DATA_STORAGE_ENABLE_TIMING_PROFILE("datastore.enableTimingProfile"),
    /**
     * Datastore timing profile threshold
     */
    CONFIG_DATA_STORAGE_TIMING_PROFILE_THRESHOLD("datastore.timingProfileThreshold");

    private String key;

    private DatastoreSettingKey(String key)
    {
        this.key = key;
    }

    @Override
    public String key()
    {
        return key;
    }
}
