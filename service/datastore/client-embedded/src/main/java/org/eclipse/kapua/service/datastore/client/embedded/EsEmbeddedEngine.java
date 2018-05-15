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

import org.elasticsearch.client.Client;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.percolator.PercolatorPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.mustache.MustachePlugin;
import org.elasticsearch.transport.Netty4Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

//import org.elasticsearch.common.settings.Settings.Builder;

/**
 * Elasticsearch embedded node engine. To be used for test purpose.<br>
 * This class will start an Elasticsearch node bounds the transport connector to the port 9300 and rest to the port 9200 on 127.0.0.1.
 *
 * @since 1.0
 */
public class EsEmbeddedEngine {

    private static final Logger LOG = LoggerFactory.getLogger(EsEmbeddedEngine.class);

    private static String defaultDataDirectory;
    private static volatile Node node;

    public EsEmbeddedEngine() {
        // lazy synchronization
        if (node == null) {
            synchronized (EsEmbeddedEngine.this) {
                defaultDataDirectory = "target/elasticsearch/data/" + UUIDs.randomBase64UUID();
                if (node == null) {
                    LOG.info("Starting Elasticsearch embedded node... (data directory: '{}')", defaultDataDirectory);
                    EmbeddedNodeSettings clientSettings = EmbeddedNodeSettings.getInstance();
                    String clusterName = clientSettings.getString(EmbeddedNodeSettingsKey.ELASTICSEARCH_CLUSTER);
                    LOG.info("Cluster name [{}]", clusterName);

                    // transport
                    int transportTcpPort = clientSettings.getInt(EmbeddedNodeSettingsKey.ELASTICSEARCH_TRANSPORT_PORT);
                    String transportTcpHost = clientSettings.getString(EmbeddedNodeSettingsKey.ELASTICSEARCH_TRANSPORT_NODE);
                    LOG.info(">>> Transport: host [{}] - port [{}]", transportTcpHost, transportTcpPort);

                    // rest
                    int restTcpPort = clientSettings.getInt(EmbeddedNodeSettingsKey.ELASTICSEARCH_REST_PORT);
                    String restTcpHost = clientSettings.getString(EmbeddedNodeSettingsKey.ELASTICSEARCH_REST_NODE);
                    LOG.info(">>> Rest: host [{}] - port [{}]", restTcpHost, restTcpPort);
                    // ES 5.3 FIX
                    // Builder elasticsearchSettings = Settings.settingsBuilder()
                    // .put("http.enabled", "false")
                    // .put("path.data", defaultDataDirectory)
                    // .put("path.home", ".");
                    Settings settings = Settings.builder()
                            .put("cluster.name", clusterName)
                            .put("transport.host", transportTcpHost)
                            .put("transport.tcp.port", transportTcpPort)
                            .put("http.enabled", "true")
                            .put("http.type", "netty4")
                            .put("http.host", restTcpHost)
                            .put("http.port", restTcpPort)
                            .put("path.data", defaultDataDirectory)
                            .put("path.home", ".").build();
                    // ES 5.3 FIX
                    // node = NodeBuilder.nodeBuilder()
                    // .local(true)
                    // .settings(elasticsearchSettings.build())
                    // .node();
                    Collection<Class<? extends Plugin>> plugins = Arrays.asList(
                            Netty4Plugin.class, ReindexPlugin.class, PercolatorPlugin.class, MustachePlugin.class);
                    node = new PluggableNode(settings, plugins);
                    // node = new Node(settings);
                    try {
                        node.start();
                    } catch (NodeValidationException e) {
                        throw new RuntimeException("Cannot start embedded node!", e);
                    }
                    LOG.info("Starting Elasticsearch embedded node... DONE");
                }
            }
        }
    }

    public Client getClient() {
        return node.client();
    }

    public void close() throws IOException {
        if (node != null) {
            LOG.info("Closing Elasticsearch embedded node...");
            node.close();
            node = null;
            LOG.info("Closing Elasticsearch embedded node... DONE");
        }
    }

    private class PluggableNode extends Node {

        public PluggableNode(Settings settings, Collection<Class<? extends Plugin>> plugins) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, null), plugins);
        }
    }

}
