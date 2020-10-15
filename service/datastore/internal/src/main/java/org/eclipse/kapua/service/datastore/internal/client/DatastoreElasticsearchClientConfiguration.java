/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.client;

import org.eclipse.kapua.service.datastore.internal.setting.DatastoreElasticsearchClientSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreElasticsearchClientSettingsKey;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DatastoreElasticsearchClientConfiguration extends ElasticsearchClientConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DatastoreElasticsearchClientConfiguration.class);

    private static final DatastoreElasticsearchClientSettings ELASTICSEARCH_CLIENT_SETTINGS = DatastoreElasticsearchClientSettings.getInstance();

    public DatastoreElasticsearchClientConfiguration() {
        setProviderClassName(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_PROVIDER));
        setModuleName(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_MODULE));

        setClusterName(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_CLUSTER));

        List<String> nodesSplitted = ELASTICSEARCH_CLIENT_SETTINGS.getList(String.class, DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_NODES);
        for (String node : nodesSplitted) {
            String[] nodeSplitted = node.split(":");
            addNode(nodeSplitted[0], nodeSplitted.length == 2 ? Integer.parseInt(nodeSplitted[1]) : 9200);
        }

        setUsername(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_USERNAME));
        setPassword(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_PASSWORD));
        getRequestConfiguration().setQueryTimeout(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_QUERY_TIMEOUT));
        getRequestConfiguration().setScrollTimeout(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_SCROLL_TIMEOUT));
        getRequestConfiguration().setRequestRetryAttemptMax(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_REST_TIMEOUT_MAX_RETRY));
        getRequestConfiguration().setRequestRetryAttemptWait(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_REST_TIMEOUT_MAX_WAIT));
        getSslConfiguration().setEnabled(ELASTICSEARCH_CLIENT_SETTINGS.getBoolean(DatastoreElasticsearchClientSettingsKey.DATASTORE_ELASTICSEARCH_SSL_ENABLED));

        getReconnectConfiguration().setReconnectDelay(30000);
    }

    public static ElasticsearchClientConfiguration getInstance() {
        return new DatastoreElasticsearchClientConfiguration();
    }
}
