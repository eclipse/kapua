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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import static java.util.stream.Collectors.toList;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Elasticsearch transport client implementation.
 *
 * @since 1.0
 */
public class EsTransportClientProvider implements ElasticsearchClientProvider {

    private static final int DEFAULT_PORT = 9300;

    private static int getDefaultPort() {
        return DatastoreSettings.getInstance().getInt(DatastoreSettingKey.ELASTICSEARCH_PORT, DEFAULT_PORT);
    }

    static Client getEsClient(List<InetSocketAddress> addresses, String clustername) throws EsClientUnavailableException, UnknownHostException {
        if (addresses == null || addresses.isEmpty()) {
            throw new EsClientUnavailableException("No ElasticSearch nodes are configured");
        }

        Settings settings = Settings.settingsBuilder().put("cluster.name", clustername).build();

        TransportClient client = TransportClient.builder().settings(settings).build();

        addresses.stream().map(InetSocketTransportAddress::new).forEachOrdered(client::addTransportAddress);

        return client;
    }

    static List<InetSocketAddress> parseAddresses(AbstractBaseKapuaSetting<DatastoreSettingKey> settings) throws EsClientUnavailableException {

        // first try the legacy map approach
        final Map<String, String> map = settings.getMap(String.class, DatastoreSettingKey.ELASTICSEARCH_NODE, "[0-9]+");
        if (map != null && !map.isEmpty()) {
            return parseAndAdd(map.values().stream());
        }

        // next try the list
        final List<String> nodes = settings.getList(String.class, DatastoreSettingKey.ELASTICSEARCH_NODES);
        if (nodes != null && !nodes.isEmpty()) {
            return parseAndAdd(nodes.stream());
        }

        // now try the single node approach
        final String node = settings.getString(DatastoreSettingKey.ELASTICSEARCH_NODE);
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

    static Client createClient(final AbstractBaseKapuaSetting<DatastoreSettingKey> settings) throws EsClientUnavailableException {
        try {
            final List<InetSocketAddress> addresses = parseAddresses(settings);
            return getEsClient(addresses, settings.getString(DatastoreSettingKey.ELASTICSEARCH_CLUSTER));
        } catch (final EsClientUnavailableException e) {
            throw e;
        } catch (final Exception e) {
            throw new EsClientUnavailableException("Failed to configure ElasticSearch transport", e);
        }
    }

    private final Client client;

    /**
     * Create a new Elasticsearch transport client based on the configuration parameters ({@link DatastoreSettingKey})
     *
     * @throws EsClientUnavailableException
     */
    public EsTransportClientProvider() throws EsClientUnavailableException {
        client = createClient(DatastoreSettings.getInstance());
    }

    @Override
    public Client getClient() {
        return client;
    }

}
