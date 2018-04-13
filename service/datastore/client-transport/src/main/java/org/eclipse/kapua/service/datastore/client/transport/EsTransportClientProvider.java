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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.client.ClientProvider;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch transport client implementation.<br>
 * Instantiate the Elasticsearch transport client.
 *
 * @since 1.0
 */
public class EsTransportClientProvider implements ClientProvider<Client> {

    private static final Logger logger = LoggerFactory.getLogger(EsTransportClientProvider.class);

    private static final String PROVIDER_NOT_INITIALIZED_MSG = "Provider not configured! please call initi method before use it!";
    private static final String PROVIDER_ALREADY_INITIALIZED_MSG = "Provider already initialized! closing it before initialize the new one!";
    private static final String PROVIDER_NO_NODE_CONFIGURED_MSG = "No ElasticSearch nodes are configured";
    private static final String PROVIDER_FAILED_TO_CONFIGURE_MSG = "Failed to configure ElasticSearch transport";
    private static final String PROVIDER_CANNOT_CLOSE_CLIENT_MSG = "Cannot close ElasticSearch client. Client is already stopped or not initialized!";

    private static final String KEY_ES_CLUSTER_NAME = "cluster.name";
    private static final int DEFAULT_PORT = 9300;

    private static EsTransportClientProvider instance;

    private TransportClient client;

    private static int getDefaultPort() {
        return ClientSettings.getInstance().getInt(ClientSettingsKey.ELASTICSEARCH_PORT, DEFAULT_PORT);
    }

    /**
     * Get the {@link EsTransportClientProvider} instance
     * 
     * @return
     * @throws ClientUnavailableException
     */
    public static EsTransportClientProvider getInstance() throws ClientUnavailableException {
        if (instance == null) {
            throw new ClientUnavailableException(PROVIDER_NOT_INITIALIZED_MSG);
        }
        return instance;
    }

    /**
     * Initialize the {@link EsTransportClientProvider} singleton instance.<br>
     * The nodes addresses, the cluster name and other parameters are read from the configuration file.<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @return
     * @throws ClientUnavailableException
     */
    public static EsTransportClientProvider init() throws ClientUnavailableException {
        synchronized (EsTransportClientProvider.class) {
            logger.info(">>> Initializing ES transport client...");
            closeIfInstanceInitialized();
            instance = new EsTransportClientProvider();
            logger.info(">>> Initializing ES transport client... DONE");
        }
        return instance;
    }

    /**
     * Initialize the {@link EsTransportClientProvider} singleton instance.<br>
     * The nodes addresses, the cluster name and other parameters are overwritten with the provided settings.<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @param settings
     * @throws ClientUnavailableException
     */
    public static void init(AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
        synchronized (EsTransportClientProvider.class) {
            logger.info(">>> Initializing ES transport client...");
            closeIfInstanceInitialized();
            instance = new EsTransportClientProvider(settings);
            logger.info(">>> Initializing ES transport client... DONE");
        }
    }

    /**
     * Initialize the {@link EsTransportClientProvider} singleton instance.<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @param addresses
     *            nodes addresses list
     * @param clustername
     *            ES cluster name
     * @throws ClientUnavailableException
     */
    public static void init(List<InetSocketAddress> addresses, String clustername) throws ClientUnavailableException {
        synchronized (EsTransportClientProvider.class) {
            logger.info(">>> Initializing ES transport client...");
            closeIfInstanceInitialized();
            instance = new EsTransportClientProvider(addresses, clustername);
            logger.info(">>> Initializing ES transport client... DONE");
        }
    }

    private static void closeIfInstanceInitialized() {
        if (instance != null) {
            logger.warn(PROVIDER_ALREADY_INITIALIZED_MSG);
            close();
        }
    }

    /**
     * Close the ES transport client
     */
    public static void close() {
        synchronized (EsTransportClientProvider.class) {
            if (instance != null) {
                instance.closeClient();
            } else {
                logger.warn(PROVIDER_CANNOT_CLOSE_CLIENT_MSG);
            }
        }
    }

    private void closeClient() {
        if (client != null) {
            try {
                client.close();
            } finally {
                client = null;
            }
        }
    }

    /**
     * Create the Elasticsearch transport client based on the default configuration settings ({@link ClientSettingsKey})
     *
     * @throws ClientUnavailableException
     */
    private EsTransportClientProvider() throws ClientUnavailableException {
        this(ClientSettings.getInstance());
    }

    /**
     * Create the Elasticsearch transport client based on the provided configuration settings
     * 
     * @param settings
     * @throws ClientUnavailableException
     */
    private EsTransportClientProvider(final AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
        this(parseAddresses(settings), settings.getString(ClientSettingsKey.ELASTICSEARCH_CLUSTER));
    }

    /**
     * Create the Elasticsearch transport client based on the provided configuration addresses and clustername
     * 
     * @param addresses
     * @param clustername
     * @throws ClientUnavailableException
     */
    private EsTransportClientProvider(List<InetSocketAddress> addresses, String clustername) throws ClientUnavailableException {
        try {
            if (addresses == null || addresses.isEmpty()) {
                throw new ClientUnavailableException(PROVIDER_NO_NODE_CONFIGURED_MSG);
            }

            Settings settings = Settings.builder().put(KEY_ES_CLUSTER_NAME, clustername).build();
            client = new PreBuiltTransportClient(settings);
            addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);
            // ES 2.3.4 version
            // Settings settings = Settings.settingsBuilder().put("cluster.name", clustername).build();
            // TransportClient client = TransportClient.builder().settings(settings).build();
            // addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);
        } catch (Throwable t) {
            throw new ClientUnavailableException(PROVIDER_FAILED_TO_CONFIGURE_MSG, t);
        }

    }

    @Override
    public TransportClient getClient() {
        return client;
    }

    // static TransportClient getClient(List<InetSocketAddress> addresses, String clustername) throws ClientUnavailableException, UnknownHostException {
    // if (addresses == null || addresses.isEmpty()) {
    // throw new ClientUnavailableException(PROVIDER_NO_NODE_CONFIGURED_MSG);
    // }
    //
    // Settings settings = Settings.builder().put(KEY_ES_CLUSTER_NAME, clustername).build();
    // TransportClient client = new PreBuiltTransportClient(settings);
    // addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);
    //
    // // ES 2.3.4 version
    // // Settings settings = Settings.settingsBuilder().put("cluster.name", clustername).build();
    // // TransportClient client = TransportClient.builder().settings(settings).build();
    // // addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);
    //
    // return client;
    // }
    //
    // static TransportClient createClient(final AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
    // try {
    // final List<InetSocketAddress> addresses = parseAddresses(settings);
    // return getClient(addresses, settings.getString(ClientSettingsKey.ELASTICSEARCH_CLUSTER));
    // } catch (final ClientUnavailableException e) {
    // throw e;
    // } catch (final Exception e) {
    // e.printStackTrace();
    // throw new ClientUnavailableException(PROVIDER_FAILED_TO_CONFIGURE_MSG, e);
    // }
    // }

    static List<InetSocketAddress> parseAddresses(AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {

        // first try the legacy map approach
        final Map<String, String> map = settings.getMap(String.class, ClientSettingsKey.ELASTICSEARCH_NODE, "[0-9]+");
        if (map != null && !map.isEmpty()) {
            return parseAndAdd(map.values().stream());
        }

        // next try the list
        final List<String> nodes = settings.getList(String.class, ClientSettingsKey.ELASTICSEARCH_NODES);
        if (nodes != null && !nodes.isEmpty()) {
            return parseAndAdd(nodes.stream());
        }

        // now try the single node approach
        final String node = settings.getString(ClientSettingsKey.ELASTICSEARCH_NODE);
        if (node != null && !node.isEmpty()) {
            return parseAndAdd(Stream.of(node));
        }

        return Collections.emptyList();
    }

    static List<InetSocketAddress> parseAndAdd(Stream<String> stream) {
        return stream.map(EsTransportClientProvider::parseAddress).filter(Objects::nonNull).collect(Collectors.toList());
    }

    static InetSocketAddress parseAddress(String node) {
        if (node == null || node.isEmpty()) {
            return null;
        }

        final int idx = node.lastIndexOf(':');
        if (idx < 0) {
            return new InetSocketAddress(node, getDefaultPort());
        } else {
            final String host = node.substring(0, idx);
            final String port = node.substring(idx + 1);
            if (port.isEmpty()) {
                return new InetSocketAddress(host, getDefaultPort());
            }
            return new InetSocketAddress(host, Integer.parseInt(port));
        }
    }

}
