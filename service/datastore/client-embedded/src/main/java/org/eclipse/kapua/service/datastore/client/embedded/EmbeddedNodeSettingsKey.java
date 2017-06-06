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
package org.eclipse.kapua.service.datastore.client.embedded;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Datastore ES embedded node embedded node setting keys.
 * 
 * @since 1.0
 *
 */
public enum EmbeddedNodeSettingsKey implements SettingKey {

    /**
     * Elasticsearch cluster name
     */
    ELASTICSEARCH_CLUSTER("datastore.elasticsearch.cluster"),
    /**
     * Elasticsearch transport node
     */
    ELASTICSEARCH_TRANSPORT_NODE("datastore.elasticsearch.transport.node"),
    /**
     * Elasticsearch transport port
     */
    ELASTICSEARCH_TRANSPORT_PORT("datastore.elasticsearch.transport.port"),
    /**
     * Elasticsearch rest node
     */
    ELASTICSEARCH_REST_NODE("datastore.elasticsearch.rest.node"),
    /**
     * Elasticsearch rest port
     */
    ELASTICSEARCH_REST_PORT("datastore.elasticsearch.rest.port");


    private String key;

    private EmbeddedNodeSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
