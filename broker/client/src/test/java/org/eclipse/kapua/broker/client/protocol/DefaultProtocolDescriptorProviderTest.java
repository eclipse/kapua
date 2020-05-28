/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.broker.client.setting.BrokerClientSetting;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class DefaultProtocolDescriptorProviderTest {


    String[] connectorName;
    private static final String BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY = "protocol_descriptor.default.disable";
    private static final String BROKER_CONN_DESC_CONF_URI_PROP_KEY = "protocol_descriptor.configuration.uri";

    @Before
    public void initialize() {
        BrokerClientSetting.resetInstance();
        connectorName = new String[]{null, "", "connector name", "name1234567890", "connector!@#$%^&*()_<>/"};
    }

    @Test
    public void getDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "false");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/protocol.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider();

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
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider();

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
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider();

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
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider();

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
            DefaultProtocolDescriptionProvider defaultProtocolDescriptionProvider = new DefaultProtocolDescriptionProvider();
            for (String name : connectorName) {
                defaultProtocolDescriptionProvider.getDescriptor(name);
            }
        });
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoProtocolExceptionTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "aaa");
        Tests.runWithProperties(properties, DefaultProtocolDescriptionProvider::new);
    }
}