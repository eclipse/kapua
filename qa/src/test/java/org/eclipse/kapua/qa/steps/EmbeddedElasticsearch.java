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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.transport.ClientSettings;
import org.eclipse.kapua.service.datastore.client.transport.ClientSettingsKey;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
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
        return ClientSettings.getInstance().getString(ClientSettingsKey.ELASTICSEARCH_CLUSTER, "kapua-datastore");
    }

    public EmbeddedElasticsearch() {
        // creating a new random path every time
        dataDirectory = Paths.get("target", "es", UUID.randomUUID().toString());
    }

    @Before
    public void start() throws IOException, NodeValidationException {
        logger.info("Starting up embedded ES instance ...");

        // ES 5.3 FIX
        // Builder elasticsearchSettings = Settings.settingsBuilder()
        // .put("http.enabled", "true")
        // .put("path.data", DEFAULT_DATA_DIRECTORY)
        // .put("path.home", ".");
        Settings settings = Settings.builder()
                .put("path.data", dataDirectory)
                .put("cluster.name", getClusterName())
                .put("http.enabled", "false")
                .put("transport.type", "local")
                .put("path.home", ".").build();

        // ES 5.3 FIX
        // node = NodeBuilder.nodeBuilder()
        // .local(true)
        // .settings(elasticsearchSettings.build())
        // .node();
        node = new Node(settings);
        node.start();
        logger.info("Starting up embedded ES instance ... done!");
    }

    @After
    public void stop() throws IOException {
        if (node != null) {
            node.close();
            node = null;
        }
    }

    public void refresh() throws IOException, ClientException {
        logger.info("Starting index refresh");
        DatastoreMediator.getInstance().refreshAllIndexes();
        logger.info("Index refresh call done");
    }
}
