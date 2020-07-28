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

import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.ModelContext;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientInitializationException;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * {@link ElasticsearchClientProvider} transport implementation.
 * <p>
 * Instantiate and manages the {@link TransportElasticsearchClient}.
 *
 * @since 1.0.0
 * @deprecated Since 1.0.0. {@link TransportElasticsearchClientProvider} will be removed in the next releases. Please use the Rest client instead.
 */
@Deprecated
public class TransportElasticsearchClientProvider implements ElasticsearchClientProvider<TransportElasticsearchClient> {

    private static final Logger LOG = LoggerFactory.getLogger(TransportElasticsearchClientProvider.class);

    private static final String PROVIDER_ALREADY_INITIALIZED_MSG = "Provider already initialized! Closing it before initialize the new one!";
    private static final String PROVIDER_NO_NODE_CONFIGURED_MSG = "No ElasticSearch nodes are configured";
    private static final String PROVIDER_FAILED_TO_CONFIGURE_MSG = "Failed to configure ElasticSearch transport";
    private static final String PROVIDER_CANNOT_CLOSE_CLIENT_MSG = "Cannot close ElasticSearch client. Client is already stopped or not initialized!";

    private TransportElasticsearchClient transportElasticsearchClient;
    private TransportClient internalElasticsearchTransportClient;

    private ElasticsearchClientConfiguration elasticsearchClientConfiguration;
    private ModelContext modelContext;
    private QueryConverter modelConverter;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private TransportElasticsearchClientProvider() {
    }


    /**
     * Initialize the {@link TransportElasticsearchClientProvider} instance.
     * <p>
     * The nodes addresses and other parameters are read from {@link ElasticsearchClientConfiguration}.
     * <p>
     * The init methods can be called more than once in order to reinitialize the underlying datastore connection.
     * It the datastore was already initialized this method close the old one before initializing the new one.
     *
     * @throws ClientUnavailableException
     */
    @Override
    public ElasticsearchClientProvider<TransportElasticsearchClient> init() throws ClientInitializationException {
        synchronized (TransportElasticsearchClientProvider.class) {
            LOG.info(">>> Initializing ES transport client...");
            if (elasticsearchClientConfiguration == null) {

            }
            if (modelContext == null) {

            }
            if (modelConverter == null) {

            }

            close();

            Settings settings =
                    Settings.builder()
                            .put(ClusterName.CLUSTER_NAME_SETTING.getKey(), elasticsearchClientConfiguration.getClusterName())
                            .build();
            internalElasticsearchTransportClient = new PreBuiltTransportClient(settings);

            elasticsearchClientConfiguration
                    .getNodes()
                    .stream()
                    .map(n -> new InetSocketAddress(n.getAddress(), n.getPort()))
                    .map(InetSocketTransportAddress::new)
                    .forEachOrdered(internalElasticsearchTransportClient::addTransportAddress);

            LOG.info(">>> Initializing ES transport client... DONE");

            return this;
        }
    }

    /**
     * Close the ES transport client
     */
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
}
