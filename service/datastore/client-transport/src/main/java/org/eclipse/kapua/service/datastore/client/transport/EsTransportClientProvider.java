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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * Elasticsearch transport client implementation.
 *
 * @since 1.0
 */
public class EsTransportClientProvider implements EsClientProvider {

    private static final int DEFAULT_PORT = 9300;

    private final TransportClient client;

    private static int getDefaultPort() {
        return ClientSettings.getInstance().getInt(ClientSettingsKey.ELASTICSEARCH_PORT, DEFAULT_PORT);
    }

    /**
     * Create a new Elasticsearch transport client based on the configuration parameters ({@link ClientSettingsKey})
     *
     * @throws ClientUnavailableException
     */
    public EsTransportClientProvider() throws ClientUnavailableException {
        client = createClient(ClientSettings.getInstance());
    }

    @Override
    public TransportClient getClient() {
        return client;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public boolean isAlive() {
        return client != null && client.connectedNodes().size() > 0;
    }

    static TransportClient getClient(List<InetSocketAddress> addresses, String clustername) throws ClientUnavailableException, UnknownHostException {
        if (addresses == null || addresses.isEmpty()) {
            throw new ClientUnavailableException("No ElasticSearch nodes are configured");
        }

        Settings settings = Settings.builder().put("cluster.name", clustername).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);

        // ES 2.3.4 version
        // Settings settings = Settings.settingsBuilder().put("cluster.name", clustername).build();
        // TransportClient client = TransportClient.builder().settings(settings).build();
        // addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);

        return client;
    }

    static TransportClient createClient(final AbstractBaseKapuaSetting<ClientSettingsKey> settings) throws ClientUnavailableException {
        try {
            final List<InetSocketAddress> addresses = parseAddresses(settings);
            if (addresses.isEmpty()) {

            }
            return getClient(addresses, settings.getString(ClientSettingsKey.ELASTICSEARCH_CLUSTER));
        } catch (final ClientUnavailableException e) {
            throw e;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ClientUnavailableException("Failed to configure ElasticSearch transport", e);
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
        return stream.map(EsTransportClientProvider::parseAddress).filter(Objects::nonNull).collect(toList());
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

}
