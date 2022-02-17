/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.server.embedded;

import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;

import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.Collections;

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

    private static final EmbeddedNodeSettings EMBEDDED_NODE_SETTINGS = EmbeddedNodeSettings.getInstance();

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
                if (node == null) {
                    LOG.info("Starting Elasticsearch embedded node...");

                    // Config
                    String clusterName = EMBEDDED_NODE_SETTINGS.getString(EmbeddedNodeSettingsKeys.ELASTICSEARCH_CLUSTER);
                    String defaultDataDirectory = "target/elasticsearch/data/" + UUIDs.randomBase64UUID();

                    // Transport Endpoint
                    String transportTcpHost = EMBEDDED_NODE_SETTINGS.getString(EmbeddedNodeSettingsKeys.ELASTICSEARCH_TRANSPORT_NODE);
                    int transportTcpPort = EMBEDDED_NODE_SETTINGS.getInt(EmbeddedNodeSettingsKeys.ELASTICSEARCH_TRANSPORT_PORT);

                    // REST Endpoint
                    String restTcpHost = EMBEDDED_NODE_SETTINGS.getString(EmbeddedNodeSettingsKeys.ELASTICSEARCH_REST_NODE);
                    int restTcpPort = EMBEDDED_NODE_SETTINGS.getInt(EmbeddedNodeSettingsKeys.ELASTICSEARCH_REST_PORT);

                    // Print config
                    ConfigurationPrinter
                            .create()
                            .withLogger(LOG)
                            .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                            .withTitle("Elasticsearch Embedded Node Configuration")
                            .addParameter("Cluster name", clusterName)
                            .addParameter("Data Directory", defaultDataDirectory)
                            .openSection("Transport Endpoint")
                            .addParameter("Host", transportTcpHost)
                            .addParameter("Port", transportTcpPort)
                            .closeSection()
                            .openSection("REST Endpoint")
                            .addParameter("Host", restTcpHost)
                            .addParameter("Port", restTcpPort)
                            .closeSection()
                            .printLog();

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
                        throw new RuntimeException("Starting Elasticsearch embedded node... ERROR!", e);
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
            super(InternalSettingsPreparer.prepareEnvironment(settings, Collections.emptyMap(), null, () -> RandomStringUtils.randomAlphanumeric(10)), plugins, true);
        }

    }

}
