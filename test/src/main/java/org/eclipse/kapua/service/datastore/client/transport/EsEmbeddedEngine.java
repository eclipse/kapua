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
package org.eclipse.kapua.service.datastore.client.transport;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;

import java.io.IOException;
import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * Embedded ES engine that uses netty4 as http transport type.
 * It is used for unit / integration with ES tests.
 */
public class EsEmbeddedEngine {

    /**
     * Data directory for storing ES indexes.
     */
    private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch/data";

    /**
     * Embedded ES node.
     */
    private EmbeddedNode node;

    public EsEmbeddedEngine() {

        Settings settings = Settings.builder()
                .put("transport.type", "netty4")
                .put("http.type", "netty4")
                .put("http.enabled", "true")
                .put("node.max_local_storage_nodes", "50")
                .put("path.data", DEFAULT_DATA_DIRECTORY)
                .put("path.home", ".").build();
        node = new EmbeddedNode(settings, asList(Netty4Plugin.class));
        try {
            node.start();
        } catch (NodeValidationException e) {
            e.printStackTrace();
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

    /**
     * Inner class for embedded ES node. Used for unit testing.
     */
    private static class EmbeddedNode extends Node {

        public EmbeddedNode(Settings preparedSettings, Collection<Class<? extends Plugin>> classpathPlugins) {

            super(InternalSettingsPreparer.prepareEnvironment(preparedSettings, null), classpathPlugins);
        }
    }
}
