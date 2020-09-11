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
package org.eclipse.kapua.service.elasticsearch.client.rest;

import com.codahale.metrics.Counter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.ModelContext;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientReconnectConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientSslConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchNode;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.eclipse.kapua.service.elasticsearch.client.rest.ssl.SkipCertificateCheckTrustStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * {@link ElasticsearchClientProvider} REST implementation.
 * <p>
 * Instantiates and manages the {@link RestElasticsearchClient}.
 *
 * @since 1.0.0
 */
public class RestElasticsearchClientProvider implements ElasticsearchClientProvider<RestElasticsearchClient> {

    private static final Logger LOG = LoggerFactory.getLogger(RestElasticsearchClientProvider.class);

    private static final String PROVIDER_CANNOT_CLOSE_CLIENT_MSG = "Cannot close ElasticSearch REST client. Client is already closed or not initialized";

    private RestElasticsearchClient restElasticsearchClient;
    private RestClient internalElasticsearchRestClient;

    private ElasticsearchClientConfiguration elasticsearchClientConfiguration;
    private ModelContext modelContext;
    private QueryConverter modelConverter;

    private static Counter clientReconnectCallCounter;
    private ScheduledExecutorService reconnectExecutorTask;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public RestElasticsearchClientProvider() {
    }

    /**
     * Initialize the {@link RestElasticsearchClientProvider} instance.
     * <p>
     * The nodes addresses and other parameters are read from {@link ElasticsearchClientConfiguration}.
     * <p>
     * The init methods can be called more than once in order to reinitialize the underlying datastore connection.
     * It the datastore was already initialized this method close the old one before initializing the new one.
     *
     * @return The initialized {@link RestElasticsearchClientProvider}.
     * @since 1.0.0
     */
    @Override
    public RestElasticsearchClientProvider init() {
        synchronized (RestElasticsearchClientProvider.class) {
            if (elasticsearchClientConfiguration == null) {

            }
            if (modelContext == null) {

            }
            if (modelConverter == null) {

            }

            clientReconnectCallCounter = MetricServiceFactory.getInstance().getCounter(elasticsearchClientConfiguration.getHolderModuleName(), "elasticsearch-client-rest", "reconnect_call", "count");

            // Close the current client if already initialized.
            close();

            try {
                initClient();
            } catch (ClientUnavailableException e) {
                e.printStackTrace();
            }

            // Start a nre reconnect task
            reconnectExecutorTask = Executors.newScheduledThreadPool(1);
            reconnectExecutorTask.scheduleWithFixedDelay(() -> {
                try {
                    reconnectClientTask(this::initClient);
                } catch (Exception e) {
                    LOG.info(">>> Initializing Elasticsearch REST client... Connecting... Error: {}", e.getMessage(), e);
                }
            }, 0, getClientReconnectConfiguration().getReconnectDelay(), TimeUnit.MILLISECONDS);

            return this;
        }
    }

    /**
     * Closes the {@link RestElasticsearchClientProvider}.
     * <p>
     * It takes care of closing the {@link RestClient}.
     *
     * @since 1.0.0
     */
    @Override
    public void close() {
        synchronized (RestElasticsearchClientProvider.class) {
            try {
                closeClient();
            } catch (IOException e) {
                LOG.warn(PROVIDER_CANNOT_CLOSE_CLIENT_MSG, e);
            }
        }
    }

    /**
     * Closes the {@link RestClient}.
     * <p>
     * It takes care of stopping the {@link #reconnectExecutorTask}.
     *
     * @throws IOException see {@link RestClient#close()} javadoc.
     * @since 1.0.0
     */
    private void closeClient() throws IOException {
        if (reconnectExecutorTask != null) {
            reconnectExecutorTask.shutdown();

            try {
                reconnectExecutorTask.awaitTermination(getClientReconnectConfiguration().getReconnectDelay(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                reconnectExecutorTask.shutdownNow();
            }

            reconnectExecutorTask = null;
        }

        if (internalElasticsearchRestClient != null) {
            try {
                internalElasticsearchRestClient.close();
            } finally {
                internalElasticsearchRestClient = null;
            }
        }
    }

    /**
     * The {@link Callable} that connects (and reconnects) the {@link RestClient}.
     *
     * @param initClientMethod The {@link Callable} that connects (and reconnects) the {@link RestClient}.
     * @throws Exception if the given {@link Callable} throws {@link Exception}.
     * @since 1.0.0
     */
    private void reconnectClientTask(Callable<RestClient> initClientMethod) throws Exception {
        if (internalElasticsearchRestClient == null) {
            synchronized (RestElasticsearchClientProvider.class) {
                if (internalElasticsearchRestClient == null) {
                    clientReconnectCallCounter.inc();

                    LOG.info(">>> Initializing Elasticsearch REST client... Connecting...");
                    internalElasticsearchRestClient = initClientMethod.call();
                    LOG.info(">>> Initializing Elasticsearch REST client... Connecting... DONE!");
                }
            }
        }
    }

    /**
     * Initializes the {@link RestClient} as per {@link ElasticsearchClientConfiguration}.
     *
     * @return The initialized {@link RestClient}.
     * @throws ClientUnavailableException if any {@link Exception} occurs while {@link RestClient} initialization.
     * @since 1.0.0
     */
    private RestClient initClient() throws ClientUnavailableException {

        ElasticsearchClientConfiguration clientConfiguration = getClientConfiguration();

        if (clientConfiguration.getNodes().isEmpty()) {
            throw new ClientUnavailableException("No Elasticsearch nodes are configured");
        }

        boolean sslEnabled = getClientConfiguration().getSslConfiguration().isEnabled();
        LOG.info("ES Rest Client - SSL Layer: {}", (sslEnabled ? "Enabled" : "Disabled "));
        List<HttpHost> hosts = new ArrayList<>();
        for (ElasticsearchNode node : getClientConfiguration().getNodes()) {
            InetSocketAddress address = new InetSocketAddress(node.getAddress(), node.getPort());
            hosts.add(
                    new HttpHost(
                            address.getAddress(),
                            address.getHostName(),
                            address.getPort(),
                            (sslEnabled ? "https" : "http")
                    )
            );
        }

        RestClientBuilder restClientBuilder = RestClient.builder(hosts.toArray(new HttpHost[0]));
        if (sslEnabled) {
            try {
                SSLContextBuilder sslBuilder = SSLContexts.custom();
                initKeyStore(sslBuilder);
                initTrustStore(sslBuilder);

                SSLContext sslContext = sslBuilder.build();
                restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setSSLContext(sslContext));
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new ClientUnavailableException(e, "Failed to build SSLContext");
            }
        }

        String username = getClientConfiguration().getUsername();
        String password = getClientConfiguration().getPassword();
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        RestClient restClient = restClientBuilder.build();

        restElasticsearchClient = new RestElasticsearchClient();
        restElasticsearchClient
                .withClientConfiguration(clientConfiguration)
                .withModelContext(modelContext)
                .withModelConverter(modelConverter)
                .withClient(restClient)
                .init();

        return restClient;
    }

    @Override
    public ElasticsearchClientProvider<RestElasticsearchClient> withClientConfiguration(ElasticsearchClientConfiguration elasticsearchClientConfiguration) {
        this.elasticsearchClientConfiguration = elasticsearchClientConfiguration;
        return this;
    }

    @Override
    public ElasticsearchClientProvider<RestElasticsearchClient> withModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
        return this;
    }

    @Override
    public ElasticsearchClientProvider<RestElasticsearchClient> withModelConverter(QueryConverter modelConverter) {
        this.modelConverter = modelConverter;
        return this;
    }

    @Override
    public RestElasticsearchClient getElasticsearchClient() throws ClientUnavailableException {
        if (restElasticsearchClient == null) {
            throw new ClientUnavailableException("Client not initialized");
        }

        return restElasticsearchClient;
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

    /**
     * Gets the {@link ElasticsearchClientSslConfiguration}.
     * <p>
     * Shortcut for {@link #getClientConfiguration()#getClientSslConfiguration()}
     *
     * @return The {@link ElasticsearchClientSslConfiguration}
     * @since 1.3.0
     */
    private ElasticsearchClientSslConfiguration getClientSslConfiguration() {
        return getClientConfiguration().getSslConfiguration();
    }

    /**
     * Gets the {@link ElasticsearchClientReconnectConfiguration}.
     * <p>
     * Shortcut for {@link #getClientConfiguration()#getClientReconnectConfiguration()}
     *
     * @return The {@link ElasticsearchClientReconnectConfiguration}
     * @since 1.3.0
     */
    private ElasticsearchClientReconnectConfiguration getClientReconnectConfiguration() {
        return getClientConfiguration().getReconnectConfiguration();
    }

    /**
     * Initializes the {@link KeyStore}  as per  {@link ElasticsearchClientSslConfiguration} with the given {@link SSLContextBuilder}.
     *
     * @param sslBuilder The {@link SSLContextBuilder} to use.
     * @throws ClientUnavailableException if {@link KeyStore} cannot be initialized.
     * @since 1.0.0
     */
    private void initKeyStore(SSLContextBuilder sslBuilder) throws ClientUnavailableException {
        ElasticsearchClientSslConfiguration sslConfiguration = getClientSslConfiguration();

        String keystorePath = sslConfiguration.getKeyStorePath();
        String keystorePassword = sslConfiguration.getKeyStorePassword();
        LOG.info("ES Rest Client - Keystore path: {}", StringUtils.isNotBlank(keystorePath) ? keystorePath : "None");

        if (StringUtils.isNotBlank(keystorePath)) {
            try {
                sslBuilder.loadKeyMaterial(loadKeyStore(keystorePath, keystorePassword), null);
            } catch (UnrecoverableKeyException | ClientUnavailableException | NoSuchAlgorithmException | KeyStoreException lkme) {
                throw new ClientUnavailableException(lkme, "Failed to init KeyStore");
            }
        }
    }

    /**
     * Loads the {@link KeyStore}  as per {@link ElasticsearchClientSslConfiguration}.
     *
     * @param keystorePath     The {@link KeyStore} path.
     * @param keystorePassword The {@link KeyStore} password.
     * @return The initialized {@link KeyStore}.
     * @throws ClientUnavailableException if {@link KeyStore} cannot be loaded.
     * @since 1.0.0
     */
    private KeyStore loadKeyStore(String keystorePath, String keystorePassword) throws ClientUnavailableException {
        try (InputStream is = Files.newInputStream(new File(keystorePath).toPath())) {
            KeyStore keystore = KeyStore.getInstance(getClientConfiguration().getSslConfiguration().getKeyStoreType());
            keystore.load(is, keystorePassword.toCharArray());
            return keystore;
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
            throw new ClientUnavailableException(e, "Failed to load KeyStore");
        }
    }

    /**
     * Initializes the {@link TrustStrategy} as per {@link ElasticsearchClientSslConfiguration} with the given {@link SSLContextBuilder}
     * <p>
     * Truststore 3 available configurations:
     * <ol>
     *     <li>No {@link javax.net.ssl.TrustManager}: if {@link ElasticsearchClientSslConfiguration#isTrustServiceCertificate()} is {@code false}</li>
     *     <li>Set the custom trust manager: if {@link ElasticsearchClientSslConfiguration#getTrustStorePath()} is defined</li>
     *     <li>Use the JVM default truststore: as fallback option</li>
     * </ol>
     *
     * @param sslBuilder The {@link SSLContextBuilder} to use.
     * @throws ClientUnavailableException if {@link KeyStore} cannot be initialized.
     * @since 1.0.0
     */
    private void initTrustStore(SSLContextBuilder sslBuilder) throws ClientUnavailableException {
        ElasticsearchClientSslConfiguration sslConfiguration = getClientSslConfiguration();

        boolean trustServerCertificate = sslConfiguration.isTrustServiceCertificate();
        String truststorePath = sslConfiguration.getKeyStorePath();
        String truststorePassword = sslConfiguration.getTrustStorePassword();
        LOG.info("ES Rest Client - SSL trust server certificate: {}", (trustServerCertificate ? "Enabled" : "Disabled"));
        LOG.info("ES Rest Client - Truststore path: {}", StringUtils.isNotBlank(truststorePath) ? truststorePath : "None");

        try {
            if (!trustServerCertificate) {
                sslBuilder.loadTrustMaterial(null, new SkipCertificateCheckTrustStrategy());
            } else if (StringUtils.isNotBlank(truststorePath)) {
                sslBuilder.loadTrustMaterial(loadKeyStore(truststorePath, truststorePassword), null);
            } else {
                sslBuilder.loadTrustMaterial((TrustStrategy) null);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | ClientUnavailableException ltme) {
            throw new ClientUnavailableException(ltme, "Failed to init TrustStore");
        }
    }
}
