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
    SCROLL_TIMEOUT("datastore.scroll.timeout"),
    /**
     * Enable Elastichsearch client ssl connection (at the present only the rest client supports it)
     */
    ELASTICSEARCH_SSL_ENABLED("datastore.elasticsearch.ssl.enabled"),
    /**
     * Force Elastichsearch client to trust the server certificate on the ssl connection handshake so, if true, no check will be performed for the server certificate (at the present only the rest
     * client supports it)
     */
    ELASTICSEARCH_SSL_TRUST_SERVER_CERTIFICATE("datastore.elasticsearch.ssl.trust_server_certificate"),
    /**
     * Set the keystore type
     */
    ELASTICSEARCH_SSL_KEYSTORE_TYPE("datastore.elasticsearch.ssl.keystore_type"),
    /**
     * Elastichsearch client key store path (at the present only the rest client supports it)
     */
    ELASTICSEARCH_SSL_KEYSTORE_PATH("datastore.elasticsearch.ssl.keystore_path"),
    /**
     * Elastichsearch client key store password (at the present only the rest client supports it)
     */
    ELASTICSEARCH_SSL_KEYSTORE_PASSWORD("datastore.elasticsearch.ssl.keystore_password"),
    /**
     * Elastichsearch client trust store path (at the present only the rest client supports it)
     */
    ELASTICSEARCH_SSL_TRUSTSTORE_PATH("datastore.elasticsearch.ssl.truststore_path"),
    /**
     * Elastichsearch client trust store password (at the present only the rest client supports it)
     */
    ELASTICSEARCH_SSL_TRUSTSTORE_PASSWORD("datastore.elasticsearch.ssl.truststore_password");

    private String key;

    private ClientSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
