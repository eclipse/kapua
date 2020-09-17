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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.transport;

import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.ModelContext;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchNode;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientProviderInitException;
import org.eclipse.kapua.service.elasticsearch.client.utils.InetAddressParser;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ElasticsearchClientProvider} transport implementation.
 * <p>
 * Instantiate and manages the {@link TransportElasticsearchClient}.
 *
 * @since 1.0.0
 * @deprecated Since 1.0.0. {@link TransportElasticsearchClientProvider} will be removed in the next releases. Please use the REST client instead.
 */
@Deprecated
public class TransportElasticsearchClientProvider implements ElasticsearchClientProvider<TransportElasticsearchClient> {

    private static final Logger LOG = LoggerFactory.getLogger(TransportElasticsearchClientProvider.class);

    private TransportClient internalElasticsearchTransportClient;

    private TransportElasticsearchClient transportElasticsearchClient;
    private ElasticsearchClientConfiguration elasticsearchClientConfiguration;
    private ModelContext modelContext;
    private QueryConverter modelConverter;

    @Override
    public ElasticsearchClientProvider<TransportElasticsearchClient> init() throws ClientProviderInitException {
        synchronized (TransportElasticsearchClientProvider.class) {

            if (getClientConfiguration() == null) {
                throw new ClientProviderInitException("Client configuration not defined");
            }

            if (getClientConfiguration().getNodes().isEmpty()) {
                throw new ClientProviderInitException("Client configuration nodes empty");
            }

            if (modelContext == null) {
                throw new ClientProviderInitException("Model context not defined");
            }
            if (modelConverter == null) {
                throw new ClientProviderInitException("Model converter not defined");
            }

            // Print Configurations
            ConfigurationPrinter configurationPrinter =
                    ConfigurationPrinter
                            .create()
                            .withLogger(LOG)
                            .withTitle("Elasticsearch Transport Provider Configuration")
                            .addParameter("Module Name", getClientConfiguration().getModuleName())
                            .addParameter("Cluster Name", getClientConfiguration().getClusterName())
                            .addHeader("Nodes")
                            .increaseIndentation();

            int nodesIndex = 1;
            for (ElasticsearchNode node : getClientConfiguration().getNodes()) {
                configurationPrinter
                        .addHeader("# " + nodesIndex++)
                        .increaseIndentation()
                        .addParameter("Host", node.getAddress())
                        .addParameter("Port", node.getPort())
                        .decreaseIndentation();
            }
            // Other configurations
            configurationPrinter
                    .decreaseIndentation()
                    .addParameter("Model Context", modelContext)
                    .addParameter("Model Converter", modelConverter)
                    .printLog();

            // Close previously initialized Transport Clients.
            close();

            // Init internal Elasticsearch Client
            LOG.info(">>> Initializing ES transport client...");
            try {
                Settings settings =
                        Settings.builder()
                                .put(ClusterName.CLUSTER_NAME_SETTING.getKey(), getClientConfiguration().getClusterName())
                                .build();

                internalElasticsearchTransportClient = new PreBuiltTransportClient(settings);
            } catch (Exception e) {
                throw new ClientProviderInitException(e, "Cannot build Elasticsearch Transport Client!");
            }

            try {
                // Add configured nodes
                InetAddressParser
                        .parseAddresses(getClientConfiguration().getNodes())
                        .stream()
                        .map(InetSocketTransportAddress::new)
                        .forEachOrdered(internalElasticsearchTransportClient::addTransportAddress);
            } catch (Exception e) {
                throw new ClientProviderInitException(e, "Error while parsing node addresses!");
            }

            // Init Kapua Elasticsearch Client
            transportElasticsearchClient = new TransportElasticsearchClient();
            try {
                transportElasticsearchClient
                        .withClientConfiguration(getClientConfiguration())
                        .withModelContext(modelContext)
                        .withModelConverter(modelConverter)
                        .withClient(internalElasticsearchTransportClient)
                        .init();
            } catch (Exception e) {
                throw new ClientProviderInitException(e, "Cannot init Kapua Elasticsearch Client!");
            }

            LOG.info(">>> Initializing ES transport client... DONE");

            return this;
        }
    }

    @Override
    public void close() {
        synchronized (TransportElasticsearchClientProvider.class) {
            closeClient();
        }
    }

    private void closeClient() {
        if (internalElasticsearchTransportClient != null) {
            try {
                internalElasticsearchTransportClient.close();
            } finally {
                internalElasticsearchTransportClient = null;
            }
        }
    }

    @Override
    public ElasticsearchClientProvider<TransportElasticsearchClient> withClientConfiguration(ElasticsearchClientConfiguration elasticsearchClientConfiguration) {
        this.elasticsearchClientConfiguration = elasticsearchClientConfiguration;
        return this;
    }

    @Override
    public ElasticsearchClientProvider<TransportElasticsearchClient> withModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
        return this;
    }

    @Override
    public ElasticsearchClientProvider<TransportElasticsearchClient> withModelConverter(QueryConverter modelConverter) {
        this.modelConverter = modelConverter;
        return this;
    }

    @Override
    public TransportElasticsearchClient getElasticsearchClient() {
        return transportElasticsearchClient;
    }

    //
    // Private methods
    //

    /**
     * Gets the {@link ElasticsearchClientConfiguration}.
     *
     * @return The {@link ElasticsearchClientConfiguration}.
     * @since 1.3.0
     */
    private ElasticsearchClientConfiguration getClientConfiguration() {
        return elasticsearchClientConfiguration;
    }

}
