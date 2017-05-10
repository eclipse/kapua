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
package org.eclipse.kapua.service.datastore.client.embedded;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch embedded node engine
 * 
 * @since 1.0
 *
 */
public class EsEmbeddedEngine {

    private static final Logger logger = LoggerFactory.getLogger(EsEmbeddedEngine.class);

    private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch/data"+ UUIDs.randomBase64UUID();

    private static Node node;

    public EsEmbeddedEngine() throws NodeValidationException {
        // lazy synchronization
        if (node == null) {
            synchronized (DEFAULT_DATA_DIRECTORY) {
                if (node == null) {
                    logger.info("Starting Elasticsearch embedded node... (data directory: '{}')", DEFAULT_DATA_DIRECTORY);
                    // ES 5.3 FIX
                    // Builder elasticsearchSettings = Settings.settingsBuilder()
                    // .put("http.enabled", "false")
                    // .put("path.data", DEFAULT_DATA_DIRECTORY)
                    // .put("path.home", ".");
                    Settings settings = Settings.builder()
                            .put("transport.type", "local")
                            .put("http.enabled", "false")
                            .put("path.data", DEFAULT_DATA_DIRECTORY)
                            .put("path.home", ".").build();
                    // ES 5.3 FIX
                    // node = NodeBuilder.nodeBuilder()
                    // .local(true)
                    // .settings(elasticsearchSettings.build())
                    // .node();
                    node = new Node(settings);
                    node.start();
                    logger.info("Starting Elasticsearch embedded node... DONE");
                }
            }
        }
    }

    public Client getClient() {
        return node.client();
    }

    public void close() throws IOException {
        if (node != null) {
            logger.info("Closing Elasticsearch embedded node...");
            node.close();
            node = null;
            logger.info("Closing Elasticsearch embedded node... DONE");
        }
    }
}
