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
package org.eclipse.kapua.service.datastore.client.rest;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.http.HttpHost;
import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientProvider;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch rest client implementation.<br>
 * Instantiate the Elasticsearch rest client.
 *
 * @since 1.0
 */
public class EsRestClientProvider implements ClientProvider<RestClient> {

    private static final Logger logger = LoggerFactory.getLogger(EsRestClientProvider.class);

    private static final String PROVIDER_NOT_INITIALIZED_MSG = "Provider not configured! please call initi method before use it!";
    private static final String PROVIDER_ALREADY_INITIALIZED_MSG = "Provider already initialized! closing it before initialize the new one!";
    private static final String PROVIDER_NO_NODE_CONFIGURED_MSG = "No ElasticSearch nodes are configured";
    private static final String PROVIDER_FAILED_TO_CONFIGURE_MSG = "Failed to configure ElasticSearch transport";

    private static final String PROVIDER_CANNOT_CLOSE_CLIENT_LOG = "Cannot close ElasticSearch rest client. Client is already stopped or not initialized!";

    private static final int DEFAULT_PORT = 9200;

    private static EsRestClientProvider instance;

    private RestClient client;

    private static int getDefaultPort() {
        return ClientSettings.getInstance().getInt(ClientSettingsKey.ELASTICSEARCH_PORT, DEFAULT_PORT);
    }

    /**
     * Get the {@link EsRestClientProvider} instance
     * 
     * @return
     * @throws ClientUnavailableException
     */
    public static EsRestClientProvider getInstance() throws ClientUnavailableException {
        if (instance == null) {
            throw new ClientUnavailableException(PROVIDER_NOT_INITIALIZED_MSG);
        }
        return instance;
    }

    /**
     * Initialize the {@link EsRestClientProvider} singleton instance.<br>
     * The nodes addresses and other parameters are read from the configuration file.<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @throws ClientException
     */
    public static void init() throws ClientException {
        synchronized (EsRestClientProvider.class) {
            if (instance != null) {
                logger.warn(PROVIDER_ALREADY_INITIALIZED_MSG);
                close();
            }
            instance = new EsRestClientProvider();
        }
    }

    /**
     * Initialize the {@link EsRestClientProvider} singleton instance.<br>
     * The nodes addresses and other parameters are overwritten with the provided settings.<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @param settings
     * @throws ClientException
     */
    public static void init(AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientException {
        synchronized (EsRestClientProvider.class) {
            if (instance != null) {
                logger.warn(PROVIDER_ALREADY_INITIALIZED_MSG);
                close();
            }
            instance = new EsRestClientProvider(settings);
        }
    }

    /**
     * Initialize the {@link EsRestClientProvider} singleton instance.<br>
     * <b>NOTE. The init methods can be called more than once in order to reinitialize the underlying datastore connection. It the datastore was already initialized this method close the old one
     * before initializing the new one.</b>
     * 
     * @param addresses
     *            nodes addresses list
     * @throws ClientException
     */
    public static void init(List<InetSocketAddress> addresses) throws ClientException {
        synchronized (EsRestClientProvider.class) {
            if (instance != null) {
                logger.warn(PROVIDER_ALREADY_INITIALIZED_MSG);
                close();
            }
            instance = new EsRestClientProvider(addresses);
        }
    }

    /**
     * Close the ES rest client
     * 
     * @throws ClientException
     */
    public static void close() throws ClientException {
        synchronized (EsRestClientProvider.class) {
            if (instance != null) {
                try {
                    instance.closeClient();
                } catch (IOException e) {
                    throw new ClientException(ClientErrorCodes.CLIENT_UNAVAILABLE, e.getMessage());
                }
            } else {
                logger.warn(PROVIDER_CANNOT_CLOSE_CLIENT_LOG);
            }
        }
    }

    private void closeClient() throws IOException {
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
    private EsRestClientProvider() throws ClientUnavailableException {
        this(ClientSettings.getInstance());
    }

    /**
     * Create the Elasticsearch rest client based on the provided configuration settings
     * 
     * @param settings
     * @throws ClientUnavailableException
     */
    private EsRestClientProvider(final AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
        this(parseAddresses(settings));
    }

    /**
     * Create the Elasticsearch rest client based on the provided configuration addresses
     * 
     * @param addresses
     * @throws ClientUnavailableException
     */
    private EsRestClientProvider(List<InetSocketAddress> addresses) throws ClientUnavailableException {
        try {
            if (addresses == null || addresses.isEmpty()) {
                throw new ClientUnavailableException(PROVIDER_NO_NODE_CONFIGURED_MSG);
            }
            client = getClient(addresses);
        } catch (Throwable t) {
            throw new ClientUnavailableException(PROVIDER_FAILED_TO_CONFIGURE_MSG, t);
        }

    }

    @Override
    public RestClient getClient() {
        return client;
    }

    static RestClient getClient(List<InetSocketAddress> addresses) throws ClientUnavailableException {
        if (addresses == null || addresses.isEmpty()) {
            throw new ClientUnavailableException(PROVIDER_NO_NODE_CONFIGURED_MSG);
        }
        List<HttpHost> hosts = new ArrayList<HttpHost>();
        for (InetSocketAddress address : addresses) {
            hosts.add(new HttpHost(address.getAddress(), address.getHostName(), address.getPort(), "http"));
        }
        RestClient client = RestClient.builder(hosts.toArray(new HttpHost[hosts.size()])).build();
        return client;
    }

    static RestClient createClient(final AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
        try {
            final List<InetSocketAddress> addresses = parseAddresses(settings);
            return getClient(addresses);
        } catch (final ClientUnavailableException e) {
            throw e;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ClientUnavailableException(PROVIDER_FAILED_TO_CONFIGURE_MSG, e);
        }
    }

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
        return stream.map(EsRestClientProvider::parseAddress).filter(Objects::nonNull).collect(toList());
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
