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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ProtocolDescriptorProvidersTest {

    @Test
    public void getInstanceNullProviderTest() {
        Assert.assertThat("Instance of ConnectorDescriptorProvider expected.", ProtocolDescriptorProviders.getInstance(), IsInstanceOf.instanceOf(ProtocolDescriptorProvider.class));
    }

    @Test
    public void getInstanceProviderTest() {
        ProtocolDescriptorProviders.getInstance();
        Assert.assertThat("Instance of ConnectorDescriptorProvider expected.", ProtocolDescriptorProviders.getInstance(), IsInstanceOf.instanceOf(ProtocolDescriptorProvider.class));
    }

    @Test
    public void getDescriptorNullTest() {
        Assert.assertThat("Instance of ConnectorDescriptor expected.", ProtocolDescriptorProviders.getDescriptor(null), IsInstanceOf.instanceOf(ProtocolDescriptor.class));
    }

    @Test
    public void getDescriptorTest() {
        String[] connectorNames = {"1234567890", "", "connectorName", "connectorName1234567890", "a", "@#$%^&*()_><connectorName", "@{123213#$%&/(/"};
        for (String connectorName : connectorNames) {
            Assert.assertThat("Instance of ConnectorDescriptor expected.", ProtocolDescriptorProviders.getDescriptor(connectorName), IsInstanceOf.instanceOf(ProtocolDescriptor.class));
        }
    }
}