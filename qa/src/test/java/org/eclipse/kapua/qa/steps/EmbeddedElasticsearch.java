/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.eclipse.kapua.service.datastore.internal.Elasticsearch;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.transport.TransportModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class EmbeddedElasticsearch {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedElasticsearch.class);

    private Node node;
    private final Path dataDirectory;

    private static String getClusterName() {
        return DatastoreSettings.getInstance().getString(DatastoreSettingKey.ELASTICSEARCH_CLUSTER, "kapua-datastore");
    }

    public EmbeddedElasticsearch() {
        // creating a new random path every time
        this.dataDirectory = Paths.get("target", "es", UUID.randomUUID().toString());
    }

    @Before
    public void start() throws IOException {
        logger.info("Starting up embedded ES instance ...");

        final Settings.Builder elasticsearchSettings = Settings.settingsBuilder()
                .put("path.home", dataDirectory.toAbsolutePath().toString())
                .put(TransportModule.TRANSPORT_TYPE_KEY, TransportModule.NETTY_TRANSPORT)
                .put("http.enabled", "true");

        node = NodeBuilder.nodeBuilder()
                .settings(elasticsearchSettings)
                .clusterName(getClusterName())
                .node();

        logger.info("Starting up embedded ES instance ... done!");
    }

    @After
    public void stop() throws IOException {
        if (node != null) {
            node.close();
            node = null;
        }
    }

    public void refresh() throws IOException {
        logger.info("Starting index refresh");
        Elasticsearch.refreshAllIndices(node.client());
        logger.info("Index refresh call done");
    }
}
