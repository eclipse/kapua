/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ConnectorDescriptorProvidersTest extends Assert {

    @Test
    public void getInstanceProviderTest() {
        try {
            ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
            assertTrue(provider.toString().startsWith("org.eclipse.kapua.broker.core.plugin.DefaultConnectorDescriptionProvider"));
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void getDescriptorNullTest() {
        ConnectorDescriptor descriptor = ConnectorDescriptorProviders.getDescriptor(null);
        assertTrue(descriptor.toString().startsWith("org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor"));
    }

    @Test
    public void getDescriptorMultipleValuesTest() {
        String[] values = {"1", "", "123123", "asdaf", "asdsad2123", "a", "#$$#žčš", "@{123213#$%&/(/\\/)\"", "iclMIJRMCu6i1GrZlsM5ZKbcltMpM7sASCbPFl0MEfE689RIs9jjnw2i7GLrgQ7hDlW0d3LosPlfgkMepUqBgLVdmuGXT3RiITICjmcA0TrxkPAjVKaOWkFBY1LiTDcQSF6BcxCCPPxqNbsx2xGnzWL0d8vILXr3mvM2MBGhVSqT4g5CpMcMEiqkJP9YRKWky7Zp41neccWoFjQwFp9jSCZySOiY6J5VB6c1mXdHGxsiuGK8vE3tA029P6jL8Zma", ""};
        for (String value : values) {
            ConnectorDescriptor descriptor = ConnectorDescriptorProviders.getDescriptor(value);
            assertTrue(descriptor.toString().startsWith("org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor"));
        }
    }
}
