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
        setProviderClassName(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.PROVIDER));
        setModuleName(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.MODULE));

        setClusterName(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.CLUSTER));

        List<String> nodesSplitted = ELASTICSEARCH_CLIENT_SETTINGS.getList(String.class, DatastoreElasticsearchClientSettingsKey.NODES);
        for (String node : nodesSplitted) {
            String[] nodeSplitted = node.split(":");
            addNode(nodeSplitted[0], nodeSplitted.length == 2 ? Integer.parseInt(nodeSplitted[1]) : 9200);
        }

        setUsername(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.USERNAME));
        setPassword(ELASTICSEARCH_CLIENT_SETTINGS.getString(DatastoreElasticsearchClientSettingsKey.PASSWORD));
        getRequestConfiguration().setQueryTimeout(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.REQUEST_QUERY_TIMEOUT));
        getRequestConfiguration().setScrollTimeout(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.REQUEST_SCROLL_TIMEOUT));
        getRequestConfiguration().setRequestRetryAttemptMax(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.REQUEST_RETRY_MAX));
        getRequestConfiguration().setRequestRetryAttemptWait(ELASTICSEARCH_CLIENT_SETTINGS.getInt(DatastoreElasticsearchClientSettingsKey.REQUEST_RETRY_WAIT));
        getSslConfiguration().setEnabled(ELASTICSEARCH_CLIENT_SETTINGS.getBoolean(DatastoreElasticsearchClientSettingsKey.SSL_ENABLED));

        getReconnectConfiguration().setReconnectDelay(30000);
    }

    public static ElasticsearchClientConfiguration getInstance() {
        return new DatastoreElasticsearchClientConfiguration();
    }
}
