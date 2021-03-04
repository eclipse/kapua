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

import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.qa.markers.Categories;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class DefaultConnectorDescriptionProviderTest extends Assert {

    String[] connectorName;

    @BeforeClass
    public static void resettingBroker() {
        BrokerSetting.resetInstance();
    }

    @Before
    public void initialize() {
        connectorName = new String[]{null, "", "connector name", "name1234567890", "connector!@#$%^&*()_<>/"};
    }

    @After
    public void cleanUp() {
        System.clearProperty("broker.connector.descriptor.default.disable");
        System.clearProperty("broker.connector.descriptor.configuration.uri");
    }

    @Test
    public void getDescriptorDisabledDefaultConnectorDescriptorTest() {
        System.setProperty("broker.connector.descriptor.default.disable", "true");
        System.setProperty("broker.connector.descriptor.configuration.uri", "");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            System.out.println("name is = " + name);
            assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
        }
    }

    @Test
    public void getDescriptorConfigurationFirstPropertiesTest() {
        System.setProperty("broker.connector.descriptor.default.disable", "true");
        System.setProperty("broker.connector.descriptor.configuration.uri", "file:src/test/resources/conector.descriptor/1.properties");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            System.out.println("name is = " + name);
            assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
        }
    }

    @Test
    public void getDescriptorConfigurationSecondPropertiesTest() {
        System.setProperty("broker.connector.descriptor.configuration.uri", "file:src/test/resources/conector.descriptor/2.properties");
        DefaultConnectorDescriptionProvider defaultConnectorDescriptionProvider = new DefaultConnectorDescriptionProvider();

        for (String name : connectorName) {
            System.out.println("name is = " + name);
            assertNull("Null expected.", defaultConnectorDescriptionProvider.getDescriptor(name));
        }
    }
}