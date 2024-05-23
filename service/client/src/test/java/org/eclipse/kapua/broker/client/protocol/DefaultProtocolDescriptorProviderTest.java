/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.client.protocol;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.client.message.MessageType;
import org.eclipse.kapua.service.client.protocol.DefaultProtocolDescriptionProvider;
import org.eclipse.kapua.service.client.protocol.ProtocolDescriptor;
import org.eclipse.kapua.service.client.setting.ServiceClientSetting;
import org.eclipse.kapua.service.client.setting.ServiceClientSettingKey;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class DefaultProtocolDescriptorProviderTest {

    String[] connectorName;
    private static final String BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY = "protocol.descriptor.default.disable";
    private static final String BROKER_CONN_DESC_CONF_URI_PROP_KEY = "protocol.descriptor.configuration.uri";

    @Before
    public void initialize() {
        connectorName = new String[] { null, "", "connector name", "name1234567890", "connector!@#$%^&*()_<>/" };
    }

    @Test
    public void getDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "false");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());

            for (String name : connectorName) {
                Assert.assertThat("Instance of ProtocolDescriptor expected.", defaultProtocolDescriptionProvider.getDescriptor(name), IsInstanceOf.instanceOf(ProtocolDescriptor.class));
            }
        });
    }

    @Test
    public void getDescriptorDisabledDefaultConnectorDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "true");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());

            for (String name : connectorName) {
                Assert.assertNull("Null expected.", defaultProtocolDescriptionProvider.getDescriptor(name));
            }
        });
    }

    @Test
    public void getDescriptorConfigurationFirstPropertiesTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "true");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());

            for (String name : connectorName) {
                Assert.assertNull("Null expected.", defaultProtocolDescriptionProvider.getDescriptor(name));
            }
        });
    }

    @Test
    public void getDescriptorConfigurationSecondPropertiesTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/protocol.descriptor/2.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());

            for (String name : connectorName) {
                Assert.assertThat("Instance of ProtocolDescriptor expected.", defaultProtocolDescriptionProvider.getDescriptor(name), IsInstanceOf.instanceOf(ProtocolDescriptor.class));
            }
        });
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoClassExceptionTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "true");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/protocol.descriptor/3.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());
            for (String name : connectorName) {
                defaultProtocolDescriptionProvider.getDescriptor(name);
            }
        });
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoProtocolExceptionTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "aaa");
        Tests.runWithProperties(properties, () -> new DefaultProtocolDescriptionProvider(new ServiceClientSetting()));
    }

    @Test
    public void defaultProviderWithDisabledDefaultDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());
            ProtocolDescriptor descriptor = provider.getDescriptor("foo");
            Assert.assertNull("Null expected.", descriptor);
        });
    }

    @Test(expected = Exception.class)
    public void defaultProviderWithNonExistingFileTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/does-not-exist.properties");

        Tests.runWithProperties(properties, () -> new DefaultProtocolDescriptionProvider(new ServiceClientSetting()));
    }

    @Test
    public void defaultProviderAllowingDefaultFallbackTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());
            ProtocolDescriptor descriptor = provider.getDescriptor("foo");
            Assert.assertNotNull("Null not expected.", descriptor);
        });
    }

    @Test
    public void defaultProviderWithEmptyFileTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");
        properties.put(ServiceClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());
            ProtocolDescriptor descriptor = provider.getDescriptor("foo");
            Assert.assertNull("Null expected.", descriptor);
        });
    }

    @Test
    public void defaultProviderUsingNonEmptyConfigurationTest() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");
        properties.put(ServiceClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/2.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider(new ServiceClientSetting());
            Assert.assertNull("Null expected.", provider.getDescriptor("foo"));

            ProtocolDescriptor descriptor = provider.getDescriptor("mqtt");
            Assert.assertNotNull("Null not expected.", descriptor);

            Assert.assertEquals("Expected and actual values should be the same.", org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class,
                    descriptor.getDeviceClass(MessageType.APP));
            Assert.assertEquals("Expected and actual values should be the same.", org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class, descriptor.getKapuaClass(MessageType.APP));

            Assert.assertNull("Null expected.", descriptor.getDeviceClass(MessageType.DATA));
            Assert.assertNull("Null expected.", descriptor.getKapuaClass(MessageType.DATA));
        });
    }

    @Test(expected = Exception.class)
    public void defaultProviderWithInvalidConfigurationTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");
        properties.put(ServiceClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/3.properties");

        Tests.runWithProperties(properties, () -> new DefaultProtocolDescriptionProvider(new ServiceClientSetting()));
    }

    @Test
    public void emptyConfigurationUrlTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(ServiceClientSettingKey.CONFIGURATION_URI.key(), "");

        Tests.runWithProperties(properties, () -> new DefaultProtocolDescriptionProvider(new ServiceClientSetting()));
    }
}