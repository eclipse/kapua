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
package org.eclipse.kapua.broker.client.protocol;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.message.MessageType;
import org.eclipse.kapua.broker.client.setting.BrokerClientSetting;
import org.eclipse.kapua.broker.client.setting.BrokerClientSettingKey;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;


@Category(JUnitTests.class)
public class ProtocolDescriptorTest {

    @Before
    public void resetSettings() throws KapuaException {
        BrokerClientSetting.resetInstance();
    }

    @Test
    public void nonNullProviderTest() {
        ProtocolDescriptorProvider provider = ProtocolDescriptorProviders.getInstance();
        Assert.assertNotNull("Null not expected.", provider);
    }

    @Test
    public void defaultDescriptorFromProviderTest() {
        ProtocolDescriptorProvider provider = ProtocolDescriptorProviders.getInstance();
        ProtocolDescriptor descriptor = provider.getDescriptor("foo");
        Assert.assertNotNull("Null not expected.", descriptor);
    }

    @Test
    public void getDescriptorFromProvidersClassTest() {
        Assert.assertNotNull("Null not expected.", ProtocolDescriptorProviders.getDescriptor("foo"));
    }

    @Test
    public void getTransportProtocolTest() {
        ProtocolDescriptorProvider provider = ProtocolDescriptorProviders.getInstance();
        ProtocolDescriptor descriptor = provider.getDescriptor("foo");
        Assert.assertEquals("Expected and actual values should be the same.", "MQTT", descriptor.getTransportProtocol());
    }

    @Test
    public void defaultProviderWithDisabledDefaultDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider();
            ProtocolDescriptor descriptor = provider.getDescriptor("foo");
            Assert.assertNull("Null expected.", descriptor);
        });
    }

    @Test(expected = Exception.class)
    public void defaultProviderWithNonExistingFileTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/does-not-exist.properties");

        Tests.runWithProperties(properties, DefaultProtocolDescriptionProvider::new);
    }

    @Test
    public void defaultProviderAllowingDefaultFallbackTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider();
            ProtocolDescriptor descriptor = provider.getDescriptor("foo");
            Assert.assertNotNull("Null not expected.", descriptor);
        });
    }

    @Test
    public void defaultProviderWithEmptyFileTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");
        properties.put(BrokerClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider();
            ProtocolDescriptor descriptor = provider.getDescriptor("foo");
            Assert.assertNull("Null expected.", descriptor);
        });
    }

    @Test
    public void defaultProviderUsingNonEmptyConfigurationTest() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");
        properties.put(BrokerClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/2.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider provider = new DefaultProtocolDescriptionProvider();
            Assert.assertNull("Null expected.", provider.getDescriptor("foo"));

            ProtocolDescriptor descriptor = provider.getDescriptor("mqtt");
            Assert.assertNotNull("Null not expected.", descriptor);

            Assert.assertEquals("Expected and actual values should be the same.", org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class, descriptor.getDeviceClass(MessageType.APP));
            Assert.assertEquals("Expected and actual values should be the same.", org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class, descriptor.getKapuaClass(MessageType.APP));

            Assert.assertNull("Null expected.", descriptor.getDeviceClass(MessageType.DATA));
            Assert.assertNull("Null expected.", descriptor.getKapuaClass(MessageType.DATA));
        });
    }

    @Test(expected = Exception.class)
    public void defaultProviderWithInvalidConfigurationTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR.key(), "true");
        properties.put(BrokerClientSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/protocol.descriptor/3.properties");

        Tests.runWithProperties(properties, DefaultProtocolDescriptionProvider::new);
    }

    @Test
    public void emptyConfigurationUrlTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerClientSettingKey.CONFIGURATION_URI.key(), "");

        Tests.runWithProperties(properties, DefaultProtocolDescriptionProvider::new);
    }

}
