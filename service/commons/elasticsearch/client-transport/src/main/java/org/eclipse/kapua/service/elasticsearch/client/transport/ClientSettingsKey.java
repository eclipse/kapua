/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.transport;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Datastore Elasticsearch transport client setting keys.
 *
 * @since 1.0.0
 * @deprecated Since 1.0.0. Elasticsearch transport client will be removed in the next releases. Please use the Rest client instead.
 */
@Deprecated
public enum ClientSettingsKey implements SettingKey {

    /**
     * Elasticsearch client provider.
     *
     * @since 1.0.0
     */
    ELASTICSEARCH_CLIENT_PROVIDER("datastore.elasticsearch.client.provider"),
    /**
     * Elasticsearch nodes count.
     *
     * @since 1.0.0
     */
    ELASTICSEARCH_NODES("datastore.elasticsearch.nodes"),
    /**
     * Elasticsearch node map.
     *
     * @since 1.0.0
     */
    ELASTICSEARCH_NODE("datastore.elasticsearch.node"),
    /**
     * Elasticsearch port.
     *
     * @since 1.0.0
     */
    ELASTICSEARCH_PORT("datastore.elasticsearch.port"),
    /**
     * Elasticsearch cluster name.
     *
     * @since 1.0.0
     */
    ELASTICSEARCH_CLUSTER("datastore.elasticsearch.cluster"),
    /**
     * Query timeout.
     *
     * @since 1.0.0
     */
    QUERY_TIMEOUT("datastore.query.timeout"),
    /**
     * Scroll timeout.
     *
     * @since 1.0.0
     */
    SCROLL_TIMEOUT("datastore.scroll.timeout");


    private String key;

    /**
     * Constructor.
     *
     * @param key The key value for this {@link SettingKey}
     * @since 1.0.0
     */
    private ClientSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
