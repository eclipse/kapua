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
import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;

public class EsEmbeddedEngine {

    private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch/data";

    private static Node node;

    public EsEmbeddedEngine() throws NodeValidationException {
        // lazy synchronization
        if (node == null) {
            synchronized (DEFAULT_DATA_DIRECTORY) {
                if (node == null) {
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
                }
            }
        }
    }

    public Client getClient() {
        return node.client();
    }

    public void close() throws IOException {
        if (node != null) {
            node.close();
            node = null;
        }
    }
}
