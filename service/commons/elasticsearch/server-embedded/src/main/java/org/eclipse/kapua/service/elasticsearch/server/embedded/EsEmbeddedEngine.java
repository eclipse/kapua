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
package org.eclipse.kapua.service.elasticsearch.server.embedded;

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

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Elasticsearch embedded node engine.
 * <p>
 * This class will start an Elasticsearch node bounds the transport connector to the port 9300 and rest to the port 9200 both bound to 127.0.0.1.
 * <p>
 * To be used for test purpose.
 *
 * @since 1.0.0
 */
public class EsEmbeddedEngine implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(EsEmbeddedEngine.class);

    /**
     * The {@link Node}
     *
     * @since 1.0.0
     */
    private static volatile Node node;

    /**
     * Constructor.
     * <p>
     * Initializes the {@link EsEmbeddedEngine} according to the given {@link EmbeddedNodeSettings}
     *
     * @since 1.0.0
     */
    public EsEmbeddedEngine() {
        if (node == null) {
            synchronized (EsEmbeddedEngine.this) {
                String defaultDataDirectory = "target/elasticsearch/data/" + UUIDs.randomBase64UUID();
                if (node == null) {
                    LOG.info("Starting Elasticsearch embedded node...");
                    LOG.info("\tData directory:     {}", defaultDataDirectory);
                    EmbeddedNodeSettings clientSettings = EmbeddedNodeSettings.getInstance();
                    String clusterName = clientSettings.getString(EmbeddedNodeSettingsKeys.ELASTICSEARCH_CLUSTER);
                    LOG.info("\tCluster name:       {}", clusterName);

                    // Transport Endpoint
                    String transportTcpHost = clientSettings.getString(EmbeddedNodeSettingsKeys.ELASTICSEARCH_TRANSPORT_NODE);
                    int transportTcpPort = clientSettings.getInt(EmbeddedNodeSettingsKeys.ELASTICSEARCH_TRANSPORT_PORT);
                    LOG.info("\tTransport Endpoint: {}:{}", transportTcpHost, transportTcpPort);

                    // REST Endpoint
                    String restTcpHost = clientSettings.getString(EmbeddedNodeSettingsKeys.ELASTICSEARCH_REST_NODE);
                    int restTcpPort = clientSettings.getInt(EmbeddedNodeSettingsKeys.ELASTICSEARCH_REST_PORT);
                    LOG.info("\tRest Endpoint:      {}:{}", restTcpHost, restTcpPort);

                    // ES 5.3 FIX
                    // Builder elasticsearchSettings = Settings.settingsBuilder()
                    // .put("http.enabled", "false")
                    // .put("path.data", defaultDataDirectory)
                    // .put("path.home", ".");
                    Settings settings = Settings.builder()
                            .put("cluster.name", clusterName)
                            .put("transport.host", transportTcpHost)
                            .put("transport.tcp.port", transportTcpPort)
                            .put("http.host", restTcpHost)
                            .put("http.port", restTcpPort)
                            .put("http.enabled", "true")
                            .put("http.type", "netty4")
                            .put("path.data", defaultDataDirectory)
                            .put("path.home", ".").build();

                    // ES 5.3 FIX
                    // node = NodeBuilder.nodeBuilder()
                    // .local(true)
                    // .settings(elasticsearchSettings.build())
                    // .node();
                    Collection<Class<? extends Plugin>> plugins =
                            Arrays.asList(
                                    Netty4Plugin.class,
                                    ReindexPlugin.class,
                                    PercolatorPlugin.class,
                                    MustachePlugin.class
                            );

                    node = new PluggableNode(settings, plugins);

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

    /**
     * Gets a {@link Client} to interact with the {@link EsEmbeddedEngine}
     *
     * @return A {@link Client} to interact with the {@link EsEmbeddedEngine}
     * @see Node#client()
     * @since 1.0.0
     */
    public Client getClient() {
        if (node == null) {
            throw new RuntimeException("Node already closed! Please init again the EsEmbeddedNode");
        }

        return node.client();
    }

    @Override
    public void close() throws IOException {
        if (node != null) {
            LOG.info("Closing Elasticsearch embedded node...");
            try {
                node.close();
            } finally {
                node = null;
            }
            LOG.info("Closing Elasticsearch embedded node... DONE!");
        }
    }

    private static class PluggableNode extends Node {
        public PluggableNode(Settings settings, Collection<Class<? extends Plugin>> plugins) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, null), plugins);
        }
    }

}
