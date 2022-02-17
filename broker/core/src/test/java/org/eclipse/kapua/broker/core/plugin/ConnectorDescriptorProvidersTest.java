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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class ConnectorDescriptorProvidersTest extends Assert {

    @Test
    public void connectorDescriptorProvidersTest() throws Exception {
        Constructor<ConnectorDescriptorProviders> connectorDescriptorProviders = ConnectorDescriptorProviders.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(connectorDescriptorProviders.getModifiers()));
        connectorDescriptorProviders.setAccessible(true);
        connectorDescriptorProviders.newInstance();
    }

    @Test
    public void getInstanceNullProviderTest() {
        assertThat("Instance of ConnectorDescriptorProvider expected.", ConnectorDescriptorProviders.getInstance(), IsInstanceOf.instanceOf(ConnectorDescriptorProvider.class));
    }

    @Test
    public void getInstanceProviderTest() {
        ConnectorDescriptorProviders.getInstance();
        assertThat("Instance of ConnectorDescriptorProvider expected.", ConnectorDescriptorProviders.getInstance(), IsInstanceOf.instanceOf(ConnectorDescriptorProvider.class));
    }

    @Test
    public void getDescriptorNullTest() {
        assertThat("Instance of ConnectorDescriptor expected.", ConnectorDescriptorProviders.getDescriptor(null), IsInstanceOf.instanceOf(ConnectorDescriptor.class));
    }

    @Test
    public void getDescriptorTest() {
        String[] connectorNames = {"1234567890", "", "connectorName", "connectorName1234567890", "a", "@#$%^&*()_><connectorName", "@{123213#$%&/(/"};
        for (String connectorName : connectorNames) {
            assertThat("Instance of ConnectorDescriptor expected.", ConnectorDescriptorProviders.getDescriptor(connectorName), IsInstanceOf.instanceOf(ConnectorDescriptor.class));
        }
    }
}