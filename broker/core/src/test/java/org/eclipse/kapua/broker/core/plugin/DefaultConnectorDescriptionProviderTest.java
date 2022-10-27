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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class DefaultConnectorDescriptionProviderTest extends Assert {

    String[] connectorName;
    private static final String BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY = "broker.connector.descriptor.default.disable";
    private static final String BROKER_CONN_DESC_CONF_URI_PROP_KEY = "broker.connector.descriptor.configuration.uri";

    @Before
    public void initialize() {
        BrokerSetting.resetInstance();
        connectorName = new String[]{null, "", "connector name", "name1234567890", "connector!@#$%^&*()_<>/"};
    }

    @Test
    public void getDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "false");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/conector.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

            for (String name : connectorName) {
                assertThat("Instance of ConnectorDescriptor expected.", defaultConnectorDescriptionProvider.getDescriptor(name), IsInstanceOf.instanceOf(ConnectorDescriptor.class));
            }
        });
    }

    @Test
    public void getDescriptorDisabledDefaultConnectorDescriptorTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "true");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

            for (String name : connectorName) {
                assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
            }
        });
    }

    @Test
    public void getDescriptorConfigurationFirstPropertiesTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "true");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/conector.descriptor/1.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

            for (String name : connectorName) {
                assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
            }
        });
    }

    @Test
    public void getDescriptorConfigurationSecondPropertiesTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/conector.descriptor/2.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

            for (String name : connectorName) {
                assertThat("Instance of ConnectorDescriptor expected.", defaultConnectorDescriptionProvider.getDescriptor(name), IsInstanceOf.instanceOf(ConnectorDescriptor.class));
            }
        });
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoClassExceptionTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_DEFAULT_DISABLE_PROP_KEY, "true");
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "file:src/test/resources/conector.descriptor/3.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();
            for (String name : connectorName) {
                defaultConnectorDescriptionProvider.getDescriptor(name);
            }
        });
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoProtocolExceptionTest() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BROKER_CONN_DESC_CONF_URI_PROP_KEY, "aaa");
        Tests.runWithProperties(properties, DefaultConnectorDescriptionProvider::new);
    }
}
