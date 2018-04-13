/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.client.transport.EsTransportClientProvider;
import org.eclipse.kapua.service.datastore.client.transport.EsTransportClientProvider;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.eclipse.kapua.commons.setting.AbstractBaseKapuaSetting;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.transport.ClientSettingsKey;
import org.eclipse.kapua.service.datastore.client.transport.EsTransportClientProvider;
import org.elasticsearch.client.Client;
import org.junit.Ignore;
import org.junit.Test;

public class EsTransportClientProviderTest {

    /**
     * A hostname which does not exists
     * <p>
     * In case this hostname does exists on your network, which is highly unlikely, it can be overridden.
     * </p>
     */
    private final static String UNKWNON_HOST = System.getProperty("test.org.eclipse.kapua.service.datastore.unknowHost", "26-non-existing-host-05.");

    private static final String CLUSTER_NAME = "foo-cluster";

    private final static Condition<InetSocketAddress> UNRESOLVED = new Condition<>(InetSocketAddress::isUnresolved, "Host unresolved");

    private void assertThatResolvedAs(InetSocketAddress result, Class<? extends InetAddress> addressClazz, String hostAddress, int port) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getAddress()).isNotNull();

        Assertions.assertThat(result).doesNotHave(UNRESOLVED);
        Assertions.assertThat(result.getHostString()).isEqualTo(hostAddress);
        Assertions.assertThat(result.getPort()).isEqualTo(port);
        Assertions.assertThat(result.getAddress()).isInstanceOf(addressClazz);
    }

    @Test
    public void test1() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress("127.0.0.1");
        assertThatResolvedAs(result, Inet4Address.class, "127.0.0.1", 9300);
    }

    @Test
    public void test2() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress("127.0.0.1:");
        assertThatResolvedAs(result, Inet4Address.class, "127.0.0.1", 9300);
    }

    @Test
    public void test3() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress("[::1]:");
        assertThatResolvedAs(result, Inet6Address.class, "0:0:0:0:0:0:0:1", 9300);
    }

    @Test
    public void test4() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress("[::1]:1234");
        assertThatResolvedAs(result, Inet6Address.class, "0:0:0:0:0:0:0:1", 1234);
    }

    @Test
    public void testEmpty1() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress("");
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testEmpty2() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testHostNotFound1() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress(UNKWNON_HOST);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).has(UNRESOLVED);
    }

    @Test
    public void testHostNotFound2() {
        InetSocketAddress result = EsTransportClientProvider.parseAddress(UNKWNON_HOST + ":123");
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).has(UNRESOLVED);
    }

    @Test
    public void testHostsEmpty1() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.emptyMap());
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testHostsEmpty2() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.singletonMap(ClientSettingsKey.ELASTICSEARCH_NODES.key(), ""));
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testHostsEmpty3() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.singletonMap(ClientSettingsKey.ELASTICSEARCH_NODE.key() + ".01", ""));
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testHostsEmpty4() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.singletonMap(ClientSettingsKey.ELASTICSEARCH_NODE.key(), ""));
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testHosts1() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.singletonMap(ClientSettingsKey.ELASTICSEARCH_NODES.key(), "127.0.0.1,127.0.0.2:1234,[::1]:5678"));
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);
        Assertions.assertThat(result).hasSize(3);

        assertThatResolvedAs(result.get(0), Inet4Address.class, "127.0.0.1", 9300);
        assertThatResolvedAs(result.get(1), Inet4Address.class, "127.0.0.2", 1234);
        assertThatResolvedAs(result.get(2), Inet6Address.class, "0:0:0:0:0:0:0:1", 5678);
    }

    @Test
    public void testHosts2() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.singletonMap(ClientSettingsKey.ELASTICSEARCH_NODE.key() + ".01", "127.0.0.1:1234"));
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);

        Assertions.assertThat(result).hasSize(1);
        assertThatResolvedAs(result.get(0), Inet4Address.class, "127.0.0.1", 1234);
    }

    @Test
    public void testHosts3() throws ClientUnavailableException {
        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(Collections.singletonMap(ClientSettingsKey.ELASTICSEARCH_NODE.key(), "127.0.0.1:1234"));
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);

        Assertions.assertThat(result).hasSize(1);
        assertThatResolvedAs(result.get(0), Inet4Address.class, "127.0.0.1", 1234);
    }

    @Test
    public void testOverride1() throws ClientUnavailableException {
        Map<String, Object> map = new HashMap<>();
        map.put(ClientSettingsKey.ELASTICSEARCH_NODE.key(), "127.0.0.1:1234");
        map.put(ClientSettingsKey.ELASTICSEARCH_NODES.key(), "127.0.0.2:5678");
        map.put(ClientSettingsKey.ELASTICSEARCH_CLUSTER.key(), CLUSTER_NAME);

        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(map);
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);

        Assertions.assertThat(result).hasSize(1);

        /*
         * There must be only one entry, as the "nodes" entry overrides the "node" entry
         */
        assertThatResolvedAs(result.get(0), Inet4Address.class, "127.0.0.2", 5678);
    }

    @Test
    public void testOverride2() throws ClientUnavailableException {
        Map<String, Object> map = new HashMap<>();
        map.put(ClientSettingsKey.ELASTICSEARCH_NODE.key(), "127.0.0.1:1234");
        map.put(ClientSettingsKey.ELASTICSEARCH_NODES.key(), "127.0.0.2:5678");
        map.put(ClientSettingsKey.ELASTICSEARCH_NODE.key() + ".01", "127.0.0.3:1234");
        map.put(ClientSettingsKey.ELASTICSEARCH_CLUSTER.key(), CLUSTER_NAME);

        AbstractBaseKapuaSetting<ClientSettingsKey> settings = AbstractBaseKapuaSetting.fromMap(map);
        List<InetSocketAddress> result = EsTransportClientProvider.parseAddresses(settings);

        Assertions.assertThat(result).hasSize(1);

        /*
         * There must be only one entry, as the "nodes" entry overrides the "node" entry
         */
        assertThatResolvedAs(result.get(0), Inet4Address.class, "127.0.0.3", 1234);
    }

    @Ignore("Disable until functionality of test is known. It locks tests on connect_timeout at connect on nonexisting address.")
    @Test
    public void testClient1() throws ClientUnavailableException {
        Map<String, Object> map = new HashMap<>();
        map.put(ClientSettingsKey.ELASTICSEARCH_NODES.key(), "127.0.0.1,127.0.0.2:1234,[::1]:5678");
        map.put(ClientSettingsKey.ELASTICSEARCH_CLUSTER.key(), CLUSTER_NAME);
        EsTransportClientProvider.init(AbstractBaseKapuaSetting.fromMap(map));
        try (Client result = EsTransportClientProvider.getInstance().getClient()) {
            Assertions.assertThat(result).isNotNull();
        }
    }

    @Test
    public void testEmpty3() {
        Assertions.assertThatExceptionOfType(ClientUnavailableException.class) //
                .isThrownBy(() -> EsTransportClientProvider.init(null, null));
    }

    @Test
    public void testEmpty4() {
        Assertions.assertThatExceptionOfType(ClientUnavailableException.class) //
                .isThrownBy(() -> EsTransportClientProvider.init(Collections.emptyList(), null));
    }

    @Ignore("Ignored until connect_timeout is resolved on Hudson CI.")
    @Test
    public void testUnknownHost() {
        Assertions.assertThatExceptionOfType(ClientUnavailableException.class) //
                .isThrownBy(() -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put(ClientSettingsKey.ELASTICSEARCH_NODES.key(), UNKWNON_HOST);
                    map.put(ClientSettingsKey.ELASTICSEARCH_CLUSTER.key(), CLUSTER_NAME);
                    EsTransportClientProvider.init(AbstractBaseKapuaSetting.fromMap(map));
                    try (Client result = EsTransportClientProvider.getInstance().getClient()) {
                    }
                });
    }
}
