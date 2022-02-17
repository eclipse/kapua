/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.KapuaBrokerJAXBContextLoader;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class ConnectorDescriptorTest extends Assert {

    private static final String BROKER_IP_RESOLVER_CLASS_NAME;
    private static final String BROKER_IP_PROP_KEY = "broker.ip";
    private static final String KAPUA_CONFIG_URL_PROP_KEY = "kapua.config.url";

    private KapuaBrokerJAXBContextLoader kapuaBrokerJAXBContextLoader;

    static {
        BrokerSetting config = BrokerSetting.getInstance();
        BROKER_IP_RESOLVER_CLASS_NAME = config.getString(BrokerSettingKey.BROKER_IP_RESOLVER_CLASS_NAME);
    }

    @Before
    public void resetSettings() throws KapuaException {
        kapuaBrokerJAXBContextLoader = new KapuaBrokerJAXBContextLoader();
        kapuaBrokerJAXBContextLoader.init();
        BrokerSetting.resetInstance();
    }

    @After
    public void resetJAXBContext() {
        kapuaBrokerJAXBContextLoader.reset();
    }

    @Test
    public void nonNullProviderTest() {
        ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
        assertNotNull("Null not expected.", provider);
    }

    @Test
    public void defaultDescriptorFromProviderTest() {
        ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
        ConnectorDescriptor descriptor = provider.getDescriptor("foo");
        assertNotNull("Null not expected.", descriptor);
    }

    @Test
    public void getDescriptorFromProvidersClassTest() {
        assertNotNull("Null not expected.", ConnectorDescriptorProviders.getDescriptor("foo"));
    }

    @Test
    public void getTransportProtocolTest() {
        ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
        ConnectorDescriptor descriptor = provider.getDescriptor("foo");
        assertEquals("Expected and actual values should be the same.", "MQTT", descriptor.getTransportProtocol());
    }

    @Test
    public void defaultProviderWithDisabledDefaultDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR.key(), "true");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider provider = new DefaultConnectorDescriptionProvider();
            ConnectorDescriptor descriptor = provider.getDescriptor("foo");
            assertNull("Null expected.", descriptor);
        });
    }

    @Test(expected = Exception.class)
    public void defaultProviderWithNonExistingFileTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/does-not-exist.properties");

        Tests.runWithProperties(properties, DefaultConnectorDescriptionProvider::new);
    }

    @Test
    public void defaultProviderAllowingDefaultFallbackTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/conector.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider provider = new DefaultConnectorDescriptionProvider();
            ConnectorDescriptor descriptor = provider.getDescriptor("foo");
            assertNotNull("Null not expected.", descriptor);
        });
    }

    @Test
    public void defaultProviderWithEmptyFileTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR.key(), "true");
        properties.put(BrokerSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/conector.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider provider = new DefaultConnectorDescriptionProvider();
            ConnectorDescriptor descriptor = provider.getDescriptor("foo");
            assertNull("Null expected.", descriptor);
        });
    }

    @Test
    public void defaultProviderUsingNonEmptyConfigurationTest() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR.key(), "true");
        properties.put(BrokerSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/conector.descriptor/2.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider provider = new DefaultConnectorDescriptionProvider();
            assertNull("Null expected.", provider.getDescriptor("foo"));

            ConnectorDescriptor descriptor = provider.getDescriptor("mqtt");
            assertNotNull("Null not expected.", descriptor);

            assertEquals("Expected and actual values should be the same.", org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class, descriptor.getDeviceClass(MessageType.APP));
            assertEquals("Expected and actual values should be the same.", org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class, descriptor.getKapuaClass(MessageType.APP));

            assertNull("Null expected.", descriptor.getDeviceClass(MessageType.DATA));
            assertNull("Null expected.", descriptor.getKapuaClass(MessageType.DATA));
        });
    }

    @Test(expected = Exception.class)
    public void defaultProviderWithInvalidConfigurationTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR.key(), "true");
        properties.put(BrokerSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/conector.descriptor/3.properties");

        Tests.runWithProperties(properties, DefaultConnectorDescriptionProvider::new);
    }

    @Test
    public void emptyConfigurationUrlTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerSettingKey.CONFIGURATION_URI.key(), "");

        Tests.runWithProperties(properties, DefaultConnectorDescriptionProvider::new);
    }

    @Test
    public void testBrokerIpOrHostNameConfigFile() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(KAPUA_CONFIG_URL_PROP_KEY, "broker.setting/kapua-broker-setting-1.properties");

        Tests.runWithProperties(properties, () -> {
            BrokerIpResolver brokerIpResolver = newInstance(BROKER_IP_RESOLVER_CLASS_NAME, DefaultBrokerIpResolver.class);
            String ipOrHostName = brokerIpResolver.getBrokerIpOrHostName();
            assertEquals("Expected and actual values should be the same.", "192.168.33.10", ipOrHostName);
        });
    }

    @Test
    public void testBrokerIpOrHostNameEnvProperty() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_IP_PROP_KEY, "192.168.33.10");

        Tests.runWithProperties(properties, () -> {
            BrokerIpResolver brokerIpResolver = newInstance(BROKER_IP_RESOLVER_CLASS_NAME, DefaultBrokerIpResolver.class);
            String ipOrHostName = brokerIpResolver.getBrokerIpOrHostName();
            assertEquals("Expected and actual values should be the same.", "192.168.33.10", ipOrHostName);
        });
    }

    @Test
    public void testBrokerIpOrHostNameEmptyConfigFile() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(KAPUA_CONFIG_URL_PROP_KEY, "broker.setting/kapua-broker-setting-2.properties");

        Tests.runWithProperties(properties, () -> {
            BrokerIpResolver brokerIpResolver = newInstance(BROKER_IP_RESOLVER_CLASS_NAME, DefaultBrokerIpResolver.class);
            String ipOrHostName = brokerIpResolver.getBrokerIpOrHostName();
            assertEquals("Expected and actual values should be the same.", "192.168.33.10", ipOrHostName);
        });
    }

    @Test(expected = Exception.class)
    public void testBrokerIpOrHostNameNoEnvProperty() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(KAPUA_CONFIG_URL_PROP_KEY, "broker.setting/kapua-broker-setting-3.properties");

        Tests.runWithProperties(properties, () -> {
            BrokerIpResolver brokerIpResolver = newInstance(BROKER_IP_RESOLVER_CLASS_NAME, DefaultBrokerIpResolver.class);
            brokerIpResolver.getBrokerIpOrHostName();
        });
    }

    /**
     * Code reused form KapuaSecurityBrokerFilter for instantiating broker ip resolver class.
     *
     * @param clazz           class that instantiates broker ip resolver
     * @param defaultInstance default instance of class
     * @param <T>             generic type
     * @return instance of ip resolver
     * @throws KapuaException
     */
    protected <T> T newInstance(String clazz, Class<T> defaultInstance) throws KapuaException {
        T instance;
        // lazy synchronization
        try {
            if (!StringUtils.isEmpty(clazz)) {
                Class<T> clazzToInstantiate = (Class<T>) Class.forName(clazz);
                instance = clazzToInstantiate.newInstance();
            } else {
                instance = defaultInstance.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e, "Class instantiation exception " + clazz);
        }

        return instance;
    }

}
