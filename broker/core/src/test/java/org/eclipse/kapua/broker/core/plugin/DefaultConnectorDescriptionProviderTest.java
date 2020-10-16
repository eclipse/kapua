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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class DefaultConnectorDescriptionProviderTest extends Assert {

    String[] connectorName;

    @Before
    public void initialize() {
        connectorName = new String[]{null, "", "connector name", "name1234567890", "connector!@#$%^&*()_<>/"};
    }

    @Test
    public void getDescriptorTest() {
        System.setProperty("broker.connector.descriptor.default.disable", "false");
        System.setProperty("broker.connector.descriptor.configuration.uri", "file:src/test/resources/conector.descriptor/1.properties");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            assertThat("Instance of ConnectorDescriptor expected.", defaultConnectorDescriptionProvider.getDescriptor(name), IsInstanceOf.instanceOf(ConnectorDescriptor.class));
        }
    }

    @Test
    public void getDescriptorDisabledDefaultConnectorDescriptorTest() {
        System.setProperty("broker.connector.descriptor.default.disable", "true");
        System.setProperty("broker.connector.descriptor.configuration.uri", "");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
        }
    }

    @Test
    public void getDescriptorConfigurationFirstPropertiesTest() {
        System.setProperty("broker.connector.descriptor.default.disable", "true");
        System.setProperty("broker.connector.descriptor.configuration.uri", "file:src/test/resources/conector.descriptor/1.properties");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
        }
    }

    @Test
    public void getDescriptorConfigurationSecondPropertiesTest() {
        System.setProperty("broker.connector.descriptor.configuration.uri", "file:src/test/resources/conector.descriptor/2.properties");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            assertThat("Instance of ConnectorDescriptor expected.", defaultConnectorDescriptionProvider.getDescriptor(name), IsInstanceOf.instanceOf(ConnectorDescriptor.class));
        }
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoClassExceptionTest() {
        System.setProperty("broker.connector.descriptor.default.disable", "true");
        System.setProperty("broker.connector.descriptor.configuration.uri", "file:src/test/resources/conector.descriptor/3.properties");

        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();
        for (String name : connectorName) {
            defaultConnectorDescriptionProvider.getDescriptor(name);
        }
    }

    @Test(expected = Exception.class)
    public void getDescriptorNoProtocolExceptionTest() {
        System.setProperty("broker.connector.descriptor.configuration.uri", "aaa");
        new DefaultConnectorDescriptionProvider();
    }
}