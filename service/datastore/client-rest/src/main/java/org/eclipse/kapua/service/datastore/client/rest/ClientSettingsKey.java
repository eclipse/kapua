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

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Datastore Elasticsearch rest client setting keys.
 * 
 * @since 1.0
 *
 */
public enum ClientSettingsKey implements SettingKey {

    /**
     * Elasticsearch nodes count
     */
    ELASTICSEARCH_NODES("datastore.elasticsearch.nodes"),
    /**
     * Elasticsearch node map
     */
    ELASTICSEARCH_NODE("datastore.elasticsearch.node"),
    /**
     * Elasticsearch port
     */
    ELASTICSEARCH_PORT("datastore.elasticsearch.port"),
    /**
     * Elasticsearch cluster name
     */
    ELASTICSEARCH_CLUSTER("datastore.elasticsearch.cluster"),
    /**
     * Elasticsearch max retry attempt (when a timeout occurred in the rest call)
     */
    ELASTICSEARCH_REST_TIMEOUT_MAX_RETRY("datastore.elasticsearch.rest.max_retry_attempt"),
    /**
     * Elasticsearch max wait time between retry attempt (in milliseconds)
     */
    ELASTICSEARCH_REST_TIMEOUT_MAX_WAIT("datastore.elasticsearch.rest.max_wait_time"),
    /**
     * Query timeout
     */
    QUERY_TIMEOUT("datastore.query.timeout"),
    /**
     * Scroll timeout
     */
    SCROLL_TIMEOUT("datastore.scroll.timeout");


    private String key;

    private ClientSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
